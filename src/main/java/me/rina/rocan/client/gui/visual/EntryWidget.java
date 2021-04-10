package me.rina.rocan.client.gui.visual;

import javafx.scene.input.Clipboard;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.IGUI;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.minecraft.TurokGUI;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokTick;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author SrRina
 * @since 18/03/2021 at 12:17
 **/
public class EntryWidget extends Widget implements IGUI {
    private TurokGUI master;

    private Type type = Type.TEXT;

    public enum Type {
        TEXT, DOUBLE;
    }

    private boolean isFocused;
    private boolean isScissored;
    private boolean isRendering = true;
    private boolean isSelected;

    private String text;
    private String save;
    private String split;
    private String postSplit;
    private String selected;

    private int splitIndex;
    private int selectedStartIndex;
    private int selectedEndIndex;

    private int lastKeyTyped;

    /* The offset space. */
    private float offsetX;
    private float offsetY;

    /* Scroll amount. */
    private float scroll;

    private TurokTick tick = new TurokTick();

    public int[] colorBackground = {0, 0, 0, 0};
    public int[] colorBackgroundOutline = {255, 255, 255, 100};
    public int[] colorSelectedBackground = {0, 0, 190, 255};
    public int[] colorString = {255, 255, 255, 255};
    public int[] colorSelectedString = {255, 255, 255, 255};

    /* We uses an float array to scissor rect. */
    public float[] rectScissor = {0f, 0f, 0f, 0f};
    public float[] rectSelected = {0f, 0f, 0f, 0f};

    private boolean isMouseClickedLeft;
    private boolean isMouseClickedMiddle;
    private boolean isMouseClickedRight;

