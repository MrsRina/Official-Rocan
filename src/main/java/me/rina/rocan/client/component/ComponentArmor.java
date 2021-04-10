package me.rina.rocan.client.component;

import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.component.impl.ComponentType;
import me.rina.rocan.api.component.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.turok.render.opengl.TurokGL;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokMath;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author SrRina
 * @since 04/04/2021 at 18:57
 **/
@Registry(name = "Armor", tag = "Armor", description = "Armor preview.", type = ComponentType.NO_TEXT)
public class ComponentArmor extends Component {
    /* Misc settings. */
    public static ValueBoolean settingHotbar = new ValueBoolean("Hotbar Position", "HotbarPosition", "Set the position in hotbar.", true);
    public static ValueBoolean settingReverse = new ValueBoolean("Reverse", "Reverse", "Reverse armors.", false);
    public static ValueNumber settingAlpha = new ValueNumber("Alpha", "Alpha", "The background alpha!", 100, 0, 255);

    private float offset;

    @Override
    public void onRenderHUD(float partialTicks) {
        if (NullUtil.isPlayer()) {
            return;
        }

        this.offset = TurokMath.lerp(this.offset, mc.world.getBlockState(PlayerUtil.getBlockPos().up()).getBlock() == Blocks.WATER ? 10 : 0, partialTicks);

        TurokGL.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableTexture2D();

        RenderItem renderItem = mc.getRenderItem();

        // :troll_face:
        final TurokDisplay display = new TurokDisplay(mc);

        final float l = display.getScaledWidth() / 2f;
        final float k = display.getScaledHeight() - 55 - (this.offset);

        if (settingHotbar.getValue()) {
            this.rect.setX(l + 8);
            this.rect.setY(k);
        }

        int x = (int) this.rect.getX();
        int y = (int) this.rect.getY();

        if (settingReverse.getValue()) {
            for (int i = 3; i >= 0; --i) {
                ItemStack itemStack = SlotUtil.getArmourItemStack(i);

                if (itemStack.getItem() != Items.AIR) {
                    renderItem.renderItemAndEffectIntoGUI(itemStack, x, y);
                    renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x, y, null);
                }

                x += 16;
            }
        } else {
            for (int i = 0; i < 4; i++) {
                ItemStack itemStack = SlotUtil.getArmourItemStack(i);

                if (itemStack.getItem() != Items.AIR) {
                    renderItem.renderItemAndEffectIntoGUI(itemStack, x, y);
                    renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x, y, null);
                }

                x += 16;
            }
        }

        renderItem.zLevel = -5f;

        GlStateManager.disableTexture2D();
        RenderHelper.disableStandardItemLighting();
        TurokGL.popMatrix();

        this.rect.setHeight(16);
        this.rect.setWidth(16 * 4);
    }
}
