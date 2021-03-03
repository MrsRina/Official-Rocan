package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.entity.PlayerMoveEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.MobEffects;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 23/02/2021 at 23:29
 **/
@Registry(name = "Strafe", tag = "Strafe", description = "Allows you control air movement.", category = ModuleCategory.MOVEMENT)
public class ModuleStrafe extends Module {
    public static ValueEnum settingType = new ValueEnum("Type", "Type", "Type strafe.", Type.MANUAL_JUMP);

    /* Normal settings. */
    public static ValueBoolean settingStrafeOnGround = new ValueBoolean("On Ground", "OnGround", "On ground controls.", true);
    public static ValueBoolean settingStrafeSmoothJump = new ValueBoolean("Smooth Jump", "SmoothJump", "Smooth jumping.", true);

    /* Increase speed setting. */
    public static ValueBoolean settingIncreaseSpeed = new ValueBoolean("Increase Speed", "IncreaseSpeed", "Increase a cool speed value when you hit speed limit vanilla.", true);
    public static ValueNumber settingSpeed = new ValueNumber("Speed", "Speed", "Speed boost.", 100, 0, 1000);

    public enum Type {
        AUTO_JUMP, MANUAL_JUMP;
    }

    private int speed;
    private int lastSpeed;
    private int jumps;

    private double speedSQRT;
    private boolean flag;

    /**
     * Ticks for set speed by steps.
     *
     */
    private TurokTick tickStrafeOld = new TurokTick();

    @Override
    public void onSetting() {
        settingSpeed.setEnabled(settingIncreaseSpeed.getValue());
    }

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        int sqrt = (int) (this.speedSQRT * 10000);

        if (this.flag && mc.player.isSprinting()) {
            switch (this.jumps) {
                case 0: {
                    this.lastSpeed = sqrt;

                    break;
                }

                case 1: {
                    this.jumps++;

                    break;
                }

                case 2: {
                    this.lastSpeed = 2873;

                    break;
                }

                case 3: {
                    this.lastSpeed = sqrt + (settingSpeed.isEnabled() ? settingSpeed.getValue().intValue() : 0);

                    break;
                }
            }

            if (this.jumps > 3) {
                this.lastSpeed = sqrt + (settingSpeed.isEnabled() ? settingSpeed.getValue().intValue() : 0);

                this.jumps = 1;
            }
        } else {
            this.lastSpeed = 2873;
            this.tickStrafeOld.reset();
        }

        this.speed = this.lastSpeed;
    }

    @Listener
    public void onListenPlayerMove(PlayerMoveEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.isInWeb || mc.player.capabilities.isFlying) {
            return;
        }

        if (settingStrafeOnGround.getValue() == false) {
            if (mc.player.onGround) {
                return;
            }
        }

        this.speedSQRT = Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ());
        this.flag = this.speedSQRT > 0.2873f;

        float speed = this.flag ? (this.speed / 10000f) : 0.2873f;

        this.print("" + this.speed + " " + this.jumps);

        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();

            speed *= (1.0d + 0.2d * (amplifier + 1));
        }

        float playerRotationYaw = mc.player.rotationYaw;
        float playerRotationPitch = mc.player.rotationPitch;

        float playerForward = mc.player.movementInput.moveForward;
        float playerStrafe = mc.player.movementInput.moveStrafe;

        if (playerForward == 0.0d && playerStrafe == 0.0d) {
            event.setX(0d);
            event.setZ(0d);

            if (settingType.getValue() == Type.AUTO_JUMP) {
                KeyUtil.press(mc.gameSettings.keyBindJump, false);
            }

            this.jumps = 0;
            this.tickStrafeOld.reset();
        } else {
            if (playerForward != 0.0d & playerStrafe != 0.0d) {
                if (playerForward != 0.0d) {
                    if (playerStrafe > 0.0d) {
                        playerRotationYaw += (playerForward > 0.0d ? -45 : 45);
                    } else if (playerStrafe < 0d) {
                        playerRotationYaw += (playerForward > 0.0d ? 45 : -45);
                    }

                    playerStrafe = 0f;

                    if (playerForward > 0.0d) {
                        playerForward = 1.0f;
                    } else if (playerForward < 0){
                        playerForward = -1.0f;
                    }
                }
            }

            if (settingType.getValue() == Type.AUTO_JUMP) {
                KeyUtil.press(mc.gameSettings.keyBindJump, true);

                mc.player.setSprinting(this.jumps >= 1);
            }

            boolean isJumping = mc.gameSettings.keyBindJump.isKeyDown();

            if (isJumping) {
                if (mc.player.onGround) {
                    ++this.jumps;

                    if (settingStrafeSmoothJump.getValue() == false) {
                        speed = 0.6174077f;
                    }

                    event.setY(mc.player.motionY = getMotionJumpY());
                }
            }
        }

        double x = Math.cos(Math.toRadians(playerRotationYaw + 90f));
        double z = Math.sin(Math.toRadians(playerRotationYaw + 90f));

        event.setX(playerForward * speed * x + playerStrafe * speed * z);
        event.setZ(playerForward * speed * z - playerStrafe * speed * x);
    }

    public float getMotionJumpY() {
        float y = 0.40123128f;

        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            final int amplify = mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier();

            y += (amplify + 1) * 0.1f;
        }

        return y;
    }
}
