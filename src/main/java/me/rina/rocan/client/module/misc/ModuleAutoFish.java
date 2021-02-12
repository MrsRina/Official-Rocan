package me.rina.rocan.client.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.tracker.Tracker;
import me.rina.rocan.api.tracker.impl.RightMouseClickTracker;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.network.ReceiveEventPacket;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 02/02/2021 at 13:28
 **/
@Registry(name = "Auto-Fish", tag = "AutoFish", description = "Automatically fish to you.", category = ModuleCategory.MISC)
public class ModuleAutoFish extends Module {
    public static ValueNumber settingSplashDelay = new ValueNumber("Splash Delay", "SplashDelay", "The MS delay after the sound splash event.", 750, 1, 3000);
    public static ValueNumber settingPacketDelay = new ValueNumber("Packet Delay", "PacketDelay", "The MS delay for sending packet.", 500, 0, 3000);

    private Flag flag = Flag.NoFishing;
    private TurokTick tick = new TurokTick();

    private Tracker tracker = new Tracker("AutoFishTrack").inject();

    @Override
    public void onSetting() {
        this.tracker.setDelay((float) settingPacketDelay.getValue().intValue());
    }

    public enum Flag {
        Splash, Fishing, NoFishing;
    }

    @Listener
    public void onListen(ReceiveEventPacket event) {
        if ((event.getPacket() instanceof SPacketSoundEffect) == false) {
            return;
        }

        // We set the variables to use.
        SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
        SoundEvent currentSoundEvent = packet.getSound();

        // Verify the current sound.
        if (currentSoundEvent == SoundEvents.ENTITY_BOBBER_SPLASH) {
            this.flag = Flag.Splash;
        }
    }

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        // We need verify if has Fishing Rod at hand!
        if (mc.player.getHeldItemMainhand().getItem() != Items.FISHING_ROD) {
            this.print("No fishing rod at hand!");
            this.setDisabled();

            return;
        }

        if (this.flag == Flag.Splash) {
            // There is the delay to late splash.
            if (this.tick.isPassedMS(settingSplashDelay.getValue().intValue())) {
                this.print("You fish!");

                // We skip queue and send.
                this.tracker.send(new RightMouseClickTracker(EnumHand.MAIN_HAND));

                // We send for queue.
                this.tracker.join(new RightMouseClickTracker(EnumHand.MAIN_HAND));

                // End flag for splash, so it back to fishing system.
                this.flag = Flag.Fishing;
            }
        } else {
            // Reset the tick for no delay for the delay..
            this.tick.reset();

            // I don't know.
            if (this.flag == Flag.NoFishing && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                this.flag = Flag.Fishing;
            }

            if (this.flag == Flag.Fishing) {
                // We can't fish out of water!
                if (mc.player.fishEntity.onGround) {
                    this.print("You can't fish out of " + ChatFormatting.BLUE + "water" + ChatFormatting.WHITE + ".");
                    this.setDisabled();

                    return;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        this.flag = Flag.NoFishing;

        // We register the tracker for listen the packets on module.
        this.tracker.register();
    }

    @Override
    public void onDisable() {
        // Not sure, but we do it.
        if (this.flag == Flag.Fishing) {
            this.tracker.send(new RightMouseClickTracker(EnumHand.MAIN_HAND));
        }

        this.flag = Flag.NoFishing;

        // Stop tracking packet.
        this.tracker.unregister();
    }
}