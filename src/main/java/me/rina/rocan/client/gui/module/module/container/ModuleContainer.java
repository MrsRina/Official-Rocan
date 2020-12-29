package me.rina.rocan.client.gui.module.module.container;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.opengl.GL11;

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

    public Flag flagMouse = Flag.MouseNotOver;

    public ModuleContainer(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widget, ModuleCategory category) {
        super(widget.getRect().getTag());

        this.master = master;
        this.frame = frame;

        this.widget = widget;
        this.category = category;

        this.updateSpecifyScale();
        this.init();
    }

    public void init() {
        if (this.loadedWidgetList == null) {
            this.loadedWidgetList = new ArrayList<>();
        } else {
            this.loadedWidgetList.clear();
        }

        /*
         * We need set for 0 variables size and offset,
         * if not the memory of the old values will be used,
         * and glitching the GUI.
         */
        this.scrollRect.setHeight(0);
        this.offsetY = 3;

        for (Module modules : ModuleManager.get(this.category)) {
            ModuleWidget moduleWidget = new ModuleWidget(this.master, this.frame, this.widget, this, modules);

            moduleWidget.setOffsetY(this.scrollRect.getHeight());

            this.loadedWidgetList.add(moduleWidget);

            this.scrollRect.height += moduleWidget.getRect().getHeight() + 1;
        }
    }

    public void updateSpecifyScale() {
        int scale = (2 * this.frame.getScale());

        this.rect.setX(this.frame.getRect().getX() + scale);
        this.rect.setY(this.frame.getRect().getY() + this.widget.getRect().getHeight() + scale);
    }

    public void resetWidget() {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;

                moduleWidget.setSelected(false);
                moduleWidget.setLocked(false);
            }
        }
    }

    public void resetWidget(Class clazz) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;

                if (moduleWidget.getClass() != clazz) {
                    moduleWidget.setLocked(false);
                    moduleWidget.setSelected(false);
                }
            }
        }
    }

    public int getWidthScale() {
        return (int) (this.frame.getRect().getWidth() / 2.3);
    }

    public int getHeightScale() {
        int scale = (2 * this.frame.getScale());

        return (this.frame.getRect().getHeight() - this.widget.getRect().getHeight()) - (scale * 2);
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
    public void onKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onKeyboard(character, key);
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomKeyboard(character, key);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomMouseReleased(button);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomMouseClicked(button);
        }
    }

    @Override
    public void onRender() {
        this.flagMouse = Flag.MouseNotOver;

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();
        }
    }

    @Override
    public void onCustomRender() {
        this.updateSpecifyScale();

        int minimumScroll = (this.rect.getHeight() - this.scrollRect.getHeight()) - 2;
        int maximumScroll = 3;

        this.scrollRect.setX(this.rect.getX());
        this.scrollRect.setY((int) TurokMath.linearInterpolation(this.scrollRect.getY(), this.rect.getY() + this.offsetY, this.master.getPartialTicks()));

        this.scrollRect.setWidth(this.rect.getWidth());

        boolean isScrollLimit = this.scrollRect.getY() + this.scrollRect.getHeight() >= this.rect.getY() + this.rect.getHeight() - 3;

        if (this.master.getMouse().hasWheel() && isScrollLimit) {
            this.offsetY -= this.master.getMouse().getScroll();

            if (this.offsetY <= minimumScroll) {
                this.offsetY = minimumScroll;
            }
        }

        if (this.offsetY >= maximumScroll) {
            this.offsetY = maximumScroll;
        }

        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

        TurokRenderGL.color(Rocan.getWrapperGUI().colorContainerBackground[0], Rocan.getWrapperGUI().colorContainerBackground[1], Rocan.getWrapperGUI().colorContainerBackground[2], Rocan.getWrapperGUI().colorContainerBackground[3]);
        TurokRenderGL.drawSolidRect(this.rect);

        int offsetFixOutline = 1;

        TurokRenderGL.enable(GL11.GL_SCISSOR_TEST);
        TurokRenderGL.drawScissor(this.rect.getX() - offsetFixOutline, this.rect.getY(), this.rect.getWidth() + (offsetFixOutline * 2), this.rect.getHeight());

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomRender();
        }

        TurokRenderGL.disable(GL11.GL_SCISSOR_TEST);
    }
}
