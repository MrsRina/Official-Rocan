package me.rina.rocan.client.gui.module.visual;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.turok.render.font.management.TurokFontManager;

import java.awt.*;

/**
 * @author SrRina
 * @since 2020-12-16 at 15:43
 **/
public class LabelWidget extends Widget {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widgetCategory;
    private ModuleContainer moduleContainer;

    private ModuleWidget widgetModule;
    private SettingContainer settingContainer;

    private boolean isScroll;

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    public Flag flagMouse = Flag.MouseNotOver;

    public LabelWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, String text) {
        super(text);

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.moduleContainer = moduleContainer;

        this.widgetModule = widgetModule;
        this.settingContainer = settingContainer;

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);
    }

    public void setText(String text) {
        this.rect.setTag(text);
    }

    public String getText() {
        return this.rect.getTag();
    }

    public void setScroll(boolean scroll) {
        this.isScroll = scroll;
    }

    public boolean isScroll() {
        return this.isScroll;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetX() {
        return offsetX;
    }

    /**
     * Set the space referenced with main rect position y.
     *
     * @param offsetY
     */
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

    @Override
    public void onRender() {
        if (this.settingContainer == null) {
            this.rect.setWidth(this.settingContainer.getRect().getWidth());
        }

        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);

        float onTopPositionY = this.isScroll ? this.settingContainer.getScrollRect().getY() : this.settingContainer.getRect().getY();

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(onTopPositionY + this.offsetY);

        TurokFontManager.render(Rocan.getWrapperGUI().fontSmallWidget, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 5, true, new Color(255, 255, 255));
    }

    @Override
    public void onCustomRender() {

    }
}
