package me.rina.rocan.api.util.math;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.util.entity.EntityUtil;
import me.rina.turok.util.TurokMath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author SrRina
 * @since 15/02/2021 at 18:10
 **/
public class RotationUtil {
    public static float[] legit(Vec3d pos) {
        Vec3d eye = EntityUtil.eye(Rocan.MC.player);

        double x = pos.x - eye.x;
        double y = pos.y - eye.y;
        double z = pos.z - eye.z;

        double diff = TurokMath.sqrt(x * x + z * z);

        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, diff));

        return new float[] {
                Rocan.MC.player.rotationYaw + MathHelper.wrapDegrees(yaw - Rocan.MC.player.rotationYaw),
                Rocan.MC.player.rotationPitch + MathHelper.wrapDegrees(pitch - Rocan.MC.player.rotationPitch)
        };
    }
}
