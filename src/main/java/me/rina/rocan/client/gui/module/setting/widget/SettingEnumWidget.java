package me.rina.rocan.client.gui.module.setting.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokClass;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 2021-01-10 at 11:44
 **/
public class SettingEnumWidget extends Widget {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widgetCategory;
    private ModuleContainer moduleContainer;

    private ModuleWidget widgetModule;
    private SettingContainer settingContainer;

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    private int alphaEffectHighlightRect;

    private boolean isStarted;
    private boolean isMouseClickedLeft;

    private int index;
    private ArrayList<Enum<?>> enumValueList;

    private ValueEnum setting;

    public Flag flagMouse = Flag.MouseNotOver;

    public SettingEnumWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, ValueEnum setting) {
        super(setting.getName());

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.moduleContainer = moduleContainer;

        this.widgetModule = widgetModule;
        this.settingContainer = settingContainer;

        this.setting = setting;

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);

        this.init();
    }

    public void init() {
        this.enumValueList = new ArrayList<>();

        for (Enum<?> enums : ((Enum<?>) this.setting.getValue()).getClass().getEnumConstants()) {
            this.enumValueList.add(enums);
        }
    }

    public void setSetting(ValueEnum setting) {
        this.setting = setting;
    }

    public ValueEnum getSetting() {
        return setting;
    }

    public void setOffsetX(float offsetX) {
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

    @Override
    public void onClose() {
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (this.isMouseClickedLeft) {
                if (this.index >= this.enumValueList.size() - 1) {
                    this.index = 0;
                } else {
                    this.index++;
                }

                this.isMouseClickedLeft = false;
            }
        } else {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            this.isMouseClickedLeft = button == 0;
        }
    }

    @Override
    public void onRender() {
        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapperGUI().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        if (this.settingContainer.flagMouseRealRect == Flag.MouseOver) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
        } else {
            this.flagMouse = Flag.MouseNotOver;
        }

        // Where the smooth animation works.
        this.alphaEffectHighlightRect = this.flagMouse == Flag.MouseOver ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapperGUI().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());

        String name = this.rect.getTag() + ": " + ((Enum<?>) this.setting.getValue()).name();

        TurokFontManager.render(Rocan.getWrapperGUI().fontSmallWidget, name, this.rect.getX() + 2, this.rect.getY() + 5, true, new Color(255, 255, 255));

        // Outline on rect render.
        TurokRenderGL.color(Rocan.getWrapperGUI().colorWidgetHighlight[0], Rocan.getWrapperGUI().colorWidgetHighlight[1], Rocan.getWrapperGUI().colorWidgetHighlight[2], this.alphaEffectHighlightRect);
        TurokRenderGL.drawOutlineRect(this.rect);

        // We verify the current enum value at mode when we load the client.
        // Loop.
        if (this.isStarted) {
            if (this.enumValueList.get(this.index) != (Enum<?>) this.setting.getValue()) {
                this.setting.setValue(this.enumValueList.get(this.index));

                // We need refresh later set the value.
                this.settingContainer.onRefreshWidget();
            }
        // Init.
        } else {
            this.index = TurokClass.getEnumByName((Enum<?>) this.setting.getValue(), ((Enum<?>) this.setting.getValue()).name()) != null ? this.enumValueList.indexOf(TurokClass.getEnumByName((Enum<?>) this.setting.getValue(), ((Enum<?>) this.setting.getValue()).name())) : 0;

            this.setting.setValue(this.enumValueList.get(this.index));

            this.isStarted = true;
        }

        if (this.flagMouse == Flag.MouseOver) {
            this.settingContainer.getDescriptionLabel().setText(this.setting.getDescription());

            this.settingContainer.flagDescription = Flag.MouseOver;
        }
    }

    @Override
    public void onCustomRender() {

    }
}