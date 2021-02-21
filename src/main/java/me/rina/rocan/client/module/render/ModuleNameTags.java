package me.rina.rocan.client.module.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.social.Social;
import me.rina.rocan.api.social.management.SocialManager;
import me.rina.rocan.api.social.type.SocialType;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokGL;
import me.rina.turok.util.TurokMath;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 07/02/2021 at 15:47
 **/
@Registry(name = "Name Tags", tag = "NameTags", description = "Better name tag player.", category = ModuleCategory.RENDER)
public class ModuleNameTags extends Module {
    public static ValueBoolean settingName = new ValueBoolean("Name", "Name", "Draws name.", true);
    public static ValueBoolean settingFriend = new ValueBoolean("Friend", "Friend", "Take HUD color to render friends.", true);
    public static ValueBoolean settingEnemy = new ValueBoolean("Enemy", "Enemy", "Focus enemy only.", false);

    public static ValueBoolean settingShadow = new ValueBoolean("Shadow", "Shadow", "String shadow.", true);
    public static ValueBoolean settingCustomFont = new ValueBoolean("Custom Font", "CustomFont", "Set custom font to render.", true);

    public static ValueBoolean settingSmartScale = new ValueBoolean("Smart Scale", "SmartScale", "Automatically scale if you are close of player.", true);
    public static ValueNumber settingScale = new ValueNumber("Scale", "Scale", "The scale of render.", 25, 1, 100);

    public static ValueNumber settingOffsetY = new ValueNumber("Offset Y", "OffsetY", "Offset y to render.", 10, 0, 100);
    public static ValueNumber settingRange = new ValueNumber("Range", "Range", "Distance to capture players.", 200, 0, 500);

    @Override
    public void onSetting() {
        Rocan.getWrapper().fontNameTags.setRenderingCustomFont(settingCustomFont.getValue());
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

            if (entities.getHealth() < 0 || entities.isDead) {
                continue;
            }

            if (mc.player.getDistance(entities) >= settingRange.getValue().intValue()) {
                continue;
            }

            if (this.doAccept(entities) == false) {
                continue;
            }

            this.doDraw(entities, partialTicks);
        }
    }

    public void doDraw(EntityPlayer entity, float partialTicks) {
        if (mc.getRenderManager().options == null) {
            return;
        }

        Vec3d vecLastTickPosition = new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
        Vec3d vecPosition = new Vec3d(entity.posX, entity.posY, entity.posZ);

        // Interpolated position to render.
        Vec3d vec = TurokMath.lerp(vecLastTickPosition, vecPosition, partialTicks);

        float playerViewX = mc.getRenderManager().playerViewX;
        float playerViewY = mc.getRenderManager().playerViewX;

        boolean flag = mc.getRenderManager().options.thirdPersonView == 2;

        double height = (entity.height + (settingOffsetY.getValue().intValue() / 100d) - (entity.isSneaking() ? 0.25f : 0f));

        double x = vec.x;
        double y = vec.y + height;
        double z = vec.z;

        /*
         * Current scale of player.
         */
        float scale = this.getScale(entity);

        TurokGL.pushMatrix();
        TurokGL.translate(x, y, z);

        // Rotate for name tag rotate with player.
        TurokGL.rotate((flag ? -1f : 1f) * playerViewX, 1f, 0f, 0f);
        TurokGL.rotate(-playerViewY, 0f, 1f, 0f);

        // Scale.
        TurokGL.scale(scale, scale, scale);
        TurokGL.scale(-0.25f, -0.25f, 0.25f);

        // Prepare.
        TurokGL.disable(GL11.GL_LIGHTING);
        TurokGL.disable(GL11.GL_DEPTH_TEST);

        TurokGL.depthMask(false);
        TurokGL.disable(GL11.GL_TEXTURE_2D);

        // Draw.
        String tag = (settingName.getValue() ? entity.getName() : "");
        Color color = getColor(entity);

        // Render string.
        TurokFontManager.render(Rocan.getWrapper().fontNameTags, tag, 0, 0, settingShadow.getValue(), color);

        // Release.
        TurokGL.translate(0, 40, 0);
        TurokGL.color(0, 0, 0);

        TurokGL.enable(GL11.GL_TEXTURE_2D);
        TurokGL.enable(GL11.GL_LIGHTING);
        TurokGL.enable(GL11.GL_DEPTH_TEST);

        TurokGL.depthMask(true);
        TurokGL.color(1, 1, 1);

        TurokGL.translate(-40, -40, -40);

        TurokGL.popMatrix();
    }

    public boolean doAccept(EntityPlayer entity) {
        boolean isAccepted = false;

        Social social = SocialManager.get(entity.getName());

        if (social != null) {
            if (social.getType() == SocialType.ENEMY && settingEnemy.getValue()) {
                isAccepted = true;
            }
        }

        if (settingEnemy.getValue() == false) {
            isAccepted = true;
        }

        return isAccepted;
    }

    public float getScale(EntityPlayer entity) {
        float distance = mc.player.getDistance(entity);
        float scaling = (float) ((distance / 8f) * Math.pow(1.2589254f, settingScale.getValue().intValue() / 100f));

        boolean flag = distance <= 4.0f;

        float scale = scaling;

        if (settingSmartScale.getValue()) {
            scale = flag ? settingScale.getValue().intValue() / 100f : scaling;
        }

        return scale;
    }

    public Color getColor(EntityPlayer entity) {
        Social social = SocialManager.get(entity.getName());

        if (social != null) {
            if (social.getType() == SocialType.FRIEND) {
                return new Color(0, 190, 190);
            }
        }

        return new Color(190, 190, 190);
    }
}