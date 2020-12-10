package me.rina.rocan.client.gui.module.module.container;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokRect;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 2020-12-08 at 10:54
 **/
public class ModuleContainer extends Container {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widget;

    private ModuleCategory category;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    private ArrayList<Widget> loadedWidgetList;

    private TurokRect scrollRect = new TurokRect("Scroll", 0, 0);

    public ModuleContainer(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widget, ModuleCategory category) {
        super(widget.getRect().getTag());

        this.master = master;
        this.frame = frame;

        this.widget = widget;
        this.category = category;

        this.init();
    }

    public void init() {
        this.loadedWidgetList = new ArrayList<>();

        for (Module modules : Rocan.getModuleManager().getModuleList()) {
            if (modules.getCategory() != this.category) {
                continue;
            }

            ModuleWidget moduleWidget = new ModuleWidget(this.master, this.frame, this.widget, this, modules);

            moduleWidget.setOffsetY(this.scrollRect.getHeight());

            this.loadedWidgetList.add(moduleWidget);

            this.scrollRect.height += moduleWidget.getRect().getHeight() + 1;
        }
    }

    public void updateScale() {
        int scale = (2 * this.frame.getScale());

        this.rect.setX(this.frame.getRect().getX() + scale);
        this.rect.setY(this.frame.getRect().getY() + this.widget.getRect().getHeight() + scale);

        this.rect.setWidth(this.frame.getRect().getWidth() / 3);
        this.rect.setHeight((this.frame.getRect().getHeight() - this.widget.getRect().getHeight()) - (scale * 2));
    }

    public void setScrollRect(TurokRect scrollRect) {
        this.scrollRect = scrollRect;
    }

    public TurokRect getScrollRect() {
        return scrollRect;
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

    @Override
    public void onRender() {
        this.updateScale();

        TurokRenderGL.color(0, 0, 0, 200);
        TurokRenderGL.drawSolidRect(this.rect);

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();
        }
    }
}
