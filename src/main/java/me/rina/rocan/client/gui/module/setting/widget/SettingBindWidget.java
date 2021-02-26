package me.rina.rocan.client.gui.module.setting.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.value.ValueBind;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.turok.hardware.keyboard.TurokKeyboard;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author SrRina
 * @since 01/02/2021 at 12:57
 **/
public class SettingBindWidget extends Widget {
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
    private boolean isListening;

    private TurokRect rectCheckbox = new TurokRect("CheckBox", 0, 0);

    private ValueBind setting;

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public SettingBindWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, ValueBind setting) {
        super(setting.getName());

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.moduleContainer = moduleContainer;

        this.widgetModule = widgetModule;
        this.settingContainer = settingContainer;

        this.setting = setting;

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);
    }

    public void setRectCheckbox(TurokRect rectCheckbox) {
        this.rectCheckbox = rectCheckbox;
    }

    public TurokRect getRectCheckbox() {
        return rectCheckbox;
    }

    public void setSetting(ValueBind setting) {
        this.setting = setting;
    }

    public ValueBind getSetting() {
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
    public void onKeyboard(char character, int key) {
        if (this.isListening) {
            if (key == Keyboard.KEY_ESCAPE) {
                this.isListening = false;
            }
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        if (this.isListening) {
            switch (key) {
                // For we cancel the binding.
                case Keyboard.KEY_ESCAPE: {
                    this.isListening = false;

                    if (this.master.isCanceledCloseGUI()) {
                        this.master.setCanceledCloseGUI(false);
                    }

                    break;
                }

                // For delete we need set the keyCode for -1.
                case Keyboard.KEY_DELETE: {
                    this.setting.setKeyCode(-1);

                    this.isListening = false;

                    if (this.master.isCanceledCloseGUI()) {
                        this.master.setCanceledCloseGUI(false);
                    }

                    break;
                }

                default: {
                    this.setting.setKeyCode(key);

                    this.isListening = false;

                    if (this.master.isCanceledCloseGUI()) {
                        this.master.setCanceledCloseGUI(false);
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouse == Flag.MOUSE_OVER) {
            if (this.isMouseClickedLeft) {
                this.isListening = true;
                this.isMouseClickedLeft = false;
            }
        } else {
            if (this.isListening) {
                if (this.master.isCanceledCloseGUI()) {
                    this.master.setCanceledCloseGUI(false);
                }


                this.isListening = false;
            }

            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flagMouse == Flag.MOUSE_NOT_OVER && this.isListening) {
            if (this.master.isCanceledCloseGUI()) {
                this.master.setCanceledCloseGUI(false);
            }

            this.isListening = false;
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.flagMouse == Flag.MOUSE_OVER) {
            this.isMouseClickedLeft = button == 0;

            if (button == 1) {
                this.setting.setState(!this.setting.getState());
                this.widgetModule.getModule().onReload();
            }
        } else {
            if (this.isListening) {
                this.isListening = false;

                if (this.master.isCanceledCloseGUI()) {
                    this.master.setCanceledCloseGUI(false);
                }
            }
        }
    }

    @Override
    public void onRender() {
        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        this.rectCheckbox.setWidth(6);
        this.rectCheckbox.setHeight(6);

        if (this.settingContainer.flagMouseRealRect == Flag.MOUSE_OVER) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        // We need set the check box rect on the end of main rect.
        this.rectCheckbox.setX(this.rect.getX() + (this.rect.getWidth() - this.rectCheckbox.getWidth() - 4));
        this.rectCheckbox.setY(this.rect.getY() + ((this.rect.getHeight() / 2) - (this.rectCheckbox.getHeight() / 2)));

        // Where the smooth animation works.
        this.alphaEffectHighlightCheckbox = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightCheckbox, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightCheckbox, 0, this.master.getPartialTicks());
        this.alphaEffectHighlightRect = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());
        this.alphaEffectPressed = this.setting.getState() ? (int) TurokMath.lerp(this.alphaEffectPressed, Rocan.getWrapper().colorWidgetPressed[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectPressed, 0, this.master.getPartialTicks());

        String currentName = this.rect.getTag() + " | " + (this.isListening ? "Listening" : TurokKeyboard.toString(this.setting.getKeyCode()));

        TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, currentName, this.rect.getX() + 2, this.rectCheckbox.getY() + ((this.rectCheckbox.getHeight() / 2) - (TurokFontManager.getStringHeight(Rocan.getWrapper().fontSmallWidget, this.rect.getTag()) / 2)), true, new Color(255, 255, 255));

        // Outline on rect render.
        TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightRect});

        // The check box outline highlight.
        TurokShaderGL.drawOutlineRect(this.rectCheckbox, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightCheckbox});

        // The check box outline unpressed.
        TurokShaderGL.drawOutlineRect(this.rectCheckbox, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], Rocan.getWrapper().colorWidgetPressed[3]});

        float checkBoxPressedOffsetX = 0.5f;
        float checkBoxPressedOffsetY = 1f;

        // The solid pressed checkbox.
        TurokShaderGL.drawSolidRect(this.rectCheckbox, new int[] {Rocan.getWrapper().colorWidgetPressed[0], Rocan.getWrapper().colorWidgetPressed[1], Rocan.getWrapper().colorWidgetPressed[2], this.alphaEffectPressed});

        if (this.isListening) {
            this.master.setCanceledCloseGUI(true);
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