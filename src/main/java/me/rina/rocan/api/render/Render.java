package me.rina.rocan.api.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.render.impl.RenderType;
import me.rina.rocan.api.util.render.RenderUtil;
import me.rina.turok.render.opengl.TurokGL;
import me.rina.turok.render.opengl.TurokShaderGL;
import net.minecraft.client.renderer.BufferBuilder;
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
 * @since 08/03/2021 at 12:00
 *
 * I was using an util file to help render blocks, but its not good.
 **/
public class Render {
    private RenderType type;

    public Render(RenderType type) {
        this.type = type;
    }

    public void setType(RenderType type) {
        this.type = type;
    }

    public RenderType getType() {
        return type;
    }

    public void onRender(ICamera frustum, BlockPos pos, double x, double y, double z, float line, Color solid, Color outline) {
        final AxisAlignedBB bb = new AxisAlignedBB(
                pos.x - Rocan.getMinecraft().getRenderManager().viewerPosX,
                pos.y - Rocan.getMinecraft().getRenderManager().viewerPosY,
                pos.z - Rocan.getMinecraft().getRenderManager().viewerPosZ,

                pos.x + x - Rocan.getMinecraft().getRenderManager().viewerPosX,
                pos.y + y - Rocan.getMinecraft().getRenderManager().viewerPosY,
                pos.z + z - Rocan.getMinecraft().getRenderManager().viewerPosZ
        );

        if (Rocan.getMinecraft().getRenderViewEntity() == null) {
            return;
        }

        frustum.setPosition(Rocan.getMinecraft().getRenderViewEntity().posX, Rocan.getMinecraft().getRenderViewEntity().posY, Rocan.getMinecraft().getRenderViewEntity().posZ);

        if (frustum.isBoundingBoxInFrustum(new AxisAlignedBB(
                bb.minX + Rocan.getMinecraft().getRenderManager().viewerPosX,
                bb.minY + Rocan.getMinecraft().getRenderManager().viewerPosY,
                bb.minZ + Rocan.getMinecraft().getRenderManager().viewerPosZ,

                bb.maxX + Rocan.getMinecraft().getRenderManager().viewerPosX,
                bb.maxY + Rocan.getMinecraft().getRenderManager().viewerPosY,
                bb.maxZ + Rocan.getMinecraft().getRenderManager().viewerPosZ)))
        {
            this.doDraw(bb, line, solid, outline);
        }
    }

    protected void doDraw(AxisAlignedBB bb, float line, Color solid, Color outline) {
        switch (this.type) {
            case NORMAL: {
                this.doDrawNormal(bb, line, solid, outline);

                break;
            }

            case GRADIENT: {
                this.drawGradient(bb, line, false, solid, outline);

                break;
            }

            case GRADIENT_INVERTED: {
                this.drawGradient(bb, line, true, solid, outline);

                break;
            }
        }
    }

    protected void doDrawNormal(AxisAlignedBB bb, float line, Color solid, Color outline) {
        /* The solid box render. */
        RenderUtil.prepare(1f);
        RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, solid.getRed() / 255f, solid.getGreen() / 255f, solid.getBlue() / 255f, solid.getAlpha() / 255f);
        RenderUtil.release();

        /* The outline box render. */
        RenderUtil.prepare(line);
        RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, outline.getRed() / 255f, outline.getGreen() / 255f, outline.getBlue() / 255f, outline.getAlpha() / 255f);
        RenderUtil.release();
    }

    protected void drawGradient(AxisAlignedBB bb, float line, boolean inverted, Color solid, Color outline) {
        RenderUtil.prepare(1f);

        BufferBuilder solidBufferBuilder = TurokShaderGL.start();

        float solidRed = solid.getRed() / 255f;
        float solidGreen = solid.getGreen() / 255f;
        float solidBlue = solid.getBlue() / 255f;
        float solidAlpha = solid.getAlpha() / 255f;

        double minX = bb.minX;
        double minY = bb.minY;
        double minZ = bb.minZ;

        double maxX = bb.maxX;
        double maxY = bb.maxY;
        double maxZ = bb.maxZ;

        GL11.glShadeModel(GL11.GL_SMOOTH);

        solidBufferBuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        solidBufferBuilder.pos(minX, minY, minZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(minX, minY, minZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(minX, minY, minZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(minX, minY, maxZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(minX, maxY, minZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(minX, maxY, maxZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(minX, maxY, maxZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(minX, minY, maxZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(maxX, maxY, maxZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(maxX, minY, maxZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(maxX, minY, maxZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(maxX, minY, minZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(maxX, maxY, maxZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(maxX, maxY, minZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(maxX, maxY, minZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(maxX, minY, minZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(minX, maxY, minZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(minX, minY, minZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(minX, minY, minZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(maxX, minY, minZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(minX, minY, maxZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(maxX, minY, maxZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(maxX, minY, maxZ).color(solidRed, solidGreen, solidBlue, solidAlpha).endVertex();
        solidBufferBuilder.pos(minX, maxY, minZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(minX, maxY, minZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(minX, maxY, maxZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(maxX, maxY, minZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(maxX, maxY, maxZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(maxX, maxY, maxZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();
        solidBufferBuilder.pos(maxX, maxY, maxZ).color(solidRed, solidGreen, solidBlue, 0).endVertex();

        TurokShaderGL.end();

        RenderUtil.release();
        RenderUtil.prepare(line);

        BufferBuilder outlineBufferBuilder = TurokShaderGL.start();

        float outlineRed = solid.getRed() / 255f;
        float outlineGreen = solid.getGreen() / 255f;
        float outlineBlue = solid.getBlue() / 255f;
        float outlineAlpha = solid.getAlpha() / 255f;

        GL11.glShadeModel(GL11.GL_SMOOTH);

        outlineBufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        outlineBufferBuilder.pos(minX, minY, minZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(minX, minY, minZ).color(outlineRed, outlineGreen, outlineBlue, outlineAlpha).endVertex();
        outlineBufferBuilder.pos(maxX, minY, minZ).color(outlineRed, outlineGreen, outlineBlue, outlineAlpha).endVertex();
        outlineBufferBuilder.pos(maxX, minY, maxZ).color(outlineRed, outlineGreen, outlineBlue, outlineAlpha).endVertex();
        outlineBufferBuilder.pos(minX, minY, maxZ).color(outlineRed, outlineGreen, outlineBlue, outlineAlpha).endVertex();
        outlineBufferBuilder.pos(minX, minY, minZ).color(outlineRed, outlineGreen, outlineBlue, outlineAlpha).endVertex();
        outlineBufferBuilder.pos(minX, maxY, minZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(maxX, maxY, minZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(maxX, maxY, maxZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(minX, maxY, maxZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(minX, maxY, minZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(minX, maxY, maxZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(minX, minY, maxZ).color(outlineRed, outlineGreen, outlineBlue, outlineAlpha).endVertex();
        outlineBufferBuilder.pos(maxX, maxY, maxZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(maxX, minY, maxZ).color(outlineRed, outlineGreen, outlineBlue, outlineAlpha).endVertex();
        outlineBufferBuilder.pos(maxX, maxY, minZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        outlineBufferBuilder.pos(maxX, minY, minZ).color(outlineRed, outlineGreen, outlineBlue, outlineAlpha).endVertex();
        outlineBufferBuilder.pos(maxX, minY, minZ).color(outlineRed, outlineGreen, outlineBlue, 0).endVertex();
        TurokShaderGL.end();

        RenderUtil.release();
    }
}