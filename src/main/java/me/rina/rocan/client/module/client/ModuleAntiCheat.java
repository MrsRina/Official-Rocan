package me.rina.rocan.client.module.client;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueEnum;

/**
 * @author SrRina
 * @since 27/02/2021 at 16:19
 **/
@Registry(name = "Anti-Cheat", tag = "AntiCheat", description = "Enables if you know what anti-cheat is on.", category = ModuleCategory.CLIENT)
public class ModuleAntiCheat extends Module {
    public static ModuleAntiCheat INSTANCE;

    public static ValueEnum settingType = new ValueEnum("Type", "Type", "Type of anti-cheat.", Type.NCP);

    public enum Type {
        NCP, VANILLA;
    }

    public ModuleAntiCheat() {
        INSTANCE = this;
    }

    public static float getRange() {
        float range = 0f;

        if (INSTANCE.isEnabled() == false) {
            range = 5f;
        }

        if (settingType.getValue() == Type.NCP) {
            range = 4.3f;
        }

        if (settingType.getValue() == Type.VANILLA) {
            range = 5.0f;
        }

        return range;
    }
}
