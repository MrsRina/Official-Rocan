package me.rina.rocan.mixin.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author SrRina
 * @since 23/02/2021 at 22:57
 **/
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    public void move(MoverType type, double x, double y, double z) {}
}
