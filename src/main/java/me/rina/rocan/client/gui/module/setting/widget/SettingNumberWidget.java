package me.rina.rocan.client.gui.module.setting.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.render.opengl.TurokShaderGL;
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

    private ValueNumber setting;

    public Flag flagMouseSlider = Flag.MOUSE_NOT_OVER;
    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public SettingNumberWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, ValueNumber setting) {
        super(setting.getName());

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.moduleContainer = moduleContainer;

        this.widgetModule = widgetModule;
        this.settingContainer = settingContainer;

        this.setting = setting;

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(8 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 8);
    }

    public void setRectSlider(TurokRect rectSlider) {
        this.rectSlider = rectSlider;
    }

    public TurokRect getRectSlider() {
        return rectSlider;
    }

    public void setSetting(ValueNumber setting) {
        this.setting = setting;
    }

    public ValueNumber getSetting() {
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
    public void onKeyboard(char character, int key) {
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouseSlider == Flag.MOUSE_OVER && this.settingContainer.flagMouseRealRect == Flag.MOUSE_OVER) {
            if (this.isMouseClickedLeft) {
                this.isMouseClickedLeft = false;
            }
        } else {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.flagMouseSlider == Flag.MOUSE_OVER && this.settingContainer.flagMouseRealRect == Flag.MOUSE_OVER) {
            if (button == 0) {
                this.isMouseClickedLeft = true;
            }
        }
    }

    @Override
    public void onRender() {
        int offsetWidthSliderRect = 2;

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(8 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 8);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        // The current value of setting, cast to Number and doubleValue();
        this._value = this.setting.getValue().doubleValue();

        // The min and maximum of the setting, here he cast to Number and doubleValue(0;
        this.minimum = this.setting.getMinimum().doubleValue();
        this.maximum = this.setting.getMaximum().doubleValue();

        float clampedSliderRectWidth = this.rect.getWidth() - (offsetWidthSliderRect * 2) - 2;

        this.offsetWidth = (float) ((clampedSliderRectWidth) * (this._value - this.minimum) / (this.maximum - this.minimum));

        this.rectSlider.setWidth(clampedSliderRectWidth);
        this.rectSlider.setHeight(6);

        if (this.settingContainer.flagMouseRealRect == Flag.MOUSE_OVER) {
            this.flagMouseSlider = this.rectSlider.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouseSlider = Flag.MOUSE_NOT_OVER;
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        String currentSettingValue = this.setting.getValue().toString();

        float fullHeight = TurokFontManager.getStringHeight(Rocan.getWrapper().fontSmallWidget, this.rect.getTag() + ": " + currentSettingValue) + 1 + this.rectSlider.getHeight();

        // We need set the slider rect on the end of main rect.
        this.rectSlider.setX(this.rect.getX() + offsetWidthSliderRect);
        this.rectSlider.setY(this.rect.getY() + ((this.rect.getHeight() / 2) + 1.5f));

        // Where the smooth animation works.
        this.alphaEffectHighlightSlider = this.flagMouseSlider == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightSlider, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightSlider, 0, this.master.getPartialTicks());
        this.alphaEffectHighlightRect = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());

        TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, this.rect.getTag() + ": " + currentSettingValue, this.rect.getX() + 2, this.rectSlider.getY() - (TurokFontManager.getStringHeight(Rocan.getWrapper().fontSmallWidget, this.rect.getTag()) + 4), true, new Color(255, 255, 255));

        float checkBoxPressedOffsetX = 0.5f;
        float checkBoxPressedOffsetY = 1f;

        // The solid pressed slider.
        TurokShaderGL.drawSolidRect(this.rectSlider.getX(), this.rectSlider.getY(), this.offsetWidth, this.rectSlider.getHeight(), new int[] {Rocan.getWrapper().colorWidgetPressed[0], Rocan.getWrapper().colorWidgetPressed[1], Rocan.getWrapper().colorWidgetPressed[2], Rocan.getWrapper().colorWidgetPressed[3]});

        // The check slider unpressed.
        TurokShaderGL.drawOutlineRect(this.rectSlider, new int[] {Rocan.getWrapper().colorWidgetPressed[0], Rocan.getWrapper().colorWidgetPressed[1], Rocan.getWrapper().colorWidgetPressed[2], Rocan.getWrapper().colorWidgetPressed[3]});

        // Outline on rect render.
        TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightRect});

        // The slider outline highlight.
        TurokShaderGL.drawOutlineRect(this.rectSlider.getX(), this.rectSlider.getY(), this.offsetWidth, this.rectSlider.getHeight(), new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightSlider});

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

        if (this.flagMouse == Flag.MOUSE_OVER) {
            this.settingContainer.getDescriptionLabel().setText(this.setting.getDescription());

            this.settingContainer.flagDescription = Flag.MOUSE_OVER;
        }
    }

    @Override
    public void onCustomRender() {

    }
}