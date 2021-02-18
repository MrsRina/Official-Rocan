package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBind;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 18/02/2021 at 09:49
 **/
@Registry(name = "Step", tag = "Step", description = "Ste utility.", category = ModuleCategory.MOVEMENT)
public class ModuleStep extends Module {
    public static ValueBind settingBindNormal = new ValueBind("Bind Normal", "Bind Normal", "Normal step.", -1);
    public static ValueNumber settingHeight = new ValueNumber("Height", "Height", "Height for step.", 2, 1, 4);

    public static ValueBoolean settingReverse = new ValueBoolean("Reverse", "Reverse", "Reverse step.", false);
    public static ValueBind settingBindReverse = new ValueBind("Bind Reverse", "BindReverse", "Step but reverse.", -1);
    public static ValueBoolean settingHole = new ValueBoolean("Hole", "Hole", "Only holes reverse.", true);


    @Override
    public void onSetting() {
        if (settingReverse.getValue()) {
            settingBindReverse.setEnabled(true);
            settingHole.setEnabled(true);
        } else {
            settingBindReverse.setEnabled(false);
            settingBindReverse.setState(false);
            settingHole.setEnabled(false);
        }
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (settingBindNormal.getState()) {
            this.doNormal();
        }

        if (settingBindReverse.getState()) {
            this.doReverse();
        }
    }

    public void doNormal() {

    }

    public void doReverse() {

    }
}
