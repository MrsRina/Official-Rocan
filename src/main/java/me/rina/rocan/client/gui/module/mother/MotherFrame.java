package me.rina.rocan.client.gui.module.mother;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

import java.util.ArrayList;

import me.rina.turok.util.TurokRect.Dock;

/**
 * @author SrRina
 * @since 07/12/20 at 11:54am
 */
public class MotherFrame extends Frame {
    private ModuleClickGUI master;

    /*
     * Scale is the real factor of scale x, y, width & height.
     * We need factor the all values to fix the screen when is opening;
     */
    protected float scale = 2;

    private float saveX;
    private float saveY;

    protected float scaleWidth;
    protected float scaleHeight;

    private float currentScaleWidth;

    private float dragX;
    private float dragY;

    private float resizeX;
    private float resizeY;

    private float size;

    private ArrayList<Widget> loadedWidgetList;

    private float widgetHeight;

    private TurokRect rectWidgetSelected = new TurokRect("Canada", 0, 0);
    private TurokRect rectResize = new TurokRect("Goo", 0, 0);

    private boolean isStarted = true;
    private boolean isMouseClickedMiddle = false;
    private boolean isMouseClickedLeft = false;

    public Flag flagMouse = Flag.MouseNotOver;
    public Flag flagMouseResize = Flag.MouseNotOver;

    public MotherFrame(ModuleClickGUI master) {
        super("Mother");

        this.master = master;

        this.init();
    }

    public void init() {
        this.loadedWidgetList = new ArrayList<>();

        for (ModuleCategory category : ModuleCategory.values()) {
            ModuleCategoryWidget moduleCategoryWidget = new ModuleCategoryWidget(this.master, this, category);

            moduleCategoryWidget.setOffsetX(this.scaleWidth);

            this.loadedWidgetList.add(moduleCategoryWidget);

            this.widgetHeight = moduleCategoryWidget.getRect().getHeight();
            this.scaleWidth += moduleCategoryWidget.getRect().getWidth() + 1;
        }
    }

    public void setRectWidgetSelected(TurokRect rectWidgetSelected) {
        this.rectWidgetSelected = rectWidgetSelected;
    }

    public TurokRect getRectWidgetSelected() {
        return rectWidgetSelected;
    }

    public void setRectResize(TurokRect rectResize) {
        this.rectResize = rectResize;
    }

    public TurokRect getRectResize() {
        return rectResize;
    }

