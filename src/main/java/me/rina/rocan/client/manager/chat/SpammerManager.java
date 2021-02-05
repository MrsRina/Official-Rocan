package me.rina.rocan.client.manager.chat;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.manager.Manager;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.turok.util.TurokTick;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 04/02/2021 at 19:09
 **/
public class SpammerManager extends Manager {
    private ArrayList<String> queue;
    private TurokTick tick = new TurokTick();

    private float delay;
    private int limit;

    private boolean isNext;

    public SpammerManager() {
        super("Spammer", "Manage");

        this.queue = new ArrayList<>();
    }

    public void setQueue(ArrayList<String> queue) {
        this.queue = queue;
    }

    public ArrayList<String> getQueue() {
        return queue;
    }

    public void setTick(TurokTick tick) {
        this.tick = tick;
    }

    public TurokTick getTick() {
        return tick;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getDelay() {
        return delay;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setNext(boolean next) {
        isNext = next;
    }

    public boolean isNext() {
        return isNext;
    }

    public void send(String message) {
        this.queue.add(message);
    }

    public void onUpdate() {
        if (this.queue.size() > limit) {
            this.queue.clear();
        }

        for (String messages : new ArrayList<>(this.queue)) {
            if (tick.isPassedMS(delay * 1000f)) {
                ChatUtil.message(messages);

                this.queue.remove(messages);
                this.isNext = true;

                tick.reset();
            } else {
                this.isNext = false;
            }
        }
    }
}
