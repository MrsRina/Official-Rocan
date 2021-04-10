package me.rina.rocan.client.gui.visual;

import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.minecraft.TurokGUI;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokShaderGL;

import java.awt.*;

/**
 * @author SrRina
 * @since 01/04/2021 at 10:22
 **/
public class ButtonWidget extends Widget {
    private Type type = Type.RELEASE;

    public enum Type {
        INSTANT, RELEASE;
    }

    private TurokGUI master;
    private TurokFont fontRenderer;

    private String title;
    private boolean isEnabled;
    private boolean isRendering = true;

    private float offsetX;
    private float offsetY;

    public int[] colorBackground = {0, 0, 0, 0};
    public int[] colorBackgroundOutline = {255, 255, 255, 100};
    public int[] colorBackgroundEnabled = {0, 0, 190, 255};
    public int[] colorString = {255, 255, 255, 255};

    private boolean isMouseClickedLeft;
    private boolean isMouseClickedMiddle;
    private boolean isMouseClickedRight;

    /* Flags. */
    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public ButtonWidget(TurokGUI master, TurokFont fontRenderer, String title, boolean value) {
        super(title);

        this.master = master;
        this.fontRenderer = fontRenderer;

        this.title = title;
        this.isEnabled = value;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setRendering(boolean rendering) {
        isRendering = rendering;
    }

    public boolean isRendering() {
        return isRendering;
    }

    public void setFontRenderer(TurokFont fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    public TurokFont getFontRenderer() {
        return fontRenderer;
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

    public void doMouseOver(TurokMouse mouse) {
        if (!this.isRendering) {
            return;
        }

        this.flagMouse = this.rect.collideWithMouse(mouse) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onCustomClose() {

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onCustomOpen() {

    }

    @Override
    public void onKeyboard(char character, int key) {

    }

    @Override
    public void onCustomKeyboard(char character, int key) {

    }

    @Override
    public void onMouseReleased(int button) {
        if (this.isMouseClickedLeft || this.isMouseClickedRight || this.isMouseClickedMiddle) {
            if (!this.isMouseClickedMiddle && this.type == Type.RELEASE) {
                this.isEnabled = !this.isEnabled;
            }

            this.isMouseClickedLeft = false;
            this.isMouseClickedRight = false;
            this.isMouseClickedMiddle = false;
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flagMouse == Flag.MOUSE_OVER) {
            this.isMouseClickedLeft = button == 0;
            this.isMouseClickedRight = button == 1;
            this.isMouseClickedMiddle = button == 2;

            if (this.type == Type.INSTANT && (this.isMouseClickedLeft || this.isMouseClickedRight)) {
                this.isEnabled = !this.isEnabled;
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        if (!this.isRendering) {
            return;
        }

        this.offsetX = 1f;
        this.offsetY = (this.rect.getHeight() - TurokFontManager.getStringHeight(this.fontRenderer, this.title)) / 2;

        if (this.isEnabled) {
            TurokShaderGL.drawSolidRect(this.rect, this.colorBackgroundEnabled);
        } else {
            TurokShaderGL.drawSolidRect(this.rect, this.colorBackground);
        }

        // Highlight effect.
        if (this.colorBackgroundOutline[3] > 0) {
            TurokShaderGL.drawOutlineRect(this.rect, this.colorBackgroundOutline);
        }

        // Title.
        TurokFontManager.render(this.fontRenderer, this.title, this.rect.getX() + this.offsetX, this.rect.getY() + this.offsetY, false, new Color(this.colorString[0], this.colorString[1], this.colorString[2], this.colorString[3]));
    }

    @Override
    public void onCustomRender() {

    }
}
