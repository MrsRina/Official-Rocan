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
public class PlayerStartBreakBlockTracker extends PacketUtil.PacketTracker {
    private BlockUtil.BlockDamage block;
    private EnumHand hand;

    public PlayerStartBreakBlockTracker(EnumHand hand, BlockUtil.BlockDamage block) {
        super("Break Block", new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, block.getPos(), block.getFacing()));

        this.block = block;
        this.hand = hand;
    }

    public void setBlock(BlockUtil.BlockDamage block) {
        this.block = block;
    }

    public BlockUtil.BlockDamage getBlock() {
        return block;
    }

    public void setHand(EnumHand hand) {
        this.hand = hand;
    }

    public EnumHand getHand() {
        return hand;
    }

    @Override
    public void onPre() {
        if (this.hand != null) {
            Rocan.MC.player.swingArm(this.hand);
        }
    }

    @Override
    public void onPost() {
    }
}
