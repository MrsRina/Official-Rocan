package me.rina.rocan.client.module.combat;

import jdk.nashorn.internal.ir.Block;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.tracker.Tracker;
import me.rina.rocan.api.tracker.impl.RightMouseClickOnBlockTracker;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.crystal.BlockUtil;
import me.rina.rocan.api.util.crystal.HoleUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.item.SlotUtil;
import me.rina.rocan.api.util.math.PosUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokTick;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import scala.tools.reflect.quasiquotes.Holes;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 11/02/2021 at 13:17
 **/
@Registry(name = "Surround", tag = "Surround", description = "Automatically places block around you.", category = ModuleCategory.COMBAT)
public class ModuleSurround extends Module {
    public static ValueNumber settingToggleDelay = new ValueNumber("Toggle Delay", "ToggleDelay", "Delay for disable module.", 2000, 0, 3000);
    public static ValueNumber settingPacketDelay = new ValueNumber("Packet Delay", "PacketDelay", "The MS delay for sending packet", 250, 0, 3000);
    public static ValueBoolean settingSwingAnimation = new ValueBoolean("Swing Animation", "SwingAnimation", "Enable swing animation.", true);


    private Tracker tracker = new Tracker("Surround Packet").inject();
    private TurokTick tick = new TurokTick();

    private int slot;
    private int oldSlot;

    @Override
    public void onSetting() {
        this.tracker.setDelay(settingPacketDelay.getValue().intValue());
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

        mc.player.inventory.currentItem = this.slot;

        if (this.tick.isPassedMS(settingToggleDelay.getValue().intValue())) {
            this.setDisabled();

            this.tick.reset();
        }

        BlockPos playerPos = PlayerUtil.getBlockPos();

        int count = 0;

        for (BlockPos offsetPos : HoleUtil.SURROUND) {
            if (count <= HoleUtil.SURROUND.length) {
                this.doPlace(playerPos.add(offsetPos));
            } else {
                this.print("debug - toggled in main surround for.");
                this.setDisabled();
            }

            count++;
        }
    }

    @Override
    public void onEnable() {
        this.tracker.register();

        if (NullUtil.isPlayer()) {
            return;
        }

        Item itemObsidian = Item.getItemFromBlock(Blocks.OBSIDIAN);

        this.oldSlot = mc.player.inventory.currentItem;
        this.slot = this.oldSlot;

        if (SlotUtil.getItemStack(this.oldSlot).getItem() != itemObsidian) {
            this.slot = SlotUtil.findItemSlotFromHotBar(itemObsidian);
        }

        mc.player.inventory.currentItem = this.slot;
    }

    @Override
    public void onDisable() {
        this.tracker.unregister();

        if (NullUtil.isPlayer()) {
            return;
        }

        mc.player.inventory.currentItem = this.oldSlot;
    }

    public void doPlace(BlockPos pos) {
        if (BlockUtil.isAir(pos) == false) {
            return;
        }

        if (mc.player.getHeldItemMainhand().getItem() != Item.getItemFromBlock(Blocks.OBSIDIAN)) {
            mc.player.inventory.currentItem = this.slot;
        }

        /*
         * Verify if is air in all holes at block,
         * but we need remove the player position block
         * to cancel the place.
         */
        boolean isCanceled = false;

        /*
         * So, need count if the offset blocks to verify if not air.
         */
        int countOffset = 0;

        for (BlockPos offsetPos : HoleUtil.SURROUND) {
            BlockPos offsetPositions = pos.add(offsetPos);

            if (BlockUtil.isAir(offsetPositions) == false) {
                countOffset++;
            }

            if (countOffset == 0) {
                isCanceled = true;
            }
        }

        if (isCanceled) {
            return;
        }

        this.tracker.send(new RightMouseClickOnBlockTracker(pos, EnumFacing.UP, settingSwingAnimation.getValue() ? EnumHand.MAIN_HAND : null, 0f, 0f, 0f));
    }
}