    private TurokFont fontRenderer;

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public EntryWidget(TurokGUI master, String name, TurokFont fontRenderer) {
        super(name);

        this.fontRenderer = fontRenderer;

        this.text = "";
        this.save = "";

        this.master = master;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setMaster(TurokGUI master) {
        this.master = master;
    }

    public TurokGUI getMaster() {
        return master;
    }

    public void setFontRenderer(TurokFont fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    public TurokFont getFontRenderer() {
        return fontRenderer;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getSave() {
        return save;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setScissored(boolean scissored) {
        this.isScissored = scissored;
    }

    public boolean isScissored() {
        return isScissored;
    }

    public void setRendering(boolean rendering) {
        isRendering = rendering;
    }

    public boolean isRendering() {
        return isRendering;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isMouseClickedLeft() {
        return isMouseClickedLeft;
    }

    public boolean isMouseClickedMiddle() {
        return isMouseClickedMiddle;
    }

    public void setMouseClickedRight(boolean mouseClickedRight) {
        isMouseClickedRight = mouseClickedRight;
    }

    /**
     * Update split for set current show but the ticks disabled, after it you will need reset the split.
     */
    public void updateSplit() {
        this.split = "|";
        this.tick.reset();
    }

    /**
     * Reset split for fix the split seek.
     */
    public void resetSplit() {
        this.split = "";
        this.tick.reset();
    }

    public int getSplitByMouse(int x, int y) {
        int split = this.text.isEmpty() ? 0 : this.text.length();

        if (this.text.isEmpty() || this.flagMouse == Flag.MOUSE_NOT_OVER) {
            return split;
        }

        // I love this names (i, l, k)... ok ok, I wont use anymore this names.
        float diffOffset = this.rect.getX() + 2f + this.scroll;
        float w = 0;

        int count = 0;

        for (String characters : this.text.split("")) {
            float charWidth = TurokFontManager.getStringWidth(this.fontRenderer, characters);
            float offset = diffOffset + w;

            // We get offset with char width and return the count for index;
            if (x >= offset && x <= offset + charWidth) {
                split = TurokMath.clamp(count, 0, this.text.length());

                break;
            }

            w += TurokFontManager.getStringWidth(this.fontRenderer, characters);
            count++;
        }

        // Double clamp verification.
        return TurokMath.clamp(split, 0, this.text.length());
    }

    public void doRenderSplit(float x, float y) {
        if (!this.isRendering) {
            return;
        }

        /*
         * The split animation, this make the entry field get a cool animation.
         */
        if (this.tick.isPassedMS(500)) {
            TurokFontManager.render(this.fontRenderer, "|", x, y, false, new Color(this.colorString[0], this.colorString[1], this.colorString[2], this.colorString[3]));
        } else {
            TurokFontManager.render(this.fontRenderer, this.split, x, y, false, new Color(this.colorString[0], this.colorString[1], this.colorString[2], this.colorString[3]));
        }

        if (this.tick.isPassedMS(1000)) {
            this.tick.reset();
        }
    }

    public void doMouseOver(TurokMouse mouse) {
        if (!this.isRendering) {
            return;
        }

        this.flagMouse = this.rect.collideWithMouse(mouse) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
    }

    public void doMouseScroll(TurokMouse mouse) {
        if (!this.isRendering) {
            return;
        }

        float stringWidth = TurokFontManager.getStringWidth(this.fontRenderer, this.text);

        float maximumPositionText = 0;
        float minimumPositionText = this.rect.getWidth() - stringWidth - 4.5f;

        boolean isScrollLimit = stringWidth + 2f >= this.rect.getWidth();

        if (this.isFocused && this.flagMouse == Flag.MOUSE_OVER && this.master.getMouse().hasWheel() && isScrollLimit) {
            this.scroll -= this.master.getMouse().getScroll();
        }

        if (this.scroll <= minimumPositionText) {
            this.scroll = minimumPositionText;
        }

        if (this.scroll >= maximumPositionText) {
            this.scroll = maximumPositionText;
        }
    }

    @Override
    public void onClose() {
        if (!this.isRendering) {
            return;
        }
    }

    @Override
    public void onCustomClose() {
        if (!this.isRendering) {
            return;
        }
    }

    @Override
    public void onOpen() {
        if (!this.isRendering) {
            return;
        }
    }

    @Override
    public void onCustomOpen() {
        if (!this.isRendering) {
            return;
        }
    }

    @Override
    public void onKeyboard(char character, int key) {
        if (!this.isRendering) {
            return;
        }

        String cache = this.text;
        int mov = this.splitIndex;

        if (this.isFocused) {
            // Cancel typing.
            if (key == Keyboard.KEY_ESCAPE) {
                this.setFocused(false);
                this.text = this.save;
            } else if (key == Keyboard.KEY_RETURN) {
                this.setFocused(false);
            } else if (key == Keyboard.KEY_RIGHT) {
                if (cache.isEmpty() || mov == cache.length() || mov < 0) {
                    return;
                }

                this.lastKeyTyped = key;

                this.updateSplit();

                mov++;
            } else if (key == Keyboard.KEY_LEFT) {
                if (cache.isEmpty() || mov <= 0) {
                    return;
                }

                this.lastKeyTyped = key;
                this.updateSplit();

                mov--;
            } else if (key == Keyboard.KEY_END) {
                if (cache.isEmpty()) {
                    return;
                }

                this.scroll = this.rect.getWidth() - TurokFontManager.getStringWidth(this.fontRenderer, cache) - 4.5f;

                mov = cache.length();
            } else if (key == Keyboard.KEY_HOME) {
                if (cache.isEmpty()) {
                    return;
                }

                this.scroll = 0;

                mov = 0;
            } else if (key == Keyboard.KEY_DELETE) {
                if (cache.isEmpty() || mov == cache.length() || mov < 0) {
                    return;
                }

                String first = cache.substring(mov);
                String second = "";

                for (int i = 0; i < mov; i++) {
                    second += Character.toString(cache.charAt(i));
                }

                cache = second + first.substring(1);

                this.lastKeyTyped = key;
                this.updateSplit();
            } else if (key == Keyboard.KEY_BACK) {
                if (cache.isEmpty() || mov <= 0 || mov > cache.length()) {
                    return;
                }

                if (mov < cache.length()) {
                    String first = cache.substring(mov);
                    String second = "";

                    for (int i = 0; i < mov; i++) {
                        second += Character.toString(cache.charAt(i));
                    }

                    cache = StringUtils.chop(second) + first;
                } else {
                    cache = StringUtils.chop(cache);
                }

                this.lastKeyTyped = key;
                this.updateSplit();

                mov--;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_V)) {
                java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable content = clipboard.getContents(null);

                if (content != null && content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        String copied = content.getTransferData(DataFlavor.stringFlavor).toString();

                        if (mov < cache.length()) {
                            String first = cache.substring(mov);
                            String second = "";

                            for (int i = 0; i < mov; i++) {
                                second += Character.toString(cache.charAt(i));
                            }

                            cache = second + copied + first;
                        } else {
                            cache = cache + copied;
                        }

                        this.lastKeyTyped = key;
                        this.updateSplit();

                        mov += copied.length();
                    } catch (UnsupportedFlavorException | IOException exc) {
                        ChatUtil.print("Exception: " + exc);

                        exc.printStackTrace();
                    }
                }
            } else {
                if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                    String c = key == Keyboard.KEY_SPACE ? " " : Character.toString(character);

                    if (mov < cache.length()) {
                        String first = cache.substring(mov);
                        String second = "";

                        for (int i = 0; i < mov; i++) {
                            second += Character.toString(cache.charAt(i));
                        }

                        cache = second + c + first;
                    } else {
                        cache = cache + c;
                    }

                    this.lastKeyTyped = key;
                    this.updateSplit();

                    mov++;
                }
            }

            this.splitIndex = TurokMath.clamp(mov, 0, cache.length());

            if (!this.text.equals(cache)) {
                this.text = cache;
            }
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        if (!this.isRendering) {
        }
    }

    @Override
    public void onMouseReleased(int button) {
        if (!this.isRendering) {
            return;
        }

        if (this.isMouseClickedLeft) {
            this.resetSplit();
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (!this.isRendering) {
            return;
        }
    }

    @Override
    public void onMouseClicked(int button) {
        if (!this.isRendering) {
            return;
        }

        if (this.flagMouse == Flag.MOUSE_OVER && button == 0) {
            this.updateSplit();

            this.isMouseClickedLeft = true;
            this.splitIndex = this.master != null ? this.getSplitByMouse(this.master.getMouse().getX(), this.master.getMouse().getY()) : this.text.length();
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (!this.isRendering) {
            return;
        }
    }

    @Override
    public void onRender() {
        if (!this.isRendering) {
            return;
        }

        if (this.isScissored) {
            TurokShaderGL.pushScissor();
            TurokShaderGL.drawScissor(this.rectScissor[0], this.rectScissor[1], this.rectScissor[2], this.rectScissor[3]);
        }

        this.offsetY = (this.rect.getHeight() - TurokFontManager.getStringHeight(this.fontRenderer, this.text)) / 2;
        this.scroll = TurokMath.lerp(this.scroll,this.scroll + this.offsetX, this.master.getPartialTicks());

        float x = this.rect.getX() + 2f + this.scroll;
        float y = this.rect.getY() + this.offsetY;

        int w = 0;
        int i = 1;
        int k = 1;

        if (this.isFocused) {
            TurokShaderGL.drawSolidRect(this.rect, this.colorBackground);

            if (this.colorBackgroundOutline[3] > 0) {
                TurokShaderGL.drawOutlineRect(this.rect, this.colorBackgroundOutline);
            }

            if (this.lastKeyTyped != -1 && Keyboard.isKeyDown(this.lastKeyTyped) == false) {
                this.resetSplit();
                this.lastKeyTyped = -1;
            }
        } else {
            this.resetSplit();
            this.save = this.text;
        }

        if (this.text.isEmpty()) {
            if (this.isFocused) {
                this.doRenderSplit(x - 0.5f, y);
            }
        } else {
            for (String characters : this.text.split("")) {
                String c = characters;

                int charWidth = TurokFontManager.getStringWidth(this.fontRenderer, c);

                /*
                 * Verify index position and render it, but we need make it true selected.
                 */
                if (i >= selectedStartIndex && i <= selectedEndIndex && this.isSelected && this.isFocused) {
                    TurokShaderGL.drawSolidRect(x + w, y, charWidth, this.rect.getHeight(), this.colorBackground);
                    TurokFontManager.render(this.fontRenderer, characters, x + w, y, false, new Color(this.colorSelectedString[0], this.colorSelectedString[1], this.colorSelectedString[2], this.colorSelectedString[3]));
                } else {
                    TurokFontManager.render(this.fontRenderer, c, x + w, y, false, new Color(this.colorString[0], this.colorString[1], this.colorString[2], this.colorString[3]));

                    if (this.isFocused) {
                        if (this.splitIndex == 0) {
                            this.doRenderSplit(x - 0.5f, y);
                        } else {
                            if (i == this.splitIndex) {
                                this.doRenderSplit(x + w + (charWidth - 2f), y);
                            }
                        }
                    }
                }

                w += charWidth;
                i++;
            }
        }

        if (this.isScissored) {
            TurokShaderGL.popScissor();
        }
    }

    @Override
    public void onCustomRender() {
    }
}
