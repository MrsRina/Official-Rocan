package me.rina.rocan.api.util.render;

import javafx.animation.Animation;
import me.rina.rocan.api.gui.EnumFlagState;
import me.rina.turok.util.TurokMath;

/**
 * @author SrRina
 * @since 09/03/2021 at 11:56
 **/
public class ElementUtil {
    public static float linearComparator(float last, float inTrue, float inFalse, boolean in, float t) {
        return TurokMath.lerp(last, in ? inTrue : inFalse, t);
    }

    public static EnumFlagState getMouseOver(boolean overing) {
        return overing ? EnumFlagState.MOUSE_OVERING : EnumFlagState.MOUSE_NOT_OVER;
    }

    public static EnumFlagState getMousePressed(int expected, int current) {
        return current == expected ? EnumFlagState.PRESSED : EnumFlagState.UNPRESSED;
    }

    public static boolean isPressed(EnumFlagState state) {
        return state == EnumFlagState.PRESSED;
    }

    public static EnumFlagState getDragging(EnumFlagState flag) {
        return flag == EnumFlagState.UNLOCKED ? EnumFlagState.DRAGGING : EnumFlagState.STABLE;
    }

    public static EnumFlagState getResizing(EnumFlagState flag) {
        return flag == EnumFlagState.RESIZABLE ? EnumFlagState.RESIZING : EnumFlagState.RESIZED;
    }
}
