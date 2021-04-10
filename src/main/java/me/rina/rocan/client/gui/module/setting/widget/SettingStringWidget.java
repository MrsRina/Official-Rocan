package me.rina.rocan.client.gui.module.setting.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.value.ValueString;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.rocan.client.gui.visual.EntryWidget;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokMath;
import org.lwjgl.input.Keyboard;

/**
 * @author SrRina
 * @since 21/01/2021 at 14:07
 **/
public class SettingStringWidget extends Widget {
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

    private int alphaEffectPressed;
    private int alphaEffectHighlightRect;

    private boolean isMouseClickedLeft;
    private ValueString setting;

    private EntryWidget entry;

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public SettingStringWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, ValueString setting) {
        super(setting.getName());

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.moduleContainer = moduleContainer;

        this.widgetModule = widgetModule;
        this.settingContainer = settingContainer;

        this.setting = setting;
        this.entry = new EntryWidget(this.master, "Entry Field", Rocan.getWrapper().fontNormalWidget);

        this.entry.setText(this.setting.getValue());

        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);
    }

    public void setSetting(ValueString setting) {
        this.setting = setting;
    }

    public ValueString getSetting() {
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
        this.entry.onKeyboard(character, key);
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        switch (key) {
            // Cancel typing.
            case Keyboard.KEY_ESCAPE: {
                if (this.master.isCanceledCloseGUI()) {
                    this.master.setCanceledCloseGUI(false);
                }

                break;
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
    }

    @Override
    public void onCustomMouseReleased(int button) {
        this.entry.onMouseReleased(button);
    }

    @Override
    public void onMouseClicked(int button) {
        this.entry.onMouseClicked(button);

        if (button == 0 && this.entry.flagMouse == Flag.MOUSE_OVER && !this.entry.isFocused()) {
            this.entry.setFocused(true);
        }

        if (this.entry.flagMouse == Flag.MOUSE_NOT_OVER && this.entry.isFocused()) {
            this.entry.setFocused(false);

            if (this.master.isCanceledCloseGUI()) {
                this.master.setCanceledCloseGUI(false);
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        this.entry.getRect().copy(this.rect);

        this.alphaEffectHighlightRect = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());

        if (this.settingContainer.flagMouseRealRect == Flag.MOUSE_OVER) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;

            // Update entry context.
            this.entry.doMouseOver(this.master.getMouse());
        } else {
            this.flagMouse = Flag.MOUSE_NOT_OVER;
            this.entry.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        if (this.entry.isFocused()) {
            if (this.entry.flagMouse == Flag.MOUSE_OVER) {
                this.entry.doMouseScroll(this.master.getMouse());
                this.settingContainer.setScrolling(false);
            }

            this.master.setCanceledCloseGUI(true);
        }

        this.entry.setScissored(true);
        this.entry.setRendering(this.widgetModule.isSelected());

        this.entry.rectScissor = new float[] {
                this.entry.getRect().getX(), (this.settingContainer.getRect().getY() + (this.settingContainer.getDescriptionLabel().getRect().getHeight() + 1)), (int) this.entry.getRect().getWidth(), this.settingContainer.getRect().getHeight() - (this.settingContainer.getDescriptionLabel().getRect().getHeight() + 1)
        };

        this.entry.onRender();

        // The outline rect effect.
        this.entry.colorBackgroundOutline = new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightRect};

        if (this.flagMouse == Flag.MOUSE_OVER) {
            this.settingContainer.getDescriptionLabel().setText(this.setting.getDescription());
            this.settingContainer.flagDescription = Flag.MOUSE_OVER;
        }

        this.setting.setValue(this.entry.getText());
    }

    @Override
    public void onCustomRender() {
        // We place here cause we need sync the focus animation clicks of entry box
        this.alphaEffectPressed = this.entry.isFocused() ? (int) TurokMath.lerp(this.alphaEffectPressed, Rocan.getWrapper().colorWidgetPressed[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectPressed, 0, this.master.getPartialTicks());
    }
}
