package me.rina.rocan.client.gui.module.module.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.module.client.ModuleClickGUI;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;

import java.awt.*;

public class ModuleCategoryWidget extends Widget {
    private ModuleClickGUI master;
    private ModuleCategory category;

    private MotherFrame frame;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    public Flag flagMouse = Flag.MouseNotOver;

    public ModuleCategoryWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategory category) {
        super(category.name());

        this.master = master;
        this.frame = frame;

        this.category = category;

        this.rect.setWidth(100);
        this.rect.setHeight(3 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag()) + 3);
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
        this.rect.setX(this.frame.getRect().getX() + this.offsetX);
        this.rect.setX(this.frame.getRect().getY() + this.offsetY);

        TurokRenderGL.color(30, 30, 30, 200);
        TurokRenderGL.drawSolidRect(this.rect);

        TurokFontManager.render(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag(), 1, 1, true, new Color(255, 255, 255));
    }
}
