package me.rina.rocan.client.gui.component.setting;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.client.gui.component.ComponentClickGUI;
import me.rina.rocan.client.gui.component.component.frame.ComponentPopupFrame;
import me.rina.rocan.client.gui.component.component.frame.ComponentPopupListFrame;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;

import java.awt.*;

/**
 * @author SrRina
 * @since 03/04/2021 at 18:49
 **/
public class SettingBooleanWidget extends Widget {
    private ComponentClickGUI master;
    private ComponentPopupFrame popup;

    private float offsetX;
    private float offsetY;

    private ValueBoolean setting;

    private int alphaEffectPressed;
    private int alphaEffectHighlight;

    private boolean isMouseClickedPressed;

    /* Flags. */
    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public SettingBooleanWidget(ComponentClickGUI master, ComponentPopupFrame popup, ValueBoolean setting) {
        super(setting.getName());

        this.master = master;
        this.popup = popup;

        this.setting = setting;

        this.rect.setHeight(3 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 3);
    }

    public ComponentClickGUI getMaster() {
        return master;
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

    @Override
    public void onClose() {
    }

    @Override
    public void onCustomClose() {

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onCustomOpen() {

    }

    @Override
    public void onKeyboard(char character, int key) {

    }

    @Override
    public void onCustomKeyboard(char character, int key) {

    }

    @Override
    public void onMouseReleased(int button) {
        if (!this.popup.isOpened()) {
            return;
        }

        if (this.isMouseClickedPressed) {
            this.isMouseClickedPressed = false;
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
    }

    @Override
    public void onMouseClicked(int button) {
        if (!this.popup.isOpened()) {
            return;
        }

        if (this.flagMouse == Flag.MOUSE_OVER) {
            if (button == 0 || button == 1 || button == 2) {
                this.setting.setValue(!this.setting.getValue());
                this.isMouseClickedPressed = true;
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        this.rect.setX(this.popup.getRect().getX() + this.offsetX);
        this.rect.setY(this.popup.getRect().getY() + this.offsetY);

        this.rect.setWidth(this.popup.getRect().getWidth() - (this.offsetX * 2));

        if (this.popup.isOpened() && this.popup.flagMouse == Flag.MOUSE_OVER) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        this.alphaEffectPressed = (int) TurokMath.lerp(this.alphaEffectPressed, this.setting.getValue() ? Rocan.getWrapper().colorWidgetPressed[3] : 0, this.master.getPartialTicks());
        this.alphaEffectHighlight = (int) TurokMath.lerp(this.alphaEffectHighlight, this.flagMouse == Flag.MOUSE_OVER ? Rocan.getWrapper().colorWidgetHighlight[3] : 0, this.master.getPartialTicks());

        // Draw background.
        TurokShaderGL.drawSolidRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetPressed[0], Rocan.getWrapper().colorWidgetPressed[1], Rocan.getWrapper().colorWidgetPressed[2], this.alphaEffectPressed});

        // Render string.
        TurokFontManager.render(Rocan.getWrapper().fontNormalWidget, this.setting.getName(), this.rect.getX() + 1, this.rect.getY() + 3, false, new Color(255, 255, 255));

        // Draw outline effect.
        TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlight});
    }

    @Override
    public void onCustomRender() {
        if (!this.popup.isOpened()) {
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }
    }
}