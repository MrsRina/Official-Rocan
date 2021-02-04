package me.rina.rocan.client.module.misc;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.network.EventPacket;;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 02/02/2021 at 13:28
 **/
public class ModuleAutoFish extends Module {
    public static ValueNumber settingMSTimePostSplash = new ValueNumber("MS Delay Post Splash Fishing", "MSDelayPostSplashFishing", "The MS later watter splash.", 750, 1, 3000);

    private Flag flag = Flag.NoFishRood;
    private int currentSlot;

    private TurokTick tick = new TurokTick();

    public ModuleAutoFish() {
        super("Auto-Fish", "AutoFish", "Automatically start fishing.", ModuleCategory.Misc);
    }

    public enum Flag {
        Splash, Fishing, NoFishing, NoFishRood;
    }

    @Listener
    public void onListenReceivePacket(EventPacket.Receive event) {
        if ((event.getPacket() instanceof SPacketSoundEffect) == false) {
            return;
        }

        SPacketSoundEffect eventPacketSoundEffect = (SPacketSoundEffect) event.getPacket();
        SoundEvent currentSoundEvent = eventPacketSoundEffect.getSound();

        ChatUtil.print(currentSoundEvent.getSoundName().toString());

        if (currentSoundEvent == SoundEvents.ENTITY_BOBBER_SPLASH) {
            this.flag = Flag.Splash;
        }
    }

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        currentSlot = SlotUtil.getCurrentItemSlotHotBar();

        if (mc.player.inventory.getStackInSlot(currentSlot).getItem() != Items.FISHING_ROD) {
            this.print("no fishing rod at hand.");

            this.setDisabled();

            return;
        }

        if (this.flag == Flag.Splash) {
            this.print("you fish!");

            mc.rightClickMouse();

            this.flag = Flag.NoFishing;
        } else {
            tick.reset();

            if (mc.player.fishEntity != null) {
                if (mc.player.fishEntity.onGround) {
                    this.print("you can not fish out of water!");

                    mc.rightClickMouse();

                    this.flag = Flag.NoFishing;
                } else {
                    if (this.flag == Flag.NoFishing) {
                        mc.rightClickMouse();

                        this.flag = Flag.Fishing;
                    }
                }
            } else {
                this.flag = Flag.Fishing;
            }
        }
    }

    @Override
    public void onEnable() {
        this.flag = Flag.NoFishing;
    }

    @Override
    public void onDisable() {
        this.flag = Flag.NoFishing;
    }
}
