package me.rina.rocan.client.gui.module.module.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;

import java.awt.*;

/**
 * @author SrRina
 * @since 2020-12-08 at 23:14
 **/
public class ModuleWidget extends Widget {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widget;
    private ModuleContainer container;

    private Module module;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    public ModuleWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widget, ModuleContainer container, Module module) {
        super(module.getTag());

        this.master = master;
        this.frame = frame;

        this.widget = widget;
        this.container = container;

        this.module = module;

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(4 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleWidget, this.rect.getTag()) + 4);
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
        this.rect.setX(this.container.getRect().getX() + this.container.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.container.getRect().getY() + this.container.getScrollRect().getY() + this.offsetY);

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(4 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleWidget, this.rect.getTag()) + 4);

        TurokRenderGL.color(0, 0, 0, 255);
        TurokRenderGL.drawSolidRect(this.rect);

        TurokFontManager.render(Rocan.getGUI().fontModuleWidget, this.rect.getTag(), this.rect.getX() + 1, this.rect.getY() + 1, true, new Color(255, 255, 255));
    }
}
