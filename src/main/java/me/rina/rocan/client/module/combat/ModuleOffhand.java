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
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.currentScreen instanceof GuiInventory) {
            return;
        }

        // Find the item offhand.
        this.doFind();

        // Set the item in offhand.
        if (this.item == null) {
            return;
        }

        if (mc.player.getHeldItemOffhand().getItem() == this.item) {
            return;
        }

        int slot = SlotUtil.findItemSlot(this.item);

        if (slot == -1) {
            return;
        }

        /*
         * We click at slot, pick, pick again at offhand (45) and for end unpick and update the controller.
         */
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }

    public void doFind() {
        if (settingTotem.getState()) {
            settingEndCrystal.setState(false);
            settingGoldenApple.setState(false);
            settingBow.setState(false);

            if (this.item != Items.TOTEM_OF_UNDYING) {
                this.print("Offhand totem is enabled.");
                this.setStatus("T");

                this.isTotem = true;
                this.isEndCrystal = false;
                this.isGoldenApple = false;
                this.isBow = false;
            }

            this.item = Items.TOTEM_OF_UNDYING;
        }

        if (settingEndCrystal.getState()) {
            settingTotem.setState(false);
            settingGoldenApple.setState(false);
            settingBow.setState(false);

            if (this.item != Items.END_CRYSTAL) {
                this.print("Offhand end crystal is enabled.");
                this.setStatus("C");

                this.isTotem = false;
                this.isEndCrystal = true;
                this.isGoldenApple = false;
                this.isBow = false;
            }

            this.item = Items.END_CRYSTAL;
        }

        if (settingGoldenApple.getState()) {
            settingTotem.setState(false);
            settingEndCrystal.setState(false);
            settingBow.setState(false);

            if (this.item != Items.GOLDEN_APPLE) {
                this.print("Offhand golden apple is enabled.");
                this.setStatus("G");

                this.isTotem = false;
                this.isEndCrystal = false;
                this.isGoldenApple = true;
                this.isBow = false;
            }

            this.item = Items.GOLDEN_APPLE;
        }

        if (settingBow.getState()) {
            settingTotem.setState(false);
            settingEndCrystal.setState(false);
            settingGoldenApple.setState(false);

            if (this.item != Items.BOW) {
                this.print("Offhand bow is enabled.");
                this.setStatus("B");

                this.isTotem = false;
                this.isEndCrystal = false;
                this.isGoldenApple = false;
                this.isBow = true;
            }

            this.item = Items.BOW;
        }

        if ((settingSmartTotem.getValue().floatValue() != 0f && mc.player.getHealth() <= settingSmartTotem.getValue().floatValue()) || (settingAutoTotem.getValue() && (this.isEndCrystal || this.isGoldenApple || this.isBow) == false)) {
            settingTotem.setState(true);
        }
    }
}