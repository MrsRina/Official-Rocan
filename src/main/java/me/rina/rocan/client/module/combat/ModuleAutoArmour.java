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
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import static me.rina.rocan.Rocan.MC;

/**
 * @author SrRina
 * @since 16/02/2021 at 12:35
 **/
@Registry(name = "Auto Armour", tag = "AutoArmour", description = "Auto set slot armour, and no, its not in bri ish.", category = ModuleCategory.COMBAT)
public class ModuleAutoArmour extends Module {
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "MS delay for next amour find.", 100, 0, 500);

    private TurokTick tick = new TurokTick();
    private int foundSlot = -1;

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        int k = settingDelay.getValue().intValue();

        if (this.tick.isPassedMS(k)) {
            int bestSlotHelmet = this.findBestArmor(0, ItemUtil.ALL_HELMETS);

            if (bestSlotHelmet != -1) {
                this.doSetArmor(bestSlotHelmet,0);

                return;
            }

            int bestSlotChestPlace = this.findBestArmor(1, ItemUtil.ALL_CHEST_PLATES);

            if (bestSlotChestPlace != -1) {
                this.doSetArmor(bestSlotChestPlace,1);

                return;
            }

            int bestSlotLegging = this.findBestArmor(2, ItemUtil.ALL_LEGGINGS);

            if (bestSlotLegging != -1) {
                this.doSetArmor(bestSlotLegging,2);

                return;
            }

            int bestSlotBoots = this.findBestArmor(3, ItemUtil.ALL_BOOTS);

            if (bestSlotBoots != -1) {
                this.doSetArmor(bestSlotBoots,3);
            }
        }
    }

    public void doSetArmor(int slot, int slotInArmor) {
        if (slot == -1) {
            this.tick.reset();

            return;
        }

        ItemStack bestArmor = SlotUtil.getItemStack(slot);
        ItemStack currentArmor = SlotUtil.getArmourItemStack(slotInArmor);

        int l = ((ItemArmor) bestArmor.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, bestArmor);
        int k = ((ItemArmor) currentArmor.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, currentArmor);

        if (k > l) {
            this.tick.reset();

            return;
        }

        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 5 + slotInArmor, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();

        this.tick.reset();
    }

    public static int findBestArmor(int slotInArmor, Item[] type) {
        ItemStack bestArmor = ItemStack.EMPTY;
        ItemStack currentArmor = SlotUtil.getArmourItemStack(slotInArmor);

        int slot = -1;
        int flag = -1;

        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = SlotUtil.getItemStack(i);

            if (bestArmor == ItemStack.EMPTY) {
                bestArmor = itemStack;
            }

            if (ItemUtil.contains(type, itemStack.getItem()) == false) {
                continue;
            }

            if (itemStack.getItem() instanceof ItemArmor) {
                if ((bestArmor.getItem() instanceof ItemArmor) == false) {
                    bestArmor = itemStack;
                }

                int l = ((ItemArmor) bestArmor.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, bestArmor);
                int k = ((ItemArmor) itemStack.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, itemStack);

                flag = i;

                if (k > l) {
                    bestArmor = itemStack;

                    slot = i;
                }
            }
        }

        return flag;
    }
}
