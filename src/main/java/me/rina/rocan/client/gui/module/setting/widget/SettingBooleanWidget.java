package me.rina.rocan.client.gui.module.setting.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;

import java.awt.*;

/**
 * @author SrRina
 * @since 2020-22-16 at 12:44
 **/
public class SettingBooleanWidget extends Widget {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widgetCategory;
    private ModuleContainer moduleContainer;

    private ModuleWidget widgetModule;
    private SettingContainer settingContainer;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    private int alphaEffectPressed;
    private int alphaEffectHighlightCheckbox;
    private int alphaEffectHighlightRect;

    private boolean isMouseClickedLeft;

    private TurokRect rectCheckbox = new TurokRect("CheckBox", 0, 0);

    private Setting setting;

    public Flag flagMouse = Flag.MouseNotOver;

    public SettingBooleanWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, Setting setting) {
        super(setting.getName());

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.moduleContainer = moduleContainer;

        this.widgetModule = widgetModule;
        this.settingContainer = settingContainer;

        this.setting = setting;

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getGUI().fontNormalWidget, this.rect.getTag()) + 5);
    }

    public void setRectCheckbox(TurokRect rectCheckbox) {
        this.rectCheckbox = rectCheckbox;
    }

    public TurokRect getRectCheckbox() {
        return rectCheckbox;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetWidth(int offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public int getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public int getOffsetHeight() {
        return offsetHeight;
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (this.isMouseClickedLeft) {
                this.setting.setValue(!(boolean) this.setting.getValue());

                this.isMouseClickedLeft = false;

                // Refresh for verify if enable or disable, pretty cool!
//                this.settingContainer.onRefreshWidget();
            }
        } else {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            this.isMouseClickedLeft = button == 0;
        }
    }

    @Override
    public void onRender() {
        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getGUI().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        this.rectCheckbox.setWidth(8);
        this.rectCheckbox.setHeight(8);

        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

        // We need set the check box rect on the end of main rect.
        this.rectCheckbox.setX(this.rect.getX() + 2);
        this.rectCheckbox.setY(this.rect.getY() + this.rect.getHeight() - (this.rectCheckbox.getHeight() + 5));

        // Where the smooth animation works.
        this.alphaEffectHighlightCheckbox = this.rectCheckbox.collideWithMouse(this.master.getMouse()) ? (int) TurokMath.linearInterpolation(this.alphaEffectHighlightCheckbox, Rocan.getGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.linearInterpolation(this.alphaEffectHighlightCheckbox, 0, this.master.getPartialTicks());
        this.alphaEffectHighlightRect = this.rect.collideWithMouse(this.master.getMouse()) ? (int) TurokMath.linearInterpolation(this.alphaEffectHighlightRect, Rocan.getGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.linearInterpolation(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());
        this.alphaEffectPressed = (boolean) this.setting.getValue() ? (int) TurokMath.linearInterpolation(this.alphaEffectPressed, Rocan.getGUI().colorWidgetPressed[3], this.master.getPartialTicks()) : (int) TurokMath.linearInterpolation(this.alphaEffectPressed, 0, this.master.getPartialTicks());

        TurokFontManager.render(Rocan.getGUI().fontSmallWidget, this.rect.getTag(), this.rectCheckbox.getX() + this.rectCheckbox.getWidth() + 2, this.rect.getY() + 5, true, new Color(255, 255, 255));

        // Outline on rect render.
        TurokRenderGL.color(Rocan.getGUI().colorWidgetHighlight[0], Rocan.getGUI().colorWidgetHighlight[1], Rocan.getGUI().colorWidgetHighlight[2], this.alphaEffectHighlightRect);
        TurokRenderGL.drawOutlineRect(this.rect);

        // The check box outline highlight.
        TurokRenderGL.color(Rocan.getGUI().colorWidgetHighlight[0], Rocan.getGUI().colorWidgetHighlight[1], Rocan.getGUI().colorWidgetHighlight[2], this.alphaEffectHighlightCheckbox);
        TurokRenderGL.drawOutlineRect(this.rectCheckbox);

        // The check box outline unpressed.
        TurokRenderGL.color(Rocan.getGUI().colorWidgetPressed[0], Rocan.getGUI().colorWidgetPressed[1], Rocan.getGUI().colorWidgetPressed[2], Rocan.getGUI().colorWidgetPressed[3]);
        TurokRenderGL.drawOutlineRect(this.rectCheckbox);

        float checkBoxPressedOffsetX = 0.5f;
        float checkBoxPressedOffsetY = 1f;

        // The solid pressed checkbox.
        TurokRenderGL.color(Rocan.getGUI().colorWidgetPressed[0], Rocan.getGUI().colorWidgetPressed[1], Rocan.getGUI().colorWidgetPressed[2], this.alphaEffectPressed);
        TurokRenderGL.drawSolidRect(this.rectCheckbox.getX() + checkBoxPressedOffsetX, this.rectCheckbox.getY() + checkBoxPressedOffsetY, this.rectCheckbox.getWidth() - (checkBoxPressedOffsetX * 2), this.rectCheckbox.getHeight() - (checkBoxPressedOffsetY * 2));

        if (this.flagMouse == Flag.MouseOver) {
            this.settingContainer.getDescriptionLabel().setText(this.setting.getDescription());

            this.settingContainer.flagDescription = Flag.MouseOver;
        }
    }

    @Override
    public void onCustomRender() {

    }
}
