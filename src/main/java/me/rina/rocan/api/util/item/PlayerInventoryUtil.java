package me.rina.rocan.api.util.item;

import me.rina.rocan.Rocan;

/**
 * @author SrRina
 * @since 07/03/2021 at 18:24
 **/
public class PlayerInventoryUtil {
    public static void setCurrentHotBarItem(int slotIn) {
        Rocan.MC.player.inventory.currentItem = slotIn;
    }
}
