package me.rina.rocan.api;

import me.rina.rocan.Rocan;
import net.minecraft.client.Minecraft;

/**
 * @author SrRina
 * @since 06/12/20 at 01:04am
 */
public interface ISLClass {
    Minecraft mc = Rocan.getMinecraft();

    /**
     * Make the class savable;
     */
    public void onSave();

    /**
     * Make the class loadable;
     */
    public void onLoad();
}