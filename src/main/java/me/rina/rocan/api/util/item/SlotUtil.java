package me.rina.rocan.api.util.item;

import me.rina.rocan.Rocan;
import net.minecraft.item.Item;

/**
 * @author SrRina
 * @since 02/02/2021 at 13:31
 **/
public class SlotUtil {
    public static int getCurrentItemSlotHotBar() {
        int slot = Rocan.MC.player.inventory.currentItem;

        return slot;
    }

    public static Item getCurrentItemHotBar() {
        Item item = Rocan.MC.player.inventory.getStackInSlot(Rocan.MC.player.inventory.currentItem).getItem();

        return item;
    }

    public static int findItemSlot(Item item) {
        int slot = -1;

        for (int i = 0; i < 45; i++) {
            Item items = Rocan.MC.player.inventory.getStackInSlot(i).getItem();

            if (items == item) {
                slot = i;
            }
        }

        return slot;
    }

    public static int findItemSlotFromInventory(Item item) {
        int slot = -1;

        for (int i = 0; i < 36; i++) {
            Item items = Rocan.MC.player.inventory.getStackInSlot(i).getItem();

            if (items == item) {
                if (i < 9) {
                    i += 36;
                }

                slot = i;
            }
        }

        return slot;
    }

    public static int findItemSlotFromHotBar(Item item) {
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            Item items = Rocan.MC.player.inventory.getStackInSlot(i).getItem();

            if (items == item) {
                slot = i;

                break;
            }
        }

        return slot;
    }
}
