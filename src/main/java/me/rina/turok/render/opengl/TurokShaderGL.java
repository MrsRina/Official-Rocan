package me.rina.turok.render.opengl;

import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 09/01/2021 at 16:43
 **/
public class TurokShaderGL {
    private static TurokShaderGL INSTANCE;

    private TurokDisplay display;
    private TurokMouse mouse;

    public static void init(TurokDisplay display, TurokMouse mouse) {
        INSTANCE = new TurokShaderGL();

        // Start the classes.
        INSTANCE.display = display;
        INSTANCE.mouse = mouse;
    }

    public static void drawOutlineRectFadingMouse(TurokRect rect, int radius, Color color) {
        drawOutlineRectFadingMouse((float) rect.getX(), (float) rect.getY(), (float) rect.getWidth(), (float) rect.getHeight(), radius, color);
    }

    public static void drawOutlineRectFadingMouse(float x, float y, float w, float h, int radius, Color color) {
        TurokGL.pushMatrix();

        float offset = 0.5f;

        float vx = x - INSTANCE.mouse.getX();
        float vy = y - INSTANCE.mouse.getY();

        float vw = (x + w) - INSTANCE.mouse.getX();
        float vh = (y + h) - INSTANCE.mouse.getY();

        int valueAlpha = color.getAlpha();

        TurokGL.enable(GL11.GL_BLEND);
        TurokGL.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        TurokGL.shaderMode(GL11.GL_SMOOTH);

        TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        TurokGL.lineSize(1.0f);

        TurokGL.prepare(GL11.GL_LINE_LOOP);
        {
            TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), valueAlpha - TurokMath.clamp(TurokMath.sqrt(vx * vx + vy * vy) / (radius / 100f), 0, valueAlpha));
            TurokGL.addVertex(x + offset, y);

            TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), valueAlpha - TurokMath.clamp(TurokMath.sqrt(vx * vx + vh * vh) / (radius / 100f), 0, valueAlpha));
            TurokGL.addVertex(x + offset,y + h + offset);

            TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), valueAlpha - TurokMath.clamp(TurokMath.sqrt(vw * vw + vh * vh) / (radius / 100f), 0, valueAlpha));
            TurokGL.addVertex(x + w, y + h);

            TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), valueAlpha - TurokMath.clamp(TurokMath.sqrt(vw * vw + vy * vy) / (radius / 100f), 0, valueAlpha));
            TurokGL.addVertex(x + w, y);
        }

        TurokGL.release();

        TurokGL.disable(GL11.GL_BLEND);
        TurokGL.popMatrix();
    }

    public static void drawSolidRectFadingMouse(TurokRect rect, int radius, Color color) {
        drawSolidRectFadingMouse((float) rect.getX(), (float) rect.getY(), (float) rect.getWidth(), (float) rect.getHeight(), radius, color);
    }

    public static void drawSolidRectFadingMouse(float x, float y, float w, float h, int radius, Color color) {
        TurokGL.pushMatrix();

        float vx = x - INSTANCE.mouse.getX();
        float vy = y - INSTANCE.mouse.getY();

        float vw = (x + w) - INSTANCE.mouse.getX();
        float vh = (y + h) - INSTANCE.mouse.getY();

        int valueAlpha = color.getAlpha();

        TurokGL.enable(GL11.GL_BLEND);
        TurokGL.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        TurokGL.prepare(GL11.GL_QUADS);
        {
            TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), valueAlpha - TurokMath.clamp(TurokMath.sqrt(vx * vx + vy * vy) / (radius / 100f), 0, valueAlpha));
            TurokGL.addVertex(x, y);

            TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), valueAlpha - TurokMath.clamp(TurokMath.sqrt(vx * vx + vh * vh) / (radius / 100f), 0, valueAlpha));
            TurokGL.addVertex(x, y + h);

            TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), valueAlpha - TurokMath.clamp(TurokMath.sqrt(vw * vw + vh * vh) / (radius / 100f), 0, valueAlpha));
            TurokGL.addVertex(x + w, y + h);

            TurokGL.color(color.getRed(), color.getGreen(), color.getBlue(), valueAlpha - TurokMath.clamp(TurokMath.sqrt(vw * vw + vy * vy) / (radius / 100f), 0, valueAlpha));
            TurokGL.addVertex(x + w, y);
        }

        TurokGL.release();

        TurokGL.disable(GL11.GL_BLEND);
        TurokGL.popMatrix();
    }

    public static void pushScissor() {
        TurokGL.enable(GL11.GL_SCISSOR_TEST);
    }

    public static void pushScissorMatrix() {
        TurokGL.pushMatrix();
        TurokGL.enable(GL11.GL_SCISSOR_TEST);
    }

    public static void drawScissor(float x, float y, float w, float h) {
        float calculatedW = x + w;
        float calculatedH = y + h;

        TurokGL.scissor((int) (x * INSTANCE.display.getScaleFactor()), (int) (INSTANCE.display.getHeight() - (calculatedH * INSTANCE.display.getScaleFactor())), (int) ((calculatedW - x) * INSTANCE.display.getScaleFactor()), (int) ((calculatedH - y) * INSTANCE.display.getScaleFactor()));
    }

    public static void popScissor() {
        TurokGL.disable(GL11.GL_SCISSOR_TEST);
    }

    public static void popScissorMatrix() {
        TurokGL.disable(GL11.GL_SCISSOR_TEST);
        TurokGL.popMatrix();
    }
}
