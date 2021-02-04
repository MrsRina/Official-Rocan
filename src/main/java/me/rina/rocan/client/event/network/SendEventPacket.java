package me.rina.rocan.client.event.network;

import me.rina.rocan.api.event.Event;
import me.rina.rocan.api.event.impl.EventStage;
import net.minecraft.network.Packet;

/**
 * @author SrRina
 * @since 03/02/2021 at 22:46
 **/
public class SendEventPacket extends Event {
    private Packet<?> packet;

    public SendEventPacket(Packet<?> packet) {
        super(EventStage.Post);

        this.packet = packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
