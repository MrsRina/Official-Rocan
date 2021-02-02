package me.rina.rocan.api.util.client;

import me.rina.rocan.Rocan;

/**
 * @author SrRina
 * @since 02/02/2021 at 00:01
 **/
public class NullUtil {
    public static boolean isPlayerWorld() {
        return Rocan.MC.player == null && Rocan.MC.world == null;
    }

    public static boolean isWorld() {
        return Rocan.MC.world == null;
    }

    public static boolean isPlayer() {
        return Rocan.MC.player == null;
    }

    public static boolean isGUI() {
        return Rocan.MC.currentScreen == null;
    }
}
