package me.rina.rocan.client.manager.world;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.manager.Manager;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.crystal.HoleUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.turok.util.TurokMath;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 06/03/2021 at 00:19
 **/
public class HoleManager extends Manager {
    private ArrayList<BlockPos> holes;
    private float range = 6;

    public HoleManager() {
        super("Hole Manager", "Client calculate holes and place in a list.");

        this.holes = new ArrayList<>();
    }

    public void setHoles(ArrayList<BlockPos> holes) {
        this.holes = holes;
    }

    public ArrayList<BlockPos> getHoles() {
        return holes;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getRange() {
        return range;
    }

    @Override
    public void onUpdateAll() {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        holes.clear();

        int r = TurokMath.ceiling(range);

        List<BlockPos> sphereList = HoleUtil.getSphereList(PlayerUtil.getBlockPos(), r, r, false, true);

        for (BlockPos blocks : sphereList) {
            if (BlockUtil.isAir(blocks) == false) {
                continue;
            }

            if (BlockUtil.isAir(blocks.add(0, 1, 0)) == false) {
                continue;
            }

            if (BlockUtil.isAir(blocks.add(0, 2, 0)) == false) {
                continue;
            }

            boolean isHole = true;

            for (BlockPos _blocks : HoleUtil.SURROUND) {
                Block block = Rocan.MC.world.getBlockState(blocks.add(_blocks)).getBlock();

                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    isHole = false;

                    break;
                }
            }

            // Clean code!
            if (isHole) {
                holes.add(blocks);
            }
        }
    }
}
