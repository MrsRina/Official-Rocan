package me.rina.rocan.api.setting.value;

import me.rina.rocan.api.setting.Setting;

import java.awt.*;

/**
 * @author SrRina
 * @since 26/02/2021 at 13:59
 **/
public class ValueColor extends Setting {
    private int r;
    private int g;
    private int b;
    private int a;

    public ValueColor(String name, String tag, String description, Color color) {
        super(name, tag, description);

        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
        this.a = color.getAlpha();
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getR() {
        return r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getG() {
        return g;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getB() {
        return b;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getA() {
        return a;
    }

    public Color getColor() {
        return new Color(this.r, this.g, this.b, this.a);
    }
}
