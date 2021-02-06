package me.rina.rocan.api.tracker;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.util.network.PacketUtil;
import me.rina.turok.util.TurokTick;
import net.minecraft.network.Packet;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 05/02/2021 at 12:34
 **/
public class Tracker {
    private String name;

    private ArrayList<PacketUtil.PacketTracker> queue;
    private TurokTick tick;

    private float delay;

    private boolean isNext;
    private boolean isRegistry;

    public Tracker(String name) {
        this.name = name;

        this.queue = new ArrayList<>();
        this.tick = new TurokTick();

        this.delay = 250f;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQueue(ArrayList<PacketUtil.PacketTracker> queue) {
        this.queue = queue;
    }

    public ArrayList<PacketUtil.PacketTracker> getQueue() {
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

    public void setNext(boolean next) {
        isNext = next;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setRegistry(boolean registry) {
        isRegistry = registry;
    }

    public boolean isRegistry() {
        return isRegistry;
    }

    /**
     * Inject in constructor it in any class you will use the tracker.
     */
    public void inject() {
        if (Rocan.getTrackerManager() == null) {
            return;
        }

        Rocan.getTrackerManager().registry(this);
    }

    public void register() {
        this.isRegistry = true;

        if (this.queue.isEmpty() == false) {
            this.queue.clear();
        }
    }

    public void unregister() {
        this.isRegistry = false;

        if (this.queue.isEmpty() == false) {
            this.queue.clear();
        }
    }

    public void send(PacketUtil.PacketTracker packetTracker) {
        packetTracker.onPre();
        PacketUtil.send(packetTracker.getPacket());
        packetTracker.onPost();
    }

    public void join(PacketUtil.PacketTracker packetTracker) {
        this.queue.add(packetTracker);
    }

    public void onUpdate() {
        if (this.queue.isEmpty()) {
            this.tick.reset();
        }

        for (PacketUtil.PacketTracker packets : new ArrayList<>(this.queue)) {
            if (this.tick.isPassedMS(this.delay)) {
                this.send(packets);

                this.queue.remove(packets);
                this.isNext = true;

                this.tick.reset();
            } else {
                this.isNext = false;
            }
        }
    }
}
