package me.rina.turok.render.opengl;

import org.lwjgl.opengl.GL11;

/**
 * @author SrRina
 * @since 09/01/2021 at 16:11
 **/
public class TurokGL {
    public static void scissor(int x, int y, int w, int h) {
        GL11.glScissor(x, y, w, h);
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void translate(float x, float y, float z) {
        GL11.glTranslatef(x, y, z);
    }

    public static void translate(float x, float y) {
        GL11.glTranslatef(x, y, 0);
    }

    public static void rotate(float angle, float x, float y, float z) {
        GL11.glRotatef(angle, x, y, z);
    }

    public static void scale(float x, float y, float z) {
        GL11.glScalef(x, y, z);
    }

    public static void hint(int target, int target1) {
        GL11.glHint(target, target1);
    }

    public static void enable(int state) {
        GL11.glEnable(state);
    }

    public static void disable(int state) {
        GL11.glDisable(state);
    }

    public static void blendFunc(int a, int b) {
        GL11.glBlendFunc(a, b);
    }

    public static void prepare(int mode) {
        GL11.glBegin(mode);
    }

    public static void release() {
        GL11.glEnd();
    }

    public static void color(float r, float g, float b, float a) {
        GL11.glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static void color(float r, float g, float b) {
        GL11.glColor3f(r / 255f, g / 255f, b / 255f);
    }

    public static void lineSize(float size) {
        GL11.glLineWidth(size);
    }

    public static void pointSize(float size) {
        GL11.glPointSize(size);
    }

    public static void addVertex(float x, float y, float z) {
        GL11.glVertex3f(x, y, z);
    }

    public static void addVertex(float x, float y) {
        GL11.glVertex2f(x, y);
    }

    public static void sewTexture(float s, float t, float r) {
        GL11.glTexCoord3f(s, t, r);
    }

    public static void sewTexture(float s, float t) {
        GL11.glTexCoord2f(s, t);
    }

    public static void polygonOffset(float factor, float units) {
        GL11.glPolygonOffset(factor, units);
    }

    public static void polygonOffset(double factor, double units) {
        GL11.glPolygonOffset((float) factor, (float) units);
    }

    public static void polygonOffset(int factor, int units) {
        GL11.glPolygonOffset(factor, units);
    }

    public static void polygonMode(int face, int mode) {
        GL11.glPolygonMode(face, mode);
    }

    public static void shaderMode(int mode) {
        GL11.glShadeModel(mode);
    }
}
