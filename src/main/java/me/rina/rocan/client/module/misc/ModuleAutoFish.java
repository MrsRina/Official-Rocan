package me.rina.rocan.client.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.network.ReceiveEventPacket;
import me.rina.rocan.client.manager.network.PacketAntiSpamManager;
import me.rina.turok.util.TurokTick;
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
    public static ValueNumber settingSplashDelay = new ValueNumber("Splash Delay", "SplashDelay", "The MS delay after the event sound splash.", 750, 1, 3000);
    public static ValueNumber settingPacketDelay = new ValueNumber("Packet Delay", "PacketDelay", "The packet delay for anti spam in somes servers.", 500.0f, 250f, 10000f);

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
            if (tick.isPassedMS(settingSplashDelay.getValue().intValue())) {
                this.print("You fish!");

                Rocan.getPacketAntiSpamManager().sendPacket(new CPacketPlayerTryUseItem());
                Rocan.getPacketAntiSpamManager().sendPacket(new CPacketPlayerTryUseItem());

                this.flag = Flag.Fishing;
            }
        } else {
            tick.reset();

            if (this.flag == Flag.NoFishing && mc.player.fishEntity == null) {
                Rocan.getPacketAntiSpamManager().sendPacket(new CPacketPlayerTryUseItem());

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

        if (Rocan.getPacketAntiSpamManager().isNext() && Rocan.getPacketAntiSpamManager().getLast() != null && Rocan.getPacketAntiSpamManager().getLast() instanceof CPacketPlayerTryUseItem) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }

        if (Rocan.getPacketAntiSpamManager().getDelay() != settingPacketDelay.getValue().floatValue()) {
            Rocan.getPacketAntiSpamManager().setDelay(settingPacketDelay.getValue().floatValue());
        }
    }

    @Override
    public void onEnable() {
        Rocan.getPacketAntiSpamManager().setDelay(settingPacketDelay.getValue().floatValue());

        this.flag = Flag.NoFishing;
    }

    @Override
    public void onDisable() {
        if (this.flag == Flag.Fishing) {
            Rocan.getPacketAntiSpamManager().sendPacket(new CPacketPlayerTryUseItem());
        }

        if (Rocan.getPacketAntiSpamManager().getDelay() != PacketAntiSpamManager.DEFAULT_DELAY) {
            Rocan.getPacketAntiSpamManager().setDelay(PacketAntiSpamManager.DEFAULT_DELAY);
        }

        this.flag = Flag.NoFishing;
    }
}