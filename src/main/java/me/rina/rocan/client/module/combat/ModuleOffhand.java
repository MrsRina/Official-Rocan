package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBind;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
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
    /* Misc. */
    public static ValueBoolean settingStatus = new ValueBoolean("Status", "Status", "Show type of item at status module.", true);
    public static ValueNumber settingSmartTotem = new ValueNumber("Smart Totem", "SmartTotem", "If 0 its disabled, make you don't die.", 1, 0, 20);
    public static ValueBoolean settingAutoTotem = new ValueBoolean("Auto totem", "AutoTotem", "Auto disable or enable totem.", true);

    /* Types. */
    public static ValueBind settingTotem = new ValueBind("Totem", "Totem", "Set totem at hand.", -1);
    public static ValueBind settingEndCrystal = new ValueBind("End Crystal", "EndCrystal", "Set end crystal at hand.", -1);
    public static ValueBind settingGoldenApple = new ValueBind("Golden Apple", "GoldenApple", "Set golden apple at hand.", -1);
    public static ValueBind settingBow = new ValueBind("Bow", "Bow", "Set bow at hand.", -1);

    public static ValueEnum settingFindMode = new ValueEnum("Find Mode", "FindMode", "Modes to find the item(s).", FindMode.FULL);

    public enum FindMode {
        INVENTORY, HOT_BAR, FULL;
    }

    private boolean isTotem;
    private boolean isEndCrystal;
    private boolean isGoldenApple;
    private boolean isBow;

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

                this.status(settingStatus.getValue() ? "T" : "");
                this.print("Totem Of Undying");

                this.isTotem = true;
            }

            doOffhand(Items.TOTEM_OF_UNDYING);
        }

        if (settingEndCrystal.getState()) {
            this.isTotem = false;
            this.isGoldenApple = false;
            this.isBow = false;

            if (this.isEndCrystal == false) {
                settingTotem.setState(false);
                settingGoldenApple.setState(false);
                settingBow.setState(false);

                this.status(settingStatus.getValue() ? "E" : "");
                this.print("End Crystal");

                this.isEndCrystal = true;
            }

            doOffhand(Items.END_CRYSTAL);
        }

        if (settingGoldenApple.getState()) {
            this.isTotem = false;
            this.isEndCrystal = false;
            this.isBow = false;

            if (this.isGoldenApple == false) {
                settingTotem.setState(false);
                settingEndCrystal.setState(false);
                settingBow.setState(false);

                this.status(settingStatus.getValue() ? "G" : "");
                this.print("Golden Apple");

                this.isGoldenApple = true;
            }

            doOffhand(Items.GOLDEN_APPLE);
        }

        if (settingBow.getState()) {
            this.isTotem = false;
            this.isEndCrystal = false;
            this.isGoldenApple = false;

            if (this.isBow == false) {
                settingTotem.setState(false);
                settingEndCrystal.setState(false);
                settingGoldenApple.setState(false);

                this.status(settingStatus.getValue() ? "B" : "");
                this.print("Bow");

                this.isBow = true;
            }

            doOffhand(Items.BOW);
        }

        if (settingAutoTotem.getValue() && settingTotem.getState() == false && settingEndCrystal.getState() == false && settingGoldenApple.getState() == false && settingBow.getState() == false) {
            settingTotem.setState(true);
        }
    }

    public void doOffhand(Item item) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiContainerCreative) {
            return;
        }

        if (item != Items.TOTEM_OF_UNDYING && settingSmartTotem.getValue().intValue() != 0f && mc.player.getHealth() <= settingSmartTotem.getValue().intValue()) {
            settingTotem.setEnabled(true);
        }

        if (mc.player.getHeldItemOffhand().getItem() == item) {
            return;
        }

        int slot = doFindSlot(item);

        if (slot != -1) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
        }
    }

    public int doFindSlot(Item item) {
        int slot = -1;

        switch ((FindMode) settingFindMode.getValue()) {
            case FULL: {
                slot = SlotUtil.findItemSlot(item);

                break;
            }

            case INVENTORY: {
                slot = SlotUtil.findItemSlotFromInventory(item);

                break;
            }

            case HOT_BAR: {
                slot = SlotUtil.findItemSlotFromHotBar(item);

                break;
            }
        }

        return slot;
    }
}