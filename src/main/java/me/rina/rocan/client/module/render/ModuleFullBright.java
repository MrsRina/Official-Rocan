package me.rina.rocan.client.module.render;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.Objects;

/**
 * Jake! not make me insane! don't put 1k in gamma!!! it glicth all game light!!!!
 * lol
 */
@Registry(name = "Full Bright", tag = "FullBright", description = "Changes the brightness of the client.", category = ModuleCategory.RENDER)
public class ModuleFullBright extends Module {
    @Override
    public void onEnable() {
        mc.gameSettings.gammaSetting = 100f;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = 1.0f;
    }
}