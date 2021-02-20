package me.rina.rocan.client.module.client;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.item.ItemStack;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 20/02/2021 at 00:18
 **/
@Registry(name = "Developer", tag = "Developer", description = "Test stuff!", category = ModuleCategory.CLIENT)
public class ModuleDeveloper extends Module {
    public static ValueNumber settingSlot = new ValueNumber("Slot ID", "SlotID", "The slot ID of minecraft stack slot.", 0, 0, 45);

    @Listener
    public void onListenClientTickEvent(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        ItemStack itemStackSlot = mc.player.inventory.getStackInSlot(settingSlot.getValue().intValue());

        this.print(itemStackSlot.getItem().getItemStackDisplayName(itemStackSlot));
    }
}
