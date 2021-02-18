package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 17/02/2021 at 13:30
 **/
@Registry(name = "Kill Aura", tag = "KillAura", description = "Make you hit any entity close of you.", category = ModuleCategory.COMBAT)
public class ModuleKillAura extends Module {
    public static ValueNumber settingRange = new ValueNumber("Range", "Range", "Range for target.", 5f, 1f, 5f);
    public static ValueBoolean settingPlayer = new ValueBoolean("Player", "Player", "Hit entity players.", true);
    public static ValueBoolean settingMob = new ValueBoolean("Mob", "Mob", "Hit entity mobs.", true);
    public static ValueBoolean settingAnimal = new ValueBoolean("Animal", "Animal", "Hit entity animal.", true);
    public static ValueBoolean settingOffhandItem = new ValueBoolean("Offhand Item", "OffhandItem", "Enable use item while aura is hitting.", true);

    private EntityLivingBase target;

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        for (Entity entities : mc.world.loadedEntityList) {
            if ((entities instanceof EntityLivingBase) == false) {
                continue;
            }

            EntityLivingBase entityLivingBase = (EntityLivingBase) entities;

            if (doVerify(entityLivingBase) == false) {
                continue;
            }

            this.target = entityLivingBase;

            if (entityLivingBase.getDistance(mc.player) >= settingRange.getValue().floatValue()) {
                this.target = null;

                continue;
            }

            if (entityLivingBase.isDead) {
                this.target = null;

                continue;
            }

            if (mc.player.getCooledAttackStrength(0) >= 1) {
                ItemStack currentOffhandItem = mc.player.getHeldItemOffhand();

                if (currentOffhandItem.getItem() != Items.AIR && currentOffhandItem.getItem() == Items.SHIELD && settingOffhandItem.getValue()) {
                    PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                }

                mc.playerController.attackEntity(mc.player, entityLivingBase);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }

        if (this.target != null) {
            this.setStatus(target.getName());
        }
    }

    public boolean doVerify(EntityLivingBase entityLivingBase) {
        boolean isVerified = false;

        if (entityLivingBase instanceof EntityPlayer && settingPlayer.getValue()) {
            isVerified = true;
        }

        if (entityLivingBase instanceof EntityMob && settingMob.getValue()) {
            isVerified = true;
        }

        if (entityLivingBase instanceof EntityAnimal && settingAnimal.getValue()) {
            isVerified = true;
        }

        if (entityLivingBase.isDead) {
            isVerified = false;
        }

        return isVerified;
    }
}
