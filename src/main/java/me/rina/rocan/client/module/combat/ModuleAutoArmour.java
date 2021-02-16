package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.item.ItemUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 16/02/2021 at 12:35
 **/
@Registry(name = "Auto Armour", tag = "AutoArmour", description = "Auto set slot armour, and no, its not in bri ish.", category = ModuleCategory.COMBAT)
public class ModuleAutoArmour extends Module {
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "MS delay for next amour find.", 100, 0, 500);
    public static ValueBoolean settingPriority = new ValueBoolean("Priority", "Priority", "Place the best armour.", true);
    public static ValueEnum settingFindMode = new ValueEnum("Find Mode", "FindMode", "Find mode to armours.", Mode.INVENTORY);

    private TurokTick tick = new TurokTick();
    private int currentArmourSlot = -1;
    private boolean isSwap;

    public enum Mode {
        INVENTORY, HOT_BAR;
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (this.tick.isPassedMS(settingDelay.getValue().intValue())) {
            if (SlotUtil.getArmourItem(3) == Items.AIR || settingPriority.getValue()) {
                if (SlotUtil.getArmourItem(3) != Items.AIR) {
                    int slot = getArmourSlot(SlotUtil.getArmourItem(3), ItemUtil.ALL_HELMETS, 3);

                    if (slot != -1) {
                        currentArmourSlot = slot;

                        this.isSwap = true;
                    }
                } else {
                    int slot = getArmourSlot(SlotUtil.getArmourItem(3), ItemUtil.ALL_HELMETS, 3);

                    this.isSwap = false;
                    this.currentArmourSlot = slot;
                }
            }

            if (this.currentArmourSlot != -1) {
                if (this.isSwap) {
                    SlotUtil.swap(mc.player.inventoryContainer.windowId, this.currentArmourSlot);
                    SlotUtil.swap(mc.player.inventoryContainer.windowId, 3);
                } else {
                    SlotUtil.pickup(0, this.currentArmourSlot);
                }

                this.isSwap = false;
                this.tick.reset();
                this.currentArmourSlot = -1;
            }
        }
    }

    public int getArmourSlot(Item item, Item[] list, int flag) {
        int find = -1;

        for (Item items : list) {
            int slot = findSlot(items);

            if (slot == -1) {
                continue;
            }

            ItemStack itemStack = SlotUtil.getItemStack(slot);

            if (itemStack.isEmpty()) {
                continue;
            }

            if (settingPriority.getValue()) {
                if (itemStack.getItem() instanceof ItemArmor) {
                    ItemArmor armor = (ItemArmor) itemStack.getItem();

                    int flagDamage = armor.damageReduceAmount;
                    int flagEnchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, itemStack);

                    int flagCurrentDamage = SlotUtil.getArmourItemArmor(flag).damageReduceAmount;
                    int flagCurrentEnchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, SlotUtil.getArmourItemStack(flag));

                    if (flagCurrentDamage + flagCurrentEnchant <= flagDamage + flagEnchant) {
                        find = slot;

                        break;
                    }
                }
            } else {
                if (itemStack.getItem() instanceof ItemArmor) {
                    find = slot;

                    break;
                }
            }
        }

        return find;
    }

    public int findSlot(Item item) {
        int slot = -1;

        if (settingFindMode.getValue() == Mode.INVENTORY) {
            slot = SlotUtil.findItemSlotFromInventory(item);
        }

        if (settingFindMode.getValue() == Mode.HOT_BAR) {
            slot = SlotUtil.findItemSlotFromHotBar(item);
        }

        return slot;
    }
}
