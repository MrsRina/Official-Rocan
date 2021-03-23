package me.rina.rocan.mixin.mixins;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.event.render.EnumHandSideEvent;
import me.rina.rocan.client.module.render.ModuleNoRender;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Inject(method = "transformSideFirstPerson", at = @At("HEAD"))
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo ci) {
        EnumHandSideEvent event = new EnumHandSideEvent(hand);
        Rocan.getPomeloEventManager().dispatchEvent(event);
    }

    @Inject(method = "transformFirstPerson", at = @At("HEAD"))
    public void transformFirstPerson(EnumHandSide hand, float p_187453_2_, CallbackInfo ci){
        EnumHandSideEvent event = new EnumHandSideEvent(hand);
        Rocan.getPomeloEventManager().dispatchEvent(event);
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    public void onRenderFireInFirstPerson(CallbackInfo ci) {
        boolean flag = ModuleManager.get(ModuleNoRender.class).isEnabled() && ModuleNoRender.settingFire.getValue();

        if (flag) {
            ci.cancel();
        }
    }
}