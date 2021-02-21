package me.rina.rocan.client.gui.module.client.widget;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.client.container.ClientContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
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
 * @since 25/01/2021 at 19:36
 **/
public class SearchModuleWidget extends Widget {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widgetCategory;
    private ModuleWidget widgetModule;

    private ClientContainer clientContainer;

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

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;
    public Flag flagMouseEntry = Flag.MOUSE_NOT_OVER;

    public SearchModuleWidget(ModuleClickGUI master, MotherFrame frame, ClientContainer clientContainer) {
        super("");

        this.master = master;
        this.frame = frame;

        this.clientContainer = clientContainer;

        this.rect.setWidth(this.clientContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);
    }

    public String removeLastChar(String string) {
        String str = StringUtils.chop(string);

        return str;
    }

    public void cancel() {
        if (this.clientContainer.getModuleContainer() != null) {
            this.clientContainer.getModuleContainer().refreshSearchWidget(this.rect.getTag());
        }

        this.isAllSelected = false;
        this.isFocused = false;
    }

    public void cancelSet() {
        if (this.clientContainer.getModuleContainer() != null) {
            this.clientContainer.getModuleContainer().refreshSearchWidget(this.cacheType.getValue());
        }

        this.rect.setTag(this.cacheType.getValue());

        this.isAllSelected = false;
        this.isFocused = false;
    }

    public void setRectEntryBox(TurokRect rectEntryBox) {
        this.rectEntryBox = rectEntryBox;
    }

    public TurokRect getRectEntryBox() {
        return rectEntryBox;
    }

    public void setWidgetModule(ModuleWidget widgetModule) {
        this.widgetModule = widgetModule;
    }

    public ModuleCategoryWidget getWidgetCategory() {
        return widgetCategory;
    }

    public void setWidgetCategory(ModuleCategoryWidget widgetCategory) {
        this.widgetCategory = widgetCategory;
    }

    public ModuleWidget getWidgetModule() {
        return widgetModule;
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

                        if (this.clientContainer.getModuleContainer() != null) {
                            this.clientContainer.getModuleContainer().refreshSearchWidget(this.cacheType.getValue());
                        }
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

                        if (this.clientContainer.getModuleContainer() != null) {
                            this.clientContainer.getModuleContainer().refreshSearchWidget(this.cacheType.getValue());
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

                            if (this.clientContainer.getModuleContainer() != null) {
                                this.clientContainer.getModuleContainer().refreshSearchWidget(this.cacheType.getValue());
                            }

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
    public void onMouseReleased(int button) {
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
        if (this.clientContainer.flagMouse == Flag.MOUSE_NOT_OVER && this.rect.getTag().isEmpty() == false) {
            this.clientContainer.getModuleContainer().refreshSearchWidget("");
            this.rect.setTag("");

            this.isFocused = false;
        }

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
        this.rect.setWidth(this.clientContainer.getRect().getWidth());
        this.rect.setHeight(5 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) + 5);

        this.rect.setX(this.clientContainer.getScrollRect().getX() + this.offsetX);
        this.rect.setY(this.clientContainer.getScrollRect().getY() + this.offsetY);

        float offsetEntryBox = 2f;

        this.rectEntryBox.setX(this.rect.getX() + offsetEntryBox);
        this.rectEntryBox.setY(this.rect.getY() + offsetEntryBox);

        this.rectEntryBox.setWidth(this.rect.getWidth() - (offsetEntryBox * 2));
        this.rectEntryBox.setHeight(this.rect.getHeight() - (offsetEntryBox * 2));

        this.alphaEffectHighlightEntryBox = this.flagMouseEntry == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightEntryBox, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightEntryBox, 0, this.master.getPartialTicks());
        this.alphaEffectHighlightRect = this.flagMouse == Flag.MOUSE_OVER ? (int) TurokMath.lerp(this.alphaEffectHighlightRect, Rocan.getWrapper().colorWidgetHighlight[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectHighlightRect, 0, this.master.getPartialTicks());

        float offsetSpace = 1.0f;

        if (this.clientContainer.flagMouse == Flag.MOUSE_OVER) {
            this.flagMouseEntry = this.rectEntryBox.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouseEntry = Flag.MOUSE_NOT_OVER;
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }

        // The outline rect effect.
        TurokRenderGL.color(Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightRect);
        TurokRenderGL.drawOutlineRect(this.rect);

        // The check box outline highlight.
        TurokRenderGL.color(Rocan.getWrapper().colorWidgetHighlight[0], Rocan.getWrapper().colorWidgetHighlight[1], Rocan.getWrapper().colorWidgetHighlight[2], this.alphaEffectHighlightEntryBox);
        TurokRenderGL.drawOutlineRect(this.rectEntryBox);

        // The typing solid effect.
        TurokRenderGL.color(255, 255, 255, this.alphaEffectPressed);
        TurokRenderGL.drawSolidRect(this.rectEntryBox);

        // Push scissor test.
        TurokShaderGL.pushScissorMatrix();
        TurokShaderGL.drawScissor(this.rectEntryBox.getX() + 0.5f, this.rectEntryBox.getY(), this.rectEntryBox.getWidth() - (0.5f), this.rectEntryBox.getHeight());

        // The selected solid effect.
        TurokRenderGL.color(0, 0, 255, this.isAllSelected ? this.alphaEffectPressed : 0);
        TurokRenderGL.drawSolidRect(this.rectEntryBox.getX(), this.rectEntryBox.getY(), offsetSpace + TurokFontManager.getStringWidth(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue()), this.rectEntryBox.getHeight());

        this.stringPositionX = TurokMath.lerp(this.stringPositionX, this.rectEntryBox.getX() + offsetSpace + this.offsetPositionTextX, this.master.getPartialTicks());
        this.stringPositionY = this.rectEntryBox.getY() + (this.rectEntryBox.getHeight() / 2 - (TurokFontManager.getStringHeight(Rocan.getWrapper().fontSmallWidget, "AaBbCc") / 2));

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

            TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, this.cacheType.getValue() + this.split, this.stringPositionX, this.stringPositionY, true, new Color(255, 255, 255));
        } else {
            this.master.setCanceledCloseGUI(false);
            this.cacheType.setValue(this.rect.getTag());

            if (this.flagMouseEntry == Flag.MOUSE_OVER) {
                TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, this.rect.getTag(), this.rectEntryBox.getX() + offsetSpace, this.stringPositionY, true, new Color(255, 255, 255));
            } else {
                TurokFontManager.render(Rocan.getWrapper().fontSmallWidget, "Search " + this.rect.getTag(), this.rectEntryBox.getX() + offsetSpace, this.stringPositionY, true, new Color(255, 255, 255, 100));
            }

            this.isAllSelected = false;
        }

        TurokShaderGL.popScissorMatrix();

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
    }

    @Override
    public void onCustomRender() {
        // We place here cause we need sync the focus animation clicks of entry box
        this.alphaEffectPressed = this.isFocused ? (int) TurokMath.lerp(this.alphaEffectPressed, Rocan.getWrapper().colorWidgetPressed[3], this.master.getPartialTicks()) : (int) TurokMath.lerp(this.alphaEffectPressed, 0, this.master.getPartialTicks());
    }
}