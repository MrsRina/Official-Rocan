package me.rina.rocan.client.manager.network;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.manager.Manager;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.turok.util.TurokTick;
import net.minecraft.network.Packet;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 04/02/2021 at 22:52
 **/
public class PacketAntiSpamManager extends Manager {
    public static final float DEFAULT_DELAY = 250f;

    private ArrayList<Packet<?>> queue;
    private Packet<?> last;
    private TurokTick tick = new TurokTick();

    // The default is 250 ms.
    private float delay = DEFAULT_DELAY;
    private int limit = 6;

    private boolean isNext;

    public PacketAntiSpamManager() {
        super("Anti Packet Spam", "Delay queue to send or receive packet.");

        this.queue = new ArrayList<>();
    }

    public void sendPacket(Packet<?> packet) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        this.queue.add(packet);
    }

    public void setQueue(ArrayList<Packet<?>> queue) {
        this.queue = queue;
    }

    public ArrayList<Packet<?>> getQueue() {
        return queue;
    }

    public void setLast(Packet<?> last) {
        this.last = last;
    }

    public Packet<?> getLast() {
        return last;
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

    public void onUpdate() {
        if (this.queue.size() > limit) {
            this.queue.clear();
        }

        for (Packet<?> packets : new ArrayList<>(this.queue)) {
            if (tick.isPassedMS(delay)) {
                Rocan.MC.player.connection.sendPacket(packets);

                this.last = packets;

                this.queue.remove(packets);
                this.isNext = true;

                tick.reset();
            } else {
                this.isNext = false;
                this.last = null;
            }
        }
    }
}
