package me.rina.rocan.client.module.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.render.RenderUtil;
import me.rina.rocan.client.module.client.ModuleAntiCheat;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

/**
 * @author SrRina
 * @since 06/12/20 at 03:48pm
 **/
@Registry(name = "Block Highlight", tag = "BlockHighlight", description = "Draw block over the mouse.", category = ModuleCategory.RENDER)
public class ModuleBlockHighlight extends Module {
    /* Misc. */
    public static ValueBoolean settingRGB = new ValueBoolean("RGB", "RGB", "RGB effect.", false);
    public static ValueNumber settingRed = new ValueNumber("Red", "Red", "Color line range red.", 0, 0, 255);
    public static ValueNumber settingGreen = new ValueNumber("Green", "Green", "Color line range green.", 0, 0, 255);
    public static ValueNumber settingBlue = new ValueNumber("Blue", "Blue", "Color line range blue.", 0, 0, 255);
    public static ValueNumber renderAlpha = new ValueNumber("Alpha", "Alpha", "Color line range alpha.", 100, 0, 255);

    public static ValueEnum renderOutline = new ValueEnum("Outline", "Outline", "Outline effect.", RenderOutline.Enabled);

    enum RenderOutline {
        Enabled, Disabled;
    }

    /* Outline misc. */
    public static ValueNumber settingOutlineLineSize = new ValueNumber("Outline Line Size", "OutlineLineSize", "Line size.", 1.0f, 1f, 3.0f);
    public static ValueBoolean settingOutlineRGB = new ValueBoolean("Outline RGB", "OutlineRGB", "360 color range.", false);
    public static ValueNumber settingOutlineRed = new ValueNumber("Outline Red", "OutlineRed", "Color line range red.", 255, 0, 255);
    public static ValueNumber settingOutlineGreen = new ValueNumber("Outline Green", "OutlineGreen", "Color line range green.", 255, 0, 255);
    public static ValueNumber settingOutlineBlue = new ValueNumber("Outline Blue", "OutlineBlue", "Color line range blue.", 255, 0, 255);
    public static ValueNumber settingOutlineAlpha = new ValueNumber("Outline Alpha", "OutlineAlpha", "Color line range alpha.", 255, 0, 255);

    @Override
    public void onSetting() {
        settingOutlineLineSize.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        settingOutlineRed.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        settingOutlineGreen.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        settingOutlineBlue.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        settingOutlineAlpha.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);
        settingOutlineRGB.setEnabled(renderOutline.getValue() == RenderOutline.Enabled);

        if (settingRGB.getValue()) {
            settingRed.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[0]);
            settingGreen.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[1]);
            settingBlue.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[2]);
        }

        if (settingOutlineRGB.getValue()) {
            settingOutlineRed.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[0]);
            settingOutlineGreen.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[1]);
            settingOutlineBlue.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[2]);
        }
    }

    @Override
    public void onRender3D() {
        if (NullUtil.isWorld()) {
            return;
        }

        Color color = new Color(settingRed.getValue().intValue(), settingGreen.getValue().intValue(), settingBlue.getValue().intValue(), renderAlpha.getValue().intValue());
        Color colorOutline = new Color(settingOutlineRed.getValue().intValue(), settingOutlineGreen.getValue().intValue(), settingOutlineBlue.getValue().intValue(), settingOutlineAlpha.getValue().intValue());

        // Get the mouse object over split.
        RayTraceResult splitResult = mc.player.rayTrace(ModuleAntiCheat.getRange(), mc.getRenderPartialTicks());

        if (splitResult != null && splitResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos block = splitResult.getBlockPos();

            // No air!
            if (mc.world.getBlockState(block).getBlock() != Blocks.AIR) {
                RenderUtil.drawSolidBlock(camera, block, color);

                if (renderOutline.getValue() == RenderOutline.Enabled) {
                    float line = (float) settingOutlineLineSize.getValue();

                    RenderUtil.drawOutlineBlock(camera, block, line, colorOutline);
                }
            }
        }
    }
}
