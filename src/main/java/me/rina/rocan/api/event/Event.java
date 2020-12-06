package me.rina.rocan.api.event;

import me.rina.rocan.api.event.impl.EventStage;

/**
 * @author SrRina
 * @since 15/11/20 at 7:45pm
 */
public class Event {
    private EventStage stage;

    private boolean isCanceled;

    public Event() {}

    public Event(EventStage stage) {
        this.stage = stage;
        this.isCanceled = false;
    }

    public void setStage(EventStage stage) {
        this.stage = stage;
        this.isCanceled = false;
    }

    public EventStage getStage() {
        return stage;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}
