package me.rina.rocan.client.module.render;

import cat.yoink.eventmanager.Listener;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.util.render.Render3DUtil;
import me.rina.rocan.client.event.render.Render3DEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

/**
 * @author SrRina
 * @since 06/12/20 at 03:48pm
 */
public class ModuleBlockHighlight extends Module {
    public static Setting renderRGB    = new Setting("RGB", "RGB", "360 color range.", false);
    public static Setting renderRed    = new Setting("Red", "Red", "Color line range red.", 255, 0, 255);
    public static Setting renderGreen  = new Setting("Green", "Green", "Color line range green.", 0, 0, 255);
    public static Setting renderBlue   = new Setting("Blue", "Blue", "Color line range blue.", 255, 0, 255);
    public static Setting renderAlpha  = new Setting("Alpha", "Alpha", "Color line range alpha.", 255, 0, 255);

    /*
     * Outline config;
     */
    public static Setting renderOutline = new Setting("Outline", "Outline", "Outline effect on block render.", RenderOutline.Enabled);

    public enum RenderOutline {
        Enabled, Disabled;
    }

    public static Setting renderOutlineLineSize  = new Setting("Outline Line Size", "OutlineLineSize", "Line size.", 1.0f, 0.1f, 3.0f);
    public static Setting renderOutlineRGB       = new Setting("Outline RGB", "OutlineRGB", "360 color range.", false);
    public static Setting renderOutlineRed       = new Setting("Outline Red", "OutlineRed", "Color line range red.", 255, 0, 255);
    public static Setting renderOutlineGreen     = new Setting("Outline Green", "OutlineGreen", "Color line range green.", 0, 0, 255);
    public static Setting renderOutlineBlue      = new Setting("Outline Blue", "OutlineBlue", "Color line range blue.", 255, 0, 255);
    public static Setting renderOutlineAlpha     = new Setting("Outline Alpha", "OutlineAlpha", "Color line range alpha.", 255, 0, 255);

    public ModuleBlockHighlight() {
        super("Block Highlight", "BlockHighlight", "Render block over split mouse.", ModuleCategory.Render);
    }

    @Listener
    public void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (renderOutline.getValue() == RenderOutline.Enabled) {
            renderOutlineLineSize.setEnabled(true);
            renderOutlineRed.setEnabled(true);
            renderOutlineGreen.setEnabled(true);
            renderOutlineBlue.setEnabled(true);
            renderOutlineAlpha.setEnabled(true);
        } else {
            renderOutlineLineSize.setEnabled(false);
            renderOutlineRed.setEnabled(false);
            renderOutlineGreen.setEnabled(false);
            renderOutlineBlue.setEnabled(false);
            renderOutlineAlpha.setEnabled(false);
        }

        if ((boolean) renderRGB.getValue()) {
            renderRed.setValue(Rocan.getClientEventManager().getCurrentRGBColor().getRed());
            renderGreen.setValue(Rocan.getClientEventManager().getCurrentRGBColor().getGreen());
            renderBlue.setValue(Rocan.getClientEventManager().getCurrentRGBColor().getBlue());
        }

        if ((boolean) renderOutlineRGB.getValue()) {
            renderRed.setValue(Rocan.getClientEventManager().getCurrentRGBColor().getRed());
            renderGreen.setValue(Rocan.getClientEventManager().getCurrentRGBColor().getGreen());
            renderBlue.setValue(Rocan.getClientEventManager().getCurrentRGBColor().getBlue());
        }

        Color color = new Color((int) renderRed.getValue(), (int) renderGreen.getValue(), (int) renderBlue.getValue(), (int) renderAlpha.getValue());
        Color colorOutline = new Color((int) renderOutlineRed.getValue(), (int) renderOutlineGreen.getValue(), (int) renderOutlineBlue.getValue(), (int) renderOutlineAlpha.getValue());

        RayTraceResult splitResult = mc.objectMouseOver;

        if (splitResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockPosition = splitResult.getBlockPos();

            if (mc.world.getBlockState(blockPosition) != Blocks.AIR) {
                Render3DUtil.render3DSolid(camera, blockPosition, color);

                if (renderOutline.getValue() == RenderOutline.Enabled) {
                    float line = (float) renderOutlineLineSize.getValue();

                    Render3DUtil.render3DOutline(camera, blockPosition, line, colorOutline);
                }
            }
        }
    }
}
