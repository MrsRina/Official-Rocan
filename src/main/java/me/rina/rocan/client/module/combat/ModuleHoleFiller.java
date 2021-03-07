package me.rina.rocan.client.module.combat;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.crystal.HoleUtil;
import me.rina.rocan.api.util.entity.PlayerRotationUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.api.util.math.PositionUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 06/03/2021 at 00:17
 **/
@Registry(name = "Hole Filler", tag = "HoleFiller", description = "Automatically places blocks in hole.", category = ModuleCategory.COMBAT)
public class ModuleHoleFiller extends Module {
    /* General settings. */
    public static ValueNumber settingRange = new ValueNumber("Range", "Range", "The maximum range for place blocks and collect.", 6f, 1f, 6f);
    public static ValueNumber settingTimeOut = new ValueNumber("Time Out", "TimeOut", "Time out delay for disable filler.", 1000, 1, 3000);
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "The MS delay for places blocks on queue system.", 50, 0, 1000);
    public static ValueNumber settingHoleLimit = new ValueNumber("Hole Limit", "HoleLimit", "Limit of holes to place blocks.", 6, 1, 24);


    private final Item obsidian = Item.getItemFromBlock(Blocks.OBSIDIAN);
    private ArrayList<BlockPos> blocks = new ArrayList<>();

    private int slot;
    private int oldSlot;

    private int universalCountHolePlaced;

    private TurokTick tickQueue = new TurokTick();
    private TurokTick tickTimeOut = new TurokTick();

    @Listener
    public void onListenClientTick(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (this.slot == -1) {
            this.setDisabled();

            return;
        }

        if (this.tickTimeOut.isPassedMS(settingTimeOut.getValue().intValue()) || (this.universalCountHolePlaced >= settingHoleLimit.getValue().intValue())) {
            this.setDisabled();
            this.tickTimeOut.reset();

            return;
        }

        for (BlockPos holes : Rocan.getHoleManager().getHoles()) {
            if (holes.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) >= settingRange.getValue().intValue()) {
                continue;
            }

            if (this.blocks.contains(holes)) {
                continue;
            }

            this.blocks.add(holes);
        }

        this.onQueue();
    }

    @Override
    public void onEnable() {
        this.tickTimeOut.reset();
        this.tickQueue.reset();
        this.blocks.clear();

        this.universalCountHolePlaced = 0;

        if (NullUtil.isPlayerWorld()) {
            return;
        }

        this.oldSlot = mc.player.inventory.currentItem;
        this.slot = this.oldSlot;

        if (SlotUtil.getItemStack(this.oldSlot).getItem() != obsidian) {
            this.slot = SlotUtil.findItemSlotFromHotBar(obsidian);
        }
    }

    @Override
    public void onDisable() {
        this.tickTimeOut.reset();
        this.tickQueue.reset();
        this.blocks.clear();

        this.universalCountHolePlaced = 0;

        if (NullUtil.isPlayerWorld()) {
            return;
        }

        mc.player.inventory.currentItem = this.oldSlot;
    }

    public void onQueue() {
        if (this.blocks.isEmpty()) {
            this.tickQueue.reset();
        } else {
            BlockPos position = this.blocks.get(0);

            if (this.tickQueue.isPassedMS(settingDelay.getValue().intValue())) {
                this.doPlace(position);

                this.blocks.remove(0);
                this.tickQueue.reset();
            }
        }
    }

    public void doUpdateCurrentItem() {
        this.slot = SlotUtil.findItemSlotFromHotBar(obsidian);

        if (this.slot == -1) {
            this.setDisabled();
        } else {
            mc.player.inventory.currentItem = this.slot;
        }
    }

    public void doPlace(BlockPos pos) {
        /*
         * First verification, for unnecessary segment.
         */
        this.doUpdateCurrentItem();

        if (BlockUtil.isAir(pos) == false) {
            return;
        }

        /*
         * Cancel if there is air in all surround of block.
         */
        boolean isCanceled = true;

        /*
         * The offset pos to place block.
         */
        BlockPos offset = pos;

        for (BlockPos offsetPos : HoleUtil.SURROUND) {
            BlockPos offsetPosition = pos.add(offsetPos);

            if (BlockUtil.isAir(offsetPosition) == false) {
                offset = offsetPosition;
                isCanceled = false;

                break;
            }
        }

        if (isCanceled) {
            return;
        }

        EnumFacing facing = BlockUtil.getFacing(offset, mc.player);
        Vec3d hit = PositionUtil.hit(offset, facing);

        /*
         * Last update in hot bar, for not hack slot kick.
         */
        this.doUpdateCurrentItem();

        // Send swing anim to server.
        mc.player.swingArm(EnumHand.MAIN_HAND);

        // Rotate.
        PlayerRotationUtil.packet(hit);

        if (mc.player.getHeldItemMainhand().getItem() == obsidian) {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, hit, EnumHand.MAIN_HAND);

            this.universalCountHolePlaced++;
        }
    }
}
