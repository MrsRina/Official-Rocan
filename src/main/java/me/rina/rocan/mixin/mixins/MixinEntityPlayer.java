package me.rina.rocan.mixin.mixins;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author SrRina
 * @since 23/02/2021 at 23:05
 **/
@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinEntity {
}
