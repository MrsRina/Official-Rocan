package me.rina.rocan.client.gui.minecraft;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * @author SrRina
 * @since 02/02/2021 at 14:41
 **/
public class CustomDeathGUI extends GuiGameOver {
    public CustomDeathGUI(@Nullable ITextComponent textComponent) {
        super(textComponent);
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {

    }
}
