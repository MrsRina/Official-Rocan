package me.rina.rocan.client.module.misc;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokTick;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 07/02/2021 at 15:57
 **/
public class ModuleAntiAFK extends Module {
    public static ValueNumber settingSpeed = new ValueNumber("Speed", "Speed", "Speed delay of movement.", 500, 10, 1000);
    public static ValueBoolean settingRotate = new ValueBoolean("Rotate", "Rotate", "Rotate camera.", true);

    private TurokTick tick = new TurokTick();
    private float angle;

    public ModuleAntiAFK() {
        super("Anti-AFK", "AntiAFK", "Make you no get kicked by server.", ModuleCategory.MISC);
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        // @everyone
        float ms = (float) settingSpeed.getValue().intValue();

        // Math skill.
        if (tick.isPassedMS(ms / 2)) {
            this.angle = TurokMath.lerp(this.angle, 0, Rocan.getClientEventManager().getCurrentRender3DPartialTicks());
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

        // Real yaw bitch.
        if (settingRotate.getValue()) {
            PlayerUtil.setPitch(this.angle);
        }
    }
}
