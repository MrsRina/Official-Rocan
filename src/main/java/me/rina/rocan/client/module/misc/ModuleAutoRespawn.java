package me.rina.rocan.client.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.client.gui.GuiGameOver;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 02/02/2021 at 00:07
 **/
@Registry(name = "Auto-Respawn", tag = "AutoRespawn", description = "Automatically respawn after you die.", category = ModuleCategory.MISC)
public class ModuleAutoRespawn extends Module {
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "The seconds delay for respawn.", 0, 0, 10);
    public static ValueBoolean settingDeathPosition = new ValueBoolean("Death Position", "DeathPosition", "Send the last position client side message after you die.", true);

    private boolean hasSentMessage;
    private TurokTick tick = new TurokTick();

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.currentScreen instanceof GuiGameOver) {
            this.doRequest();
        } else {
            this.hasSentMessage = true;

            tick.reset();
        }
    }

    public void doRequest() {
        if (tick.isPassedMS(settingDelay.getValue().floatValue() * 1000)) {
            if (settingDeathPosition.getValue()) {
                double[] pos = PlayerUtil.getPos();

                // We need set the color for the last position, to sync dimension with color.
                String position = "[" + ((int) pos[0]) + ", " + ((int) pos[1]) + ", " + ((int) pos[2]) + "]";

                // Don't fucking repeat the fucking message.
                if (this.hasSentMessage) {
                    this.print("You died at " + this.getColorBasedDimension() + position);

                    // Don't fucking repeat the fucking message.
                    this.hasSentMessage = false;
                }
            }

            // What does it do.
            mc.player.respawnPlayer();
        }
    }

    // Based.
    public String getColorBasedDimension() {
        String string = null;

        if (PlayerUtil.getCurrentDimension() == PlayerUtil.Dimension.WORLD) {
            string = ("" + ChatFormatting.GREEN);
        }

        if (PlayerUtil.getCurrentDimension() == PlayerUtil.Dimension.NETHER) {
            string = ("" + ChatFormatting.RED);
        }

        if (PlayerUtil.getCurrentDimension() == PlayerUtil.Dimension.END) {
            string = ("" + ChatFormatting.BLUE);
        }

        // Can't speak.
        return string;
    }
}
