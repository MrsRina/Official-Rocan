package me.rina.rocan.client.gui.module.module.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokTick;

import java.awt.*;

/**
 * @author SrRina
 * @since 2020-12-08 at 23:14
 **/
public class ModuleWidget extends Widget {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widget;
    private ModuleContainer container;

    private Module module;

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    private int alphaEffectHighlight;
    private int alphaEffectSelected;
    private int alphaEffectPressed;

    private TurokTick doubleClickTick = new TurokTick();

    /*
     * We load here the setting container, but the author said about
     * this specify comment.
     *
     * Author:
     * "I don't use one class to all events of the GUI and renders,
     * this make the code not professional.".
     */
    private SettingContainer settingContainer;

    private boolean isMouseClickedLeft;
    private boolean isSelected;
    private boolean isLocked;

    public Flag flagMouse = Flag.MouseNotOver;

    public ModuleWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widget, ModuleContainer container, Module module) {
        super(module.getName());

        this.master = master;
        this.frame = frame;

        this.widget = widget;
        this.container = container;

        this.module = module;

        this.settingContainer = new SettingContainer(this.master, this.frame, this.widget, this.container, this);

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);
    }

    protected void setSettingContainer(SettingContainer settingContainer) {
        this.settingContainer = settingContainer;
    }

    public SettingContainer getSettingContainer() {
        return settingContainer;
    }

    protected void setModule(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isLocked() {
        return isLocked;
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
        this.settingContainer.onClose();
    }

    @Override
    public void onOpen() {
        this.settingContainer.onOpen();
    }

    @Override
    public void onKeyboard(char character, int key) {
        this.settingContainer.onKeyboard(character, key);
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        this.settingContainer.onCustomKeyboard(character, key);
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.isMouseClickedLeft) {
            if (this.flagMouse == Flag.MouseOver) {
                this.module.toggle();
            }

            this.isMouseClickedLeft = false;
        }

        this.settingContainer.onCustomMouseReleased(button);
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.settingContainer.flagMouse == Flag.MouseNotOver && this.flagMouse == Flag.MouseNotOver && this.frame.flagMouseResize == Flag.MouseNotOver) {
            this.container.resetWidget();
        }

        if (this.flagMouse == Flag.MouseOver) {
            if (button == 0) {
                // The double click button up to enable module.
                if (!this.doubleClickTick.isPassedMS(500)) {
                    this.isMouseClickedLeft = true;
                } else {
                    this.doubleClickTick.reset();
                }

                // Reset the main container of modules using this class.
                this.container.resetWidget(this.getClass());

                // Lock the container setting to render.
                this.isLocked = true;
            }

            // Here works as main button but do not enable module.
            if (button == 1) {
                this.container.resetWidget(this.getClass());

                this.isLocked = true;
            }
        }

        this.settingContainer.onCustomMouseClicked(button);
    }

    @Override
    public void onRender() {
        this.flagMouse = Flag.MouseNotOver;

        this.settingContainer.onRender();
    }

    @Override
    public void onCustomRender() {
        this.rect.setX(this.container.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.container.getScrollRect().getY() + this.offsetY);

        // Automatically set cool off
        this.offsetX = 2;

        if (this.container.flagMouse == Flag.MouseOver) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
        } else {
            this.flagMouse = Flag.MouseNotOver;
        }

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);

        this.alphaEffectHighlight = (int) (this.flagMouse == Flag.MouseOver ? TurokMath.lerp(this.alphaEffectHighlight, Rocan.getWrapperGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : TurokMath.lerp(this.alphaEffectHighlight, 0, this.master.getPartialTicks()));

        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetHighlight[0], Rocan.getWrapperGUI().colorWidgetHighlight[1], Rocan.getWrapperGUI().colorWidgetHighlight[2], this.alphaEffectHighlight);
        TurokRenderGL.drawOutlineRect(this.rect);

        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetPressed[0], Rocan.getWrapperGUI().colorWidgetPressed[1], Rocan.getWrapperGUI().colorWidgetPressed[2], this.alphaEffectPressed);
        TurokRenderGL.drawSolidRect(this.rect);

        TurokFontManager.render(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 5, true, new Color(255, 255, 255));

        if (this.module.isEnabled()) {
            this.alphaEffectPressed = (int) TurokMath.lerp(this.alphaEffectPressed, Rocan.getWrapperGUI().colorWidgetPressed[3], this.master.getPartialTicks());
        } else {
            this.alphaEffectPressed = (int) TurokMath.lerp(this.alphaEffectPressed, 0, this.master.getPartialTicks());
        }

        if (this.isLocked) { // I need verify if is locked to set selected, actually this works great with animation.
            this.isSelected = true;
        } else {
            // The fun animation is here, so set the selected when mouse over with flag.
            // OBS: this make mixed the settings sometimes but is pretty cool!
            this.isSelected = this.flagMouse == Flag.MouseOver;
        }

        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetSelected[0], Rocan.getWrapperGUI().colorWidgetSelected[1], Rocan.getWrapperGUI().colorWidgetSelected[2], this.alphaEffectSelected);
        TurokRenderGL.drawOutlineRect(this.rect);

        // If this is selected the current setting, we use lerp to set the sizes and if minimum 10 set the fully scaled width&height.
        if (this.isSelected) {
            this.alphaEffectSelected = (int) TurokMath.lerp(this.alphaEffectSelected, Rocan.getWrapperGUI().colorWidgetSelected[3], this.master.getPartialTicks());

            this.settingContainer.getRect().setWidth((int) TurokMath.lerp(this.settingContainer.getRect().getWidth(), this.settingContainer.getWidthScale(), this.master.getPartialTicks()));

            if (this.settingContainer.getRect().getWidth() >= this.settingContainer.getWidthScale() - 10) {
                this.settingContainer.getRect().setWidth(this.settingContainer.getWidthScale());
            }

            this.settingContainer.getRect().setHeight(TurokMath.lerp(this.settingContainer.getRect().getHeight(), this.container.getRect().getHeight(), this.master.getPartialTicks()));

            if (this.settingContainer.getRect().getHeight() >= this.container.getHeightScale() - 10) {
                this.settingContainer.getRect().setHeight(this.container.getHeightScale());
            }
        } else {
            this.alphaEffectSelected = (int) TurokMath.lerp(this.alphaEffectSelected, 0, this.master.getPartialTicks());

            this.settingContainer.getRect().setWidth((int) TurokMath.lerp(this.settingContainer.getRect().getWidth(), 0, this.master.getPartialTicks()));
            this.settingContainer.getRect().setHeight((int) TurokMath.lerp(this.settingContainer.getRect().getHeight(), 0, this.master.getPartialTicks()));
        }

        this.settingContainer.onCustomRender();
    }
}