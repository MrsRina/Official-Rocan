package me.rina.rocan.client.gui.module.UI;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.EnumFlagState;
import me.rina.rocan.api.gui.element.container.Container;
import me.rina.rocan.api.gui.element.frame.Frame;
import me.rina.rocan.api.gui.element.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.util.render.ElementUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.UI.category.WidgetCategory;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 09/03/2021 at 11:35
 **/
public class MainFrameUI extends Frame {
    private ModuleClickGUI master;

    /* All loaded lists UI. */
    private ArrayList<Container> loadedContainerList;
    private ArrayList<WidgetCategory> loadedWidgetCategoryList;
    private ArrayList<Frame> loadedFrameList;

    /* Handler sizes. */
    private float dragX;
    private float dragY;

    private float resX;
    private float resY;

    private float width;

    private TurokRect rectResize = new TurokRect("Resize", 0, 0, 20, 20);

    /* Flags. */
    public EnumFlagState flagMouse = EnumFlagState.MOUSE_NOT_OVER;

    public EnumFlagState flagMouseMouseClickedLeft = EnumFlagState.UNPRESSED;
    public EnumFlagState flagMouseMouseClickedMiddle = EnumFlagState.UNPRESSED;
    public EnumFlagState flagMouseMouseClickedRight = EnumFlagState.UNPRESSED;

    public EnumFlagState flagDragging = EnumFlagState.STABLE;
    public EnumFlagState flagDraggable = EnumFlagState.UNLOCKED;

    public EnumFlagState flagResizable = EnumFlagState.RESIZABLE;
    public EnumFlagState flagResizing = EnumFlagState.RESIZED;

    public EnumFlagState flagMouseResizable = EnumFlagState.MOUSE_NOT_OVER;

    public MainFrameUI(ModuleClickGUI master) {
        super("Rocan");

        this.master = master;

        // Init all lists.
        this.loadedContainerList = new ArrayList<>();
        this.loadedWidgetCategoryList = new ArrayList<>();
        this.loadedFrameList = new ArrayList<>();

        // Init categories.
        this.initCategory();
    }

    public void initCategory() {
        float offsetX = 1f;

        for (ModuleCategory categories : ModuleCategory.values()) {
            WidgetCategory widgetCategory = new WidgetCategory(this.master, categories);

            widgetCategory.setMainFrameUI(this);
            widgetCategory.setOffsetX(offsetX);

            offsetX += widgetCategory.getRect().getWidth();

            this.loadedWidgetCategoryList.add(widgetCategory);
            this.width = offsetX;
        }
    }

    public void setRectResize(TurokRect rectResize) {
        this.rectResize = rectResize;
    }

    public TurokRect getRectResize() {
        return rectResize;
    }

    public ArrayList<Container> getLoadedContainerList() {
        return loadedContainerList;
    }

    public ArrayList<WidgetCategory> getLoadedWidgetCategoryList() {
        return loadedWidgetCategoryList;
    }

    public ArrayList<Frame> getLoadedFrameList() {
        return loadedFrameList;
    }

    public void onRefreshWidgetCategoryListSelected(String categoryName) {
        for (WidgetCategory widgets : this.loadedWidgetCategoryList) {
            if (widgets != null) {
                WidgetCategory widgetCategory = (WidgetCategory) widgets;

                if (widgetCategory.getCategory().name().equals(categoryName)) {
                    widgetCategory.setSelected(false);
                }
            }
        }
    }

