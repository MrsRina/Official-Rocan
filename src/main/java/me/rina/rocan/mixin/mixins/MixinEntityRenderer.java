package me.rina.rocan.mixin.mixins;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.module.render.ModuleNoRender;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author SrRina
 * @since 22/03/2021 at 12:32
 **/
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void onHurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (ModuleNoRender.settingHurtEffectCamera.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "setupFog", at = @At("RETURN"), cancellable = true)
    public void onSetupFrog(int startCoords, float partialTicks, CallbackInfo ci) {
        if (ModuleNoRender.settingFog.getValue()) {
            GlStateManager.disableFog();
        }
    }
}
