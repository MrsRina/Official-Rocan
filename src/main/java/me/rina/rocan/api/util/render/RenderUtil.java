package me.rina.rocan.api.util.render;

import me.rina.rocan.Rocan;
import me.rina.turok.render.opengl.TurokGL;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 22/10/2020 at 1:44pm
 */
public class RenderUtil {
    public static Tessellator tessellator = Tessellator.getInstance();

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

    public static void drawSolidBlock(ICamera camera, BlockPos blockpos, Color color) {
        drawSolidBlock(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, color);
    }

    public static void drawSolidBlock(ICamera camera, double x, double y, double z, Color color) {
        drawSolidBlock(camera, x, y, z, 1, 1, 1, color);
    }

    public static void drawSolidBlock(ICamera camera, double x, double y, double z, double offsetX, double offsetY, double offsetZ, Color color) {
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

    public static void drawOutlineBlock(ICamera camera, BlockPos blockpos, Color color) {
        drawOutlineBlock(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, 1.0f, color);
    }

    public static void drawOutlineBlock(ICamera camera, BlockPos blockpos, float line, Color color) {
        drawOutlineBlock(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, line, color);
    }

    public static void drawOutlineBlock(ICamera camera, double x, double y, double z, Color color) {
        drawOutlineBlock(camera, x, y, z, 1, 1, 1, 1.0f, color);
    }

    public static void drawOutlineBlock(ICamera camera, double x, double y, double z, float line, Color color) {
        drawOutlineBlock(camera, x, y, z, 1, 1, 1, line, color);
    }

    public static void drawOutlineBlock(ICamera camera, double x, double y, double z, double offsetX, double offsetY, double offsetZ, Color color) {
        drawOutlineBlock(camera, x, y, z, offsetX, offsetY, offsetZ, 1.0f, color);
    }

    public static void drawOutlineBlock(ICamera camera, double x, double y, double z, double offsetX, double offsetY, double offsetZ, float line, Color color) {
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

    public static void drawOutlineLegacyBlock(ICamera camera, BlockPos blockpos, Color color) {
        drawOutlineLegacyBlock(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, 1.0f, color);
    }

    public static void drawOutlineLegacyBlock(ICamera camera, BlockPos blockpos, float line, Color color) {
        drawOutlineLegacyBlock(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, line, color);
    }

    public static void drawOutlineLegacyBlock(ICamera camera, double x, double y, double z, Color color) {
        drawOutlineLegacyBlock(camera, x, y, z, 1, 1, 1, 1.0f, color);
    }

    public static void drawOutlineLegacyBlock(ICamera camera, double x, double y, double z, float line, Color color) {
        drawOutlineLegacyBlock(camera, x, y, z, 1, 1, 1, line, color);
    }

    public static void drawOutlineLegacyBlock(ICamera camera, double x, double y, double z, double offsetX, double offsetY, double offsetZ, Color color) {
        drawOutlineLegacyBlock(camera, x, y, z, offsetX, offsetY, offsetZ, 1.0f, color);
    }

    public static void drawOutlineLegacyBlock(ICamera camera, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float line, Color color) {
        final AxisAlignedBB bb = new AxisAlignedBB(
                minX - Rocan.getMinecraft().getRenderManager().viewerPosX,
                minY - Rocan.getMinecraft().getRenderManager().viewerPosY,
                minZ - Rocan.getMinecraft().getRenderManager().viewerPosZ,

                minX + maxX - Rocan.getMinecraft().getRenderManager().viewerPosX,
                minY + maxY - Rocan.getMinecraft().getRenderManager().viewerPosY,
                minZ + maxZ - Rocan.getMinecraft().getRenderManager().viewerPosZ
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

            float red = color.getRed() / 255f;
            float green = color.getGreen() / 255f;
            float blue = color.getBlue() / 255f;
            float alpha = color.getAlpha() / 255f;

            prepare(line);

            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

            bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
            bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
            bufferBuilder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
            bufferBuilder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
            bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, 0.0F).endVertex();

            tessellator.draw();

            release();
        }
    }

    public static void drawSolidLegacyBlock(ICamera camera, BlockPos blockpos, Color color) {
        drawSolidLegacyBlock(camera, blockpos.x, blockpos.y, blockpos.z, 1, 1, 1, color);
    }

    public static void drawSolidLegacyBlock(ICamera camera, BlockPos blockpos, double offsetX, double offsetY, double offsetZ, Color color) {
        drawSolidLegacyBlock(camera, blockpos.x, blockpos.y, blockpos.z, offsetX, offsetY, offsetZ, color);
    }

    public static void drawSolidLegacyBlock(ICamera camera, double x, double y, double z, Color color) {
        drawSolidLegacyBlock(camera, x, y, z, 1, 1, 1, color);
    }

    public static void drawSolidLegacyBlock(ICamera camera, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        final AxisAlignedBB bb = new AxisAlignedBB(
                minX - Rocan.getMinecraft().getRenderManager().viewerPosX,
                minY - Rocan.getMinecraft().getRenderManager().viewerPosY,
                minZ - Rocan.getMinecraft().getRenderManager().viewerPosZ,

                minX + maxX - Rocan.getMinecraft().getRenderManager().viewerPosX,
                minY + maxY - Rocan.getMinecraft().getRenderManager().viewerPosY,
                minZ + maxZ - Rocan.getMinecraft().getRenderManager().viewerPosZ
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

            float red = color.getRed() / 255f;
            float green = color.getGreen() / 255f;
            float blue = color.getBlue() / 255f;
            float alpha = color.getAlpha() / 255f;

            prepare(1f);

            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

            bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
            bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(minX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
            bufferBuilder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
            bufferBuilder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
            bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, 0.0F).endVertex();

            tessellator.draw();

            release();
        }
    }

    public static void drawOutlineLegacyFadingBlock(ICamera camera, int x, int y, int z, double offsetX, double offsetY, double offsetZ, float line, Color color) {

    }

    public static void drawSolidLegacyFadingBlock(ICamera camera, int x, int y, int z, double offsetX, double offsetY, double offsetZ, Color color) {

    }
}