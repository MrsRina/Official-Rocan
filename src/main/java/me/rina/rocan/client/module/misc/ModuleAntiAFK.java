package me.rina.rocan.client.module.misc;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokTick;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.util.EnumHand;

/**
 * @author SrRina
 * @since 07/02/2021 at 15:57
 **/
@Registry(name = "Anti-AFK", tag = "AntiAFK", description = "Make you no get kicked by server.", category = ModuleCategory.MISC)
public class ModuleAntiAFK extends Module {
    public static ValueBoolean settingRotate = new ValueBoolean("Rotate", "Rotate", "Rotate camera.", true);

    private TurokTick tick = new TurokTick();
    private float angle;

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        float ms = 3500f;

        if (tick.isPassedMS(ms / 2)) {
            this.angle = TurokMath.lerp(this.angle, 0, Rocan.getClientEventManager().getCurrentRender3DPartialTicks());
        
            // We send swing to...
            mc.player.sendSwing(EnumHand.MAIN_HAND);
        } else {
            this.angle = TurokMath.lerp(this.angle, 90.0f, Rocan.getClientEventManager().getCurrentRender3DPartialTicks());
        }

        // I don't understand.
        if (tick.isPassedMS(ms)) {
            tick.reset();
        }

        if (mc.player.onGround) {
            KeyUtil.press(mc.gameSettings.keyBindJump, true);
        } else {
            KeyUtil.press(mc.gameSettings.keyBindJump, false);
        }

        // Real pitch.
        if (settingRotate.getValue()) {
            PlayerUtil.setPitch(this.angle);
        }
    }
}
