package me.rina.rocan.client.event.network;

import me.rina.rocan.api.event.Event;
import me.rina.rocan.api.event.impl.EventStage;
import net.minecraft.network.Packet;

public class EventPacket extends Event {
    public static class Send extends EventPacket {
        public Send(Packet packet) {
            super(packet, EventStage.Pre);
        }
    }

    public static class Receive extends EventPacket {
        public Receive(Packet packet) {
            super(packet, EventStage.Post);
        }
    }

    private Packet packet;

    public EventPacket(Packet packet, EventStage stage) {
        super(stage);

        this.packet = packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
}
