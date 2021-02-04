package me.rina.rocan.client.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
;
import me.rina.rocan.client.event.network.ReceiveEventPacket;
import me.rina.turok.util.TurokTick;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 02/02/2021 at 13:28
 **/
public class ModuleAutoFish extends Module {
    public static ValueNumber settingMSDelay = new ValueNumber("Splash MS Delay", "SplashMSDelay", "The MS delay after the event sound splash.", 750, 1, 3000);

    private Flag flag = Flag.NoFishing;
    private TurokTick tick = new TurokTick();

    public ModuleAutoFish() {
        super("Auto-Fish", "AutoFish", "Automatically fish to you.", ModuleCategory.Misc);
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
            if (tick.isPassedMS(settingMSDelay.getValue().intValue())) {
                this.print("You fish!");

                this.swingFishingRod();
                this.swingFishingRod();

                this.flag = Flag.Fishing;
            }
        } else {
            tick.reset();

            if (this.flag == Flag.NoFishing && mc.player.fishEntity == null) {
                this.swingFishingRod();
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
    }

    @Override
    public void onDisable() {
        if (this.flag == Flag.Fishing) {
            this.swingFishingRod();
        }

        this.flag = Flag.NoFishing;
    }

    public void swingFishingRod() {
        EnumHand currentHand = EnumHand.MAIN_HAND;

        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(currentHand));
        mc.player.swingArm(currentHand);
    }
}