package me.rina.rocan.client.event.entity;

import me.rina.rocan.api.event.Event;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 08/02/2021 at 15:31
 **/
public class PlayerDamageBlockEvent extends Event {
    private BlockPos pos;
    private EnumFacing facing;

    public PlayerDamageBlockEvent(BlockPos pos, EnumFacing facing) {
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
