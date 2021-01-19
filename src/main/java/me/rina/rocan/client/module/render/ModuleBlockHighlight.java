package me.rina.rocan.client.module.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.render.Render3DUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

/**
 * @author SrRina
 * @since 06/12/20 at 03:48pm
 */
public class ModuleBlockHighlight extends Module {
    public static Setting<Boolean> renderRGB    = new Setting("RGB", "RGB", "360 color range.", false);
    public static Setting<Integer> renderRed    = new Setting("Red", "Red", "Color line range red.", 255, 0, 255);
    public static Setting<Integer> renderGreen  = new Setting("Green", "Green", "Color line range green.", 0, 0, 255);
    public static Setting<Integer> renderBlue   = new Setting("Blue", "Blue", "Color line range blue.", 255, 0, 255);
    public static Setting<Integer> renderAlpha  = new Setting("Alpha", "Alpha", "Color line range alpha.", 255, 0, 255);

    /*
     * Outline config;
     */
    public static Setting<RenderOutline> renderOutline = new Setting("Outline", "Outline", "Outline effect on block render.", RenderOutline.Enabled);

    public enum RenderOutline {
        Enabled, Disabled;
    }

    public static Setting<Float> renderOutlineLineSize    = new Setting("Outline Line Size", "OutlineLineSize", "Line size.", 1.0f, 0.1f, 3.0f);
    public static Setting<Boolean> renderOutlineRGB       = new Setting("Outline RGB", "OutlineRGB", "360 color range.", false);
    public static Setting<Integer> renderOutlineRed       = new Setting("Outline Red", "OutlineRed", "Color line range red.", 255, 0, 255);
    public static Setting<Integer> renderOutlineGreen     = new Setting("Outline Green", "OutlineGreen", "Color line range green.", 0, 0, 255);
    public static Setting<Integer> renderOutlineBlue      = new Setting("Outline Blue", "OutlineBlue", "Color line range blue.", 255, 0, 255);
    public static Setting<Integer> renderOutlineAlpha     = new Setting("Outline Alpha", "OutlineAlpha", "Color line range alpha.", 255, 0, 255);

    public ModuleBlockHighlight() {
        super("Block Highlight", "BlockHighlight", "Render block over split mouse.", ModuleCategory.Render);
    }

    @Override
    public void onSetting() {
        renderOutlineLineSize.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        renderOutlineRed.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        renderOutlineGreen.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        renderOutlineBlue.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        renderOutlineAlpha.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        renderOutlineRGB.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);

        if (renderRGB.getValue()) {
            renderRed.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[0]);
            renderGreen.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[1]);
            renderBlue.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[2]);
        }

        if (renderOutlineRGB.getValue()) {
            renderOutlineRed.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[0]);
            renderOutlineGreen.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[1]);
            renderOutlineBlue.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[2]);
        }
    }

    @Override
    public void onRender3D() {
        if (mc.world == null) {
            return;
        }

        Color color = new Color(255, 255, 255, 255);
        Color colorOutline = new Color(255, 255, 255, 255);

        RayTraceResult splitResult = mc.objectMouseOver;

        if (splitResult != null && splitResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            ChatUtil.print(splitResult.toString());

            BlockPos blockPosition = splitResult.getBlockPos();

            if (mc.world.getBlockState(blockPosition) != Blocks.AIR) {
                Render3DUtil.render3DSolid(camera, blockPosition, color);

                if (renderOutline.getValue() == RenderOutline.Enabled) {
                    float line = renderOutlineLineSize.getValue();

                    Render3DUtil.render3DOutline(camera, blockPosition, line, colorOutline);
                }
            }
        }
    }
}
