package me.rina.rocan.client.gui.module.UI.category;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.EnumFlagState;
import me.rina.rocan.api.gui.element.widget.Widget;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import me.rina.rocan.api.util.render.ElementUtil;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.UI.MainFrameUI;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokShaderGL;

import java.awt.*;

/**
 * @author SrRina
 * @since 09/03/2021 at 12:56
 **/
public class WidgetCategory extends Widget {
    private ModuleClickGUI master;
    private ModuleCategory category;

    private MainFrameUI mainFrameUI;

    private boolean isSelected;
    private boolean isEnabled;

    private int alphaEffectSelected;

    /* Drag positions. */
    private float dragX;
    private float dragY;

    /* Flags. */
    public EnumFlagState flagMouse = EnumFlagState.MOUSE_NOT_OVER;

    public EnumFlagState flagMouseMouseClickedLeft = EnumFlagState.UNPRESSED;
    public EnumFlagState flagMouseMouseClickedMiddle = EnumFlagState.UNPRESSED;
    public EnumFlagState flagMouseMouseClickedRight = EnumFlagState.UNPRESSED;

    public EnumFlagState flagDragging = EnumFlagState.STABLE;
    public EnumFlagState flagDraggable = EnumFlagState.LOCKED;

    public EnumFlagState flagResizable = EnumFlagState.STATIC;
    public EnumFlagState flagResizing = EnumFlagState.RESIZABLE;

    public WidgetCategory(ModuleClickGUI master, ModuleCategory category) {
        super(category.name());

        this.master = master;
        this.category = category;

        this.onUpdateSize();
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setMainFrameUI(MainFrameUI mainFrameUI) {
        this.mainFrameUI = mainFrameUI;
    }

    public MainFrameUI getMainFrameUI() {
        return mainFrameUI;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void onUpdateSize() {
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()));
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
        if (this.flagMouseMouseClickedLeft == EnumFlagState.PRESSED) {
            if (this.isSelected == false) {
                this.isSelected = true;

                if (this.mainFrameUI != null) {
                    this.mainFrameUI.onRefreshWidgetCategoryListSelected(this.rect.getTag());
                }
            }

            if (this.flagDragging == EnumFlagState.DRAGGING) {
                this.flagDragging = EnumFlagState.STABLE;
            }
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flagMouse == EnumFlagState.MOUSE_OVERING) {
            EnumFlagState flag = ElementUtil.getMousePressed(0, button);

            if (ElementUtil.isPressed(flag) != false) {
                // Gets drags stuff.
                this.dragX = this.master.getMouse().getX() - this.rect.getX();
                this.dragY = this.master.getMouse().getY() - this.rect.getY();

                this.flagDraggable = ElementUtil.getDragging(this.flagDraggable);
                this.flagMouseMouseClickedLeft = flag;
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
    }

    @Override
    public void onRender() {
        this.flagMouse = ElementUtil.getMouseOver(this.rect.collideWithMouse(this.master.getMouse()));

        // Alpha effect.
        this.alphaEffectSelected = (int) ElementUtil.linearComparator(this.alphaEffectSelected, Rocan.getWrapper().colorWidgetPressed[3], 0, this.isSelected, this.master.getPartialTicks());

        /*
         * Update the normal size of category.
         */
        this.onUpdateSize();

        if (this.mainFrameUI != null) {
            this.rect.setX(this.mainFrameUI.getRect().getX() + this.offsetX);
            this.rect.setY(this.mainFrameUI.getRect().getY() + this.offsetY);
        }

        if (this.flagDragging == EnumFlagState.DRAGGING) {
            float draggingX = this.master.getMouse().getX() - this.dragX;
            float draggingY = this.master.getMouse().getY() - this.dragY;

            this.rect.setX(draggingX);
            this.rect.setY(draggingY);
        }

        if (this.isSelected) {
            ChatUtil.print(this.category.name() + " was selected...");
        }

        // Draw the category widget.
        TurokShaderGL.drawSolidRect(this.rect, new int[] {Rocan.getWrapper().colorWidgetBackground[0], Rocan.getWrapper().colorWidgetBackground[1], Rocan.getWrapper().colorWidgetBackground[2], this.alphaEffectSelected});

        // Draw String.
        TurokFontManager.render(Rocan.getWrapper().fontNormalWidget, this.rect.getTag(), this.rect.getX() + ((this.rect.getWidth() / 2) - (TurokFontManager.getStringWidth(Rocan.getWrapper().fontNormalWidget, this.rect.getTag()) / 2)), this.rect.getY() + 6, true, new Color(255, 255, 255, 255));
    }

    @Override
    public void onCustomRender() {
    }
}
