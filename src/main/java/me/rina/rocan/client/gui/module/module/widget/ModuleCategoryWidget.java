package me.rina.rocan.client.gui.module.module.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModuleCategoryWidget extends Widget {
    private ModuleClickGUI master;
    private ModuleCategory category;

    private MotherFrame frame;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    private ModuleContainer container;

    public Flag flagMouse = Flag.MouseNotOver;

    public ModuleCategoryWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategory category) {
        super(category.name());

        this.master = master;
        this.frame = frame;

        this.category = category;

        this.rect.setWidth(100);
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag()) + 6);

        this.container = new ModuleContainer(this.master, this.frame, this);
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
        this.rect.setY(this.frame.getRect().getY() + this.offsetY);

        this.rect.setWidth(100);
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag()) + 6);

        TurokRenderGL.enable(GL11.GL_SCISSOR_TEST);

        TurokRenderGL.drawScissor(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), this.rect.getHeight());

        TurokRenderGL.disable(GL11.GL_SCISSOR_TEST);

        TurokFontManager.render(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag(), this.rect.getX() + (this.rect.getWidth() / 2 - (TurokFontManager.getStringWidth(Rocan.getGUI().fontModuleCategoryWidget, this.rect.getTag()) / 2)), this.rect.getY() + 6, true, new Color(255, 255, 255));
    }

    @Override
    public void onCustomRender() {
        this.container.onRender();
    }
}
