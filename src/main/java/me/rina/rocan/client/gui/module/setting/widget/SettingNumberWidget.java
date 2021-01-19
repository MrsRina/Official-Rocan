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
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

import java.awt.*;

/**
 * @author SrRina
 * @since 03/01/2021 at 22:31
 **/
public class SettingNumberWidget extends Widget {
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

    private int alphaEffectHighlightSlider;
    private int alphaEffectHighlightRect;

    /**
     * We use doubles to set the current value, minimum & maximum.
     * We need verifiy and cast to double.
     */
    private double _value, minimum, maximum;

    private boolean isMouseClickedLeft;

    private TurokRect rectSlider = new TurokRect("CheckBox", 0, 0);

    private Setting<Number> setting;

    public Flag flagMouseSlider = Flag.MouseNotOver;
    public Flag flagMouse = Flag.MouseNotOver;

    public SettingNumberWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, Setting<Number> setting) {
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

    public void setRectSlider(TurokRect rectSlider) {
        this.rectSlider = rectSlider;
    }

    public TurokRect getRectSlider() {
        return rectSlider;
    }

    public void setSetting(Setting<Number> setting) {
        this.setting = setting;
    }

    public Setting<Number> getSetting() {
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
        this.isMouseClickedLeft = false;
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouseSlider == Flag.MouseOver && this.settingContainer.flagMouseRealRect == Flag.MouseOver) {
            if (this.isMouseClickedLeft) {
                this.isMouseClickedLeft = false;
            }
        } else {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.flagMouseSlider == Flag.MouseOver && this.settingContainer.flagMouseRealRect == Flag.MouseOver) {
            if (button == 0) {
                this.isMouseClickedLeft = true;
            }
        }
    }

    @Override
    public void onRender() {
        int offsetWidthSliderRect = 2;

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        // The current value of setting, cast to Number and doubleValue();
        this._value = ((Number) this.setting.getValue()).doubleValue();

        // The min and maximum of the setting, here he cast to Number and doubleValue(0;
        this.minimum = ((Number) this.setting.getMinimum()).doubleValue();
        this.maximum = ((Number) this.setting.getMaximum()).doubleValue();

        float clampedSliderRectWidth = this.rect.getWidth() - (offsetWidthSliderRect * 2);

        this.offsetWidth = (float) ((clampedSliderRectWidth) * (this._value - this.minimum) / (this.maximum - this.minimum));

        this.rectSlider.setWidth(clampedSliderRectWidth);
        this.rectSlider.setHeight(6);

        if (this.settingContainer.flagMouseRealRect == Flag.MouseOver) {
            this.flagMouseSlider = this.rectSlider.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
        } else {
            this.flagMouseSlider = Flag.MouseNotOver;
            this.flagMouse = Flag.MouseNotOver;
        }

        // We need set the slider rect on the end of main rect.
        this.rectSlider.setX(this.rect.getX() + offsetWidthSliderRect);
        this.rectSlider.setY(this.rect.getY() + this.rect.getHeight() - (this.rectSlider.getHeight() + 2));

        // Where the smooth animation works.
        this.alphaEffectHighlightSlider = this.flagMouseSlider == Flag.MouseOver ? (int) TurokMath.lerp(this.alphaEffectHighlightSlider, Rocan.getWrapperGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightSlider, 0, this.master.getPartialTicks());
        this.alphaEffectHighlightRect = this.flagMouse == Flag.MouseOver ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapperGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());

        String currentSettingValue = ((Number) this.setting.getValue()).toString();

        TurokFontManager.render(Rocan.getWrapperGUI().fontSmallWidget, this.rect.getTag() + ": " + currentSettingValue, this.rect.getX() + 2, this.rect.getY() + 1, true, new Color(255, 255, 255));

        float checkBoxPressedOffsetX = 0.5f;
        float checkBoxPressedOffsetY = 1f;

        // The solid pressed slider.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetPressed[0], Rocan.getWrapperGUI().colorWidgetPressed[1], Rocan.getWrapperGUI().colorWidgetPressed[2], Rocan.getWrapperGUI().colorWidgetPressed[3]);
        TurokRenderGL.drawSolidRect(this.rectSlider.getX(), this.rectSlider.getY(), this.offsetWidth, this.rectSlider.getHeight());

        // The check slider unpressed.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetPressed[0], Rocan.getWrapperGUI().colorWidgetPressed[1], Rocan.getWrapperGUI().colorWidgetPressed[2], Rocan.getWrapperGUI().colorWidgetPressed[3]);
        TurokRenderGL.drawOutlineRect(this.rectSlider);

        // Outline on rect render.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetHighlight[0], Rocan.getWrapperGUI().colorWidgetHighlight[1], Rocan.getWrapperGUI().colorWidgetHighlight[2], this.alphaEffectHighlightRect);
        TurokRenderGL.drawOutlineRect(this.rect);

        // The slider outline highlight.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetHighlight[0], Rocan.getWrapperGUI().colorWidgetHighlight[1], Rocan.getWrapperGUI().colorWidgetHighlight[2], this.alphaEffectHighlightSlider);
        TurokRenderGL.drawOutlineRect(this.rectSlider.getX(), this.rectSlider.getY(), this.offsetWidth, this.rectSlider.getHeight());

        double mouse = Math.min(this.rectSlider.getWidth(), Math.max(0, this.master.getMouse().getX() - this.rect.getX()));

        if (this.isMouseClickedLeft) {
            if (mouse != 0) {
                if (this.setting.getValue() instanceof Integer) {
                    int roundedValue = (int) TurokMath.round(((mouse / this.rectSlider.getWidth()) * (maximum - minimum) + minimum));

                    this.setting.setValue((Integer) roundedValue);
                } else if (this.setting.getValue() instanceof Double) {
                    double roundedValue = TurokMath.round(((mouse / this.rectSlider.getWidth()) * (maximum - minimum) + minimum));

                    this.setting.setValue((Double) roundedValue);
                } else if (this.setting.getValue() instanceof Float) {
                    float roundedValue = (float) TurokMath.round(((mouse / this.rectSlider.getWidth()) * (maximum - minimum) + minimum));

                    this.setting.setValue((Float) roundedValue);
                }
            } else {
                this.setting.setValue(this.setting.getMinimum());
            }
        }

        if (this.flagMouse == Flag.MouseOver) {
            this.settingContainer.getDescriptionLabel().setText(this.setting.getDescription());

            this.settingContainer.flagDescription = Flag.MouseOver;
        }
    }

    @Override
    public void onCustomRender() {

    }
}