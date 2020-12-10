package me.rina.rocan.client.gui.module.mother;

import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;

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

    public Flag flagMouse = Flag.MouseNotOver;

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
            this.widgetSelected = moduleCategoryWidget;

            this.scaleWidth += moduleCategoryWidget.getRect().getWidth();
        }
    }

    public void updateScale() {
        this.scaleX = (this.master.getDisplay().getScaledWidth() - this.scaleWidth) /  (this.scale);
        this.scaleY = this.master.getDisplay().getScaledHeight() / (((this.scale * 2) + (this.scale * 2)));
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
            widgets.onCustomMouseClicked(button);
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

        TurokRenderGL.color(0, 0, 0, 100);
        TurokRenderGL.drawSolidRect(this.rect);

        TurokRenderGL.color(0, 0, 0, 200);
        TurokRenderGL.drawSolidRect(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), this.widgetHeight);

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();
        }

        this.widgetSelected = this.loadedWidgetList.get(this.loadedWidgetList.size() - 1);

        if (this.widgetSelected != null) {
            this.widgetSelected.onCustomRender();
        }

        TurokRenderGL.color(255, 255, 255, 100);
        TurokRenderGL.drawOutlineRect(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), this.widgetHeight);
    }
}