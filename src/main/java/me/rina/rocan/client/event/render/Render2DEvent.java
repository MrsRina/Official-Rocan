package me.rina.rocan.client.event.render;

import me.rina.rocan.api.event.Event;
import me.rina.rocan.api.event.impl.EventStage;

/**
 * @author SrRina
 * @since 06/12/20 at 12:22am
 */
public class Render2DEvent extends Event {
    private float partialTicks;

    public Render2DEvent(float partialTicks) {
        super(EventStage.Pre);

        this.partialTicks = partialTicks;
    }

    protected void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
