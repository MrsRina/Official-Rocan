package me.rina.rocan.mixin.mixins;

import me.rina.rocan.Rocan;
import me.rina.rocan.client.event.entity.PlayerMoveEvent;
import me.rina.rocan.mixin.interfaces.IEntityPlayerSP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author SrRina
 * @since 23/02/2021 at 23:06
 **/
@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinEntityPlayer implements IEntityPlayerSP {
    @Override
    public void move(MoverType type, double x, double y, double z) {
        PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);

        Rocan.getPomeloEventManager().dispatchEvent(event);

        super.move(event.getType(), event.getX(), event.getY(), event.getZ());
    }
}