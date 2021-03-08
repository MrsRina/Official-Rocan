package me.rina.rocan.api.render;

import me.rina.rocan.Rocan;
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
    }
}