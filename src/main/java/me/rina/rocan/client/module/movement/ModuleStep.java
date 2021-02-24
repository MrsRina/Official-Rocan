package me.rina.rocan.client.module.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import ibxm.Player;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBind;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.entity.PlayerPositionUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.math.PositionUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokMath;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 18/02/2021 at 09:49
 **/
@Registry(name = "Step", tag = "Step", description = "Step up blocks.", category = ModuleCategory.MOVEMENT)
public class ModuleStep extends Module {
    /* Normal. */
    public static ValueBind settingBindNormal = new ValueBind("Bind Normal", "Bind Normal", "Normal step.", -1);
    public static ValueNumber settingHeight = new ValueNumber("Height", "Height", "Height for step.", 2, 1, 4);
    public static ValueBoolean settingVanilla = new ValueBoolean("Vanilla", "Vanilla", "Vanilla step.", false);
    public static ValueBoolean settingSmooth = new ValueBoolean("Smooth", "Smooth", "Smooth step.", false);
    public static ValueBoolean settingDisable = new ValueBoolean("Disable", "Disable", "Disable after you step up.", false);

    /* Reverse. */
    public static ValueBoolean settingReverse = new ValueBoolean("Reverse", "Reverse", "Reverse step.", false);
    public static ValueBind settingBindReverse = new ValueBind("Bind Reverse", "BindReverse", "Step but reverse.", -1);
    public static ValueBoolean settingHole = new ValueBoolean("Hole", "Hole", "Only holes reverse.", true);

    private int packetSpam;
    private int normalStepAlert;

    @Override
    public void onSetting() {
        settingBindReverse.setEnabled(settingReverse.getValue());
        settingHole.setEnabled(settingReverse.getValue());

        if (settingReverse.getValue() == false) {
            settingBindReverse.setState(false);
        }
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (settingBindNormal.getState()) {
            if (this.normalStepAlert != -1) {
                this.print(ChatFormatting.GREEN + "Normal step");
                this.normalStepAlert = -1;
            }

            this.doNormal();
        } else {
            if (this.normalStepAlert != 0) {
                this.print(ChatFormatting.RED + "Normal step");
                this.normalStepAlert = 0;
            }

            if (mc.player.stepHeight != 0) {
                mc.player.stepHeight = 0;
            }
        }

        if (settingBindReverse.getState()) {
            this.doReverse();
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        if (NullUtil.isPlayer()) {
            return;
        }

        mc.player.stepHeight = 0;
    }

    public void doNormal() {
        float[] stepHeight = this.getStepHeight();

        if (settingVanilla.getValue()) {
            mc.player.stepHeight = settingHeight.getValue().intValue();
        } else {
            if (mc.player.stepHeight != 0) {
                mc.player.stepHeight = 0;
            }
        }

        if (mc.player.collidedHorizontally && mc.player.onGround) {
            ++this.packetSpam;
        }

        if (mc.player.onGround && mc.player.isInsideOfMaterial(Material.WATER) == false && mc.player.isInsideOfMaterial(Material.LAVA) == false && mc.player.isInWeb == false && mc.player.collidedVertically && mc.player.fallDistance == 0f && KeyUtil.isPressed(mc.gameSettings.keyBindJump) == false && mc.player.collidedHorizontally && mc.player.isOnLadder() == false && this.packetSpam > stepHeight.length - 2) {
            Vec3d playerPosition = PlayerUtil.getVec();

            for (float positions : stepHeight) {
                PacketUtil.send(new CPacketPlayer.Position(playerPosition.x, playerPosition.y + positions, playerPosition.z, true));
            }

            Vec3d flag = new Vec3d(playerPosition.x, playerPosition.y + stepHeight[stepHeight.length - 1], playerPosition.z);

            if (settingSmooth.getValue()) {
                Vec3d interpolation = TurokMath.lerp(playerPosition, flag, Rocan.getClientEventManager().getCurrentRender3DPartialTicks());

                PlayerUtil.setPosition(interpolation.x, interpolation.y, interpolation.z);
            } else {
                PlayerUtil.setPosition(flag.x, flag.y, flag.z);
            }

            this.packetSpam = 0;

            if (settingDisable.getValue()) {
                settingBindNormal.setState(false);
            }
        }
    }

    public void doReverse() {

    }

    public float[] getStepHeight() {
        float[] height = new float[] {0};

        switch (settingHeight.getValue().intValue()) {
            case 1: {
                height = BlockUtil.ONE_BLOCK_HEIGHT;

                break;
            }

            case 2: {
                height = BlockUtil.TWO_BLOCKS_HEIGHT;

                break;
            }

            case 3: {
                height = BlockUtil.THREE_BLOCKS_HEIGHT;

                break;
            }

            case 4: {
                height = BlockUtil.FOUR_BLOCKS_HEIGHT;

                break;
            }
        }

        return height;
    }
}
