package me.rina.rocan.client.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
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
public class ModuleAutoFish extends Module {
    public static ValueNumber settingSplashDelay = new ValueNumber("Splash Delay", "SplashDelay", "The MS delay after the event sound splash.", 750, 1, 3000);
    public static ValueNumber settingPacketDelay = new ValueNumber("Packet Delay", "PacketDelay", "The MS delay for send packet.", 500, 0, 3000);

    private Flag flag = Flag.NoFishing;
    private TurokTick tick = new TurokTick();

    private Tracker tracker;

    public ModuleAutoFish() {
        super("Auto-Fish", "AutoFish", "Automatically fish to you.", ModuleCategory.MISC);

        this.tracker = new Tracker("AutoFishTrack");
        this.tracker.inject();
    }

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

        SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
        SoundEvent currentSoundEvent = packet.getSound();

        if (currentSoundEvent == SoundEvents.ENTITY_BOBBER_SPLASH) {
            this.flag = Flag.Splash;
        }
    }

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.player.getHeldItemMainhand().getItem() != Items.FISHING_ROD) {
            this.print("No fishing rod at hand!");
            this.setDisabled();

            return;
        }

        if (this.flag == Flag.Splash) {
            if (tick.isPassedMS(settingSplashDelay.getValue().intValue())) {
                this.print("You fish!");

                this.tracker.send(new RightMouseClickTracker(EnumHand.MAIN_HAND));
                this.tracker.join(new RightMouseClickTracker(EnumHand.MAIN_HAND));

                this.flag = Flag.Fishing;
            }
        } else {
            tick.reset();

            if (this.flag == Flag.NoFishing && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                this.flag = Flag.Fishing;
            }

            if (this.flag == Flag.Fishing) {
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
        this.tracker.register();
    }

    @Override
    public void onDisable() {
        if (this.flag == Flag.Fishing) {
            this.tracker.send(new RightMouseClickTracker(EnumHand.MAIN_HAND));
        }

        this.tracker.unregister();
        this.flag = Flag.NoFishing;
    }
}