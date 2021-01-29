package me.rina.rocan.client.module.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.crystal.HoleUtil;
import me.rina.rocan.api.util.player.PlayerUtil;
import me.rina.rocan.api.util.render.Render3DUtil;
import me.rina.turok.util.TurokMath;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 28/01/2021 at 13:38
 **/
public class ModuleHoleESP extends Module {
    private ValueNumber range = new ValueNumber("Range", "Range", "Maximum distance to render.", 6f, 0.1f, 10f);

    public static ValueBoolean renderRGB = new ValueBoolean("RGB", "RGB", "RGB effect.", false);

    public static ValueNumber renderRed = new ValueNumber("Red", "Red", "Color line range red.", 255, 0, 255);
    public static ValueNumber renderGreen = new ValueNumber("Green", "Green", "Color line range green.", 0, 0, 255);
    public static ValueNumber renderBlue = new ValueNumber("Blue", "Blue", "Color line range blue.", 255, 0, 255);
    public static ValueNumber renderAlpha = new ValueNumber("Alpha", "Alpha", "Color line range alpha.", 255, 0, 255);

    /*
     * Outline config;
     */
    public static ValueEnum renderOutline = new ValueEnum("Outline", "Outline", "Outline effect.", ModuleBlockHighlight.RenderOutline.Enabled);

    enum RenderOutline {
        Enabled, Disabled;
    }

    public static ValueNumber renderOutlineLineSize = new ValueNumber("Outline Line Size", "OutlineLineSize", "Line size.", 1.0f, 0.1f, 3.0f);
    public static ValueBoolean renderOutlineRGB = new ValueBoolean("Outline RGB", "OutlineRGB", "360 color range.", false);

    public static ValueNumber renderOutlineRed = new ValueNumber("Outline Red", "OutlineRed", "Color line range red.", 255, 0, 255);
    public static ValueNumber renderOutlineGreen = new ValueNumber("Outline Green", "OutlineGreen", "Color line range green.", 0, 0, 255);
    public static ValueNumber renderOutlineBlue = new ValueNumber("Outline Blue", "OutlineBlue", "Color line range blue.", 255, 0, 255);
    public static ValueNumber renderOutlineAlpha = new ValueNumber("Outline Alpha", "OutlineAlpha", "Color line range alpha.", 255, 0, 255);

    private ArrayList<BlockPos> holes;

    public ModuleHoleESP() {
        super("Hole ESP", "HoleESP", "Draw the holes to help visibility.", ModuleCategory.Render);
    }

    @Override
    public void onSetting() {
        renderOutlineLineSize.setEnabled(renderOutline.getValue() == ModuleBlockHighlight.RenderOutline.Enabled);
        renderOutlineRed.setEnabled(renderOutline.getValue() == ModuleBlockHighlight.RenderOutline.Enabled);
        renderOutlineGreen.setEnabled(renderOutline.getValue() == ModuleBlockHighlight.RenderOutline.Enabled);
        renderOutlineBlue.setEnabled(renderOutline.getValue() == ModuleBlockHighlight.RenderOutline.Enabled);
        renderOutlineAlpha.setEnabled(renderOutline.getValue() == ModuleBlockHighlight.RenderOutline.Enabled);
        renderOutlineRGB.setEnabled(renderOutline.getValue() == ModuleBlockHighlight.RenderOutline.Enabled);

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
        if (holes == null) {
            holes = new ArrayList<>();
        } else {
            holes.clear();
        }

        if (mc.player != null || mc.world != null) {
            int r = TurokMath.ceiling((float) range.getValue());

            List<BlockPos> sphereList = HoleUtil.getSphereList(PlayerUtil.getBlockPos(), r, r, false, true);

            for (BlockPos blocks : sphereList) {
                if (mc.world.getBlockState(blocks).getBlock() != Blocks.AIR) {
                    continue;
                }

                if (mc.world.getBlockState(blocks.add(0, 1, 0)).getBlock() != Blocks.AIR) {
                    continue;
                }

                if (mc.world.getBlockState(blocks.add(0, 2, 0)).getBlock() != Blocks.AIR) {
                    continue;
                }

                boolean isHole = true;

                for (BlockPos _blocks : HoleUtil.SURROUND) {
                    Block block = mc.world.getBlockState(_blocks).getBlock();

                    if (block != Blocks.BEDROCK || block != Blocks.OBSIDIAN || block != Blocks.ENDER_CHEST || block != Blocks.ANVIL) {
                        isHole = false;

                        break;
                    }
                }

                if (isHole) {
                    holes.add(blocks);
                }
            }

            Color color = new Color(renderRed.getValue().intValue(), renderGreen.getValue().intValue(), renderBlue.getValue().intValue(), renderAlpha.getValue().intValue());
            Color colorOutline = new Color(renderOutlineRed.getValue().intValue(), renderOutlineGreen.getValue().intValue(), renderOutlineBlue.getValue().intValue(), renderOutlineAlpha.getValue().intValue());

            for (BlockPos blocks : holes) {
                Render3DUtil.render3DSolid(camera, blocks, color);

                if (renderOutline.getValue() == ModuleBlockHighlight.RenderOutline.Enabled) {
                    float line = (float) renderOutlineLineSize.getValue();

                    Render3DUtil.render3DOutline(camera, blocks, line, colorOutline);
                }
            }
        }
    }
}
