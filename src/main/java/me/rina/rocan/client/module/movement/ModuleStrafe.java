package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBind;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.math.PositionUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.entity.PlayerMoveEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 23/02/2021 at 23:29
 **/
@Registry(name = "Strafe", tag = "Strafe", description = "Allows you control air movement.", category = ModuleCategory.MOVEMENT)
public class ModuleStrafe extends Module {
    /* Normal settings. */
    public static ValueBoolean settingStrafeOnGround = new ValueBoolean("On Ground", "OnGround", "On ground controls.", true);
    public static ValueBoolean settingStrafeSmoothJump = new ValueBoolean("Smooth Jump", "SmoothJump", "Smooth jumping.", true);
    public static ValueNumber settingJumpY = new ValueNumber("Jump Y", "JumpY", "The Y divided by 1000.", 401, 0, 420);

    public static ValueEnum settingJumpMode = new ValueEnum("Jump Mode", "JumpMode", "Mode jump.", JumpMode.AUTO);
    public static ValueBind settingBypass = new ValueBind("Bypass", "Bypass", "Make you jump.", -1);

    public enum JumpMode {
        AUTO, MANUAL;
    }

    public static ValueEnum settingStrafingType = new ValueEnum("Strafing Type", "StrafingType", "Uses diff types of strafing calculation.", StrafingType.LARGE);

    public enum StrafingType {
        MINIMAL, LARGE;
    }

    /* Increase speed setting. */
    public static ValueEnum settingIncreaseSpeed = new ValueEnum("Increase Mode", "IncreaseMode", "Increase modes to strafe.", IncreaseMode.NCP);
    public static ValueNumber settingSpeed = new ValueNumber("Speed", "Speed", "Speed boost.", 100, 0, 1000);

    /* NCP. */
    public static ValueNumber settingSpeedTrace = new ValueNumber("Speed Trace", "SpeedTrace", "The speed trace to boost.", 2873, 2873, 3500);

    /* Hop. */
    public static ValueNumber settingJump = new ValueNumber("Jump", "Jump", "Jump count to increase speed.", 3, 1, 10);
    public static ValueBoolean settingReset = new ValueBoolean("Reset", "Reset", "Reset speed in latest jump.", true);
    public static ValueNumber settingBHOPTick = new ValueNumber("BHOP Tick", "BHOPTick", "BHOP update tick.", 250, -1, 1000);

    /* Tick. */
    public static ValueNumber settingTickUpdate = new ValueNumber("Tick Update", "TickUpdate", "The tick for update.", 250, 2, 1000);
    public static ValueNumber settingTimeOutSpeed = new ValueNumber("Time Out Speed", "TimeOutSpeed", "Tiem out tick speed for reset ticks.", 250, 0, 1000);

    public enum IncreaseMode {
        NCP, VANILLA, HOP, TICK, NONE;
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
    private TurokTick tickTick = new TurokTick();
    private TurokTick tickHOP = new TurokTick();

    @Override
    public void onSetting() {
        settingSpeed.setEnabled(settingIncreaseSpeed.getValue() != IncreaseMode.NONE);
        settingSpeedTrace.setEnabled(settingIncreaseSpeed.getValue() == IncreaseMode.NCP);

        settingJump.setEnabled(settingIncreaseSpeed.getValue() == IncreaseMode.HOP);
        settingReset.setEnabled(settingIncreaseSpeed.getValue() == IncreaseMode.HOP);
        settingBHOPTick.setEnabled(settingIncreaseSpeed.getValue() == IncreaseMode.HOP);

        settingTickUpdate.setEnabled(settingIncreaseSpeed.getValue() == IncreaseMode.TICK);
        settingTimeOutSpeed.setEnabled(settingIncreaseSpeed.getValue() == IncreaseMode.TICK);
    }

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (settingBypass.getState() && mc.player.onGround && ((settingJumpMode.getValue() == JumpMode.AUTO && (mc.player.movementInput.moveStrafe != 0d || mc.player.movementInput.moveForward != 0d)) || (settingJumpMode.getValue() == JumpMode.MANUAL && mc.gameSettings.keyBindJump.isKeyDown()))) {
            mc.player.jump();
        }
    }

    @Listener
    public void onListenPlayerMove(PlayerMoveEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.player.isSneaking() || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.isInWeb || mc.player.capabilities.isFlying) {
            return;
        }

        if (settingStrafeOnGround.getValue() == false) {
            if (mc.player.onGround) {
                return;
            }
        }

        int sqrt = speedSQRT < 0.2873 ? 2873 : (int) (this.speedSQRT * 10000);

