package me.rina.rocan.mixin.mixins;

import me.rina.rocan.Rocan;
import me.rina.rocan.client.event.entity.PlayerDamageBlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author SrRina
 * @since 08/02/2021 at 15:29
 **/
@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "onPlayerDamageBlock", at = @At("INVOKE"), cancellable = true)
    private void onPlayerDamageBlock(BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> info) {
        PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(pos, facing);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            info.cancel();
        }
    }
}
