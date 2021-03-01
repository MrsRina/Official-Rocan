package me.rina.rocan.client.manager.entity;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.manager.Manager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SrRina
 * @since 01/03/2021 at 09:45
 **/
public class EntityWorldManager extends Manager {
    public static final Minecraft mc = Rocan.getMinecraft();

    private HashMap<Integer, Entity> entitySavedList;

    public EntityWorldManager() {
        super("Entity World Manager", "Save or request entity from space abstract client.");

        this.entitySavedList = new HashMap<>();
    }

    public void setEntitySavedList(HashMap<Integer, Entity> entitySavedList) {
        this.entitySavedList = entitySavedList;
    }

    public HashMap<Integer, Entity> getEntitySavedList() {
        return entitySavedList;
    }

    public void saveEntity(int entityId, Entity entity) {
        this.entitySavedList.put(entityId, entity);
    }

    public Entity getEntity(int entityId) {
        return (Entity) this.entitySavedList.get(entityId);
    }

    @Override
    public void onUpdateAll() {
        for (Map.Entry<Integer, Entity> entities : new HashMap<>(this.entitySavedList).entrySet()) {
            int id = entities.getKey();
            Entity entity = entities.getValue();

            boolean isManageable = false;

            if (entity.isDead) {
                isManageable = true;
            }

            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

                if (entityLivingBase.getHealth() < 0f) {
                    isManageable = true;
                }
            }

            if (entity instanceof EntityPlayer && mc.getConnection() != null) {
                EntityPlayer entityPlayer = (EntityPlayer) entities;
                NetworkPlayerInfo playerInfo = mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID());

                if (playerInfo == null) {
                    isManageable = true;
                }
            }

            if (isManageable) {
                this.entitySavedList.remove(id);
            }
        }
    }
}
