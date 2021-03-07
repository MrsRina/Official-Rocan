package me.rina.rocan.client.module.combat;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.crystal.HoleUtil;
import me.rina.rocan.api.util.entity.PlayerPositionUtil;
import me.rina.rocan.api.util.entity.PlayerRotationUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.api.util.math.PositionUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.entity.player.EntityPlayer;
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
    /* Misc utils. */
    public static ValueBoolean settingOnGround = new ValueBoolean("On Ground", "OnGround", "Stay on ground only for places blocks.", true);
    public static ValueBoolean settingManualRotation = new ValueBoolean("Manual Rotation", "ManualRotation", "Player manually rotate.", false);
    public static ValueBoolean settingSmoothRotation = new ValueBoolean("Smooth Rotation", "SmoothRotation", "Smooth camera rotation.", false);
    public static ValueBoolean settingAutoCenter = new ValueBoolean("Auto-Center", "AutoCenter", "Set center position of player for start place blocks.", true);
    public static ValueBoolean settingSmoothCenter = new ValueBoolean("Smooth Center", "SmoothCenter", "Smooth center movement.", false);

    /* Misc. */
    public static ValueNumber settingTimeOut = new ValueNumber("Time Out", "TimeOut", "The time out for cancel everything.", 3000, 0, 3000);
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "MS delay for places blocks.", 250, 0, 500);

    public static ValueEnum settingMode = new ValueEnum("Mode", "Mode", "Modes to place blocks.", Mode.SURROUND);

    private ArrayList<BlockPos> blocks = new ArrayList<>();

    private TurokTick tickQueue = new TurokTick();
    private TurokTick tickTimeOut = new TurokTick();

    private int slot;
    private int oldSlot;
    private int length;

    /**
     * Make player center before start place blocks.
     */
    private boolean isCentered;
    private boolean isEventCentered;

    public enum Mode {
        HOLE, SURROUND;
    }

    public enum Flag {
        PLACED, STUCK, AIR;
    }

    private final Item obsidian = Item.getItemFromBlock(Blocks.OBSIDIAN);

    @Override
    public void onSetting() {
        settingSmoothRotation.setEnabled(settingManualRotation.getValue());
        settingSmoothCenter.setEnabled(settingAutoCenter.getValue());
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (settingOnGround.getValue() && mc.player.onGround == false) {
            this.setDisabled();

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

                countPlaces = 0;
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
        // Clear blocks.
        this.blocks.clear();

        // Reset ticks.
        this.tickTimeOut.reset();
        this.tickQueue.reset();

        // We need set because, anti spam position so the anti cheat don't make player backs to last position.
        this.isEventCentered = true;

        if (NullUtil.isPlayer()) {
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
        this.blocks.clear();

        this.tickTimeOut.reset();
        this.tickQueue.reset();

        this.isEventCentered = true;

        if (NullUtil.isPlayer()) {
            return;
        }

        mc.player.inventory.currentItem = this.oldSlot;
    }

    public void onQueue() {
        /*
         * No spam position, so the player does not back to last position.
         */
        if (this.isEventCentered) {
            this.isCentered = settingAutoCenter.getValue() ? this.doCenterPosition() : true;
            this.isEventCentered = false;
        }

        if (this.isCentered == false) {
            return;
        }

        if (this.blocks.isEmpty()) {
            this.tickQueue.reset();
        } else {
            BlockPos position = this.blocks.get(0);

            if (this.tickQueue.isPassedMS(settingDelay.getValue().intValue())) {
                Flag flag = this.doPlace(position);

                this.blocks.remove(0);
                this.tickQueue.reset();
            }
        }
    }

    /*
     * Uses to update current item to obi at hot bar.
     */
    public void doUpdateCurrentItem() {
        this.slot = SlotUtil.findItemSlotFromHotBar(obsidian);

        if (this.slot == -1) {
            this.setDisabled();
        } else {
            mc.player.inventory.currentItem = this.slot;
        }
    }

    public boolean doCenterPosition() {
        BlockPos pos = PlayerUtil.getBlockPos();
        Vec3d center = PositionUtil.toVec(pos).add(0.5, 0, 0.5);

        if (settingSmoothCenter.getValue()) {
            PlayerPositionUtil.smooth(center, Rocan.getClientEventManager().getCurrentRender2DPartialTicks());
        } else {
            PlayerPositionUtil.teleportation(center);
        }

        return true;
    }

    public Flag doPlace(BlockPos pos) {
        /*
         * First verification, for unnecessary segment.
         */
        this.doUpdateCurrentItem();

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
        Vec3d hit = PositionUtil.hit(offset, facing);

        /*
         * Last update in hot bar, for not hack slot kick.
         */
        this.doUpdateCurrentItem();

        // Send swing anim to server.
        mc.player.swingArm(EnumHand.MAIN_HAND);

        // Rotate.
        if (settingManualRotation.getValue()) {
            if (settingSmoothRotation.getValue()) {
                PlayerRotationUtil.manual(hit, Rocan.getClientEventManager().getCurrentRender2DPartialTicks());
            } else {
                PlayerRotationUtil.manual(hit);
            }
        } else {
            PlayerRotationUtil.packet(hit);
        }

        if (mc.player.getHeldItemMainhand().getItem() == obsidian) {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, hit, EnumHand.MAIN_HAND);
        }

        return Flag.PLACED;
    }
}