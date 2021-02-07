package me.rina.rocan.client.event.render;

import me.rina.rocan.api.event.Event;
import net.minecraft.util.EnumHandSide;

public class EnumHandSideEvent extends Event {

    private final EnumHandSide handSide;

    public EnumHandSideEvent(EnumHandSide handSide) {
        this.handSide = handSide;
    }

    public EnumHandSide getHandSide() {
        return handSide;
    }
}
