package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.social.Social;
import me.rina.rocan.api.social.management.SocialManager;
import me.rina.rocan.api.social.type.SocialType;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import javax.swing.plaf.IconUIResource;

/**
 * @author SrRina
 * @since 17/02/2021 at 13:30
 **/
@Registry(name = "Kill Aura", tag = "KillAura", description = "Make you hit any entity close of you.", category = ModuleCategory.COMBAT)
public class ModuleKillAura extends Module {
    public static ValueBoolean settingStatus = new ValueBoolean("Status", "Status", "Show target at status module.", false);
    public static ValueBoolean settingOnlySword = new ValueBoolean("Only Sword", "OnlySword", "Only sword to hit.", false);
    public static ValueBoolean settingFriendHit = new ValueBoolean("Friend Hit", "FriendHit", "Hit social friend.", true);

    public static ValueBoolean settingPlayer = new ValueBoolean("Player", "Player", "Hit entity players.", true);
    public static ValueBoolean settingMob = new ValueBoolean("Mob", "Mob", "Hit entity mobs.", true);
    public static ValueBoolean settingAnimal = new ValueBoolean("Animal", "Animal", "Hit entity animal.", true);
    public static ValueBoolean settingVehicles = new ValueBoolean("Vehicle", "Vehicle", "Hit entity vehicles.", true);
    public static ValueBoolean settingProjectiles = new ValueBoolean("Projectile", "Projectile", "Hit entity projectiles.", true);
    public static ValueBoolean settingOffhandItem = new ValueBoolean("Offhand Item", "OffhandItem", "Enable use item while aura is hitting.", true);

    public static ValueNumber settingRange = new ValueNumber("Range", "Range", "Range for target.", 4f, 1f, 5f);
    public static ValueEnum settingTargetHitMode = new ValueEnum("Target Hit Mode", "TargetHitMode", "Target type to accept hit.", TargetHitMode.UNKNOWN);
    public static ValueEnum settingTargetMode = new ValueEnum("Target Mode", "TargetMode", "Modes for get target.", TargetMode.CLOSEST);

    public enum TargetHitMode {
        ENEMY, UNKNOWN;
    }

    public enum TargetMode {
        CLOSEST, NORMAL;
    }

    private Entity target;

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        this.target = this.doFind();

        if (this.target != null) {
            // Only sword.
            boolean flag = settingOnlySword.getValue() ? mc.player.getHeldItemMainhand().getItem() instanceof ItemSword : true;

            if (this.target instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) this.target;

                if (entityLivingBase.isDead || entityLivingBase.getHealth() < 0) {
                    this.target = null;
                }
            }

            this.setStatus(settingStatus.getValue() ? this.target.getName() : "");

            if (mc.player.getCooledAttackStrength(0) >= 1 && flag) {
                final ItemStack offhand = mc.player.getHeldItemOffhand();

                if (offhand != null && offhand.getItem() == Items.SHIELD && settingOffhandItem.getValue()) {
                    PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                }

                mc.playerController.attackEntity(mc.player, this.target);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    public boolean doVerify(Entity entity) {
        boolean isVerified = false;

        if (entity instanceof EntityPlayer && entity != mc.player && settingPlayer.getValue()) {
            isVerified = true;
        }

        if (entity instanceof IMob && settingMob.getValue()) {
            isVerified = true;
        }

        if (entity instanceof IAnimals && (entity instanceof IMob) == false && settingAnimal.getValue()) {
            isVerified = true;
        }

        if ((entity instanceof EntityBoat || entity instanceof EntityMinecart || entity instanceof EntityMinecartContainer) && settingVehicles.getValue()) {
            isVerified = true;
        }

        if ((entity instanceof EntityShulkerBullet || entity instanceof EntityFireball) && settingProjectiles.getValue()) {
            isVerified = true;
        }

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

            if (entityLivingBase.isDead || entityLivingBase.getHealth() < 0) {
                isVerified = false;
            }
        }

        return isVerified;
    }

    public Entity doFind() {
        Entity entity = null;

        float targetRange = settingRange.getValue().floatValue();

        for (Entity entities : mc.world.loadedEntityList) {
            if (entities == null) {
                continue;
            }

            if (doVerify(entities) == false) {
                continue;
            }

            float currentRange = mc.player.getDistance(entities);

            if (currentRange <= targetRange) {
                targetRange = settingTargetMode.getValue() == TargetMode.CLOSEST ? currentRange : settingRange.getValue().floatValue();

                if (entities instanceof EntityPlayer) {
                    EntityPlayer players = (EntityPlayer) entities;

                    if (this.doAccept(players)) {
                        entity = entities;
                    }
                } else {
                    entity = entities;
                }
            }
        }

        return entity;
    }

    public boolean doAccept(EntityPlayer player) {
        boolean isAccepted = false;

        Social social = SocialManager.get(player.getName());

        if (social != null && social.getType() == SocialType.FRIEND && settingFriendHit.getValue()) {
            isAccepted = true;
        }

        if (social != null && social.getType() == SocialType.ENEMY && settingTargetHitMode.getValue() == TargetHitMode.ENEMY) {
            isAccepted = true;
        }

        if (settingTargetHitMode.getValue() == TargetHitMode.UNKNOWN) {
            isAccepted = true;
        }

        return isAccepted;
    }
}