    @Override
    public void onClose() {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onClose();
        }
    }

    @Override
    public void onCustomClose() {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onCustomClose();
        }
    }

    @Override
    public void onOpen() {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onOpen();
        }
    }

    @Override
    public void onCustomOpen() {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onCustomOpen();
        }
    }

    @Override
    public void onKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onKeyboard(character, key);
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onCustomKeyboard(character, key);
        }
    }

    @Override
    public void onMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onMouseReleased(button);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        for (WidgetCategory widgets : this.loadedWidgetCategoryList) {
            widgets.onCustomMouseReleased(button);
        }

        if (ElementUtil.isPressed(this.flagMouseMouseClickedLeft)) {
            if (this.flagDragging == EnumFlagState.DRAGGING) {
                this.flagDragging = EnumFlagState.STABLE;
            }

            if (this.flagResizing == EnumFlagState.RESIZING) {
                this.flagResizing = EnumFlagState.RESIZED;
            }

            this.flagMouseMouseClickedLeft = EnumFlagState.UNPRESSED;
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (WidgetCategory widgets : this.loadedWidgetCategoryList) {
            widgets.onMouseClicked(button);
        }

        if (this.flagMouse == EnumFlagState.MOUSE_OVERING && this.flagMouseResizable != EnumFlagState.MOUSE_OVERING) {
            EnumFlagState flag = ElementUtil.getMousePressed(button, 0);

            if (ElementUtil.isPressed(flag) != false) {
                // Gets drags stuff.
                this.dragX = this.master.getMouse().getX() - this.rect.getX();
                this.dragY = this.master.getMouse().getY() - this.rect.getY();

                this.flagDragging = ElementUtil.getDragging(this.flagDraggable);
                this.flagMouseMouseClickedLeft = flag;
            }
        }

        if (this.flagMouseResizable == EnumFlagState.MOUSE_OVERING) {
            EnumFlagState flag = ElementUtil.getMousePressed(button, 0);

            if (ElementUtil.isPressed(flag) != false) {
                // Gets drags stuff.
                this.resX = this.master.getMouse().getX() - this.rect.getX();
                this.resY = this.master.getMouse().getY() - this.rect.getY();

                this.flagResizing = ElementUtil.getResizing(this.flagResizable);
                this.flagMouseMouseClickedLeft = flag;
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onCustomMouseClicked(button);
        }
    }

    @Override
    public void onRender() {
        this.flagMouse = ElementUtil.getMouseOver(this.rect.collideWithMouse(this.master.getMouse()));
        this.flagMouseResizable = ElementUtil.getMouseOver(this.rectResize.collideWithMouse(this.master.getMouse()));

        this.rectResize.setWidth(6);
        this.rectResize.setHeight(6);

        this.rectResize.setX(this.rect.getX() + this.rect.getWidth() - (this.rectResize.getWidth()));
        this.rectResize.setY(this.rect.getY() + this.rect.getHeight() - (this.rectResize.getHeight()));

        if (this.flagDragging == EnumFlagState.DRAGGING) {
            float draggingX = this.master.getMouse().getX() - this.dragX;
            float draggingY = this.master.getMouse().getY() - this.dragY;

            this.rect.setX(draggingX);
            this.rect.setY(draggingY);
        }

        if (this.flagResizing == EnumFlagState.RESIZING) {
            float resizingX = (this.master.getMouse().getX()) - this.rect.getX();
            float resizingY = (this.master.getMouse().getY()) - this.rect.getY();

            this.width = resizingX;
            this.rect.setHeight(TurokMath.min(resizingY, 200));
        }

        // Draw the frame.
        TurokShaderGL.drawSolidRect(this.rect, Rocan.getWrapper().colorFrameBackground);
        TurokShaderGL.drawSolidRect(this.rectResize, Rocan.getWrapper().colorWidgetHighlight);

        int minimumSize = 76;
        int maximumSize = 270;

        float theLIMIT = TurokMath.clamp(this.width, (minimumSize * ModuleCategory.values().length), (maximumSize * ModuleCategory.values().length));

        this.rect.setWidth(TurokMath.lerp(this.rect.getWidth(), theLIMIT, this.master.getPartialTicks()));

        float countWidth = 0f;

        for (WidgetCategory widgets : this.loadedWidgetCategoryList) {
            widgets.onRender();

            float l = this.loadedWidgetCategoryList.indexOf(widgets) != this.loadedWidgetCategoryList.size() ? 1f : 0f;
            float w = TurokMath.clamp((this.width / this.loadedWidgetCategoryList.size()) + 1, minimumSize, maximumSize);

            widgets.setOffsetX(countWidth);
            widgets.getRect().setWidth(w);

            countWidth += widgets.getRect().getWidth() + l;
        }
    }

    @Override
    public void onCustomRender() {
        for (Widget widgets : this.loadedWidgetCategoryList) {
            widgets.onCustomRender();
        }
    }
}