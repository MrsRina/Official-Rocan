package me.rina.rocan.api.util.entity;

import jdk.nashorn.internal.ir.Block;
import me.rina.rocan.Rocan;
import me.rina.turok.util.TurokMath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author SrRina
 * @since 28/01/2021 at 16:54
 **/
public class PlayerUtil {
    public enum Dimension {
        WORLD, NETHER, END;
    }

    public static BlockPos getBlockPos() {
        return new BlockPos(Rocan.MC.player.posX, Rocan.MC.player.posY, Rocan.MC.player.posZ);
    }

    public static Vec3d getVec() {
        return new Vec3d(Rocan.MC.player.posX, Rocan.MC.player.posY, Rocan.MC.player.posZ);
    }

    /**
     * 0, 1, 2 are position x, y & z;
     *
     * @return position
     */
    public static double[] getPos() {
        return new double[] {
                Rocan.MC.player.posX, Rocan.MC.player.posY, Rocan.MC.player.posZ
        };
    }

    /**
     * 0, 1, 2 are last tick pos x, y & z;
     *
     * @return last tick pos.
     */
    public static double[] getLastTickPos() {
        return new double[] {
                Rocan.MC.player.lastTickPosX, Rocan.MC.player.lastTickPosY, Rocan.MC.player.lastTickPosZ
        };
    }

    /**
     * 0, 1, 2 are prev position x, y & z;
     *
     * @return prev position
     */
    public static double[] getPrevPos() {
        return new double[] {
                Rocan.MC.player.prevPosX, Rocan.MC.player.prevPosY, Rocan.MC.player.prevPosZ
        };
    }

    /**
     * 0, 1, 2 are motion x, y & z;
     *
     * @return motions movement
     */
    public static double[] getMotion() {
        return new double[] {
                Rocan.MC.player.motionX, Rocan.MC.player.motionY, Rocan.MC.player.motionZ
        };
    }

    /**
     * 0 is head yaw, 1 yaw player & 2 pitch.
     *
     * @return rotations player
     */
    public static float[] getRotation() {
        return new float[] {Rocan.MC.player.rotationYawHead, Rocan.MC.player.rotationYaw, Rocan.MC.player.rotationPitch};
    }

    /**
     * Calculate blocks per second.
     *
     * @return blocks per tick
     */
    public static double getBPS() {
        double[] prevPosition = getPrevPos();
        double[] position = getPos();

        // Delta values.
        double x = position[0] - prevPosition[0];
        double z = position[2] - prevPosition[2];

        return TurokMath.sqrt(x * x + z * z) / (Rocan.MC.timer.tickLength / 1000.0d);
    }

    public static void setPosition(double x, double y, double z) {
        Rocan.MC.player.setPosition(x, y, z);
    }

    public static void setYaw(float yaw) {
        Rocan.MC.player.rotationYaw = yaw;
        Rocan.MC.player.rotationYawHead = yaw;
    }

    public static void setPitch(float pitch) {
        Rocan.MC.player.rotationPitch = pitch;
    }

    public static Dimension getCurrentDimension() {
        Dimension dimension = null;

        if (Rocan.MC.player.dimension == -1) {
            dimension = Dimension.NETHER;
        }

        if (Rocan.MC.player.dimension == 0) {
            dimension = Dimension.WORLD;
        }

        if (Rocan.MC.player.dimension == 1) {
            dimension = Dimension.END;
        }

        return dimension;
    }
}
