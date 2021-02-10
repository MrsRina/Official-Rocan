package me.rina.rocan.api.util.crystal;

import me.rina.rocan.Rocan;
import me.rina.turok.util.TurokMath;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 08/02/2021 at 14:42
 **/
public class BlockUtil {
    /**
     * We create a custom block damage to help at some modules.
     */
    public static class BlockDamage {
        private BlockPos pos;
        private EnumFacing facing;

        public BlockDamage(BlockPos pos, EnumFacing facing) {
            this.pos = pos;
            this.facing = facing;
        }

        public BlockPos getPos() {
            return pos;
        }

        public EnumFacing getFacing() {
            return facing;
        }
    }

    public static Block getBlock(BlockPos pos) {
        return Rocan.MC.world.getBlockState(pos).getBlock();
    }

    public static boolean isAir(BlockPos pos) {
        return Rocan.MC.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    public static float getHardness(BlockPos pos) {
        IBlockState blockState = Rocan.MC.world.getBlockState(pos);

        return blockState.getBlockHardness(Rocan.MC.world, pos);
    }

    public static int getDistanceI(BlockPos pos, Entity entity) {
        int x = (int) (pos.x - entity.posX);
        int y = (int) (pos.y - entity.posY);
        int z = (int) (pos.z - entity.posZ);

        return TurokMath.sqrt(x * x + y * y + z * z);
    }

    public static double getDistanceD(BlockPos pos, Entity entity) {
        double x = (pos.x - entity.posX);
        double y = (pos.y - entity.posY);
        double z = (pos.z - entity.posZ);

        return TurokMath.sqrt(x * x + y * y + z * z);
    }
}