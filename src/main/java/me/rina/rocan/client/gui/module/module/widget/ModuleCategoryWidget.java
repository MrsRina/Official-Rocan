package me.rina.rocan.client.gui.module.module.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
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

        this.rect.setWidth(75);
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontBigWidget, this.rect.getTag()) + 6);

        this.container = new ModuleContainer(this.master, this.frame, this, this.category);
        this.container.init();
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

    public void setMouseClickedLeft(boolean mouseClickedLeft) {
        isMouseClickedLeft = mouseClickedLeft;
    }

    public boolean isMouseClickedLeft() {
        return isMouseClickedLeft;
    }

    @Override
    public void onKeyboard(char character, int key) {
        this.container.onKeyboard(character, key);
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        this.container.onCustomKeyboard(character, key);
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.flagMouse == Flag.MouseOver && this.isMouseClickedLeft) {
            this.frame.resetWidget();

            this.container.init();

            this.isSelected = true;
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        this.container.onCustomMouseReleased(button);
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
        this.container.onCustomMouseClicked(button);
    }

    @Override
    public void onRender() {
        this.rect.setX(this.frame.getRect().getX() + this.offsetX);
        this.rect.setY(this.frame.getRect().getY() + this.offsetY);

        this.rect.setWidth(75);
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontBigWidget, this.rect.getTag()) + 6);

        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

        this.alphaEffect = (int) (this.flagMouse == Flag.MouseOver ? TurokMath.lerp(this.alphaEffect, Rocan.getWrapperGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : TurokMath.lerp(this.alphaEffect, 0, this.master.getPartialTicks()));

        TurokFontManager.render(Rocan.getWrapperGUI().fontBigWidget, this.rect.getTag(), this.rect.getX() + (this.rect.getWidth() / 2 - (TurokFontManager.getStringWidth(Rocan.getWrapperGUI().fontBigWidget, this.rect.getTag()) / 2)), this.rect.getY() + 6, true, new Color(255, 255, 255));

        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetHighlight[0], Rocan.getWrapperGUI().colorWidgetHighlight[1], Rocan.getWrapperGUI().colorWidgetHighlight[2], this.alphaEffect);
        TurokRenderGL.drawOutlineRect(this.rect);

        /*
         * The selected refresh effects,
         * I implemented ifs to compare distance and skip & set,
         * to fix slow linear slow interpolation.
         */
        if (this.isSelected) {
            this.container.getRect().setWidth((int) TurokMath.lerp(this.container.getRect().getWidth(), this.container.getWidthScale(), this.master.getPartialTicks()));

            if (this.container.getRect().getWidth() >= this.container.getWidthScale() - 10) {
                this.container.getRect().setWidth(this.container.getWidthScale());
            }

            this.container.getRect().setHeight((int) TurokMath.lerp(this.container.getRect().getHeight(), this.container.getHeightScale(), this.master.getPartialTicks()));

            if (this.container.getRect().getHeight() >= this.container.getHeightScale() - 10) {
                this.container.getRect().setHeight(this.container.getHeightScale());
            }

            this.frame.getRectWidgetSelected().setX((int) TurokMath.lerp(this.frame.getRectWidgetSelected().getX(), this.rect.getX(), this.master.getPartialTicks()));

            if (this.frame.getRectWidgetSelected().getDistance(this.rect) <= 10) {
                this.frame.getRectWidgetSelected().setX(this.rect.getX());
            }
        } else {
            this.container.getRect().setWidth((int) TurokMath.lerp(this.container.getRect().getWidth(), 0, this.master.getPartialTicks()));
            this.container.getRect().setHeight((int) TurokMath.lerp(this.container.getRect().getHeight(), 0, this.master.getPartialTicks()));
        }

        this.frame.getRectWidgetSelected().setWidth(this.rect.getWidth());

        this.container.onRender();
    }

    @Override
    public void onCustomRender() {
        this.container.onCustomRender();
    }
}
