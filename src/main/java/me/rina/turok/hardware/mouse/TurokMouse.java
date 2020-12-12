package me.rina.turok.hardware.mouse;

import org.lwjgl.input.Mouse;

/**
 * @author Rina.
 * @since 02/10/2020.
 */
public class TurokMouse {
    private int scroll;

    private int x;
    private int y;

    public TurokMouse(int mx, int my) {
        this.x = mx;
        this.y = my;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;

        Mouse.setCursorPosition(this.x, this.y);
    }

    public int[] getPos() {
        return new int[] {
                this.x, this.y
        };
    }

    public void setX(int x) {
        this.x = x;

        Mouse.setCursorPosition(this.x, this.y);
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;

        Mouse.setCursorPosition(this.x, this.y);
    }

    public int getY() {
        return y;
    }

    public int getScroll() {
        return -(Mouse.getDWheel() / 10);
    }

    public boolean hasWheel() {
        return Mouse.hasWheel();
    }
}
