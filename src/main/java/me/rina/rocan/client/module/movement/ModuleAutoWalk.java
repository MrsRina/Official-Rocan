package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * Created by Jake!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * Update by Rina at 07/02/21 at 21:03.
 */
@Registry(name = "Auto-Walk", tag = "AutoWalk", description = "Automatically walks.", category = ModuleCategory.MOVEMENT)
public class ModuleAutoWalk extends Module {
    @Listener
    public void onUpdate(ClientTickEvent event) {
        KeyUtil.press(mc.gameSettings.keyBindForward, true);
    }

    @Override
    public void onDisable() {
        KeyUtil.press(mc.gameSettings.keyBindForward, false);
    }
}