package me.rina.rocan.mixin.mixins;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.event.impl.EventStage;
import me.rina.rocan.client.event.render.RenderNameEvent;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author SrRina
 * @since 25/02/2021 at 16:21
 **/
@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase<T extends EntityLivingBase> {
    @Inject(method = "renderName", at = @At("HEAD"), cancellable = true)
    public void onRenderNamePre(T entity, double x, double y, double z, CallbackInfo ci) {
        RenderNameEvent event = new RenderNameEvent(EventStage.PRE);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderName", at = @At("RETURN"), cancellable = true)
    public void onRenderNamePost(T entity, double x, double y, double z, CallbackInfo ci) {
        RenderNameEvent event = new RenderNameEvent(EventStage.POST);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
