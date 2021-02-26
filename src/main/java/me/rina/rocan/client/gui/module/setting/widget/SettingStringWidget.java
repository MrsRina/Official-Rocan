package me.rina.rocan.client.gui.module.setting.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.value.ValueString;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.container.SettingContainer;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokGeneric;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import me.rina.turok.util.TurokTick;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

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

    private float offsetPositionTextX;

    private float stringPositionX;
    private float stringPositionY;

    private int alphaEffectPressed;
    private int alphaEffectHighlightEntryBox;
    private int alphaEffectHighlightRect;

    private boolean isMouseClickedLeft;
    private boolean isTyping;
    private boolean isAllSelected;
    private boolean isFocused;

    private char lastTypedCharacter;
    private String split;

    private TurokGeneric<String> cacheType = new TurokGeneric<String>("");
    private TurokRect rectEntryBox = new TurokRect("EntryBox", 0, 0);
    private TurokTick tickAnimationSplit = new TurokTick();

    private ValueString setting;

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;
    public Flag flagMouseEntry = Flag.MOUSE_NOT_OVER;

    public SettingStringWidget(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer moduleContainer, ModuleWidget widgetModule, SettingContainer settingContainer, ValueString setting) {
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
    }

    public String removeLastChar(String string) {
        String str = StringUtils.chop(string);

        return str;
    }

    public void cancel() {
        this.isAllSelected = false;
        this.isFocused = false;
    }

    public void cancelSet() {
        this.setting.setValue(this.cacheType.getValue());

        this.isAllSelected = false;
        this.isFocused = false;
    }

    public void setRectEntryBox(TurokRect rectEntryBox) {
        this.rectEntryBox = rectEntryBox;
    }

    public TurokRect getRectEntryBox() {
        return rectEntryBox;
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
        this.isFocused = false;
    }

    @Override
    public void onOpen() {
        this.isFocused = false;
    }

    @Override
    public void onKeyboard(char character, int key) {
        if (this.isFocused) {
            if (key == Keyboard.KEY_ESCAPE) {
                this.cancel();
            }
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        if (this.isFocused) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_V)) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable content = clipboard.getContents(null);

                if (content != null && content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        if (this.isAllSelected) {
                            this.cacheType.setValue("");

                            this.isAllSelected = false;
                        }

                        this.cacheType.setValue(this.cacheType.getValue() + content.getTransferData(DataFlavor.stringFlavor));
                    } catch (UnsupportedFlavorException | IOException exc) {
                        ChatUtil.print("Exception: " + exc);

                        exc.printStackTrace();
                    }

                    boolean isScrollLimit = TurokFontManager.getStringWidth(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue()) + 1f >= this.rectEntryBox.getWidth();

                    if (isScrollLimit) {
                        this.offsetPositionTextX = this.rectEntryBox.getWidth() - TurokFontManager.getStringWidth(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue()) - 7;
                    }
                }
            } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
                if (this.cacheType.getValue().isEmpty() == false) {
                    this.isAllSelected = true;
                }
            } else {
                switch (key) {
                    // Cancel typing.
                    case Keyboard.KEY_ESCAPE: {
                        this.cancel();

                        break;
                    }

                    // Set the new string.
                    case Keyboard.KEY_RETURN: {
                        this.cancelSet();

                        break;
                    }

                    // Delete last char in string.
                    case Keyboard.KEY_BACK: {
                        this.cacheType.setValue(this.isAllSelected ? "" : this.removeLastChar(this.cacheType.getValue()));

                        if (this.isAllSelected) {
                            this.isAllSelected = false;
                        }

                        break;
                    }

                    default: {
                        // Some characters are not allowed at Minecraft.
                        if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                            if (this.isAllSelected) {
                                this.cacheType.setValue("");

                                this.isAllSelected = false;
                            }

                            this.lastTypedCharacter = character;

                            this.cacheType.setValue(this.cacheType.getValue() + this.lastTypedCharacter);

                            boolean isScrollLimit = TurokFontManager.getStringWidth(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue()) + 1f >= this.rectEntryBox.getWidth();

                            if (isScrollLimit) {
                                this.offsetPositionTextX = this.rectEntryBox.getWidth() - TurokFontManager.getStringWidth(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue()) - 7;
                            }
                        }

                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouseEntry == Flag.MOUSE_OVER) {
            if (this.isMouseClickedLeft) {
                this.isMouseClickedLeft = false;
            }
        } else {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flagMouseEntry == Flag.MOUSE_NOT_OVER && this.isFocused) {
            this.cancelSet();

            if (this.master.isCanceledCloseGUI()) {
                this.master.setCanceledCloseGUI(false);
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.flagMouseEntry == Flag.MOUSE_OVER) {
            this.isAllSelected = false;

            if (button == 0) {
                this.isFocused = true;
                this.isMouseClickedLeft = true;
            }
        } else {
            if (this.isFocused) {
                this.cancelSet();
            }

            if (this.master.isCanceledCloseGUI()) {
                this.master.setCanceledCloseGUI(false);
            }
        }
    }

    @Override
    public void onRender() {
        this.rect.setWidth(this.settingContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.settingContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.settingContainer.getScrollRect().getY() + this.offsetY);

        float offsetEntryBox = 2f;

        this.rectEntryBox.setX(this.rect.getX() + offsetEntryBox);
        this.rectEntryBox.setY(this.rect.getY() + offsetEntryBox);

        this.rectEntryBox.setWidth(this.rect.getWidth() - (offsetEntryBox * 2));
        this.rectEntryBox.setHeight(this.rect.getHeight() - (offsetEntryBox * 2));

        this.alphaEffectHighlightEntryBox = this.flagMouseEntry == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightEntryBox, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightEntryBox, 0, this.master.getPartialTicks());
        this.alphaEffectHighlightRect = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());

        float offsetSpace = 1.0f;

        if (this.settingContainer.flagMouseRealRect == Flag.MOUSE_OVER) {
            this.flagMouseEntry = this.rectEntryBox.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouseEntry = Flag.MOUSE_NOT_OVER;
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        // The outline rect effect.
        TurokShaderGL.drawOutlineRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightRect});

        // The check box outline highlight.
        TurokShaderGL.drawOutlineRect(this.rectEntryBox, new int[] {Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightEntryBox});

        // The typing solid effect.
        TurokShaderGL.drawSolidRect(this.rectEntryBox, new int[] {255, 255, 255, this.alphaEffectPressed});

        // The selected solid effect.
        TurokShaderGL.drawSolidRect(this.rectEntryBox.getX(), this.rectEntryBox.getY(), offsetSpace + TurokFontManager.getStringWidth(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue()), this.rectEntryBox.getHeight(), new int[] {0, 0, 255, this.isAllSelected ? this.alphaEffectPressed : 0});

        this.stringPositionX = TurokMath.lerp(this.stringPositionX, this.rectEntryBox.getX() + offsetSpace + this.offsetPositionTextX, this.master.getPartialTicks());
        this.stringPositionY = this.rectEntryBox.getY() + (this.rectEntryBox.getHeight() / 2 - (TurokFontManager.getStringHeight(Rocan.getWrapper().fontSmallWidget, "AaBbCc") / 2));

        // We push the scissor.
        TurokShaderGL.drawScissor(this.rectEntryBox.getX() + 0.5f, this.rectEntryBox.getY(), this.rectEntryBox.getWidth() - (0.5f), this.rectEntryBox.getHeight());

        if (this.isFocused) {
            this.master.setCanceledCloseGUI(true);

            /*
             * The split animation, this make the entry field get a cool animation.
             */
            if (this.tickAnimationSplit.isPassedMS(500)) {
                this.split = "_";
            } else {
                this.split = "";
            }

            if (this.tickAnimationSplit.isPassedMS(1000)) {
                this.tickAnimationSplit.reset();
            }

            TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue() + (this.isAllSelected ? "" : this.split), this.stringPositionX, this.stringPositionY, true, new Color(255, 255, 255));
        } else {
            this.cacheType.setValue(this.setting.getValue());
            this.isAllSelected = false;

            String currentFormat = this.setting.getValue().isEmpty() ? this.setting.getFormat() : this.setting.getValue();

            if (this.flagMouseEntry == Flag.MOUSE_OVER) {
                TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, this.setting.getValue(), this.rectEntryBox.getX() + offsetSpace, this.stringPositionY, true, new Color(255, 255, 255));
            } else {
                TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, this.rect.getTag() + " " + currentFormat, this.rectEntryBox.getX() + offsetSpace, this.stringPositionY, true, new Color(255, 255, 255, 100));
            }
        }

        this.isTyping = false;

        float stringWidth = TurokFontManager.getStringWidth(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue());

        float maximumPositionText = 0;
        float minimumPositionText = this.rectEntryBox.getWidth() - stringWidth - 7;

        boolean isScrollLimit = stringWidth + offsetSpace >= this.rectEntryBox.getWidth();

        if (this.isFocused && this.flagMouseEntry == Flag.MOUSE_OVER && this.master.getMouse().hasWheel() && isScrollLimit) {
            this.offsetPositionTextX += this.master.getMouse().getScroll();
        }

        if (this.offsetPositionTextX <= minimumPositionText) {
            this.offsetPositionTextX = minimumPositionText;
        }

        if (this.offsetPositionTextX >= maximumPositionText) {
            this.offsetPositionTextX = maximumPositionText;
        }

        if (this.flagMouse == Flag.MOUSE_OVER) {
            this.settingContainer.getDescriptionLabel().setText(this.setting.getDescription());

            this.settingContainer.flagDescription = Flag.MOUSE_OVER;
        }
    }

    @Override
    public void onCustomRender() {
        // We place here cause we need sync the focus animation clicks of entry box
        this.alphaEffectPressed = this.isFocused ? (int) TurokMath.lerp(this.alphaEffectPressed, Rocan.getWrapper().colorWidgetPressed[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectPressed, 0, this.master.getPartialTicks());
    }
}
