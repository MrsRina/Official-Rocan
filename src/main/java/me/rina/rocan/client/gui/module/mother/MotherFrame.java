package me.rina.rocan.client.gui.module.mother;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.util.chat.ChatUtil;
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

    private Widget widgetSelected;
    private TurokRect rectWidgetSelected;

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
            this.widgetSelected = moduleCategoryWidget;

            this.scaleWidth += moduleCategoryWidget.getRect().getWidth() + 1;
        }
    }

    public void setRectWidgetSelected(TurokRect rectWidgetSelected) {
        this.rectWidgetSelected = rectWidgetSelected;
    }

    public TurokRect getRectWidgetSelected() {
        return rectWidgetSelected;
    }

    public void updateScale() {
        this.scaleY = this.master.getDisplay().getScaledHeight() / (((this.scale * 2) + (this.scale * 2)));
    }

    public void resetModuleCategoryWidget() {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleCategoryWidget) {
                ModuleCategoryWidget moduleCategoryWidget = (ModuleCategoryWidget) widgets;

                moduleCategoryWidget.setSelected(false);
            }
        }
    }

    public void setWidgetSelected(Widget widgetSelected) {
        this.widgetSelected = widgetSelected;
    }

    public Widget getWidgetSelected() {
        return widgetSelected;
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
    public void onMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseReleased(button);
        }

        if (this.widgetSelected != null) {
            this.widgetSelected.onCustomMouseReleased(button);
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseClicked(button);
        }

        if (this.widgetSelected != null) {
            this.widgetSelected.onCustomMouseClicked(button);
        }
    }

    @Override
    public void onRender() {
        this.updateScale();

        this.rect.setX(this.scaleX);
        this.rect.setY(this.scaleY);

        this.rect.setWidth(this.scaleWidth);
        this.rect.setHeight(TurokMath.min(this.scaleHeight, 200));

        /*
         * We just set the sizes for height and y.
         */
        this.rectWidgetSelected.setY(this.rect.getY());
        this.rectWidgetSelected.setHeight(this.widgetHeight);

        TurokRenderGL.color(Rocan.getGUI().colorFrameBackground[0], Rocan.getGUI().colorFrameBackground[1], Rocan.getGUI().colorFrameBackground[2], Rocan.getGUI().colorFrameBackground[3]);
        TurokRenderGL.drawSolidRect(this.rect);

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();
        }

        if (this.widgetSelected != null) {
            this.widgetSelected.onCustomRender();

            TurokRenderGL.color(Rocan.getGUI().colorWidgetSelected[0], Rocan.getGUI().colorWidgetSelected[1], Rocan.getGUI().colorWidgetSelected[2], Rocan.getGUI().colorWidgetSelected[3]);
            TurokRenderGL.drawOutlineRect(this.rectWidgetSelected);
        }
    }
}