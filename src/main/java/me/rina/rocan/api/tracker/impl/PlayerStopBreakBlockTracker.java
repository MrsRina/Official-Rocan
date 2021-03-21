package me.rina.rocan.api.tracker.impl;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 09/02/2021 at 19:18
 **/
public class PlayerStopBreakBlockTracker extends PacketUtil.PacketTracker {
    private BlockUtil.BlockDamage block;

    public PlayerStopBreakBlockTracker(BlockUtil.BlockDamage block) {
            super("Break Block", new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block.getPos(), block.getFacing()));

        this.block = block;
    }

    public void setBlock(BlockUtil.BlockDamage block) {
        this.block = block;
    }

    public BlockUtil.BlockDamage getBlock() {
        return block;
    }

    @Override
    public void onPre() {
    }

    @Override
    public void onPost() {
    }
}