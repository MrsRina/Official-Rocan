package me.rina.turok.render.font.management;

import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.opengl.TurokGL;
import me.rina.turok.render.opengl.TurokRenderGL;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 26/09/20 at 1:23pm
 */
public class TurokFontManager {
	public static void render(TurokFont fontRenderer, String string, float x, float y, boolean shadow, Color color) {
		TurokGL.pushMatrix();
		TurokGL.enable(GL11.GL_TEXTURE_2D);

		TurokGL.enable(GL11.GL_BLEND);
		TurokGL.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if (shadow) {
			if (fontRenderer.isRenderingCustomFont()) {
				fontRenderer.drawStringWithShadow(string, x, y, color.getRGB());
			} else {
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string, (int) x, (int) y, color.getRGB());
			}
		} else {
			if (fontRenderer.isRenderingCustomFont()) {
				fontRenderer.drawString(string, x, y, color.getRGB());
			} else {
				Minecraft.getMinecraft().fontRenderer.drawString(string, (int) x, (int) y, color.getRGB());
			}
		}

		TurokGL.disable(GL11.GL_TEXTURE_2D);
		TurokGL.popMatrix();
	}

	public static int getStringWidth(TurokFont fontRenderer, String string) {
		return fontRenderer.isRenderingCustomFont() ? (int) fontRenderer.getStringWidth(string) : Minecraft.getMinecraft().fontRenderer.getStringWidth(string);
	}

	public static int getStringHeight(TurokFont fontRenderer, String string) {
		return fontRenderer.isRenderingCustomFont() ? (int) fontRenderer.getStringHeight(string) : Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * fontRenderer.getFontSize();
	}
}
