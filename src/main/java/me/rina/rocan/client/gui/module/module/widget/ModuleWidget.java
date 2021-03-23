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
import me.rina.turok.render.opengl.TurokShaderGL;
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

    private float animationY;
    private float animationX;

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

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public ModuleWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widget, ModuleContainer container, Module module) {
        super(module.getName());

        this.master = master;
        this.frame = frame;

        this.widget = widget;
        this.container = container;

        this.module = module;

        this.settingContainer = new SettingContainer(this.master, this.frame, this.widget, this.container, this);

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);
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

    public void setAnimationX(float animationX) {
        this.animationX = animationX;
    }

    public float getAnimationX() {
        return animationX;
    }

    public void setAnimationY(float animationY) {
        this.animationY = animationY;
    }

    public float getAnimationY() {
        return animationY;
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
    public void onMouseReleased(int button) {
        this.settingContainer.onMouseReleased(button);
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.isMouseClickedLeft) {
            if (this.flagMouse == Flag.MOUSE_OVER) {
                this.module.toggle();
            }

            this.isMouseClickedLeft = false;
        }

        this.settingContainer.onCustomMouseReleased(button);
    }

    @Override
    public void onMouseClicked(int button) {
        this.settingContainer.onMouseClicked(button);
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.settingContainer.flagMouse == Flag.MOUSE_NOT_OVER && this.flagMouse == Flag.MOUSE_NOT_OVER && this.frame.flagMouseResize == Flag.MOUSE_NOT_OVER && this.container.isModuleOpen() && this.isLocked && this.frame.getClientContainer().flagMouseModule == Flag.MOUSE_NOT_OVER) {
            this.container.resetWidget();
        }

        if (this.flagMouse == Flag.MOUSE_OVER) {
            if (button == 0) {
                // The double click button up to enable module.
                if (!this.doubleClickTick.isPassedMS(500)) {
                    this.isMouseClickedLeft = true;
                } else {
                    this.doubleClickTick.reset();
                }

                // Reset the main container of modules using the class.
                this.container.resetWidget(this.module.getName());

                // Lock the container setting to render.
                this.isLocked = true;
            }

            // Here works as main button but do not enable module.
            if (button == 1) {
                this.container.resetWidget(this.module.getName());

                this.isLocked = true;
            }
        }

        this.settingContainer.onCustomMouseClicked(button);
    }

    @Override
    public void onRender() {
        this.flagMouse = Flag.MOUSE_NOT_OVER;

        this.settingContainer.onRender();
    }

    @Override
    public void onCustomRender() {
        this.rect.setX(this.container.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.container.getScrollRect().getY() + this.offsetY);

        // The animation.
        this.offsetY = TurokMath.lerp(this.offsetY, this.animationY, this.master.getPartialTicks());

        if (this.container.flagMouse == Flag.MOUSE_OVER && this.container.getSearchWidget().flagMouse == Flag.MOUSE_NOT_OVER) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        this.rect.setWidth(this.container.getRect().getWidth() - 4);
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);

        this.alphaEffectHighlight = (int) (this.flagMouse == Flag.MOUSE_OVER ? TurokMath.lerp(this.alphaEffectHighlight, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : TurokMath.lerp(this.alphaEffectHighlight, 0, this.master.getPartialTicks()));

        this.settingContainer.onCustomRender();

        // Outline mouse effect.
        TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlight});

        // Background.
        TurokShaderGL.drawSolidRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetPressed[0], Rocan.getWrapper().colorWidgetPressed[1], Rocan.getWrapper().colorWidgetPressed[2], this.alphaEffectPressed});

        // String.
        TurokFontManager.render(Rocan.getWrapper().fontNormalWidget, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 5, true, new Color(255, 255, 255));

        if (this.module.isEnabled()) {
            this.alphaEffectPressed = (int) TurokMath.lerp(this.alphaEffectPressed, Rocan.getWrapper().colorWidgetPressed[3], this.master.getPartialTicks());
        } else {
            this.alphaEffectPressed = (int) TurokMath.lerp(this.alphaEffectPressed, 0, this.master.getPartialTicks());
        }

        if (this.isLocked) { // I need verify if is locked to set selected, actually this works great with animation.
            this.isSelected = true;

            // We need set as open to client container do not glitch.
            this.container.setModuleOpen(true);

            // Update color only when is locked.
            this.alphaEffectSelected = (int) TurokMath.lerp(this.alphaEffectSelected, Rocan.getWrapper().colorWidgetSelected[3], this.master.getPartialTicks());
        } else {
            // The fun animation is here, so set the selected when mouse over with flag.
            // OBS: this make mixed the settings sometimes but is pretty cool!
            if (this.flagMouse == Flag.MOUSE_OVER && this.container.isModuleOpen() == false) {
                // We need get one current list when mouse is over, to make readable if one module is open.
                // So we can set the isModuleOpen in container, to do not glitch with container client.
                this.container.resetWidget(false);

                // Set over.
                this.frame.getClientContainer().flagMouseModule = Flag.MOUSE_OVER;

                this.isSelected = true;
            } else {
                this.isSelected = false;
            }
        }

        // Selected.
        TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetSelected[0], Rocan.getWrapper().colorWidgetSelected[1], Rocan.getWrapper().colorWidgetSelected[2], this.alphaEffectSelected});

        // If this is selected the current setting, we use lerp to set the sizes and if minimum 10 set the fully scaled width&height.
        if (this.isSelected) {
            this.settingContainer.getRect().setWidth(TurokMath.lerp(this.settingContainer.getRect().getWidth(), this.settingContainer.getWidthScale(), this.master.getPartialTicks()));

            if (this.settingContainer.getRect().getWidth() >= this.settingContainer.getWidthScale() - 10) {
                this.settingContainer.getRect().setWidth(this.settingContainer.getWidthScale());
            }

            this.settingContainer.getRect().setHeight(TurokMath.lerp(this.settingContainer.getRect().getHeight(), this.container.getRect().getHeight(), this.master.getPartialTicks()));

            if (this.settingContainer.getRect().getHeight() >= this.container.getHeightScale() - 10) {
                this.settingContainer.getRect().setHeight(this.container.getHeightScale());
            }
        } else {
            this.alphaEffectSelected = (int) TurokMath.lerp(this.alphaEffectSelected, 0, this.master.getPartialTicks());

            this.settingContainer.getRect().setWidth(TurokMath.lerp(this.settingContainer.getRect().getWidth(), 0, this.master.getPartialTicks()));
            this.settingContainer.getRect().setHeight(TurokMath.lerp(this.settingContainer.getRect().getHeight(), 0, this.master.getPartialTicks()));
        }
    }
}