package me.rina.rocan.api.util.math;

import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 12/02/2021 at 12:20
 **/
public class PosUtil {
    public static BlockPos toBlockPos(double x, double y, double z) {
        return new BlockPos(x, y, z);
    }

    public static boolean collideBlockPos(BlockPos pos, BlockPos another) {
        return pos.getX() == pos.getX() && pos.getY() == another.getY() && pos.getZ() == another.getZ();
    }
}