    public void resetWidget() {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                moduleCategoryWidget.setSelected(false);
            }
        }
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public void setSaveX(float saveX) {
        this.saveX = saveX;
    }

    public float getSaveX() {
        return saveX;
    }

    public void setSaveY(float saveY) {
        this.saveY = saveY;
    }

    public float getSaveY() {
        return saveY;
    }

    public void setScaleWidth(float scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    public float getScaleWidth() {
        return scaleWidth;
    }

    public void setScaleHeight(float scaleHeight) {
        this.scaleHeight = scaleHeight;
    }

    public float getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void onClose() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onClose();
        }

        this.isMouseClickedLeft = false;
        this.isMouseClickedMiddle = false;
    }

    @Override
    public void onOpen() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onOpen();
        }
    }

    @Override
    public void onKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onKeyboard(character, key);
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                if (moduleCategoryWidget.isSelected()) moduleCategoryWidget.onCustomKeyboard(character, key);
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.isMouseClickedMiddle) {
            this.isMouseClickedMiddle = false;
        }

        if (this.isMouseClickedLeft) {
            this.isMouseClickedLeft = false;
        }

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseReleased(button);

            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                if (moduleCategoryWidget.isSelected()) moduleCategoryWidget.onCustomMouseReleased(button);
            }
        }
    }

    @Override
    public void onMouseClicked(int button) {
        if (button == TurokMouse.BUTTON_MIDDLE) {
            this.dragX = this.master.getMouse().getX() - this.rect.getX();
            this.dragY = this.master.getMouse().getY() - this.rect.getY();

            this.isMouseClickedMiddle = this.flagMouse == Flag.MouseOver;
        }

        if (button == TurokMouse.BUTTON_LEFT) {
            if (this.flagMouseResize == Flag.MouseOver) {
                this.resizeX = this.master.getMouse().getX() - this.rect.getX();
                this.resizeY = this.master.getMouse().getY() - this.rect.getY();

                this.isMouseClickedLeft = true;
            }
        }

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseClicked(button);

            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                if (moduleCategoryWidget.isSelected()) moduleCategoryWidget.onCustomMouseClicked(button);
            }
        }
    }

    @Override
    public void onRender() {
        this.rect.setWidth(this.scaleWidth);
        this.rect.setHeight(TurokMath.min(this.scaleHeight, 200));

        // Set the x, y and size of rect resize, to we resize the main scale variables.
        this.rectResize.setWidth(8);
        this.rectResize.setHeight(8);

        this.rectResize.setX(this.rect.getX() + this.rect.getWidth() - this.rectResize.getWidth());
        this.rectResize.setY(this.rect.getY() + this.rect.getHeight() - this.rectResize.getHeight());

        // Set the rect of selected widget for y & width, so we can't change.
        this.rectWidgetSelected.setY(this.rect.getY());
        this.rectWidgetSelected.setHeight(this.widgetHeight);

        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
        this.flagMouseResize = this.rectResize.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

        this.scaleWidth = TurokMath.lerp(this.scaleWidth, (TurokMath.clamp(this.size, (76) * this.loadedWidgetList.size(), (151) * this.loadedWidgetList.size())), this.master.getPartialTicks());

        TurokRenderGL.color(Rocan.getWrapperGUI().colorFrameBackground[0], Rocan.getWrapperGUI().colorFrameBackground[1], Rocan.getWrapperGUI().colorFrameBackground[2], Rocan.getWrapperGUI().colorFrameBackground[3]);
        TurokRenderGL.drawSolidRect(this.rect);

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();
            widgets.onCustomRender();

            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                moduleCategoryWidget.getRect().setWidth(TurokMath.clamp((this.size - this.loadedWidgetList.size()) / this.loadedWidgetList.size(), 75, 150));
                moduleCategoryWidget.setOffsetX((moduleCategoryWidget.getRect().getWidth() + 1) * this.loadedWidgetList.indexOf(widgets));
            }
        }

        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetSelected[0], Rocan.getWrapperGUI().colorWidgetSelected[1], Rocan.getWrapperGUI().colorWidgetSelected[2], Rocan.getWrapperGUI().colorWidgetSelected[3]);
        TurokRenderGL.drawOutlineRect(this.rectWidgetSelected);

        if (this.isMouseClickedMiddle && this.master.isOpened()) {
            float x = this.master.getMouse().getX() - this.dragX;
            float y = this.master.getMouse().getY() - this.dragY;

            // We set the drag position to rect smooooth.
            this.rect.setX((int) TurokMath.lerp(this.rect.getX(), x, this.master.getPartialTicks()));
            this.rect.setY((int) TurokMath.lerp(this.rect.getY(), y, this.master.getPartialTicks()));
        }

        if (this.isMouseClickedLeft) {
            float x = (this.master.getMouse().getX() - this.rect.getX());
            float y = (this.master.getMouse().getY() - this.rect.getY());

            // The current scale have minimum 75 and maximum 160, but I add for fix the minimum problem.
            this.size = x;
            this.scaleHeight = (int) TurokMath.lerp(this.scaleHeight, y, this.master.getPartialTicks());
        }

        if (this.isStarted) {
            Widget widget = this.loadedWidgetList.get(0);

            // Set true the first widget on loadedWidgetList.
            if (widget instanceof ModuleCategoryWidget) ((ModuleCategoryWidget) widget).setSelected(true);

            this.isStarted = false;
        }
    }
}