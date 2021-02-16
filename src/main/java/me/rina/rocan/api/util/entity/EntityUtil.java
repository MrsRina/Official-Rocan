package me.rina.rocan.api.util.entity;

import net.minecraft.client.renderer.debug.DebugRendererNeighborsUpdate;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * @author SrRina
 * @since 15/02/2021 at 18:11
 **/
public class EntityUtil {
    public static Vec3d eye(Entity entity) {
        return new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
    }
}
