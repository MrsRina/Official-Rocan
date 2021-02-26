package me.rina.rocan.client.event.render;

import me.rina.rocan.api.event.Event;
import me.rina.rocan.api.event.impl.EventStage;

/**
 * @author SrRina
 * @since 25/02/2021 at 22:29
 **/
public class RenderPotionEffects extends Event {
    public RenderPotionEffects(EventStage stage) {
        super(stage);
    }
}
