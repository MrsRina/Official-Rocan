package me.rina.rocan.client.module.misc;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.client.gui.GuiGameOver;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 02/02/2021 at 00:07
 **/
public class ModuleAutoRespawn extends Module {
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "The secoonds delay for respawn.", 0, 0, 10);

    private TurokTick tick = new TurokTick();

    public ModuleAutoRespawn() {
        super("Auto-Respawn", "AutoRespawn", "Automatically respawn after you die.", ModuleCategory.MISC);
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.currentScreen instanceof GuiGameOver) {
            this.doRequest();
        } else {
            tick.reset();
        }
    }

    public void doRequest() {
        if (tick.isPassedMS(settingDelay.getValue().floatValue() * 1000)) {
            mc.player.respawnPlayer();
        }
    }
}
