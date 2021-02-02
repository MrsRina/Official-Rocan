package me.rina.rocan.client.module.misc;

import cat.yoink.eventmanager.Listener;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.gui.minecraft.CustomDeathGUI;
import me.rina.turok.util.TurokTick;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;

/**
 * @author SrRina
 * @since 02/02/2021 at 00:07
 **/
public class ModuleAutoRespawn extends Module {
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "Delay for respawn.", 0.5f, 0f, 10f);
    public static ValueBoolean settingCloseGUI = new ValueBoolean("Close GUI", "CloseGUI", "Close death screen GUI.", true);

    private TurokTick tick = new TurokTick();

    private CustomDeathGUI customDeathGUI = new CustomDeathGUI(null);

    public ModuleAutoRespawn() {
        super("Auto-Respawn", "AutoRespawn", "Automatically close dead guiscreen.", ModuleCategory.Misc);
    }

    @Listener
    public void onListenClientEvent(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (settingCloseGUI.getValue()) {
            if (mc.player.isDead || mc.player.getHealth() <= 0) {
                mc.currentScreen = null;
                mc.displayGuiScreen(null);

                doRequest();
            } else {
                tick.reset();
            }
        } else {
            if (mc.currentScreen instanceof GuiGameOver) {
                doRequest();
            } else {
                tick.reset();
            }
        }
    }

    public void doRequest() {
        if (tick.isPassedMS(settingDelay.getValue().floatValue() * 1000)) {
            mc.player.respawnPlayer();

            tick.reset();
        }
    }
}
