package me.rina.rocan.client.module.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.event.impl.EventStage;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.social.Social;
import me.rina.rocan.api.social.management.SocialManager;
import me.rina.rocan.api.social.type.SocialType;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.network.ServerUtil;
import me.rina.rocan.client.event.render.RenderNameEvent;
import me.rina.rocan.client.manager.network.PlayerServerManager;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokGL;
import me.rina.turok.util.TurokMath;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 07/02/2021 at 15:47
 **/
@Registry(name = "Name Tags", tag = "NameTags", description = "Better name tag.", category = ModuleCategory.RENDER)
public class ModuleNameTags extends Module {
    /* Player stuff. */
    public static ValueBoolean settingFriend = new ValueBoolean("Friend", "Friend", "Allows render friends name tag.", true);
    public static ValueBoolean settingEnemy = new ValueBoolean("Enemy", "Enemy", "Allows render enemies name tag.", false);
    public static ValueBoolean settingPing = new ValueBoolean("Ping", "Ping", "Show ping player.", false);
    public static ValueBoolean settingName = new ValueBoolean("Name", "Name", "Draws name.", true);
    public static ValueBoolean settingMainHand = new ValueBoolean("Main Hand", "MainHand", "Render item main hand.", true);
    public static ValueBoolean settingOffhand = new ValueBoolean("Offhand", "Offhand", "Render item offhand.", true);

    /* Fonts setting. */
    public static ValueBoolean settingShadow = new ValueBoolean("Shadow", "Shadow", "String shadow.", true);
    public static ValueBoolean settingCustomFont = new ValueBoolean("Custom Font", "CustomFont", "Set custom font to render.", true);

    /* Misc settings. */
    public static ValueBoolean settingSmartScale = new ValueBoolean("Smart Scale", "SmartScale", "Automatically scale if you are close of entity.", true);
    public static ValueNumber settingScale = new ValueNumber("Scale", "Scale", "The scale of render.", 25, 1, 1000);
    public static ValueNumber settingOffsetY = new ValueNumber("Offset Y", "OffsetY", "Offset y to render.", 10, 0, 100);
    public static ValueNumber settingRange = new ValueNumber("Range", "Range", "Distance to capture players.", 200, 0, 200);

    private float scaled;

    private ArrayList<EntityPlayer> entityToDraw;

    @Override
    public void onSetting() {
        Rocan.getWrapper().fontNameTags.setRenderingCustomFont(settingCustomFont.getValue());
    }

    @Listener
    public void onListenRenderNameEvent(RenderNameEvent event) {
        if (event.getStage() == EventStage.PRE) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onRender3D() {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        float partialTicks = Rocan.getClientEventManager().getCurrentRender3DPartialTicks();

        for (EntityPlayer entities : mc.world.playerEntities) {
            if (entities == null) {
                continue;
            }

            if (entities.isDead || entities.getHealth() < 0) {
                continue;
            }

            if (mc.player.getDistance(entities) >= settingRange.getValue().intValue()) {
                continue;
            }

            if (this.doAccept(entities) == false) {
                continue;
            }

            double x = TurokMath.lerp(entities.lastTickPosX, entities.posX, partialTicks);
            double y = TurokMath.lerp(entities.lastTickPosY, entities.posY, partialTicks);
            double z = TurokMath.lerp(entities.lastTickPosZ, entities.posZ, partialTicks);

            /*
             * Draw the name tags with interpolation position.
             */
            this.doNameTags(entities, x, y, z, partialTicks);
        }
    }

    public void doNameTags(EntityPlayer entity, double x, double y, double z, float partialTicks) {
        if (mc.getRenderManager().options == null) {
            return;
        }

        float playerViewX = mc.getRenderManager().playerViewX;
        float playerViewY = mc.getRenderManager().playerViewY;

        boolean flag = mc.getRenderManager().options.thirdPersonView == 2;

        double height = (entity.height + (settingOffsetY.getValue().intValue() / 100d) - (entity.isSneaking() ? 0.25f : 0f));

        double referencedX = x - mc.getRenderManager().renderPosX;
        double referencedY = (y + height) - mc.getRenderManager().renderPosY;
        double referencedZ = z - mc.getRenderManager().renderPosZ;

        /*
         * Current scale of entity.
         */
        this.doScale(entity);

        RenderHelper.enableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.translate(referencedX, referencedY, referencedZ);

        // Rotate for name tag.
        GlStateManager.rotate(-playerViewY, 0f, 1f, 0f);
        GlStateManager.rotate((flag ? -1f : 1f) * playerViewX, 1f, 0f, 0f);

        // Scale.
        GlStateManager.scale(this.scaled, this.scaled, this.scaled);
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        GlStateManager.depthMask(false);

        // Draw.
        GlStateManager.enableTexture2D();

        String tag = this.getTag(entity);
        Color color = this.getColor(entity);

        int width = TurokFontManager.getStringWidth(Rocan.getWrapper().fontNameTags, tag) / 2;

        TurokFontManager.render(Rocan.getWrapper().fontNameTags, tag, -width, 0, settingShadow.getValue(), color);

        int positionItems = 0;

        if (entity.getHeldItemOffhand() != null && settingOffhand.getValue()) {
            positionItems -= 8;
        }

        for (int i = 0; i < 4; i++) {
            ItemStack item = mc.player.inventory.armorItemInSlot(i);

            if (item != null) {
                positionItems += 8;
            }
        }

        if (entity.getHeldItemMainhand() != null && settingMainHand.getValue()) {
            positionItems += 8;
        }

        GlStateManager.disableTexture2D();

        // Release.
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();
    }

    public void doScale(EntityLivingBase entity) {
        float distance = mc.player.getDistance(entity);
        float scaling = (float) ((distance / 8f) * Math.pow(1.2589254f, settingScale.getValue().intValue() / 100f));

        boolean flag = distance <= 4.0f;
        
        if (flag && settingSmartScale.getValue()) {
            this.scaled = settingScale.getValue().intValue() / 100f;
        } else {
            this.scaled = scaling;
        }
    }

    public boolean doAccept(EntityPlayer entity) {
        boolean isAccepted = false;

        Social social = SocialManager.get(entity.getName());

        if (social != null) {
            if (social.getType() == SocialType.FRIEND && settingFriend.getValue()) {
                isAccepted = true;
            }

            if (social.getType() == SocialType.ENEMY && settingEnemy.getValue()) {
                isAccepted = true;
            }
        } else {
            isAccepted = true;
        }

        return isAccepted;
    }

    public String getTag(EntityPlayer entity) {
        String name = settingName.getValue() ? entity.getName() : "";
        String ping = "";

        if (settingPing.getValue() && mc.getConnection() != null) {
            final NetworkPlayerInfo playerInfo = mc.getConnection().getPlayerInfo(entity.getUniqueID());

            if (playerInfo != null) {
                ping = (ServerUtil.getPing(playerInfo) >= 140 ? ChatFormatting.RED + " " + ServerUtil.getPing(playerInfo) : ChatFormatting.GREEN + " " + ServerUtil.getPing(playerInfo));
            }
        } else {
            ping = "";
        }

        return name + ping;
    }

    public Color getColor(EntityLivingBase entity) {
        EntityPlayer entityPlayer = (EntityPlayer) entity;

        Social social = SocialManager.get(entity.getName());

        if (social != null) {
            if (social.getType() == SocialType.FRIEND) {
                return new Color(0, 190, 190);
            }
        }

        return new Color(190, 190, 190);
    }
}