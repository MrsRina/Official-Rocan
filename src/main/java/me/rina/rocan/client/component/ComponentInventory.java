package me.rina.rocan.client.component;

import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.component.impl.ComponentType;
import me.rina.rocan.api.component.registry.Registry;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.module.client.ModuleHUD;
import me.rina.turok.render.opengl.TurokGL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;

/**
 * @author SrRina
 * @since 04/04/2021 at 18:41
 **/
@Registry(name = "Inventory", tag = "Inventory", description = "Draws the inventory player.", type = ComponentType.NO_TEXT)
public class ComponentInventory extends Component {
    public static ValueNumber settingAlpha = new ValueNumber("Alpha", "Alpha", "The background alpha!", 100, 0, 255);

    @Override
    public void onRenderHUD(float partialTicks) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        TurokGL.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();

        if (!ModuleManager.get(ModuleHUD.class).isEnabled()) {
            this.render(this.rect, false, new Color(0, 0, 0, settingAlpha.getValue().intValue()));
        }

        RenderItem renderItem = mc.getRenderItem();

        for (int i = 0; i < 27; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i + 9);

            int x = (int) this.rect.getX() + (i % 9) * 16;
            int y = (int) this.rect.getY() + (i / 9) * 16;

            if (itemStack.getItem() != Items.AIR) {
                renderItem.renderItemAndEffectIntoGUI(itemStack, x, y);
                renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x, y, null);
            }
        }

        renderItem.zLevel = -5f;

        RenderHelper.disableStandardItemLighting();
        TurokGL.popMatrix();

        this.rect.setWidth(144);
        this.rect.setHeight(48);
    }
}
