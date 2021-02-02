package me.rina.rocan.client.module.misc;

import cat.yoink.eventmanager.Listener;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.network.EventPacket;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokTick;
import net.minecraft.block.Block;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * @author SrRina
 * @since 02/02/2021 at 13:28
 **/
public class ModuleAutoFish extends Module {
    public static ValueNumber settingTimePostSplash = new ValueNumber("Time Post Splash Fishing", "TimePostSplashFishing", "The time later watter splash.", 3, 1, 60);

    private boolean isFishing;
    private boolean isSplash;

    private int currentSlot;

    private TurokTick tick = new TurokTick();

    public ModuleAutoFish() {
        super("Auto-Fish", "AutoFishing", "Automatically start fishing.", ModuleCategory.Misc);
    }

    public void onListenReceivePacket(EventPacket.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {

            SPacketSoundEffect eventPacketSoundEffect = (SPacketSoundEffect) event.getPacket();

            SoundEvent currentSoundEvent = eventPacketSoundEffect.getSound();

            double x = mc.player.fishEntity.posX - eventPacketSoundEffect.posX;
            double y = mc.player.fishEntity.posY - eventPacketSoundEffect.posY;
            double z = mc.player.fishEntity.posZ - eventPacketSoundEffect.posZ;

            double distance = TurokMath.sqrt(x * x + y * y + z * z);

            if (currentSoundEvent.getSoundName().equals("entity.bobber.splash") && distance <= 5) {
                mc.rightClickMouse();

                this.isSplash = true;
            }
        }
    }

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld() && this.isEnabled() == false) {
            return;
        }

        if (mc.player.isDead || mc.player.getHealth() <= 0f) {
            this.setDisabled();

            this.isFishing = false;

            return;
        }

        currentSlot = SlotUtil.getCurrentItemSlotHotBar();

        if (mc.player.inventory.getStackInSlot(currentSlot).getItem() != Items.FISHING_ROD) {
            ChatUtil.print("Current item at hand is not a fishing rod.");

            this.isFishing = false;

            this.setDisabled();
            return;
        }

        if (this.isSplash) {
            if (tick.isPassedMS(this.settingTimePostSplash.getValue().intValue() * 1000)) {
                mc.rightClickMouse();

                this.isSplash = false;
            }
        } else {
            tick.reset();
        }

        if (mc.player.fishEntity != null) {
            Vec3d vec = new Vec3d(mc.player.fishEntity.posX, mc.player.fishEntity.posY, mc.player.fishEntity.posZ);
            Block block = mc.world.getBlockState(new BlockPos(vec.x, vec.y, vec.z)).getBlock();

            if (!mc.player.fishEntity.isOverWater() || !mc.player.fishEntity.isInWater()) {
                mc.rightClickMouse();
            }
        }
    }

    @Override
    public void onDisable() {
        this.isFishing = false;
    }
}
