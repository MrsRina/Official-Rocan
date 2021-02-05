package me.rina.rocan.api.util.entity;

import me.rina.rocan.Rocan;
import me.rina.turok.util.TurokMath;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 28/01/2021 at 16:54
 **/
public class PlayerUtil {
    public static BlockPos getBlockPos() {
        return new BlockPos(Rocan.MC.player.posX,  Rocan.MC.player.posY,  Rocan.MC.player.posZ);
    }

    public static double[] getPos() {
        return new double[] {
                Rocan.MC.player.posX, Rocan.MC.player.posY, Rocan.MC.player.posZ
        };
    }

    public static double[] getLastPos() {
        return new double[] {
                Rocan.MC.player.lastTickPosX, Rocan.MC.player.lastTickPosY, Rocan.MC.player.lastTickPosZ
        };
    }

    public static double[] getMotion() {
        return new double[] {
                Rocan.MC.player.motionX, Rocan.MC.player.motionY, Rocan.MC.player.motionZ
        };
    }

    public static double getSpeed() {
        double[] motion = getMotion();

        return TurokMath.sqrt(motion[0] * motion[0] + motion[1] * motion[1] + motion[2] * motion[2]);
    }
}
