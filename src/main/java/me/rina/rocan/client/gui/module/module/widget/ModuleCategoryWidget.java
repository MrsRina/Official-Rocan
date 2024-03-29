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
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;

import java.awt.*;

public class ModuleCategoryWidget extends Widget {
    private ModuleClickGUI master;
    private ModuleCategory category;

    private MotherFrame frame;

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    private ModuleContainer container;

    private boolean isMouseClickedLeft;
    private boolean isSelected;

    private int alphaEffect;
    private int alphaEffectSelected;

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public ModuleCategoryWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategory category) {
        super(category.name().substring(0, 1).toUpperCase() + category.name().substring(1));

        this.master = master;
        this.frame = frame;

        this.category = category;

        this.rect.setWidth(75);
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontBigWidget, this.rect.getTag()) + 6);

        this.container = new ModuleContainer(this.master, this.frame, this, this.category);
        this.container.init();
    }

    public void setContainer(ModuleContainer container) {
        this.container = container;
    }

    public ModuleContainer getContainer() {
        return container;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
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

    public void setMouseClickedLeft(boolean mouseClickedLeft) {
        isMouseClickedLeft = mouseClickedLeft;
    }

    public boolean isMouseClickedLeft() {
        return isMouseClickedLeft;
    }

    @Override
    public void onClose() {
        this.container.onClose();
    }

    @Override
    public void onOpen() {
        this.container.onOpen();
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
        if (this.flagMouse == Flag.MOUSE_OVER && this.isMouseClickedLeft) {
            this.isMouseClickedLeft = false;
        }

        this.container.onMouseReleased(button);
    }

    @Override
    public void onCustomMouseReleased(int button) {
        this.container.onCustomMouseReleased(button);
    }

    @Override
    public void onMouseClicked(int button) {
        if ((this.flagMouse == Flag.MOUSE_OVER && button == 0) || (this.frame.flagMouse == Flag.MOUSE_OVER && button == 2)) {
            this.frame.setDragX(this.master.getMouse().getX() - this.frame.getRect().getX());
            this.frame.setDragY(this.master.getMouse().getY() - this.frame.getRect().getY());

            this.frame.setDragging(true);
        }

        if (this.flagMouse == Flag.MOUSE_OVER) {
            if (button == 0 && this.isSelected == false) {
                this.frame.resetWidget();
                this.frame.getClientContainer().setModuleContainer(this.container);

                this.container.refreshWidget();

                this.isSelected = true;
                this.isMouseClickedLeft = true;
            }
        }

        this.container.onMouseClicked(button);
    }

    @Override
    public void onCustomMouseClicked(int button) {
        this.container.onCustomMouseClicked(button);
    }

    @Override
    public void onRender() {
        this.rect.setX(this.frame.getRect().getX() + this.offsetX);
        this.rect.setY(this.frame.getRect().getY() + this.offsetY);

        this.container.getRect().setWidth(this.container.getWidthScale());
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontBigWidget, this.rect.getTag()) + 6);

        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        this.alphaEffect = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffect, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffect, 0, this.master.getPartialTicks());

        TurokFontManager.render(Rocan.getWrapper().fontBigWidget, this.rect.getTag(), this.rect.getX() + (this.rect.getWidth() / 2 - (TurokFontManager.getStringWidth(Rocan.getWrapper().fontBigWidget, this.rect.getTag()) / 2)), this.rect.getY() + 6, true, new Color(255, 255, 255));

        // Outline effect.
        TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffect});

        // Selected category effect.
        TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetSelected[0], Rocan.getWrapper().colorWidgetSelected[1], Rocan.getWrapper().colorWidgetSelected[2], this.alphaEffectSelected});

        /*
         * The selected refresh effects,
         * I implemented ifs to compare distance and skip & set,
         * to fix slow linear slow interpolation.
         */
        if (this.isSelected) {
            this.alphaEffectSelected = (int) TurokMath.lerp(this.alphaEffectSelected, Rocan.getWrapper().colorWidgetSelected[3], this.master.getPartialTicks());

            this.container.getRect().setHeight(this.container.getHeightScale());
            this.container.onRender();
        } else {
            this.alphaEffectSelected = (int) TurokMath.lerp(this.alphaEffectSelected, 0, this.master.getPartialTicks());

            this.container.getRect().setHeight(0);
        }
    }

    @Override
    public void onCustomRender() {
        this.container.onCustomRender();
    }
}
