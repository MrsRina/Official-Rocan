package me.rina.rocan.client.gui.module.setting.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
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
    private boolean isMouseClicked;

    private int index;
    private ArrayList<Enum<?>> enumValueList;

    private ValueEnum setting;

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

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
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);

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
    public void onKeyboard(char character, int key) {
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouse == Flag.MOUSE_OVER) {
            if (this.isMouseClicked) {
                if (this.index >= this.enumValueList.size() - 1) {
                    this.index = 0;
                } else {
                    this.index++;
                }

                this.isMouseClicked = false;
            }
        } else {
            this.isMouseClicked = false;
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.flagMouse == Flag.MOUSE_OVER) {
            this.isMouseClicked = button == 0 || button == 1;
        }
    }

    @Override
    public void onRender() {
        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        if (this.settingContainer.flagMouseRealRect == Flag.MOUSE_OVER) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        // Where the smooth animation works.
        this.alphaEffectHighlightRect = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());

        // We verify the current enum value at mode when we load the client.
        // Loop.
        if (this.isStarted) {
            if (this.enumValueList.get(this.index) != this.setting.getValue()) {
                this.setting.setValue(this.enumValueList.get(this.index));

                // We need refresh later set the value.
                this.settingContainer.onRefreshWidget();
            }
        // Init.
        } else {
            this.index = this.setting != null ? (TurokClass.getEnumByName(this.setting.getValue(), this.setting.getValue().name()) != null ? this.enumValueList.indexOf(TurokClass.getEnumByName(this.setting.getValue(), this.setting.getValue().name())) : 0) : 0;

            this.setting.setValue(this.enumValueList.get(this.index));

            this.isStarted = true;
        }

        if (this.flagMouse == Flag.MOUSE_OVER) {
            this.settingContainer.getDescriptionLabel().setText(this.setting.getDescription());

            this.settingContainer.flagDescription = Flag.MOUSE_OVER;
        }

        String name = this.rect.getTag() + ": " + this.setting.getValue().name();

        TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, name, this.rect.getX() + 2, this.rect.getY() + ((this.rect.getHeight() / 2) - (TurokFontManager.getStringHeight(Rocan.getWrapper().fontSmallWidget, this.rect.getTag()) / 2) - 1), true, new Color(255, 255, 255));

        // Outline on rect render.
        TurokRenderGL.color(Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightRect);
        TurokRenderGL.drawOutlineRect(this.rect);
    }

    @Override
    public void onCustomRender() {

    }
}