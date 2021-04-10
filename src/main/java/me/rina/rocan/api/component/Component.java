package me.rina.rocan.api.component;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.component.impl.ComponentType;
import me.rina.rocan.api.component.registry.Registry;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.client.module.client.ModuleHUD;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokGL;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 23/03/2021 at 15:21
 **/
public class Component implements ISLClass {
    private String name = getRegistry().name();
    private String tag = getRegistry().tag();
    private String description = getRegistry().description();

    private ComponentType type = getRegistry().type();
    private ArrayList<Setting> settingList;

    private boolean isEnabled;
    protected TurokRect rect;

    private ValueNumber settingStringAlpha = new ValueNumber("String Alpha", "StringAlpha", "The string alpha value.", 255, 0, 255);
    private ValueBoolean settingCustomFont = new ValueBoolean("Custom Font", "CustomFont", "Smooth font to render.", false);
    private ValueBoolean settingShadowFont = new ValueBoolean("Shadow Font", "ShadowFont", "Shadow font to render.", true);
    private ValueBoolean settingRGB = new ValueBoolean("RGB", "RGB", "RGB effect to render, else false HUD module color.", false);

    private boolean isDragging;

    public Component() {
        this.rect = new TurokRect("unknown", 0, 0);

        if (this.type == ComponentType.TEXT) {
            this.registry(settingStringAlpha);
            this.registry(settingCustomFont);
            this.registry(settingShadowFont);
            this.registry(settingRGB);
        }
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public ComponentType getType() {
        return type;
    }

    public void setSettingList(ArrayList<Setting> settingList) {
        this.settingList = settingList;
    }

    public ArrayList<Setting> getSettingList() {
        return settingList;
    }

    public void setRect(TurokRect rect) {
        this.rect = rect;
    }

    public TurokRect getRect() {
        return rect;
    }

    public Registry getRegistry() {
        Registry details = null;

        if (getClass().isAnnotationPresent(Registry.class)) {
            details = getClass().getAnnotation(Registry.class);
        }

        return details;
    }

    public void registry(Setting setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList<>();
        }

        this.settingList.add(setting);
    }

