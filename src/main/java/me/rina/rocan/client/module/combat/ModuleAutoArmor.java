package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.event.impl.EventStage;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.util.item.ItemUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

@Registry(name = "Auto Armor", tag = "AutoArmor", description = "Automatically places armour on.", category = ModuleCategory.COMBAT)
public class ModuleAutoArmor extends Module {
  private TurokTick timer = new TurokTick();

  @Listener
  public void onTick(ClientTickEvent event) {
    if (mc.player == null) return;
    if(event.getStage() == EventStage.PRE) {
      int selectedSlotId = -1;
      if (timer.isPassedMS((long) 100.69420)) {
        if (mc.player.inventory.armorItemInSlot(2).getItem() == Items.AIR) {
          for (Item item : ItemUtil.ALL_CHEST_PLATES) {
            int slotId = SlotUtil.findItemSlotFromHotBar(item);
            if (slotId != -1) {
              selectedSlotId = slotId;
            }
          }
        }

        if (mc.player.inventory.armorItemInSlot(1).getItem() == Items.AIR) {
          for (Item item : ItemUtil.ALL_LEGGINGS) {
            int slotId = SlotUtil.findItemSlotFromHotBar(item);
            if (slotId != -1) {
              selectedSlotId = slotId;
            }
          }
        }

        if (mc.player.inventory.armorItemInSlot(0).getItem() == Items.AIR) {
          for (Item item : ItemUtil.ALL_BOOTS) {
            int slotId = SlotUtil.findItemSlotFromHotBar(item);
            if (slotId != -1) {
              selectedSlotId = slotId;
            }
          }
        }

        if (mc.player.inventory.armorItemInSlot(3).getItem() == Items.AIR) {
          for (Item item : ItemUtil.ALL_HELMETS) {
            int slotId = SlotUtil.findItemSlotFromHotBar(item);
            if (slotId != -1) {
              selectedSlotId = slotId;
            }
          }
        }

        if (selectedSlotId != -1) {
          if (selectedSlotId < 9)
            selectedSlotId += 36;
          mc.playerController.windowClick(0, selectedSlotId, 0, ClickType.QUICK_MOVE, mc.player);
          timer.reset();
        }
      }
    }
  }
}