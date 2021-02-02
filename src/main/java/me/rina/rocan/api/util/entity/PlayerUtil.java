package me.rina.rocan.api.util.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 28/01/2021 at 16:54
 **/
public class PlayerUtil {
    public static BlockPos getBlockPos() {
        return new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ);
    }
}
