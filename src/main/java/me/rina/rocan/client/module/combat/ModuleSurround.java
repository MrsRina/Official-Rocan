package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.tracker.Tracker;
import me.rina.rocan.api.tracker.impl.RightMouseClickOnBlockTracker;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.crystal.HoleUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.api.util.math.PosUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 11/02/2021 at 13:17
 **/
@Registry(name = "Surround", tag = "Surround", description = "Automatically places block around of you.", category = ModuleCategory.COMBAT)
public class ModuleSurround extends Module {
    public static ValueNumber settingMaximumTime = new ValueNumber("Maximum Time", "MaximumTime", "Time out ticks to disable module.", 1000,  250, 3000);
    public static ValueEnum settingMode = new ValueEnum("Mode", "Mode", "Modes to place blocks.", Mode.SURROUND);
    public static ValueBoolean settingSwingAnimation = new ValueBoolean("Swing Animation", "SwingAnimation", "Enable swing animation.", true);

    private TurokTick tick = new TurokTick();

    private int slot;
    private int oldSlot;

    public enum Mode {
        HOLE, SURROUND;
    }

    public enum Flag {
        PLACED, STUCK, AIR;
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (this.slot == -1) {
            this.setDisabled();

            return;
        }

        if (this.tick.isPassedMS(settingMaximumTime.getValue().intValue())) {
            this.setDisabled();
            this.tick.reset();
        }

        BlockPos playerPos = PlayerUtil.getBlockPos();

        int placed = 0;
        int stuck = 0;
        int air = 0;

        BlockPos[] currentSurround = settingMode.getValue() == Mode.HOLE ? HoleUtil.SURROUND : HoleUtil.FULL_SURROUND;

        for (BlockPos offsetPos : currentSurround) {
            Flag flag = this.doPlace(playerPos.add(offsetPos));

            if (flag == Flag.AIR) {
                air++;
            }

            if (flag == Flag.STUCK) {
                stuck++;

                continue;
            }

            if (flag == Flag.PLACED) {
                placed++;
            }

            if (placed + air >= currentSurround.length) {
                this.tick.reset();
                this.setDisabled();
            }
        }
    }

    @Override
    public void onEnable() {
        if (NullUtil.isPlayer()) {
            return;
        }

        Item itemObsidian = Item.getItemFromBlock(Blocks.OBSIDIAN);

        this.oldSlot = mc.player.inventory.currentItem;
        this.slot = this.oldSlot;

        if (SlotUtil.getItemStack(this.oldSlot).getItem() != itemObsidian) {
            this.slot = SlotUtil.findItemSlotFromHotBar(itemObsidian);
        }
    }

    @Override
    public void onDisable() {
        if (NullUtil.isPlayer()) {
            return;
        }

        mc.player.inventory.currentItem = this.oldSlot;
    }

    public Flag doPlace(BlockPos pos) {
        if (BlockUtil.isAir(pos) == false) {
            return Flag.AIR;
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
            return Flag.STUCK;
        }

        EnumFacing facing = BlockUtil.getFacing(offset, mc.player);
        Vec3d hit = PosUtil.getHit(offset, facing);

        if (mc.player.getHeldItemMainhand().getItem() != Item.getItemFromBlock(Blocks.OBSIDIAN)) {
            mc.player.inventory.currentItem = this.slot;
        }

        if (settingSwingAnimation.getValue()) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }

        mc.playerController.processRightClickBlock(mc.player, mc.world, offset, facing, hit, EnumHand.MAIN_HAND);

        return Flag.PLACED;
    }
}