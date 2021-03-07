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

        if (this.tick.isPassedMS(settingDelay.getValue().intValue())) {
            if (SlotUtil.getArmourItem(0) == Items.AIR) {
                for (Item helmets : ItemUtil.ALL_HELMETS) {
                    int slot = SlotUtil.findItemSlotFromInventory(helmets);

                    if (slot != -1) {
                        this.foundSlot = slot;
                    }
                }
            }

            if (SlotUtil.getArmourItem(1) == Items.AIR) {
                for (Item helmets : ItemUtil.ALL_CHEST_PLATES) {
                    int slot = SlotUtil.findItemSlotFromInventory(helmets);

                    if (slot != -1) {
                        this.foundSlot = slot;
                    }
                }
            }

            if (SlotUtil.getArmourItem(2) == Items.AIR) {
                for (Item helmets : ItemUtil.ALL_LEGGINGS) {
                    int slot = SlotUtil.findItemSlotFromInventory(helmets);

                    if (slot != -1) {
                        this.foundSlot = slot;
                    }
                }
            }

            if (SlotUtil.getArmourItem(3) == Items.AIR) {
                for (Item helmets : ItemUtil.ALL_BOOTS) {
                    int slot = SlotUtil.findItemSlotFromInventory(helmets);

                    if (slot != -1) {
                        this.foundSlot = slot;
                    }
                }
            }

            if (this.foundSlot != -1) {
                mc.playerController.windowClick(0, this.foundSlot, 0, ClickType.QUICK_MOVE, MC.player);

                this.tick.reset();
                this.foundSlot = -1;
            }
        }
    }
}
