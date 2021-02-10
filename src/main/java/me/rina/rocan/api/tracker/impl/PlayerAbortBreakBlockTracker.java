package me.rina.rocan.api.tracker.impl;

import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;

/**
 * @author SrRina
 * @since 09/02/2021 at 23:40
 **/
public class PlayerAbortBreakBlockTracker extends PacketUtil.PacketTracker {
    private BlockUtil.BlockDamage block;

    public PlayerAbortBreakBlockTracker(BlockUtil.BlockDamage block) {
        super("Abort Break Tracker", new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, block.getPos(), block.getFacing()));

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
