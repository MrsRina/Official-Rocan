package me.rina.rocan.client.module.render;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.Objects;

public class ModuleFullBright extends Module {

    public ModuleFullBright() {
        super("FullBright", "FullBright", "Changes the brightness of the client.", ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        // Very hard module.
        mc.gameSettings.gammaSetting = 1000.6E9f;
    }


    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = 1.0f;
    }
}