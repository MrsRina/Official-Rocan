package me.rina.rocan.api.util.render;

import me.rina.rocan.Rocan;
import me.rina.turok.render.opengl.TurokGL;
import me.rina.turok.render.opengl.TurokRenderGL;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 22/10/2020 at 1:44pm
 */
public class Render3DUtil  {
    public static void prepare(float line) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        TurokGL.enable(GL11.GL_LINE_SMOOTH);
        TurokGL.hint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        TurokGL.lineSize(line);
    }

    public static void release() {
        TurokGL.disable(GL11.GL_LINE_SMOOTH);

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void render3DSolid(ICamera camera, BlockPos blockpos, Color color) {
        render3DSolid(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, color);
    }

    public static void render3DSolid(ICamera camera, int x, int y, int z, Color color) {
        render3DSolid(camera, x, y, z, 1, 1, 1, color);
    }

    public static void render3DSolid(ICamera camera, int x, int y, int z, double offsetX, double offsetY, double offsetZ, Color color) {
        final AxisAlignedBB bb = new AxisAlignedBB(
            x - Rocan.getMinecraft().getRenderManager().viewerPosX,
            y - Rocan.getMinecraft().getRenderManager().viewerPosY,
            z - Rocan.getMinecraft().getRenderManager().viewerPosZ,

            x + offsetX - Rocan.getMinecraft().getRenderManager().viewerPosX,
            y + offsetY - Rocan.getMinecraft().getRenderManager().viewerPosY,
            z + offsetZ - Rocan.getMinecraft().getRenderManager().viewerPosZ
        );

        camera.setPosition(Rocan.getMinecraft().getRenderViewEntity().posX, Rocan.getMinecraft().getRenderViewEntity().posY, Rocan.getMinecraft().getRenderViewEntity().posZ);

        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(
            bb.minX + Rocan.getMinecraft().getRenderManager().viewerPosX,
            bb.minY + Rocan.getMinecraft().getRenderManager().viewerPosY,
            bb.minZ + Rocan.getMinecraft().getRenderManager().viewerPosZ,

            bb.maxX + Rocan.getMinecraft().getRenderManager().viewerPosX,
            bb.maxY + Rocan.getMinecraft().getRenderManager().viewerPosY,
            bb.maxZ + Rocan.getMinecraft().getRenderManager().viewerPosZ)))
        {
            prepare(1.0f);

            RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);

            release();
        }
    }

    public static void render3DOutline(ICamera camera, BlockPos blockpos, Color color) {
        render3DOutline(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, 1.0f, color);
    }

    public static void render3DOutline(ICamera camera, BlockPos blockpos, float line, Color color) {
        render3DOutline(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, line, color);
    }

    public static void render3DOutline(ICamera camera, int x, int y, int z, Color color) {
        render3DOutline(camera, x, y, z, 1, 1, 1, 1.0f, color);
    }

    public static void render3DOutline(ICamera camera, int x, int y, int z, float line, Color color) {
        render3DOutline(camera, x, y, z, 1, 1, 1, line, color);
    }

    public static void render3DOutline(ICamera camera, int x, int y, int z, double offsetX, double offsetY, double offsetZ, Color color) {
        render3DOutline(camera, x, y, z, offsetX, offsetY, offsetZ, 1.0f, color);
    }

    public static void render3DOutline(ICamera camera, int x, int y, int z, double offsetX, double offsetY, double offsetZ, float line, Color color) {
        final AxisAlignedBB bb = new AxisAlignedBB(
                x - Rocan.getMinecraft().getRenderManager().viewerPosX,
                y - Rocan.getMinecraft().getRenderManager().viewerPosY,
                z - Rocan.getMinecraft().getRenderManager().viewerPosZ,

                x + offsetX - Rocan.getMinecraft().getRenderManager().viewerPosX,
                y + offsetY - Rocan.getMinecraft().getRenderManager().viewerPosY,
                z + offsetZ - Rocan.getMinecraft().getRenderManager().viewerPosZ
        );

        camera.setPosition(Rocan.getMinecraft().getRenderViewEntity().posX, Rocan.getMinecraft().getRenderViewEntity().posY, Rocan.getMinecraft().getRenderViewEntity().posZ);

        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(
                bb.minX + Rocan.getMinecraft().getRenderManager().viewerPosX,
                bb.minY + Rocan.getMinecraft().getRenderManager().viewerPosY,
                bb.minZ + Rocan.getMinecraft().getRenderManager().viewerPosZ,

                bb.maxX + Rocan.getMinecraft().getRenderManager().viewerPosX,
                bb.maxY + Rocan.getMinecraft().getRenderManager().viewerPosY,
                bb.maxZ + Rocan.getMinecraft().getRenderManager().viewerPosZ)))
        {
            prepare(line);

            RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);

            release();
        }
    }
}