package me.rina.rocan.mixin;

import io.netty.channel.ChannelHandlerContext;
import me.rina.rocan.Rocan;
import me.rina.rocan.client.event.network.EventPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author SrRina
 * @since 16/11/20 at 10:05pm
 */
@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        EventPacket.Send eventPacket_Send = new EventPacket.Send(packet);

        Rocan.EVENT_BUS.dispatchEvent(eventPacket_Send);

        if (eventPacket_Send.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        EventPacket.Receive eventPacket_Receive = new EventPacket.Receive(packet);

        Rocan.EVENT_BUS.dispatchEvent(eventPacket_Receive);

        if (eventPacket_Receive.isCanceled()) {
            callbackInfo.cancel();
        }
    }
}
