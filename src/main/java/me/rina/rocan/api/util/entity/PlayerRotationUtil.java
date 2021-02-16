package me.rina.rocan.api.util.entity;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.util.math.RotationUtil;
import me.rina.turok.util.TurokMath;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author SrRina
 * @since 14/02/2021 at 11:40
 **/
public class PlayerRotationUtil {
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

    public static void manual(Vec3d vec, float partialTicks) {
        float[] rotate = RotationUtil.legit(vec);

        float yaw = TurokMath.lerp(Rocan.MC.player.rotationYaw, rotate[0], partialTicks);
        float pitch = TurokMath.lerp(Rocan.MC.player.rotationPitch, rotate[1], partialTicks);

        PlayerUtil.setYaw(yaw);
        PlayerUtil.setPitch(pitch);
    }

    public static void manual(float yaw, float pitch, float partialTicks) {
        float _yaw = TurokMath.lerp(Rocan.MC.player.rotationYaw, yaw, partialTicks);
        float _pitch = TurokMath.lerp(Rocan.MC.player.rotationPitch, pitch, partialTicks);

        PlayerUtil.setYaw(yaw);
        PlayerUtil.setPitch(pitch);
    }
}