        if (this.flag) {
            int i = settingSpeed.getValue().intValue();

            switch ((IncreaseMode) settingIncreaseSpeed.getValue()) {
                /*
                 * The NCP mode, based on old Rocan strafe bypass.
                 */
                case NCP: {
                    int k = settingSpeedTrace.getValue().intValue();

                    this.lastSpeed = sqrt >= k ? sqrt + i : sqrt;

                    break;
                }

                /*
                 * Vanilla static strafing, works in server without anti-cheat.
                 */
                case VANILLA: {
                    this.lastSpeed = sqrt + i;

                    break;
                }

                /*
                 * Change speed in hops.
                 */
                case HOP: {
                    int k = settingJump.getValue().intValue();

                    boolean flagHOP = settingBHOPTick.getValue().intValue() != -1 ? this.tickHOP.isPassedMS(settingBHOPTick.getValue().intValue()) : false;

                    if (this.jumps == (k - 1)) {
                        this.lastSpeed = settingReset.getValue() ? sqrt : sqrt + i;
                    }

                    if (this.jumps == k) {
                        this.lastSpeed = sqrt + i;

                        this.jumps = 1;
                    }

                    if (this.jumps == 1 && settingBHOPTick.getValue().intValue() != -1 && mc.player.onGround == false && flagHOP) {
                        event.setY(mc.player.motionY--);

                        this.tickHOP.reset();
                    }

                    break;
                }

                /*
                 * Set speed by MS, spamming speed.
                 */
                case TICK: {
                    int k = settingTickUpdate.getValue().intValue();
                    int t = settingTimeOutSpeed.getValue().intValue();

                    if (this.tickTick.isPassedMS(k)) {
                        this.lastSpeed = sqrt + i;

                        if (this.tickTick.isPassedMS(k + t)) {
                            this.tickTick.reset();
                        }
                    } else {
                        this.lastSpeed = sqrt;
                    }

                    break;
                }

                case NONE: {
                    this.lastSpeed = sqrt;

                    break;
                }
            }
        } else {
            this.lastSpeed = 2873;
            this.tickTick.reset();
            this.tickHOP.reset();
        }

        this.speed = this.lastSpeed;

        this.speedSQRT = Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ());
        this.flag = this.speedSQRT > 0.2873f;

        float speed = this.flag ? (this.speed / 10000f) : 0.2873f;

        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();

            speed *= (1.0d + 0.2d * (amplifier + 1));
        }

        if (mc.player.isSneaking()) {
            speed = 0.1500f;
        }

        float playerRotationYaw = mc.player.rotationYaw;
        float playerRotationPitch = mc.player.rotationPitch;

        float playerForward = mc.player.movementInput.moveForward;
        float playerStrafe = mc.player.movementInput.moveStrafe;

        if (playerForward == 0.0d && playerStrafe == 0.0d) {
            event.setX(0d);
            event.setZ(0d);

            if (settingJumpMode.getValue() == JumpMode.AUTO) {
                KeyUtil.press(mc.gameSettings.keyBindJump, false);
            }

            this.jumps = 0;
        } else {
            if (playerForward != 0.0d && playerStrafe != 0.0d) {
                if (playerForward != 0.0d) {
                    if (playerStrafe > 0.0d) {
                        playerRotationYaw += (playerForward > 0.0d ? -45 : 45);
                    } else if (playerStrafe < 0d) {
                        playerRotationYaw += (playerForward > 0.0d ? 45 : -45);
                    }

                    playerStrafe = 0f;

                    if (playerForward > 0.0d) {
                        playerForward = 1.0f;
                    } else if (playerForward < 0) {
                        playerForward = -1.0f;
                    }
                }
            }

            if (settingJumpMode.getValue() == JumpMode.AUTO) {
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

            switch ((StrafingType) settingStrafingType.getValue()) {
                case MINIMAL: {
                    event.setX((playerForward * speed) * Math.cos(Math.toRadians((playerRotationYaw + 90.0f))) + (playerStrafe * speed) * Math.sin(Math.toRadians((playerRotationYaw + 90.0f))));
                    event.setZ((playerForward * speed) * Math.sin(Math.toRadians((playerRotationYaw + 90.0f))) - (playerStrafe * speed) * Math.cos(Math.toRadians((playerRotationYaw + 90.0f))));

                    break;
                }

                case LARGE: {
                    double x = Math.cos(Math.toRadians(playerRotationYaw + 90f));
                    double z = Math.sin(Math.toRadians(playerRotationYaw + 90f));

                    event.setX(playerForward * speed * x + playerStrafe * speed * z);
                    event.setZ(playerForward * speed * z - playerStrafe * speed * x);

                    break;
                }
            }
        }
    }

    public float getMotionJumpY() {
        float y = settingJumpY.getValue().intValue() / 1000f;

        boolean isCancelled = false;

        for (BlockPos add : new BlockPos[] {
                new BlockPos(1, -1, 0),
                new BlockPos(-1, -1, 0),
                new BlockPos(1, -1, 1),
                new BlockPos(-1, -1, 1),
                new BlockPos(-1, -1, -1),
                new BlockPos(0, -1, 1),
                new BlockPos(0, -1, -1)
        }) {
            BlockPos offset = PlayerUtil.getBlockPos().add(add);

            if (mc.world.getBlockState(offset).getBlock() == Blocks.AIR) {
                isCancelled = true;

                break;
            }
        }

        if (mc.player.collidedHorizontally && y < 0.4 || isCancelled) {
            y = 0.40123128f;
        }

        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            final int amplify = mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier();

            y += (amplify + 1) * 0.1f;
        }

        return y;
    }
}
