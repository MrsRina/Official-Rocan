package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.entity.PlayerMoveEvent;
import me.rina.rocan.client.event.network.PacketEvent;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 28/02/2021 at 21:54
 **/
@Registry(name = "Freecam", tag = "Freecam", description = "Cancel your server movements and fly client.", category = ModuleCategory.MOVEMENT)
public class ModuleFreecam extends Module {
    public static ValueNumber settingSpeed = new ValueNumber("Speed", "Speed", "Speed fly.", 50, 0, 100);
    public static ValueBoolean settingRotate = new ValueBoolean("Rotate", "Rotate", "Enable rotation for you watch your player rotating at freecam.", true);
    public static ValueBoolean settingRotateHead = new ValueBoolean("Rotate Head", "RotateHead", "Enable head rotate to your main player at freecam.", true);

    private EntityOtherPlayerMP customPlayer;
    private Entity ridingEntity;
    private boolean isRiding;

    private double[] lastPosition;
    private float[] lastRotation;

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        mc.player.setVelocity(0, 0, 0);

        mc.player.capabilities.isFlying = true;
        mc.player.capabilities.setFlySpeed((settingSpeed.getValue().intValue() * 2) / 1000f);

        this.customPlayer.setRotationYawHead(settingRotateHead.getValue() ? mc.player.getRotationYawHead() : this.lastRotation[0]);

        if (settingRotate.getValue()) {
            this.customPlayer.rotationYaw = mc.player.rotationYaw;
            this.customPlayer.rotationPitch = mc.player.rotationPitch;
        }

        float playerRotationYaw = mc.player.rotationYaw;
        float playerRotationPitch = mc.player.rotationPitch;

        float playerForward = mc.player.movementInput.moveForward;
        float playerStrafe = mc.player.movementInput.moveStrafe;

        float speed = (settingSpeed.getValue().intValue() * 6) / 1000f;

        if (playerForward == 0.0d && playerStrafe == 0.0d) {
            mc.player.motionX = (0d);
            mc.player.motionZ = (0d);
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
        }

        mc.player.motionX = ((playerForward * speed) * Math.cos(Math.toRadians(playerRotationYaw + 90f)) + (playerStrafe * speed) * Math.sin(Math.toRadians(playerRotationYaw + 90f)));
        mc.player.motionZ = ((playerForward * speed) * Math.sin(Math.toRadians(playerRotationYaw + 90f)) - (playerStrafe * speed) * Math.cos(Math.toRadians(playerRotationYaw + 90f)));

    }

    @Listener
    public void onListenPushPlayer(PlayerSPPushOutOfBlocksEvent event) {
        event.setCanceled(true);
    }

    @Listener
    public void onListenEvent(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) {
            event.setCanceled(true);
        }
    }

    @Listener
    public void onListenPlayerMove(PlayerMoveEvent event) {
        mc.player.noClip = true;
    }

    @Override
    public void onDisable() {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        mc.player.capabilities.isFlying = false;
        mc.player.noClip = false;

        mc.world.removeEntityFromWorld(-100);

        if (this.isRiding) {
            mc.player.startRiding(this.ridingEntity, true);
        } else {
            mc.player.setPositionAndRotation(this.lastPosition[0], this.lastPosition[1], this.lastPosition[2], this.lastRotation[1], this.lastRotation[2]);
        }
    }

    @Override
    public void onEnable() {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        this.lastPosition = PlayerUtil.getPos();
        this.lastRotation = PlayerUtil.getRotation();

        this.customPlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        this.customPlayer.copyLocationAndAnglesFrom(mc.player);

        this.isRiding = (mc.player.isRiding() && mc.player.getRidingEntity() != null);

        if (this.isRiding) {
            this.ridingEntity = mc.player.getRidingEntity();

            mc.player.dismountRidingEntity();
        }

        mc.world.addEntityToWorld(-100, this.customPlayer);
    }
}
