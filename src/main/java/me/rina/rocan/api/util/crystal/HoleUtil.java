package me.rina.rocan.api.util.crystal;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 28/01/2021 at 16:46
 **/
public class HoleUtil {
    public static final BlockPos[] SURROUND = {
            new BlockPos(0, -1, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, -1)
    };

    public static List<BlockPos> getSphereList(BlockPos blockPos, float r, int h, boolean hollow, boolean sphere) {
        List<BlockPos> sphereList = new ArrayList<>();

        int cx = blockPos.x;
        int cy = blockPos.y;
        int cz = blockPos.z;

        for (int x = cx - (int) r; x <= cx + r; ++x) {
            for (int z = cz - (int) r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int) r) : cy; y < (sphere ? (cy + r) : ((float) (cy + h))); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        BlockPos spheres = new BlockPos(x, y, z);

                        sphereList.add(spheres);
                    }
                }
            }
        }

        return sphereList;
    }
}
