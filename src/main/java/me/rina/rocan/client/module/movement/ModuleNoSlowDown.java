package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import net.minecraftforge.client.event.InputUpdateEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 24/02/2021 at 16:01
 **/
@Registry(name = "No Slow Down", tag = "NoSlowDown", description = "No slow down while use item.", category = ModuleCategory.MOVEMENT)
public class ModuleNoSlowDown extends Module {
    @Listener
    public void onUpdate(InputUpdateEvent event) {
        if (mc.player.isHandActive() && mc.player.isRiding() == false) {
            event.getMovementInput().moveStrafe *= 5;
            event.getMovementInput().moveForward *= 5;
        }
    }
}
