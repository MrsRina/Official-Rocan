package me.rina.rocan.client.gui.module.module.container;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.opengl.GL11;

import java.awt.*;
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

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    private ArrayList<Widget> loadedWidgetList;
    private TurokRect scrollRect = new TurokRect("Scroll", 0, 0);

    private boolean isModuleOpen;

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
        float scale = (2 * this.frame.getScale());

        this.rect.setX(this.frame.getRect().getX() + scale);
        this.rect.setY(this.frame.getRect().getY() + this.widget.getRect().getHeight() + scale);
    }

    public void refreshWidget() {
        this.scrollRect.setHeight(0);

        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;
                moduleWidget.setOffsetY(this.scrollRect.getHeight());

                this.scrollRect.height += moduleWidget.getRect().getHeight() + 1;
            }
        }
    }

    public void refreshSearchWidget(String currentSearch) {
        if (currentSearch.isEmpty()) {
            return;
        }

        this.scrollRect.setHeight(0);

        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;

                if (moduleWidget.getModule().getName().contains(currentSearch)) {
                    moduleWidget.setOffsetY(this.scrollRect.getHeight());

                    this.scrollRect.height += moduleWidget.getRect().getHeight() + 1;
                } else {
                    moduleWidget.setOffsetY(-1000.0f);
                }
            }
        }
    }

    public void resetWidget() {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;

                this.isModuleOpen = false;

                moduleWidget.setSelected(false);
                moduleWidget.setLocked(false);
            }
        }
    }

    public void resetWidget(boolean lock) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;

                this.isModuleOpen = lock;

                moduleWidget.setSelected(false);
                moduleWidget.setLocked(lock);
            }
        }
    }

    public void resetWidget(String name) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;

                this.isModuleOpen = true;

                if (moduleWidget.getModule().getName().equalsIgnoreCase(name)) {
                    moduleWidget.setLocked(false);
                    moduleWidget.setSelected(false);
                }
            }
        }
    }

    public float getWidthScale() {
        return this.frame.getRect().getWidth() / 2.3f;
    }

    public float getHeightScale() {
        float scale = (2 * this.frame.getScale());

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

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetWidth(float offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public float getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetHeight(float offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public float getOffsetHeight() {
        return offsetHeight;
    }

    public void setModuleOpen(boolean moduleOpen) {
        isModuleOpen = moduleOpen;
    }

    public boolean isModuleOpen() {
        return isModuleOpen;
    }

    @Override
    public void onClose() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onClose();
        }
    }

    @Override
    public void onOpen() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onOpen();
        }
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
    public void onMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseReleased(button);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomMouseReleased(button);
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseClicked(button);
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

        float minimumScroll = this.rect.getHeight() - this.scrollRect.getHeight() - 2f;
        float maximumScroll = 3f;

        this.scrollRect.setX(this.rect.getX());
        this.scrollRect.setY(TurokMath.lerp(this.scrollRect.getY(), this.rect.getY() + this.offsetY, this.master.getPartialTicks()));

        this.scrollRect.setWidth(this.rect.getWidth());

        boolean isScrollLimit = this.scrollRect.getY() + this.scrollRect.getHeight() >= this.rect.getY() + this.rect.getHeight() - 3;

        if (this.flagMouse == Flag.MouseOver && this.master.getMouse().hasWheel() && isScrollLimit) {
            this.offsetY -= this.master.getMouse().getScroll();
        }

        if (this.offsetY <= minimumScroll) {
            this.offsetY = minimumScroll;
        }

        if (this.offsetY >= maximumScroll) {
            this.offsetY = maximumScroll;
        }

        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

        // Background of container.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorContainerBackground[0], Rocan.getWrapperGUI().colorContainerBackground[1], Rocan.getWrapperGUI().colorContainerBackground[2], Rocan.getWrapperGUI().colorContainerBackground[3]);
        TurokRenderGL.drawSolidRect(this.rect);

        float offsetFixOutline = 1;

        TurokShaderGL.pushScissorMatrix();
        TurokShaderGL.drawScissor(this.rect.getX() - offsetFixOutline, this.rect.getY(), this.rect.getWidth() + (offsetFixOutline * 2), this.rect.getHeight());

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomRender();
        }

        TurokShaderGL.popScissorMatrix();

        if (this.widget.isSelected()) {
            String string = "ModuleOpen: " + this.isModuleOpen + " Client Container Flag: " + this.frame.getClientContainer().flagMouseModule.toString();

            TurokFontManager.render(Rocan.getWrapperGUI().fontSmallWidget, string, 10, 10, true, new Color(255, 255, 255));
        }
    }
}
