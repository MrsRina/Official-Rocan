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
import me.rina.rocan.api.util.render.RenderUtil;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

/**
 * @author SrRina
 * @since 28/01/2021 at 13:38
 **/
@Registry(name = "Hole ESP", tag = "HoleESP", description = "Draws holes to help visibility at crystal pvp.", category = ModuleCategory.RENDER)
public class ModuleHoleESP extends Module {
    /* Misc hole ESP. */
    private ValueNumber settingRange = new ValueNumber("Range", "Range", "Maximum distance to render.", 6f, 0.1f, 6f);
    private ValueNumber settingOffsetY = new ValueNumber("Offset Y", "OffsetY", "Offset y block render.", 25, 0, 100);

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

    private Render render = new Render(RenderType.NORMAL);

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
        for (BlockPos holes : Rocan.getHoleManager().getHoles()) {
            if (holes.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) >= settingRange.getValue().intValue()) {
                continue;
            }

            float y = settingOffsetY.getValue().intValue() / 100f;
            float l = settingOutlineLineSize.getValue().floatValue();

            this.render.onRender(camera, holes, 1, y, 1, l, this.solid, this.outline);
        }
    }
}
