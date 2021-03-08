package me.rina.rocan.api.manager.impl;

import me.rina.rocan.Rocan;
import net.minecraft.client.Minecraft;

/**
 * @author SrRina
 * @since 14/02/2021 at 11:12
 **/
public interface ManageStructure {
    Minecraft mc = Rocan.getMinecraft();

    /**
     * Called in ClientTickEvent forge.
     */
    public void onUpdateAll();
}
