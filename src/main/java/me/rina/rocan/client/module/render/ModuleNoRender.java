package me.rina.rocan.client.module.render;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.network.PacketEvent;
import me.rina.rocan.client.event.render.RenderPortalOverlayEvent;
import me.rina.rocan.client.event.render.RenderPotionEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.*;

/**
 * @author SrRina
 * @since 25/02/2021 at 15:46
 **/
@Registry(name = "No Render", tag = "NoRender", description = "Preserve some elements render.", category = ModuleCategory.RENDER)
public class ModuleNoRender extends Module {
    /* Elements. */
    public static ValueBoolean settingBossInfo = new ValueBoolean("Boss Info", "BossInfo", "No render boss info.", true);
    public static ValueBoolean settingCrossHair = new ValueBoolean("Cross Hair", "CrossHair", "No render split cross hair.", false);
    public static ValueBoolean settingPotionIcons = new ValueBoolean("Potion Icons", "PotionIcons", "No render potion icons.", true);
    public static ValueBoolean settingPortal = new ValueBoolean("Portal", "Portal", "No render portal... ?", false);
    public static ValueBoolean settingPumpkin = new ValueBoolean("Pumpkin", "Pumpkin", "Disables pumpkin render.", true);

    /* Others. */
    public static ValueBoolean settingHurtEffectCamera = new ValueBoolean("Hurt Effect Camera", "HurtEffectCamera", "Disables hurt effect in camera.",true);
    public static ValueBoolean settingFire = new ValueBoolean("Fire", "Fire", "No render fire.", true);
    public static ValueBoolean settingFog = new ValueBoolean("Fog", "Fog", "No render fog.", false);
    public static ValueBoolean settingRain = new ValueBoolean("Rain", "Rain", "No render rain.", true);
    public static ValueBoolean settingFloorDroppedItem = new ValueBoolean("Floor Dropped Item", "FloorDroppedItem", "No render dropped item.", true);

    /* World time. */
    public static ValueBoolean settingCustomWorldTime = new ValueBoolean("Custom World Time", "CustomWorldTime", "Customize world time.", false);
    public static ValueNumber settingWorldTime = new ValueNumber("World Time", "WorldTime", "Set world time.", 1000, 0, 23000);

    @Override
    public void onSetting() {
        settingWorldTime.setEnabled(settingCustomWorldTime.getValue());
    }

    @Listener
    public void onListenPacketEvent(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate && settingCustomWorldTime.getValue()) {
            event.setCanceled(true);
        }
    }

    @Listener
    public void onListenClientTickEvent(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.player.isPotionActive(MobEffects.NAUSEA)) {
            mc.player.removePotionEffect(MobEffects.NAUSEA);
        }

        if (settingFloorDroppedItem.getValue()) {
            for (Entity entities : mc.world.loadedEntityList) {
                if (entities instanceof EntityItem) {
                    EntityItem entityItem = (EntityItem) entities;

                    entityItem.setDead();
                }
            }
        }

        if (mc.world.isRaining() && settingRain.getValue()) {
            mc.world.setRainStrength(0f);
        }

        if (settingCustomWorldTime.getValue()) {
            mc.world.setWorldTime(settingWorldTime.getValue().intValue());
        }
    }

    @SubscribeEvent
    public void onOverlay(RenderGameOverlayEvent event) {
        if (!this.isEnabled()) {
            return;
        }

        if (event.getType() == ElementType.CROSSHAIRS && settingCrossHair.getValue()) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}