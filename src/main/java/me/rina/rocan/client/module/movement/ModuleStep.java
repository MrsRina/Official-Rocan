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
import net.minecraft.util.math.BlockPos;
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
    public static ValueBoolean settingVanilla = new ValueBoolean("Vanilla", "Vanilla", "Vanilla step.", false);
    public static ValueNumber settingHeight = new ValueNumber("Height", "Height", "Height for step.", 2, 1, 4);

    /* Reverse. */
    public static ValueBoolean settingReverse = new ValueBoolean("Reverse", "Reverse", "Reverse step.", false);
    public static ValueBind settingBindReverse = new ValueBind("Bind Reverse", "BindReverse", "Step but reverse.", -1);
    public static ValueBoolean settingHole = new ValueBoolean("Hole", "Hole", "Only holes reverse.", true);
    public static ValueBoolean settingSmooth = new ValueBoolean("Smooth", "Smooth", "Smooth reverse step.", false);

    private int packetSpam;
    private int normalStepAlert;

    @Override
    public void onSetting() {
        settingBindReverse.setEnabled(settingReverse.getValue());
        settingHole.setEnabled(settingReverse.getValue());

        if (settingReverse.getValue() == false) {
            settingBindReverse.setState(false);
        }

        settingHeight.setEnabled(settingVanilla.getValue());
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (this.doVerifyPlayerFlags()) {
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
        if (settingVanilla.getValue()) {
            mc.player.stepHeight = settingHeight.getValue().intValue();

            return;
        } else {
            if (mc.player.stepHeight != 0) {
                mc.player.stepHeight = 0;
            }
        }

        mc.player.stepHeight = 0.5f;

        double step = this.getStepHeight();

        if (step < 0 || step > 2) {
            return;
        }

        if (step == 2.0d) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.78, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.63, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.51, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.9, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.21, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.45, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.43, mc.player.posZ, mc.player.onGround));
            mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
        }

        if (step == 1.5) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.24918707874468, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1707870772188, mc.player.posZ, mc.player.onGround));
            mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
        }

        if (step == 1.0) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
            mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
        }
    }

    public void doReverse() {
        if (settingHole.getValue()) {
            BlockPos pos = PlayerUtil.getBlockPos();

            if (Rocan.getHoleManager().getHoles().contains(pos.add(0, -1, 0)) && !Rocan.getHoleManager().getHoles().contains(pos)) {
                this.doFastFall();
            }
        } else {
            this.doFastFall();
        }
    }

    public void doFastFall() {
        if (settingSmooth.getValue()) {
            mc.player.posY--;
        } else {
            mc.player.posY = -1;
        }
    }

    public boolean doVerifyPlayerFlags() {
        return ((mc.player.onGround == false || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.jump || mc.player.noClip) && mc.player.moveForward == 0 && mc.player.moveStrafing == 0);
    }

    public double getStepHeight() {
        double h = -1d;

        final AxisAlignedBB bb = mc.player.getEntityBoundingBox().offset(0, 0.05, 0).grow(0.05);

        if (mc.world.getCollisionBoxes(mc.player, bb.offset(0, 2, 0)).isEmpty()) {
            return 100;
        }

        for (final AxisAlignedBB aabbs : mc.world.getCollisionBoxes(mc.player, bb)) {
            if (aabbs.maxY > h) {
                h = aabbs.maxY;
            }
        }

        return h - mc.player.posY;
    }
}
