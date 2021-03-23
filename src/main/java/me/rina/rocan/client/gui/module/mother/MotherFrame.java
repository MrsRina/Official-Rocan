package me.rina.rocan.client.gui.module.mother;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.client.container.ClientContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

import java.awt.*;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

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

    private float dragX;
    private float dragY;

    private float resizeX;
    private float resizeY;

    private float size;

    private ArrayList<Widget> loadedWidgetList;

    private ClientContainer clientContainer;

    private float widgetHeight;

    private TurokRect rectResize = new TurokRect("Goo", 0, 0);

    private boolean isStarted = true;
    private boolean isDragging;
    private boolean isMouseClickedLeft;
    private boolean isReturnCenterPressed;

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;
    public Flag flagMouseResize = Flag.MOUSE_NOT_OVER;

    public MotherFrame(ModuleClickGUI master) {
        super("Mother");

        this.master = master;

        this.init();
    }

    public void init() {
        this.loadedWidgetList = new ArrayList<>();
        this.clientContainer = new ClientContainer(this.master, this);

        for (ModuleCategory category : ModuleCategory.values()) {
            ModuleCategoryWidget moduleCategoryWidget = new ModuleCategoryWidget(this.master, this, category);

            moduleCategoryWidget.setOffsetX(this.scaleWidth);

            this.loadedWidgetList.add(moduleCategoryWidget);

            this.widgetHeight = moduleCategoryWidget.getRect().getHeight();
            this.scaleWidth += moduleCategoryWidget.getRect().getWidth() + 1;
        }
    }

    public void resetWidget() {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                moduleCategoryWidget.setSelected(false);
            }
        }
    }

    public void setRectResize(TurokRect rectResize) {
        this.rectResize = rectResize;
    }

    public TurokRect getRectResize() {
        return rectResize;
    }

    public void setClientContainer(ClientContainer clientContainer) {
        this.clientContainer = clientContainer;
    }

    public ClientContainer getClientContainer() {
        return clientContainer;
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

    public void setDragX(float dragX) {
        this.dragX = dragX;
    }

    public float getDragX() {
        return dragX;
    }

    public void setDragY(float dragY) {
        this.dragY = dragY;
    }

    public float getDragY() {
        return dragY;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public void onClose() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onClose();
        }

        this.isMouseClickedLeft = false;
        this.isDragging = false;

        this.clientContainer.onClose();
    }

    @Override
    public void onOpen() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onOpen();
        }

        this.clientContainer.onOpen();
    }

    @Override
    public void onKeyboard(char character, int key) {
        // Its reset the current position of screen to center, its help if GUI glitch.
        if (key == Keyboard.KEY_F1 && (this.isMouseClickedLeft == false && this.isDragging == false) && this.master.isPositionBack() && this.master.isOpened()) {
            this.isReturnCenterPressed = true;
        }

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onKeyboard(character, key);
        }

        this.clientContainer.onKeyboard(character, key);
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                if (moduleCategoryWidget.isSelected()) moduleCategoryWidget.onCustomKeyboard(character, key);
            }
        }

        this.clientContainer.onCustomKeyboard(character, key);
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.isDragging) {
            this.isDragging = false;
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

        this.clientContainer.onMouseReleased(button);
        this.clientContainer.onCustomMouseReleased(button);
    }

    @Override
    public void onMouseClicked(int button) {
        if (button == TurokMouse.BUTTON_LEFT) {
            if (this.flagMouseResize == Flag.MOUSE_OVER) {
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

        this.clientContainer.onMouseClicked(button);
        this.clientContainer.onCustomMouseClicked(button);
    }

    @Override
    public void onRender() {
        this.rect.setWidth(this.scaleWidth);
        this.rect.setHeight(TurokMath.clamp(this.scaleHeight, 200, 1080));

        // Set the x, y and size of rect resize, to we resize the main scale variables.
        this.rectResize.setWidth(8);
        this.rectResize.setHeight(8);

        this.rectResize.setX(this.rect.getX() + this.rect.getWidth() - this.rectResize.getWidth());
        this.rectResize.setY(this.rect.getY() + this.rect.getHeight() - this.rectResize.getHeight());

        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        this.flagMouseResize = this.rectResize.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;

        /*
         * Its the width of widget category, if you:
         * min  = 75;
         * off  = 1;
         * size = 7;
         * x = ?;
         *
         * *
         * x + off = min * size;       (x = 75 + 1 * 7);
         *       x = min + off * size; (x = 532);
         * *
         *
         * The return is x = 532, so, the minimum width is 532;
         *
         */
        int minimumWidth = 75;
        int maximumWidth = 270;

        this.scaleWidth = TurokMath.lerp(this.scaleWidth, (TurokMath.clamp(this.size, (minimumWidth + 1) * this.loadedWidgetList.size(), (maximumWidth + 1) * this.loadedWidgetList.size())), this.master.getPartialTicks());

        // Background of frame.
        TurokShaderGL.drawSolidRect(this.rect, new int[] {Rocan.getWrapper().colorFrameBackground[0], Rocan.getWrapper().colorFrameBackground[1], Rocan.getWrapper().colorFrameBackground[2], Rocan.getWrapper().colorFrameBackground[3]});

        /*
         * We need render the normal and custom, because yes.
         */
        this.clientContainer.onRender();
        this.clientContainer.onCustomRender();

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();
            widgets.onCustomRender();

            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                moduleCategoryWidget.getRect().setWidth(TurokMath.clamp((this.size - this.loadedWidgetList.size()) / this.loadedWidgetList.size(), minimumWidth, maximumWidth));
                moduleCategoryWidget.setOffsetX((moduleCategoryWidget.getRect().getWidth() + 1) * this.loadedWidgetList.indexOf(widgets));
            }
        }

        if (this.isDragging && this.master.isOpened()) {
            float x = this.master.getMouse().getX() - this.dragX;
            float y = this.master.getMouse().getY() - this.dragY;

            // We set the drag position very smooooth.
            this.rect.setX(TurokMath.lerp(this.rect.getX(), x, this.master.getPartialTicks()));
            this.rect.setY(TurokMath.lerp(this.rect.getY(), y, this.master.getPartialTicks()));
        }

        if (this.isMouseClickedLeft) {
            float x = (this.master.getMouse().getX() - this.rect.getX());
            float y = (this.master.getMouse().getY() - this.rect.getY());

            // The current scale have minimum 75 and maximum 160, but I add for fix the minimum problem.
            this.size = x;
            this.scaleHeight = TurokMath.lerp(this.scaleHeight, y, this.master.getPartialTicks());
        }

        if (this.isStarted) {
            Widget widget = this.loadedWidgetList.get(0);

            // Set true the first widget on loadedWidgetList.
            if (widget instanceof ModuleCategoryWidget) ((ModuleCategoryWidget) widget).setSelected(true);

            this.isStarted = false;
        }

        /*
         * We need make the GUI return backs, so we need get distance and bypass the interpolation delay.
         */
        if (this.isReturnCenterPressed && ((this.isMouseClickedLeft == false && this.isDragging == false) && this.master.isPositionBack() && this.master.isOpened())) {
            float x = this.master.getDisplay().getScaledWidth() / 2 - (this.rect.getWidth() / 2);
            float y = this.master.getDisplay().getScaledHeight() / 2 - (this.rect.getHeight() / 2);

            this.rect.setX(TurokMath.lerp(this.rect.getX(), x, this.master.getPartialTicks()));
            this.rect.setY(TurokMath.lerp(this.rect.getY(), y, this.master.getPartialTicks()));

            if (this.rect.getDistance(new TurokRect(x, y, 10, 10)) <= 100) {
                this.isReturnCenterPressed = false;
            } else {
                this.isMouseClickedLeft = false;
                this.isDragging = false;
            }
        }
    }
}