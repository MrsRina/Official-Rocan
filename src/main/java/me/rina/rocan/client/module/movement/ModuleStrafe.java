package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.entity.PlayerMoveEvent;
import me.rina.turok.util.TurokMath;
import net.minecraft.init.MobEffects;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 23/02/2021 at 23:29
 **/
@Registry(name = "Strafe", tag = "Strafe", description = "Allows you control air movement.", category = ModuleCategory.MOVEMENT)
public class ModuleStrafe extends Module {
    public static ValueEnum settingType = new ValueEnum("Type", "Type", "Type strafe.", Type.OLD);

    /* Old misc. */
    public static ValueBoolean settingOldStrafeOnGround = new ValueBoolean("On Ground", "OnGround", "On ground controls.", true);
    public static ValueBoolean settingOldStrafeSmoothJump = new ValueBoolean("Smooth Jump", "SmoothJump", "Smooth jumping.", true);
    public static ValueNumber settingOldStrafeSpeedSprint = new ValueNumber("Speed Sprint", "SpeedSprint", "Add cool speed if player is sprinting.", 0, 0, 10000);

    public enum Type {
        OLD;
    }

    private int oldSpeed;
    private int oldSpeedLast;
    private double oldSpeedSQRT;

    private boolean flagOldSpeed;

    @Override
    public void onSetting() {
        settingOldStrafeOnGround.setEnabled(settingType.getValue() == Type.OLD);
        settingOldStrafeSmoothJump.setEnabled(settingType.getValue() == Type.OLD);
        settingOldStrafeSpeedSprint.setEnabled(settingType.getValue() == Type.OLD);
    }

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        switch ((Type) settingType.getValue()) {
            case OLD: {
                this.doOldTick();

                break;
            }
        }
    }

    @Listener
    public void onListenPlayerMove(PlayerMoveEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        switch ((Type) settingType.getValue()) {
            case OLD: {
                this.doOld(event);

                break;
            }
        }
    }

    public void doOldTick() {
        if (this.flagOldSpeed && mc.player.isSprinting()) {
            this.oldSpeedLast = (int) (this.oldSpeedSQRT * 10000d) + settingOldStrafeSpeedSprint.getValue().intValue();
        } else {
            this.oldSpeedLast = 2873;
        }

        this.oldSpeed = (int) TurokMath.lerp(this.oldSpeed, this.oldSpeedLast, mc.isGamePaused() ? mc.renderPartialTicksPaused : mc.getRenderPartialTicks());
    }

    public void doOld(PlayerMoveEvent event) {
        if (mc.player.isSneaking() || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.isInWeb || mc.player.capabilities.isFlying) {
            return;
        }

        if (settingOldStrafeOnGround.getValue() == false && mc.player.onGround) {
            return;
        }

        this.oldSpeedSQRT = Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ());
        this.flagOldSpeed = this.oldSpeedSQRT > 0.2873f;

        float speed = this.oldSpeed / 10000f;

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

            if (KeyUtil.isPressed(mc.gameSettings.keyBindJump) && mc.player.onGround) {
                if (settingOldStrafeSmoothJump.getValue()) {
                    speed = 0.6174077f;
                }

                event.setY(mc.player.motionY = getMotionJumpY());
            }

            double x = ((playerForward * speed) * Math.cos(Math.toRadians(playerRotationYaw + 90f)) + (playerStrafe * speed) * Math.sin(Math.toRadians(playerRotationYaw + 90f)));
            double z = ((playerForward * speed) * Math.sin(Math.toRadians(playerRotationYaw + 90f)) - (playerStrafe * speed) * Math.cos(Math.toRadians(playerRotationYaw + 90f)));

            event.setX(x);
            event.setZ(z);
        }
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