    public void unregister(Setting setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList<>();
        } else {
            if (this.get(setting.getClass()) != null) {
                this.settingList.remove(setting);
            }
        }
    }

    public Setting get(Class<?> clazz) {
        for (Setting settings : this.settingList) {
            if (settings.getClass() == clazz) {
                return settings;
            }
        }

        return null;
    }

    public Setting get(String tag) {
        for (Setting settings : this.settingList) {
            if (settings.getTag().equalsIgnoreCase(tag)) {
                return settings;
            }
        }

        return null;
    }

    public void render(TurokRect rect, boolean outline, Color color) {
        this.render(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), outline, color);
    }

    public void render(float x, float y, float w, float h, boolean outline, Color color) {
        float processedX = this.rect.getX() + this.calculateDockingX(x, w);
        float processedY = this.rect.getY() + this.calculateDockingY(y, h);

        if (outline) {
            TurokShaderGL.drawOutlineRect(processedX, processedY, w, h, new int[] {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()});
        } else {
            TurokShaderGL.drawSolidRect(processedX, processedY, w, h, new int[] {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()});
        }
    }

    public void render(String string, float x, float y) {
        if (this.type == ComponentType.NO_TEXT) {
            return;
        }

        final float w = this.getStringWidth(string);
        final float h = this.getStringHeight(string);

        final float processedX = this.rect.getX() + this.calculateDockingX(x, w);
        final float processedY = this.rect.getY() + this.calculateDockingY(y, h);

        final boolean flag = this.settingRGB.getValue();
        final Color color = new Color(flag ? Rocan.getClientEventManager().getCurrentRGBColor()[0] : ModuleHUD.settingRed.getValue().intValue(), flag ? Rocan.getClientEventManager().getCurrentRGBColor()[1] : ModuleHUD.settingGreen.getValue().intValue(), flag ? Rocan.getClientEventManager().getCurrentRGBColor()[2] : ModuleHUD.settingBlue.getValue().intValue(), this.settingStringAlpha.getValue().intValue());

        if (this.settingCustomFont.getValue()) {
            TurokFontManager.render(Rocan.getWrapper().fontNormalWidget, string, processedX, processedY, this.settingShadowFont.getValue(), color);
        } else {
            final FontRenderer fontRenderer = Rocan.MC.fontRenderer;

            TurokGL.pushMatrix();
            TurokGL.enable(GL11.GL_TEXTURE_2D);

            TurokGL.enable(GL11.GL_BLEND);
            TurokGL.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            if (this.settingShadowFont.getValue()) {
                fontRenderer.drawStringWithShadow(string, processedX, processedY, color.getRGB());
            } else {
                fontRenderer.drawString(string, (int) processedX, (int) processedY, color.getRGB());
            }

            TurokGL.disable(GL11.GL_TEXTURE_2D);
            TurokGL.popMatrix();
        }
    }

    public int getStringWidth(String string) {
        int width = -1;

        if (this.type == ComponentType.NO_TEXT) {
            return width;
        }

        if (this.settingCustomFont.getValue()) {
            width = TurokFontManager.getStringWidth(Rocan.getWrapper().fontNormalWidget, string);
        } else {
            width = Rocan.MC.fontRenderer.getStringWidth(string);
        }

        return width;
    }

    public int getStringHeight(String string) {
        int height = -1;

        if (this.type == ComponentType.NO_TEXT) {
            return height;
        }

        if (this.settingCustomFont.getValue()) {
            height = TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, string);
        } else {
            height = Rocan.MC.fontRenderer.FONT_HEIGHT;
        }

        return height;
    }

    /**
     * Apply collision for rect docking.
     *
     * @param mc - The Minecraft for factor scale using TurokDisplay.
     */
    public void applyCollision(final Minecraft mc) {
        final TurokDisplay display = new TurokDisplay(mc);

        int diff = 1;
        this.rect.getDockHit(display, 0);

        float dx = this.rect.getX();
        float dy = this.rect.getY();

        float w = this.rect.getWidth();
        float h = this.rect.getHeight();

        if (dx <= diff) dx = diff;
        if (dy <= diff) dy = diff;
        if (dx >= display.getScaledWidth() - w - diff) dx = display.getScaledWidth() - w - diff;
        if (dy >= display.getScaledHeight() - h - diff) dy = display.getScaledHeight() - h - diff;

        this.rect.setX(dx);
        this.rect.setY(dy);
    }

    /**
     * Override renders HUD.
     *
     * @param partialTicks The partial ticks of Minecraft overlay/GUI;
     */
    public void onRenderHUD(float partialTicks) {}

    /**
     * Get correct positions using default position and size for X.
     *
     * @param x X requested.
     * @param w Width Requested.
     * @return Real size.
     */
    public float calculateDockingX(float x, float w) {
        float calculated = x;

        if (this.rect.getDocking() == TurokRect.Dock.TOP_RIGHT || this.rect.getDocking() == TurokRect.Dock.BOTTOM_RIGHT) {
            calculated = this.rect.getWidth() - w - x;
        }

        return calculated;
    }

    /**
     * Get correct positions using default position and size for Y.
     *
     * @param y Y requested.
     * @param h Height Requested.
     * @return Real size.
     */
    public float calculateDockingY(float y, float h) {
        float calculated = y;

        if (this.rect.getDocking() == TurokRect.Dock.BOTTOM_LEFT || this.rect.getDocking() == TurokRect.Dock.BOTTOM_RIGHT) {
            calculated = this.rect.getHeight() - h - y;
        }

        return calculated;
    }

    @Override
    public void onSave() {

    }

    @Override
    public void onLoad() {

    }
}
