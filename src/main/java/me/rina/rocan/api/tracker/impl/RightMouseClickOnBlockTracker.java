package me.rina.rocan.api.tracker.impl;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.util.network.PacketUtil;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 06/02/2021 at 00:59
 **/
public class RightMouseClickOnBlockTracker extends PacketUtil.PacketTracker {
    private BlockPos pos;
    private EnumFacing facing;
    private EnumHand hand;

    private float facingX;
    private float facingY;
    private float facingZ;

    public RightMouseClickOnBlockTracker(BlockPos pos, EnumFacing facing, EnumHand hand, float facingX, float facingY, float facingZ) {
        super("Right Mouse Click On Block Tracker", new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, facingX, facingY, facingZ));

        this.pos = pos;
        this.facing = facing;
        this.hand = hand;

        this.facingX = facingX;
        this.facingY = facingY;
        this.facingZ = facingZ;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacingX(float facingX) {
        this.facingX = facingX;
    }

    public float getFacingX() {
        return facingX;
    }

    public void setFacingY(float facingY) {
        this.facingY = facingY;
    }

    public float getFacingY() {
        return facingY;
    }

    public void setFacingZ(float facingZ) {
        this.facingZ = facingZ;
    }

    public float getFacingZ() {
        return facingZ;
    }

    public void setHand(EnumHand hand) {
        this.hand = hand;
    }

    public EnumHand getHand() {
        return hand;
    }

    @Override
    public void onPre() {
    }

    @Override
    public void onPost() {
        Rocan.MC.player.swingArm(this.hand);
    }
}
