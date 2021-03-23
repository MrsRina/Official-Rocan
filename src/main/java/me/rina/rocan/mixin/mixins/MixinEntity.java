package me.rina.rocan.mixin.mixins;

import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.module.render.ModuleNoRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author SrRina
 * @since 23/02/2021 at 22:57
 **/
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    public void move(MoverType type, double x, double y, double z) {}

    @Shadow public abstract boolean removeTag(String tag);
}
