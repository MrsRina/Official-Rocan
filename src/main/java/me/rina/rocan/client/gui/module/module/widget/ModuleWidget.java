package me.rina.rocan.client.gui.module.module.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.util.chat.ChatUtil;
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

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    private int alphaEffect;

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

    public Flag flagMouse = Flag.MouseNotOver;

    public ModuleWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widget, ModuleContainer container, Module module) {
        super(module.getName());

        this.master = master;
        this.frame = frame;

        this.widget = widget;
        this.container = container;

        this.module = module;

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleWidget, this.rect.getTag()) + 5);

        if (this.settingContainer == null) {
            this.settingContainer = new SettingContainer(this.master, this.frame, this.widget, this.container, this);
        }
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
        if (this.settingContainer.flagMouse == Flag.MouseNotOver) {
            this.container.resetWidget();
        }

        if (this.flagMouse == Flag.MouseOver) {
            if (button == 0) {
                if (!this.doubleClickTick.isPassedMS(500)) {
                    this.isMouseClickedLeft = true;
                } else {
                    this.doubleClickTick.reset();
                }

                this.container.resetWidget();

                this.isSelected = true;
            }

            if (button == 1) {
                this.container.resetWidget();

                this.isSelected = true;
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

        if (this.container.flagMouse == Flag.MouseOver) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
        } else {
            this.flagMouse = Flag.MouseNotOver;
        }

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleWidget, this.rect.getTag()) + 5);

        this.alphaEffect = (int) (this.flagMouse == Flag.MouseOver ? TurokMath.linearInterpolation(this.alphaEffect, Rocan.getGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : TurokMath.linearInterpolation(this.alphaEffect, 0, this.master.getPartialTicks()));

        TurokRenderGL.color(Rocan.getGUI().colorWidgetHighlight[0], Rocan.getGUI().colorWidgetHighlight[1], Rocan.getGUI().colorWidgetHighlight[2], this.alphaEffect);
        TurokRenderGL.drawOutlineRect(this.rect);

        TurokFontManager.render(Rocan.getGUI().fontModuleWidget, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 5, true, new Color(255, 255, 255));

        if (this.isSelected) {
            TurokRenderGL.color(Rocan.getGUI().colorWidgetSelected[0], Rocan.getGUI().colorWidgetSelected[1], Rocan.getGUI().colorWidgetSelected[2], Rocan.getGUI().colorWidgetSelected[3]);
            TurokRenderGL.drawOutlineRect(this.rect);

            this.settingContainer.getRect().setWidth((int) TurokMath.linearInterpolation(this.settingContainer.getRect().getWidth(), this.settingContainer.getWidthScale(), this.master.getPartialTicks()));

            if (this.settingContainer.getRect().getWidth() >= this.settingContainer.getWidthScale() - 10) {
                this.settingContainer.getRect().setWidth(this.settingContainer.getWidthScale());
            }

            this.settingContainer.getRect().setHeight((int) TurokMath.linearInterpolation(this.settingContainer.getRect().getHeight(), this.container.getRect().getHeight(), this.master.getPartialTicks()));

            if (this.settingContainer.getRect().getHeight() >= this.container.getHeightScale() - 10) {
                this.settingContainer.getRect().setHeight(this.container.getHeightScale());
            }
        } else {
            this.settingContainer.getRect().setWidth((int) TurokMath.linearInterpolation(this.settingContainer.getRect().getWidth(), 0, this.master.getPartialTicks()));
            this.settingContainer.getRect().setHeight((int) TurokMath.linearInterpolation(this.settingContainer.getRect().getHeight(), 0, this.master.getPartialTicks()));
        }

        this.settingContainer.onCustomRender();
    }
}