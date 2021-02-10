package me.rina.rocan.api.util.client;

import me.rina.rocan.Rocan;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author SrRina
 * @since 06/02/2021 at 12:50
 **/
public class KeyUtil {
    public static final KeyBinding[] ALL_MOVEMENT_KEY_BIND = {
            Rocan.MC.gameSettings.keyBindForward, Rocan.MC.gameSettings.keyBindBack, Rocan.MC.gameSettings.keyBindLeft, Rocan.MC.gameSettings.keyBindRight
    };

    public static void press(KeyBinding keyBinding, boolean pressed) {
        KeyBinding.setKeyBindState(keyBinding.getKeyCode(), pressed);
    }

    public static boolean isMoving() {
        if (NullUtil.isPlayerWorld()) {
            return false;
        }

        return (Rocan.MC.gameSettings.keyBindForward.isKeyDown() || Rocan.MC.gameSettings.keyBindBack.isKeyDown() || Rocan.MC.gameSettings.keyBindLeft.isKeyDown() || Rocan.MC.gameSettings.keyBindRight.isKeyDown());
    }

    public static boolean isJumping() {
        return Rocan.MC.gameSettings.keyBindJump.isKeyDown();
    }

    public static boolean isPressed(KeyBinding keyBinding) {
        return keyBinding.isKeyDown();
    }
}