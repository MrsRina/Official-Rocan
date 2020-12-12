package me.rina.rocan.client.gui.module.module.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokMath;

import java.awt.*;

public class ModuleCategoryWidget extends Widget {
    private ModuleClickGUI master;
    private ModuleCategory category;

    private MotherFrame frame;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    private ModuleContainer container;

    private boolean isMouseClickedLeft;
    private boolean isSelected;

    private int alphaEffect;

    public Flag flagMouse = Flag.MouseNotOver;

    public ModuleCategoryWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategory category) {
        super(category.name());

        this.master = master;
        this.frame = frame;

        this.category = category;

        this.rect.setWidth(100);
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag()) + 6);

        this.container = new ModuleContainer(this.master, this.frame, this, this.category);
        this.container.init();
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

    public void setMouseClickedLeft(boolean mouseClickedLeft) {
        isMouseClickedLeft = mouseClickedLeft;
    }

    public boolean isMouseClickedLeft() {
        return isMouseClickedLeft;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.flagMouse == Flag.MouseOver && this.isMouseClickedLeft) {
            this.frame.resetModuleCategoryWidget();
            this.frame.setWidgetSelected(this);

            this.container.init();

            this.isSelected = true;
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (button == 0) {
                this.isMouseClickedLeft = true;
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
    }

    @Override
    public void onRender() {
        this.rect.setX(this.frame.getRect().getX() + this.offsetX);
        this.rect.setY(this.frame.getRect().getY() + this.offsetY);

        this.rect.setWidth(100);
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag()) + 6);

        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

        this.alphaEffect = this.flagMouse == Flag.MouseOver ? (int) TurokMath.linearInterpolation(this.alphaEffect, 100, this.master.getDisplay().getPartialTicks()) : (int) TurokMath.linearInterpolation(this.alphaEffect, 0, this.master.getDisplay().getPartialTicks());

        TurokFontManager.render(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag(), this.rect.getX() + (this.rect.getWidth() / 2 - (TurokFontManager.getStringWidth(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag()) / 2)), this.rect.getY() + 6, true, new Color(255, 255, 255));

        if (this.isSelected) {
            this.container.getRect().setWidth((int) TurokMath.linearInterpolation(this.container.getRect().getWidth(), this.container.getWidthScale(), this.master.getPartialTicks()));
            this.container.getRect().setHeight((int) TurokMath.linearInterpolation(this.container.getRect().getHeight(), this.container.getHeightScale(), this.master.getPartialTicks()));

            this.frame.getRectWidgetSelected().setX((int) TurokMath.linearInterpolation(this.frame.getRectWidgetSelected().getX(), this.rect.getX(), this.master.getDisplay().getPartialTicks()));
        } else {
            this.container.getRect().setWidth((int) TurokMath.linearInterpolation(this.container.getRect().getWidth(), 0, this.master.getPartialTicks()));
            this.container.getRect().setHeight((int) TurokMath.linearInterpolation(this.container.getRect().getHeight(), 0, this.master.getPartialTicks()));
        }

        this.frame.getRectWidgetSelected().setWidth(this.rect.getWidth());

        this.container.onCustomRender();
    }

    @Override
    public void onCustomRender() {
        this.container.onRender();
    }
}
