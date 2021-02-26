package me.rina.rocan.client.event.render;

import me.rina.rocan.api.event.Event;
import me.rina.rocan.api.event.impl.EventStage;

/**
 * @author SrRina
 * @since 25/02/2021 at 22:36
 **/
public class RenderPortalOverlayEvent extends Event {
    public RenderPortalOverlayEvent(EventStage stage) {
        super(stage);
    }
}
