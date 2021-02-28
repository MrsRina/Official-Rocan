package me.rina.rocan.api.util.crystal;

import me.rina.rocan.Rocan;
import me.rina.turok.util.TurokMath;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 08/02/2021 at 14:42
 **/
public class BlockUtil {
    public static float[] ONE_BLOCK_HEIGHT = {
            0.42f, 0.75f
    };

    public static float[] TWO_BLOCKS_HEIGHT = {
            0.4f, 0.75f, 0.5f, 0.41f, 0.83f, 1.16f, 1.41f, 1.57f, 1.58f, 1.42f
    };

    public static float[] THREE_BLOCKS_HEIGHT = {
            0.42f, 0.78f, 0.63f, 0.51f, 0.9f, 1.21f, 1.45f, 1.43f, 1.78f, 1.63f, 1.51f, 1.9f, 2.21f, 2.45f, 2.43f
    };

    public static float[] FOUR_BLOCKS_HEIGHT = {
            0.42f, 0.78f, 0.63f, 0.51f, 0.9f, 1.21f, 1.45f, 1.43f, 1.78f, 1.63f, 1.51f, 1.9f, 2.21f, 2.45f, 2.43f, 2.78f, 2.63f, 2.51f, 2.9f, 3.21f, 3.45f, 3.43f
    };

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

    /**
     * For all air blocks type, the tall grass and snow is considered air.
     *
     * @param pos
     * @return
     */
    public static boolean isAir(BlockPos pos) {
        return Rocan.MC.world.getBlockState(pos).getBlock() == Blocks.AIR || Rocan.MC.world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER || Rocan.MC.world.getBlockState(pos).getBlock() == Blocks.TALLGRASS;
    }

    public static boolean isUnbreakable(BlockPos pos) {
        IBlockState blockState = Rocan.MC.world.getBlockState(pos);

        return Rocan.MC.world.getBlockState(pos).getBlockHardness(Rocan.MC.world, pos) == -1;
    }

    public static float getHardness(BlockPos pos) {
        IBlockState blockState = Rocan.MC.world.getBlockState(pos);

        return blockState.getBlockHardness(Rocan.MC.world, pos);
    }

    public static IBlockState getState(BlockPos pos) {
        return Rocan.MC.world.getBlockState(pos);
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

    public static EnumFacing getFacing(BlockPos pos, EntityLivingBase entityLivingBase) {
        return EnumFacing.getDirectionFromEntityLiving(pos, entityLivingBase);
    }
}