package me.rina.rocan.client.gui.module.module.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.visual.EntryWidget;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author SrRina
 * @since 22/03/2021 at 21:38
 **/
public class ModuleSearchWidget extends Widget {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widgetCategory;
    private ModuleWidget widgetModule;

    private ModuleContainer moduleContainer;
    private EntryWidget entry;

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    private int alphaEffectHighlightRect;

    private boolean isMouseClickedLeft;

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public ModuleSearchWidget(ModuleClickGUI master, MotherFrame frame, ModuleContainer moduleContainer) {
        super("");

        this.master = master;
        this.frame = frame;

        this.entry = new EntryWidget(this.master,"Entry Field", Rocan.getWrapper().fontNormalWidget);

        this.moduleContainer = moduleContainer;
        this.widgetCategory = moduleContainer.getWidget();

        this.rect.setWidth(this.moduleContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);
    }

    public void setWidgetModule(ModuleWidget widgetModule) {
        this.widgetModule = widgetModule;
    }

    public ModuleCategoryWidget getWidgetCategory() {
        return widgetCategory;
    }

    public void setWidgetCategory(ModuleCategoryWidget widgetCategory) {
        this.widgetCategory = widgetCategory;
    }

    public ModuleWidget getWidgetModule() {
        return widgetModule;
    }

    public void setEntry(EntryWidget entry) {
        this.entry = entry;
    }

    public EntryWidget getEntry() {
        return entry;
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
        this.entry.onKeyboard(character, key);
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        this.entry.onCustomKeyboard(character, key);

        switch (key) {
            // Cancel typing.
            case Keyboard.KEY_ESCAPE: {
                this.entry.setText("");

                if (this.master.isCanceledCloseGUI()) {
                    this.master.setCanceledCloseGUI(false);
                }

                break;
            }
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.entry.flagMouse == Flag.MOUSE_OVER) {
            this.entry.onMouseReleased(button);

            if (this.isMouseClickedLeft) {
                this.isMouseClickedLeft = false;
            }
        } else {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (this.entry.flagMouse == Flag.MOUSE_NOT_OVER && this.entry.isFocused()) {
            this.entry.setFocused(false);

            if (this.master.isCanceledCloseGUI()) {
                this.master.setCanceledCloseGUI(false);
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.entry.flagMouse == Flag.MOUSE_OVER) {
            this.isMouseClickedLeft = button == 0;
            this.entry.onMouseClicked(button);

            if (this.entry.isFocused() == false) {
                this.entry.setFocused(true);
            }
        }
    }

    @Override
    public void onRender() {
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);

        this.entry.getRect().setX(this.rect.getX() + 1f);
        this.entry.getRect().setY(this.rect.getY() + 1f);

        this.entry.getRect().setWidth(this.rect.getWidth() - 2f);
        this.entry.getRect().setHeight(this.rect.getHeight() - 2f);

        this.alphaEffectHighlightRect = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());

        this.entry.setScissored(true);
        this.entry.setRendering(this.widgetCategory.isSelected());

        this.entry.rectScissor = new float[] {
                this.entry.getRect().getX(), this.entry.getRect().getY(), this.entry.getRect().getWidth(), this.entry.getRect().getHeight()
        };

        if (this.moduleContainer.flagMouse == Flag.MOUSE_OVER) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;

            // Update entry context.
            this.entry.doMouseOver(this.master.getMouse());
            this.entry.doMouseScroll(this.master.getMouse());
        } else {
            this.flagMouse = Flag.MOUSE_NOT_OVER;
            this.entry.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        // The outline rect effect.
        TurokShaderGL.drawOutlineRect(this.rect, Rocan.getWrapper().colorWidgetHighlight);

        if (this.entry.isFocused()) {
            this.master.setCanceledCloseGUI(true);

            // The outline rect effect.
            TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], Rocan.getWrapper().colorWidgetHighlight[3]});
        } else {
            // The outline rect effect.
            TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightRect});
        }

        this.entry.onRender();

        if (this.entry.getText().isEmpty()) {
            TurokFontManager.render(Rocan.getWrapper().fontNormalWidget, "Search...", this.entry.getRect().getX() + 2f, this.entry.getRect().getY() + 4f, true, new Color(255, 255, 255,100));
        }
    }

    @Override
    public void onCustomRender() {
        this.entry.onCustomRender();
    }
}
