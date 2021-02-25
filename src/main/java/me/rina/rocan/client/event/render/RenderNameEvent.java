package me.rina.rocan.client.event.render;

import me.rina.rocan.api.event.Event;
import me.rina.rocan.api.event.impl.EventStage;

/**
 * @author SrRina
 * @since 25/02/2021 at 16:26
 **/
public class RenderNameEvent extends Event {
    public RenderNameEvent(EventStage stage) {
        super(stage);
    }
}