package me.rina.rocan.client.module.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
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
    public static ValueBoolean renderRGB = new ValueBoolean("RGB", "RGB", "RGB effect.", false);

    public static ValueNumber renderRed = new ValueNumber("Red", "Red", "Color line range red.", 255, 0, 255);
    public static ValueNumber renderGreen = new ValueNumber("Green", "Green", "Color line range green.", 0, 0, 255);
    public static ValueNumber renderBlue = new ValueNumber("Blue", "Blue", "Color line range blue.", 255, 0, 255);
    public static ValueNumber renderAlpha = new ValueNumber("Alpha", "Alpha", "Color line range alpha.", 255, 0, 255);

    /*
     * Outline config;
     */
    public static ValueEnum renderOutline = new ValueEnum("Outline", "Outline", "Outline effect.", RenderOutline.Enabled);

    enum RenderOutline {
        Enabled, Disabled;
    }

    public static ValueNumber renderOutlineLineSize = new ValueNumber("Outline Line Size", "OutlineLineSize", "Line size.", 1.0f, 0.1f, 3.0f);
    public static ValueBoolean renderOutlineRGB = new ValueBoolean("Outline RGB", "OutlineRGB", "360 color range.", false);

    public static ValueNumber renderOutlineRed = new ValueNumber("Outline Red", "OutlineRed", "Color line range red.", 255, 0, 255);
    public static ValueNumber renderOutlineGreen = new ValueNumber("Outline Green", "OutlineGreen", "Color line range green.", 0, 0, 255);
    public static ValueNumber renderOutlineBlue = new ValueNumber("Outline Blue", "OutlineBlue", "Color line range blue.", 255, 0, 255);
    public static ValueNumber renderOutlineAlpha = new ValueNumber("Outline Alpha", "OutlineAlpha", "Color line range alpha.", 255, 0, 255);

    public ModuleBlockHighlight() {
        super("Block Highlight", "BlockHighlight", "Draw block over the mouse.", ModuleCategory.Render);
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
        if (NullUtil.isWorld()) {
            return;
        }

        Color color = new Color(renderRed.getValue().intValue(), renderGreen.getValue().intValue(), renderBlue.getValue().intValue(), renderAlpha.getValue().intValue());
        Color colorOutline = new Color(renderOutlineRed.getValue().intValue(), renderOutlineGreen.getValue().intValue(), renderOutlineBlue.getValue().intValue(), renderOutlineAlpha.getValue().intValue());

        RayTraceResult splitResult = mc.objectMouseOver;

        if (splitResult != null && splitResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos block = splitResult.getBlockPos();

            if (mc.world.getBlockState(block).getBlock() != Blocks.AIR) {
                Render3DUtil.render3DSolid(camera, block, color);

                if (renderOutline.getValue() == RenderOutline.Enabled) {
                    float line = (float) renderOutlineLineSize.getValue();

                    Render3DUtil.render3DOutline(camera, block, line, colorOutline);
                }
            }
        }
    }
}
