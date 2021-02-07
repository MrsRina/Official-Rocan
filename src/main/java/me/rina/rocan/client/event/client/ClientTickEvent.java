package me.rina.rocan.client.event.client;

import me.rina.rocan.api.event.Event;
import me.rina.rocan.api.event.impl.EventStage;

/**
 * @author SrRina
 * @since 06/12/20 at 12:02am
 */
public class ClientTickEvent extends Event {
    public ClientTickEvent() {
        super(EventStage.PRE);
    }
}