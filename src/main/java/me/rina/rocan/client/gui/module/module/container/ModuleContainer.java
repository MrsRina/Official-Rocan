package me.rina.rocan.client.gui.module.module.container;

import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.opengl.TurokRenderGL;

/**
 * @author SrRina
 * @since 2020-12-08 at 10:54
 **/
public class ModuleContainer extends Container {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widget;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    public ModuleContainer(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widget) {
        super(widget.getRect().getTag());

        this.master = master;
        this.frame = frame;

        this.widget = widget;
    }

    public void updateScale() {
        int scale = (2 * this.frame.getScale());

        this.rect.setX(this.frame.getRect().getX() + scale);
        this.rect.setY(this.frame.getRect().getY() + this.widget.getRect().getHeight() + scale);

        this.rect.setWidth(this.frame.getRect().getWidth() / 3);
        this.rect.setHeight((this.frame.getRect().getHeight() - this.widget.getRect().getHeight()) - (scale * 2));
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
    }
}
