package me.rina.turok.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author SrRina
 * @since 02/10/20 at 1:36pm
 */
public class TurokDisplay {
    private Minecraft mc;

    private int scaleFactor;

    public TurokDisplay(Minecraft mc) {
        this.mc = mc;

        this.onUpdate();
    }

    public int getWidth() {
        return this.mc.displayWidth;
    }

    public int getHeight() {
        return this.mc.displayHeight;
    }

    public int getScaledWidth() {
        return (int) ((double) this.mc.displayWidth / this.scaleFactor);
    }

    public int getScaledHeight() {
        return (int) ((double) this.mc.displayHeight / this.scaleFactor);
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    protected void onUpdate() {
        this.scaleFactor = 1;

        boolean isUnicode = this.mc.isUnicode();

        int minecraftScale = this.mc.gameSettings.guiScale;

        if (minecraftScale == 0) {
            minecraftScale = 1000;
        }

        while (this.scaleFactor < minecraftScale && (getWidth() / (this.scaleFactor + 1) >= 320) && (getHeight() / (this.scaleFactor + 1) >= 240)) {
            ++this.scaleFactor;
        }

        if (isUnicode && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
    }
}
