package me.rina.rocan.client.gui.module.setting.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

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

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    private int alphaEffectPressed;
    private int alphaEffectHighlightCheckbox;
    private int alphaEffectHighlightRect;

    private boolean isMouseClickedLeft;

    private TurokRect rectCheckbox = new TurokRect("CheckBox", 0, 0);

    private ValueBoolean setting;

    public Flag flagMouse = Flag.MouseNotOver;

    public SettingBooleanWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, ValueBoolean setting) {
        super(setting.getName());

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.moduleContainer = moduleContainer;

        this.widgetModule = widgetModule;
        this.settingContainer = settingContainer;

        this.setting = setting;

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);
    }

    public void setRectCheckbox(TurokRect rectCheckbox) {
        this.rectCheckbox = rectCheckbox;
    }

    public TurokRect getRectCheckbox() {
        return rectCheckbox;
    }

    public void setSetting(ValueBoolean setting) {
        this.setting = setting;
    }

    public ValueBoolean getSetting() {
        return setting;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetWidth(float offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public float getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetHeight(float offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public float getOffsetHeight() {
        return offsetHeight;
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (this.isMouseClickedLeft) {
                this.setting.setValue(!(boolean) this.setting.getValue());

                this.isMouseClickedLeft = false;
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
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        this.rectCheckbox.setWidth(10);
        this.rectCheckbox.setHeight(10);

        if (this.settingContainer.flagMouseRealRect == Flag.MouseOver) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
        } else {
            this.flagMouse = Flag.MouseNotOver;
        }

        // We need set the check box rect on the end of main rect.
        this.rectCheckbox.setX(this.rect.getX() + 2);
        this.rectCheckbox.setY(this.rect.getY() + this.rect.getHeight() - (this.rectCheckbox.getHeight() + 5));

        // Where the smooth animation works.
        this.alphaEffectHighlightCheckbox = this.flagMouse == Flag.MouseOver ? (int) TurokMath.lerp(this.alphaEffectHighlightCheckbox, Rocan.getWrapperGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightCheckbox, 0, this.master.getPartialTicks());
        this.alphaEffectHighlightRect = this.flagMouse == Flag.MouseOver ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapperGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());
        this.alphaEffectPressed = (boolean) this.setting.getValue() ? (int) TurokMath.lerp(this.alphaEffectPressed, Rocan.getWrapperGUI().colorWidgetPressed[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectPressed, 0, this.master.getPartialTicks());

        TurokFontManager.render(Rocan.getWrapperGUI().fontSmallWidget, this.rect.getTag(), this.rectCheckbox.getX() + this.rectCheckbox.getWidth() + 2, this.rect.getY() + 5, true, new Color(255, 255, 255));

        // Outline on rect render.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetHighlight[0], Rocan.getWrapperGUI().colorWidgetHighlight[1], Rocan.getWrapperGUI().colorWidgetHighlight[2], this.alphaEffectHighlightRect);
        TurokRenderGL.drawOutlineRect(this.rect);

        // The check box outline highlight.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetHighlight[0], Rocan.getWrapperGUI().colorWidgetHighlight[1], Rocan.getWrapperGUI().colorWidgetHighlight[2], this.alphaEffectHighlightCheckbox);
        TurokRenderGL.drawOutlineRect(this.rectCheckbox);

        // The check box outline unpressed.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetPressed[0], Rocan.getWrapperGUI().colorWidgetPressed[1], Rocan.getWrapperGUI().colorWidgetPressed[2], Rocan.getWrapperGUI().colorWidgetPressed[3]);
        TurokRenderGL.drawOutlineRect(this.rectCheckbox);

        float checkBoxPressedOffsetX = 0.5f;
        float checkBoxPressedOffsetY = 1f;

        // The solid pressed checkbox.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetPressed[0], Rocan.getWrapperGUI().colorWidgetPressed[1], Rocan.getWrapperGUI().colorWidgetPressed[2], this.alphaEffectPressed);
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
