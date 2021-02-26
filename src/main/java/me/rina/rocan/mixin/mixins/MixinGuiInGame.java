package me.rina.rocan.mixin.mixins;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.event.impl.EventStage;
import me.rina.rocan.client.event.render.RenderPortalOverlayEvent;
import me.rina.rocan.client.event.render.RenderPotionEffects;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author SrRina
 * @since 25/02/2021 at 22:26
 **/
@Mixin(GuiIngame.class)
public class MixinGuiInGame {
    @Inject(method = "renderPotionEffects", at = @At("HEAD"), cancellable = true)
    public void onRenderPotionEffects(ScaledResolution resolution, CallbackInfo ci) {
        RenderPotionEffects event = new RenderPotionEffects(EventStage.PRE);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPotionEffects", at = @At("RETURN"), cancellable = true)
    public void onRenderPotionEffectsPost(ScaledResolution resolution, CallbackInfo ci) {
        RenderPotionEffects event = new RenderPotionEffects(EventStage.POST);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true)
    public void onRenderPortal(float timeInPortal, ScaledResolution scaledRes, CallbackInfo ci) {
        RenderPortalOverlayEvent event = new RenderPortalOverlayEvent(EventStage.PRE);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPortal", at = @At("RETURN"), cancellable = true)
    public void onRenderPortalPost(float timeInPortal, ScaledResolution scaledRes, CallbackInfo ci) {
        RenderPortalOverlayEvent event = new RenderPortalOverlayEvent(EventStage.POST);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
