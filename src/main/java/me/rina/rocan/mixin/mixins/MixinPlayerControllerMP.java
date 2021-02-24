package me.rina.rocan.mixin.mixins;

import me.rina.rocan.Rocan;
import me.rina.rocan.client.event.entity.PlayerDamageBlockEvent;
import me.rina.rocan.client.module.client.ModuleTPSSync;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author SrRina
 * @since 08/02/2021 at 15:29
 **/
@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {
    @Inject(method = "onPlayerDamageBlock", at = @At("INVOKE"), cancellable = true)
    private void onPlayerDamageBlock(BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> info) {
        PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(pos, facing);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Redirect(method = "onPlayerDamageBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockProperties;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"))
    float getPlayerRelativeBlockHardness(IBlockProperties iBlockProperties, EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return iBlockProperties.getPlayerRelativeBlockHardness(entityPlayer, world, blockPos) * (ModuleTPSSync.INSTANCE.isEnabled() ? (ModuleTPSSync.getTPS() / 20f) : 1f);
    }
}
