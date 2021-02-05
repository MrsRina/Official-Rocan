package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.event.impl.EventStage;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

public class ModuleAutoArmour extends Module {

  private static final Item[] HELMETS = {Items.DIAMOND_HELMET, Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_HELMET, Items.LEATHER_HELMET};
  private static final Item[] CHESTPLATES = {Items.DIAMOND_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.LEATHER_CHESTPLATE};
  private static final Item[] LEGGINGS = {Items.DIAMOND_LEGGINGS, Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.LEATHER_LEGGINGS};
  private static final Item[] BOOTS = {Items.DIAMOND_BOOTS, Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.CHAINMAIL_BOOTS, Items.LEATHER_BOOTS};
  private TurokTick timer = new TurokTick();

  public ModuleAutoArmour() {
    super("Auto Armour", "autoarmour", "Automatically places armour on.", ModuleCategory.Combat);
  }


  @Listener
  public void onTick(ClientTickEvent event) {
    if (mc.player == null) return;
    if(event.getStage() == EventStage.Pre) {
      int selectedSlotId = -1;
      if (timer.isPassedMS((long) 100.69420)) {
        if (mc.player.inventory.armorItemInSlot(2).getItem() == Items.AIR) {
          for (Item item : CHESTPLATES) {
            int slotId = SlotUtil.findItemSlotFromHotBar(item);
            if (slotId != -1) {
              selectedSlotId = slotId;
            }
          }
        }

        if (mc.player.inventory.armorItemInSlot(1).getItem() == Items.AIR) {
          for (Item item : LEGGINGS) {
            int slotId = SlotUtil.findItemSlotFromHotBar(item);
            if (slotId != -1) {
              selectedSlotId = slotId;
            }
          }
        }

        if (mc.player.inventory.armorItemInSlot(0).getItem() == Items.AIR) {
          for (Item item : BOOTS) {
            int slotId = SlotUtil.findItemSlotFromHotBar(item);
            if (slotId != -1) {
              selectedSlotId = slotId;
            }
          }
        }

        if (mc.player.inventory.armorItemInSlot(3).getItem() == Items.AIR) {
          for (Item item : HELMETS) {
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