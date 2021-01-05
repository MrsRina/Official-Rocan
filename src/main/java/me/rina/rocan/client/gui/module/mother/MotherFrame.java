package me.rina.rocan.client.gui.module.mother;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

import java.util.ArrayList;

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
    protected int scale = 2;

    protected int scaleX;
    protected int scaleY;

    protected int scaleWidth;
    protected int scaleHeight;

    private ArrayList<Widget> loadedWidgetList;

    private int widgetHeight;

    private TurokRect rectWidgetSelected;

    private boolean isStarted = true;

    public Flag flagMouse = Flag.MouseNotOver;

    public MotherFrame(ModuleClickGUI master) {
        super("Mother");

        this.master = master;

        this.rectWidgetSelected = new TurokRect("Canada", 0, 0);

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

    public void setNewScaleWidth(int size) {
        this.scaleWidth = 1;

        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                moduleCategoryWidget.getRect().setWidth(TurokMath.clamp(size, 75, 300));
                moduleCategoryWidget.setOffsetX(this.scaleWidth);

                this.scaleWidth += TurokMath.clamp(size, 75, 300) + 1;
            }
        }
    }

    public void setRectWidgetSelected(TurokRect rectWidgetSelected) {
        this.rectWidgetSelected = rectWidgetSelected;
    }

    public TurokRect getRectWidgetSelected() {
        return rectWidgetSelected;
    }

    public void updateScale() {
        this.scaleY = (this.master.getDisplay().getScaledHeight() / 2) - (this.rect.getHeight() / this.scale);
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

    public int getScale() {
        return scale;
    }

    public void setScaleX(int scaleX) {
        this.scaleX = scaleX;
    }

    public int getScaleX() {
        return scaleX;
    }

    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
    }

    public int getScaleY() {
        return scaleY;
    }

    public void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    public int getScaleWidth() {
        return scaleWidth;
    }

    public void setScaleHeight(int scaleHeight) {
        this.scaleHeight = scaleHeight;
    }

    public int getScaleHeight() {
        return scaleHeight;
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
        this.updateScale();

        this.rect.setX(this.scaleX);
        this.rect.setY(this.scaleY);

        this.rect.setWidth(this.scaleWidth);
        this.rect.setHeight(TurokMath.min(this.scaleHeight, 200));

        // Set the rect of selected widget for y & width, so we can't change.
        this.rectWidgetSelected.setY(this.rect.getY());
        this.rectWidgetSelected.setHeight(this.widgetHeight);

        this.scaleHeight = this.master.getDisplay().getScaledHeight() / 2;

        TurokRenderGL.color(Rocan.getWrapperGUI().colorFrameBackground[0], Rocan.getWrapperGUI().colorFrameBackground[1], Rocan.getWrapperGUI().colorFrameBackground[2], Rocan.getWrapperGUI().colorFrameBackground[3]);
        TurokRenderGL.drawSolidRect(this.rect);

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();

            if (widgets instanceof ModuleCategoryWidget) widgets.onCustomRender();
        }

        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetSelected[0], Rocan.getWrapperGUI().colorWidgetSelected[1], Rocan.getWrapperGUI().colorWidgetSelected[2], Rocan.getWrapperGUI().colorWidgetSelected[3]);
        TurokRenderGL.drawOutlineRect(this.rectWidgetSelected);

        if (this.isStarted) {
            Widget widget = this.loadedWidgetList.get(0);

            // Set true the first widget on loadedWidgetList.
            if (widget instanceof ModuleCategoryWidget) ((ModuleCategoryWidget) widget).setSelected(true);

            this.isStarted = false;
        }
    }
}