package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBind;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.HashMap;

/**
 * @author SrRina
 * @since 10/02/2021 at 16:50
 **/
@Registry(name = "Offhand", tag = "Offhand", description = "Automatically set offhand item.", category = ModuleCategory.COMBAT)
public class ModuleOffhand extends Module {
    public static ValueNumber settingSmartTotem = new ValueNumber("Smart Totem", "SmartTotem", "If 0 its disabled, make you don't die.", 0.5f, 0f, 36f);
    public static ValueBoolean settingAutoTotem = new ValueBoolean("Auto totem", "AutoTotem", "Auto disable or enable totem.", true);

    public static ValueBind settingTotem = new ValueBind("Totem", "Totem", "Set totem at hand.", -1);
    public static ValueBind settingEndCrystal = new ValueBind("End Crystal", "EndCrystal", "Set end crystal at hand.", -1);
    public static ValueBind settingGoldenApple = new ValueBind("Golden Apple", "GoldenApple", "Set golden apple at hand.", -1);
    public static ValueBind settingBow = new ValueBind("Bow", "Bow", "Set bow at hand.", -1);

    private boolean isTotem;
    private boolean isEndCrystal;
    private boolean isGoldenApple;
    private boolean isBow;

    private Item item;

    @Listener
    public void onListen(ClientTickEvent event) {
        if (settingTotem.getState()) {
            this.isEndCrystal = false;
            this.isGoldenApple = false;
            this.isBow = false;

            if (this.isTotem == false) {
                settingEndCrystal.setState(false);
                settingGoldenApple.setState(false);
                settingBow.setState(false);

                this.print("Totem Of Undying");

                this.isTotem = true;
            }
        }
    }

    public void doOffhand(Item item) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.currentScreen instanceof GuiInventory) {
            return;
        }

        if (item != Items.TOTEM_OF_UNDYING && settingSmartTotem.getValue().floatValue() != 0f && mc.player.getHealth() <= settingSmartTotem.getValue().floatValue()) {
            settingTotem.setEnabled(true);

            return;
        }

        if (mc.player.getHeldItemOffhand().getItem() == item) {
            return;
        }

        int slot = SlotUtil.findItemSlotFromInventory(item);

        if (slot != -1) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
        }
    }
}