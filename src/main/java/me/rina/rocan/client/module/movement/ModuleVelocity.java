package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.client.event.network.PacketEvent;
import net.minecraft.network.play.server.SPacketExplosion;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 16/02/2021 at 11:01
 **/
@Registry(name = "Velocity", tag = "Velocity", description = "No kinetic force for Minecraft.", category = ModuleCategory.MOVEMENT)
public class ModuleVelocity extends Module {
    public static ValueBoolean settingCancelExplosion = new ValueBoolean("Cancel Explosion", "CancelExplosion", "Client cancel explosion packet event.", true);

    @Listener
    public void onListen(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketExplosion && settingCancelExplosion.getValue()) {
            event.setCanceled(true);
        }
    }
}
