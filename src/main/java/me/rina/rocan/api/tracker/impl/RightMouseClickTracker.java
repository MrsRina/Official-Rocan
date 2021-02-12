package me.rina.rocan.api.tracker.impl;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

/**
 * @author SrRina
 * @since 05/02/2021 at 13:16
 **/
public class RightMouseClickTracker extends PacketUtil.PacketTracker {
    private EnumHand hand;

    public RightMouseClickTracker(EnumHand hand) {
        super("Right Mouse Click Tracker", new CPacketPlayerTryUseItem(hand));

        this.hand = hand;
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
        if (this.hand != null) {
            Rocan.MC.player.swingArm(this.hand);
        }
    }
}
