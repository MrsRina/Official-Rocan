package me.rina.rocan.client.module.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.crystal.HoleUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.render.Render3DUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokMath;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 28/01/2021 at 13:38
 **/
@Registry(name = "Hole ESP", tag = "HoleESP", description = "Draws holes to help visibility at crystal pvp.", category = ModuleCategory.RENDER)
public class ModuleHoleESP extends Module {
    /* Misc hole ESP. */
    private ValueNumber settingRange = new ValueNumber("Range", "Range", "Maximum distance to render.", 6f, 0.1f, 10f);
    private ValueNumber settingOffsetY = new ValueNumber("Offset Y", "OffsetY", "Offset y block render.", 25, 0, 100);

    /* Color. */
    public static ValueBoolean settingRGB = new ValueBoolean("RGB", "RGB", "RGB effect.", false);
    public static ValueNumber settingRed = new ValueNumber("Red", "Red", "Color line range red.", 255, 0, 255);
    public static ValueNumber settingGreen = new ValueNumber("Green", "Green", "Color line range green.", 0, 0, 255);
    public static ValueNumber settingBlue = new ValueNumber("Blue", "Blue", "Color line range blue.", 255, 0, 255);
    public static ValueNumber settingAlpha = new ValueNumber("Alpha", "Alpha", "Color line range alpha.", 255, 0, 255);

    public static ValueEnum settingOutline = new ValueEnum("Outline", "Outline", "Outline effect.", RenderOutline.Enabled);

    enum RenderOutline {
        Enabled, Disabled;
    }

    /* Outline misc. */
    public static ValueNumber settingOutlineLineSize = new ValueNumber("Outline Line Size", "OutlineLineSize", "Line size.", 1.0f, 1f, 3.0f);
    public static ValueBoolean settingOutlineRGB = new ValueBoolean("Outline RGB", "OutlineRGB", "360 color range.", false);
    public static ValueNumber settingOutlineRed = new ValueNumber("Outline Red", "OutlineRed", "Color line range red.", 255, 0, 255);
    public static ValueNumber settingOutlineGreen = new ValueNumber("Outline Green", "OutlineGreen", "Color line range green.", 0, 0, 255);
    public static ValueNumber settingOutlineBlue = new ValueNumber("Outline Blue", "OutlineBlue", "Color line range blue.", 255, 0, 255);
    public static ValueNumber settingOutlineAlpha = new ValueNumber("Outline Alpha", "OutlineAlpha", "Color line range alpha.", 255, 0, 255);

    private ArrayList<BlockPos> holes = new ArrayList<>();

    @Override
    public void onSetting() {
        settingOutlineLineSize.setEnabled(settingOutline.getValue() == RenderOutline.Enabled);
        settingOutlineRed.setEnabled(settingOutline.getValue() == RenderOutline.Enabled);
        settingOutlineGreen.setEnabled(settingOutline.getValue() == RenderOutline.Enabled);
        settingOutlineBlue.setEnabled(settingOutline.getValue() == RenderOutline.Enabled);
        settingOutlineAlpha.setEnabled(settingOutline.getValue() == RenderOutline.Enabled);
        settingOutlineRGB.setEnabled(settingOutline.getValue() == RenderOutline.Enabled);

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

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        holes.clear();

        int r = TurokMath.ceiling((float) settingRange.getValue());

        List<BlockPos> sphereList = HoleUtil.getSphereList(PlayerUtil.getBlockPos(), r, r, false, true);

        // Its not kami, is diff.
        for (BlockPos blocks : sphereList) {
            if (BlockUtil.isAir(blocks) == false) {
                continue;
            }

            if (BlockUtil.isAir(blocks.add(0, 1, 0)) == false) {
                continue;
            }

            if (BlockUtil.isAir(blocks.add(0, 2, 0)) == false) {
                continue;
            }

            boolean isHole = true;

            for (BlockPos _blocks : HoleUtil.SURROUND) {
                Block block = mc.world.getBlockState(blocks.add(_blocks)).getBlock();

                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    isHole = false;

                    break;
                }
            }

            // Clean code!
            if (isHole) {
                holes.add(blocks);
            }
        }
    }

    @Override
    public void onRender3D() {
        Color color = new Color(settingRed.getValue().intValue(), settingGreen.getValue().intValue(), settingBlue.getValue().intValue(), settingAlpha.getValue().intValue());
        Color colorOutline = new Color(settingOutlineRed.getValue().intValue(), settingOutlineGreen.getValue().intValue(), settingOutlineBlue.getValue().intValue(), settingOutlineAlpha.getValue().intValue());

        for (BlockPos blocks : holes) {
            float offsetY = settingOffsetY.getValue().intValue() / 100f;
            
            Render3DUtil.render3DSolid(camera, blocks.x, blocks.y, blocks.z, 1, offsetY, 1, color);

            if (settingOutline.getValue() == RenderOutline.Enabled) {
                float line = settingOutlineLineSize.getValue().floatValue();

                Render3DUtil.render3DOutline(camera, blocks.x, blocks.y, blocks.z, 1, offsetY, 1, line, colorOutline);
            }
        }
    }
}
