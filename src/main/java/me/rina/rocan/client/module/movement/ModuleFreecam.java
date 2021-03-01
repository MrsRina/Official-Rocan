package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.network.PacketEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 28/02/2021 at 21:54
 **/
@Registry(name = "Freecam", tag = "Freecam", description = "Cancel your server movements and fly client.", category = ModuleCategory.MOVEMENT)
public class ModuleFreecam extends Module {
    public static ValueNumber settingSpeed = new ValueNumber("Speed", "Speed", "Speed fly.", 50, 0, 100);

    private boolean isPacketDisabled;

    @Listener
    public void onListenPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            this.isPacketDisabled = true;

            event.setCanceled(true);
        }
    }

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (this.isPacketDisabled) {
            mc.player.capabilities.isFlying = true;
            mc.player.noClip = true;
        }
    }

    @Override
    public void onDisable() {
        this.isPacketDisabled = false;

        if (mc.player.capabilities.isFlying) {
            mc.player.capabilities.isFlying = false;
        }

        mc.player.noClip = false;
    }

    @Override
    public void onEnable() {
        this.isPacketDisabled = false;
    }
}
