package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 10/02/2021 at 12:36
 **/
@Registry(name = "Move GUI", tag = "MoveGUI", description = "You can move while a GUI is open.", category = ModuleCategory.MOVEMENT)
public class ModuleMoveGUI extends Module {
    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayer()) {
            return;
        }

        if (mc.currentScreen instanceof GuiChat || mc.currentScreen == null) {
            return;
        }

        for (KeyBinding keys : KeyUtil.ALL_MOVEMENT_KEY_BIND) {
            if (Keyboard.isKeyDown(keys.getKeyCode())) {
                KeyUtil.press(keys, true);
            } else {
                KeyUtil.press(keys, false);
            }
        }
    }
}
