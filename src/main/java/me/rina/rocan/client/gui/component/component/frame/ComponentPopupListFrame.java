package me.rina.rocan.client.gui.component.component.frame;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.client.gui.component.ComponentClickGUI;
import me.rina.rocan.client.gui.component.component.widget.ComponentWidget;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 27/03/2021 at 00:05
 **/
public class ComponentPopupListFrame extends Frame {
    private ComponentClickGUI master;

    private boolean isOpened;
    private boolean isMouseClickedLeft;

    private float offsetX;
    private float offsetY;

    private int maximumHeight;
    private ArrayList<Widget> loadedWidgetList;

    /* Flags. */
    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public ComponentPopupListFrame(ComponentClickGUI master) {
        super("Mother");

        this.master = master;

        this.rect.setWidth(100);
        this.init();
    }

    public void init() {
        if (this.loadedWidgetList == null) {
            this.loadedWidgetList = new ArrayList<>();
        }

        this.maximumHeight = 1;

        for (Component components : Rocan.getComponentManager().getComponentList()) {
            ComponentWidget widget = new ComponentWidget(this.master, this, components);

            widget.setOffsetY(this.maximumHeight);
            widget.setOffsetX(1);

            this.maximumHeight += widget.getRect().getHeight() + 1;
            this.loadedWidgetList.add(widget);
        }
    }

    public ComponentClickGUI getMaster() {
        return master;
    }

    public void setMaximumHeight(int maximumHeight) {
        this.maximumHeight = maximumHeight;
    }

    public int getMaximumHeight() {
        return maximumHeight;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean isOpened() {
        return isOpened;
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
        this.isMouseClickedLeft = false;

        if (this.isOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onClose();
            }
        }
    }

    @Override
    public void onCustomClose() {
        this.isMouseClickedLeft = false;

        if (this.isOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomClose();
            }
        }
    }

    @Override
    public void onOpen() {
        this.isMouseClickedLeft = false;

        if (this.isOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onOpen();
            }
        }
    }

    @Override
    public void onCustomOpen() {
        this.isMouseClickedLeft = false;

        if (this.isOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomOpen();
            }
        }
    }

    @Override
    public void onKeyboard(char character, int key) {
        if (this.isOpened) {
            if (key == Keyboard.KEY_ESCAPE) {
                this.master.resetRectBlocking();
                this.isOpened = false;

                if (this.master.isCancelledToCloseGUI()) {
                    this.master.setCancelledToCloseGUI(false);
                }
            } else {
                for (Widget widgets : this.loadedWidgetList) {
                    widgets.onKeyboard(character, key);
                }
            }
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {

    }

    @Override
    public void onMouseReleased(int button) {
        if (this.isMouseClickedLeft) {
            float xWidth = this.master.getMouse().getX() + this.rect.getWidth();
            float yHeight = this.master.getMouse().getY() + this.maximumHeight;

            float xDiff = this.master.getDisplay().getScaledWidth();
            float yDiff = this.master.getDisplay().getScaledHeight();

            float x = this.master.getMouse().getX();
            float y = this.master.getMouse().getY();

            if (xWidth > xDiff) {
                x = this.master.getDisplay().getScaledWidth() - this.rect.getWidth();
            }

            if (yHeight > yDiff) {
                y = this.master.getDisplay().getScaledHeight() - maximumHeight;
            }

            this.rect.setX(x);
            this.rect.setY(y);

            this.master.getRectBlocking().setX(this.rect.getX());
            this.master.getRectBlocking().setY(this.rect.getY());

            this.isOpened = true;
            this.isMouseClickedLeft = false;
        }

        if (this.isOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onMouseReleased(button);
            }
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.master.flagMouseRectBlocking == Flag.MOUSE_NOT_OVER) {
            if (this.master.isCancelledToCloseGUI()) {
                this.master.setCancelledToCloseGUI(false);
            }

            this.master.resetRectBlocking();
            this.isOpened = false;

            if (button == 1 && !this.master.isMouseOverComponent()) {
                this.isMouseClickedLeft = true;
            }
        }

        if (this.isOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onMouseClicked(button);
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        if (this.isOpened) {
            this.master.setCancelledToCloseGUI(true);

            this.rect.setHeight(TurokMath.lerp(this.rect.getHeight(), this.maximumHeight, this.master.getPartialTicks()));

            this.master.getRectBlocking().setWidth(this.rect.getWidth());
            this.master.getRectBlocking().setHeight(this.rect.getHeight());

            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouse = Flag.MOUSE_NOT_OVER;

            this.rect.setX(0);
            this.rect.setY(0);

            this.rect.setHeight(0);
        }

        // Draw background.
        TurokShaderGL.drawSolidRect(this.rect, Rocan.getWrapper().colorFrameBackground);
        TurokShaderGL.drawSolidRect(this.rect, Rocan.getWrapper().colorFrameBackground);

        TurokShaderGL.drawOutlineRect(this.rect, new int[] {190, 190, 190, 100});

        TurokShaderGL.pushScissor();
        TurokShaderGL.drawScissor(this.rect);

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();
        }

        TurokShaderGL.popScissor();
    }

    @Override
    public void onCustomRender() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomRender();
        }
    }
}
