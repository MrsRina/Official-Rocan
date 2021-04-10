package me.rina.rocan.client.component;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.component.impl.ComponentType;
import me.rina.rocan.api.component.registry.Registry;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.util.client.NullUtil;
import net.minecraft.util.EnumFacing;

/**
 * @author SrRina
 * @since 04/04/2021 at 18:31
 **/
@Registry(name = "Coordinates", tag = "Coordinates", description = "Shows current player coordinates.")
public class ComponentCoordinates extends Component {
    public static ValueEnum settingDirection = new ValueEnum("Direction", "Direction", "Direction looking player.", Direction.XZ);

    public enum Direction {
        NSWE, XZ, NONE;
    }

    @Override
    public void onRenderHUD(float partialTicks) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        String x = String.format("%.1f", mc.player.posX);
        String y = String.format("%.1f", mc.player.posY);
        String z = String.format("%.1f", mc.player.posZ);

        final float value = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell") ? 8 : 0.125f;

        String xNether = String.format("%.1f", mc.player.posX * value);
        String zNether = String.format("%.1f", mc.player.posZ * value);

        String coordinates = this.getDirection() + "XYZ " + ChatFormatting.GRAY + x + ", " + y + ", " + z + ChatFormatting.RESET + " [" + ChatFormatting.GRAY + xNether + ", " + zNether + ChatFormatting.RESET + "]";

        this.render(coordinates, 0, 0);

        this.rect.setWidth(getStringWidth(coordinates));
        this.rect.setHeight(getStringHeight(coordinates));
    }

    public String getDirection() {
        String the = "";

        switch ((Direction) settingDirection.getValue()) {
            case XZ: {
                the = this.getFaceDirection(true, false);

                break;
            }

            case NSWE: {
                the = this.getFaceDirection(false, true);

                break;
            }
        }

        return the;
    }

    public String getFaceDirection(boolean xz, boolean nswe) {
        EnumFacing facing = mc.getRenderViewEntity().getHorizontalFacing();

        String value = "Invalid";

        String l = ChatFormatting.RESET + "[" + ChatFormatting.GRAY;
        String r = ChatFormatting.RESET + "]";

        switch (facing) {
            case NORTH: value = xz ? l + "-Z" + r : nswe ? l + "N" + r : "North " + l + "-Z" + r; break;
            case SOUTH: value = xz ? l + "+Z" + r : nswe ? l + "S" + r : "South " + l + "+Z" + r; break;
            case WEST: value  = xz ? l + "-X" + r : nswe ? l + "W" + r : "West " + l + "-X" + r; break;
            case EAST: value  = xz ? l + "+X" + r : nswe ? l + "E" + r : "East " + l + "+X" + r;
        }

        return value + " ";
    }
}