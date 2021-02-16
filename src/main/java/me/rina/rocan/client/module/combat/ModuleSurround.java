package me.rina.rocan.client.module.combat;

import ibxm.Player;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.crystal.HoleUtil;
import me.rina.rocan.api.util.entity.PlayerRotationUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.api.util.math.BlockPosUtil;
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
 * @since 11/02/2021 at 13:17
 **/
@Registry(name = "Surround", tag = "Surround", description = "Automatically places block around of you.", category = ModuleCategory.COMBAT)
public class ModuleSurround extends Module {
    public static ValueBoolean settingSwingAnimation = new ValueBoolean("Swing Animation", "SwingAnimation", "Hand swing animation.", true);
    public static ValueBoolean settingManualRotation = new ValueBoolean("Manual Rotation", "ManualRotation", "Player manually rotate.", false);

    public static ValueNumber settingTimeOut = new ValueNumber("Time Out", "TimeOut", "The time out for cancel everything.", 3000, 0, 3000);
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "Delay for places blocks.", 250, 0, 500);
    public static ValueEnum settingMode = new ValueEnum("Mode", "Mode", "Modes to place blocks.", Mode.SURROUND);

    private ArrayList<BlockPos> blocks = new ArrayList<>();

    private TurokTick tickQueue = new TurokTick();
    private TurokTick tickTimeOut = new TurokTick();

    private int slot;
    private int oldSlot;
    private int length;

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

        if (this.tickTimeOut.isPassedMS(settingTimeOut.getValue().intValue())) {
            this.setDisabled();
            this.tickTimeOut.reset();
        }

        BlockPos playerPos = PlayerUtil.getBlockPos();
        BlockPos[] currentSurround = settingMode.getValue() == Mode.HOLE ? HoleUtil.SURROUND : HoleUtil.FULL_SURROUND;

        this.length = currentSurround.length;

        int countPlaces = 0;

        for (BlockPos offsetPos : HoleUtil.SURROUND) {
            BlockPos offset = playerPos.add(offsetPos);

            if (BlockUtil.isAir(offset)) {
                continue;
            }

            if (countPlaces >= HoleUtil.SURROUND.length - 1) {
                this.setDisabled();
            }

            countPlaces++;
        }

        for (BlockPos offsetPos : currentSurround) {
            BlockPos offset = playerPos.add(offsetPos);

            if (BlockUtil.isAir(offset) == false) {
                continue;
            }

            if (this.blocks.contains(offset) == false) {
                this.blocks.add(offset);
            }
        }

        /*
         * A queue to place blocks, its cool to preserve ghost blocks.
         */
        this.onQueue();
    }

    @Override
    public void onEnable() {
        this.blocks.clear();

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
        this.blocks.clear();

        if (NullUtil.isPlayer()) {
            return;
        }

        mc.player.inventory.currentItem = this.oldSlot;
    }

    public void onQueue() {
        if (this.blocks.isEmpty()) {
            this.tickQueue.reset();
        }

        for (BlockPos pos : new ArrayList<>(this.blocks)) {
            if (this.tickQueue.isPassedMS(settingDelay.getValue().intValue())) {
                Flag flag = this.doPlace(pos);

                this.blocks.remove(pos);
                this.tickQueue.reset();
            }
        }
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
        Vec3d hit = BlockPosUtil.hit(offset, facing);

        if (mc.player.getHeldItemMainhand().getItem() != Item.getItemFromBlock(Blocks.OBSIDIAN)) {
            mc.player.inventory.currentItem = this.slot;
        }

        if (settingSwingAnimation.getValue()) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }

        // Rotate.
        if (settingManualRotation.getValue()) {
            PlayerRotationUtil.manual(hit);
        } else {
            PlayerRotationUtil.packet(hit);
        }

        mc.playerController.processRightClickBlock(mc.player, mc.world, offset, facing, hit, EnumHand.MAIN_HAND);

        return Flag.PLACED;
    }
}