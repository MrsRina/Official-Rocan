package me.rina.rocan.api.util.entity;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.util.math.RotationUtil;
import me.rina.rocan.client.module.combat.ModuleSurround;
import me.rina.turok.util.TurokMath;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author SrRina
 * @since 14/02/2021 at 11:40
 **/
public class PlayerRotationUtil {
    public enum RotationMode {
        PACKET, MANUAL, NONE;
    }

    public static void packet(Vec3d vec) {
        float[] rotate = RotationUtil.legit(vec);

        float yaw = rotate[0];
        float pitch = rotate[1];

        boolean flag = Rocan.MC.player.onGround;

        Rocan.MC.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, flag));
    }

    public static void packet(float yaw, float pitch) {
        boolean flag = Rocan.MC.player.onGround;

        Rocan.MC.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, flag));
    }

    public static void manual(Vec3d vec) {
        float[] rotate = RotationUtil.legit(vec);

        float yaw = rotate[0];
        float pitch = rotate[1];

        PlayerUtil.setYaw(yaw);
        PlayerUtil.setPitch(pitch);
    }

    public static void manual(float yaw, float pitch) {
        PlayerUtil.setYaw(yaw);
        PlayerUtil.setPitch(pitch);
    }

    public static void makeRotate(Vec3d hit, RotationMode rotate) {
        switch (rotate) {
            case PACKET: {
                packet(hit);
            }

            case MANUAL: {
                manual(hit);
            }

            case NONE: {
                // No rotate!
            }
        }
    }
}
