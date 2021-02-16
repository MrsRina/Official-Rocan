package me.rina.rocan.api.util.entity;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.util.math.PositionUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import me.rina.turok.util.TurokMath;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author SrRina
 * @since 16/02/2021 at 09:13
 **/
public class PlayerPositionUtil {
    public static void teleportation(BlockPos pos) {
        Vec3d vec = PositionUtil.toVec(pos);

        teleportation(vec);
    }

    public static void teleportation(Vec3d vec) {
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;

        boolean flag = Rocan.MC.player.onGround;

        PacketUtil.send(new CPacketPlayer.Position(x, y, z, flag));
        PlayerUtil.setPosition(x, y, z);
    }

    public static void smooth(BlockPos pos, float partialTicks) {
        Vec3d vec = PositionUtil.toVec(pos);

        smooth(vec, partialTicks);
    }

    public static void smooth(Vec3d vec, float partialTicks) {
        Vec3d last = PlayerUtil.getVec();
        Vec3d interpolation = TurokMath.lerp(last, vec, partialTicks);

        boolean flag = Rocan.MC.player.onGround;

        PlayerUtil.setPosition(interpolation.x, interpolation.y, interpolation.z);
        PacketUtil.send(new CPacketPlayer.Position(interpolation.x, interpolation.y, interpolation.z, flag));
    }
}
