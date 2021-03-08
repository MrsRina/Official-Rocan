package me.rina.rocan.client.module.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.render.Render;
import me.rina.rocan.api.render.impl.RenderType;
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
    /* Render style. */
    public static ValueEnum settingRenderType = new ValueEnum("Render Type", "RenderType", "Type to render.", RenderType.NORMAL);

    /* Color. */
    public static ValueBoolean settingRGB = new ValueBoolean("RGB", "RGB", "RGB effect.", false);
    public static ValueNumber settingRed = new ValueNumber("Red", "Red", "Color line range red.", 255, 0, 255);
    public static ValueNumber settingGreen = new ValueNumber("Green", "Green", "Color line range green.", 0, 0, 255);
    public static ValueNumber settingBlue = new ValueNumber("Blue", "Blue", "Color line range blue.", 255, 0, 255);
    public static ValueNumber settingAlpha = new ValueNumber("Alpha", "Alpha", "Color line range alpha.", 255, 0, 255);

    /* Outline misc. */
    public static ValueNumber settingOutlineLineSize = new ValueNumber("Outline Line Size", "OutlineLineSize", "Line size.", 1.0f, 1f, 3.0f);
    public static ValueBoolean settingOutlineRGB = new ValueBoolean("Outline RGB", "OutlineRGB", "360 color range.", false);
    public static ValueNumber settingOutlineAlpha = new ValueNumber("Outline Alpha", "OutlineAlpha", "Color line range alpha.", 255, 0, 255);

    /* The render engine. */
    private Render render = new Render(RenderType.NORMAL);

    /* Color for render. */
    private Color outline = new Color(255, 255, 255, 255);
    private Color solid = new Color(255, 255, 255, 255);

    @Override
    public void onSetting() {
        if (settingRGB.getValue()) {
            settingRed.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[0]);
            settingGreen.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[1]);
            settingBlue.setValue(Rocan.getClientEventManager().getCurrentRGBColor()[2]);
        }

        this.solid = new Color(settingRed.getValue().intValue(), settingGreen.getValue().intValue(), settingBlue.getValue().intValue(), settingAlpha.getValue().intValue());

        if (settingOutlineRGB.getValue()) {
            this.outline = new Color(Rocan.getClientEventManager().getCurrentRGBColor()[0], Rocan.getClientEventManager().getCurrentRGBColor()[1], Rocan.getClientEventManager().getCurrentRGBColor()[2], settingOutlineAlpha.getValue().intValue());
        } else {
            this.outline = new Color(settingRed.getValue().intValue(), settingGreen.getValue().intValue(), settingBlue.getValue().intValue(), settingOutlineAlpha.getValue().intValue());;
        }

        this.render.setType((RenderType) settingRenderType.getValue());
    }

    @Override
    public void onRender3D() {
        if (NullUtil.isWorld()) {
            return;
        }

        // Get the mouse object over split.
        RayTraceResult splitResult = mc.player.rayTrace(ModuleAntiCheat.getRange(), mc.getRenderPartialTicks());

        if (splitResult != null && splitResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos block = splitResult.getBlockPos();

            // No air!
            float l = settingOutlineLineSize.getValue().floatValue();

            this.render.onRender(camera, block, 1, 1, 1, l, this.solid, this.outline);
        }
    }
}
