package me.rina.rocan.api.util.item;

import me.rina.rocan.Rocan;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import static me.rina.rocan.Rocan.MC;

/**
 * @author SrRina
 * @since 02/02/2021 at 13:31
 **/
public class SlotUtil {
    public static int getCurrentItemSlotHotBar() {
        int slot = MC.player.inventory.currentItem;

        return slot;
    }

    public static Item getCurrentItemHotBar() {
        Item item = MC.player.inventory.getStackInSlot(MC.player.inventory.currentItem).getItem();

        return item;
    }

    public static int findItemSlotFromInventory(Item item) {
        int slot = -1;

        for (int i = 0; i < 45; i++) {
            Item items = MC.player.inventory.getStackInSlot(i).getItem();

            if (items == item) {
                slot = i;
            }
        }

        return slot;
    }

    public static int findItemSlot(Item item) {
        int slot = -1;

        for (int i = 0; i < 36; i++) {
            Item items = MC.player.inventory.getStackInSlot(i).getItem();

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
            Item items = MC.player.inventory.getStackInSlot(i).getItem();

            if (items == item) {
                slot = i;

                break;
            }
        }

        return slot;
    }

    public static ItemStack getItemStack(int slot) {
        return MC.player.inventory.getStackInSlot(slot);
    }
}
