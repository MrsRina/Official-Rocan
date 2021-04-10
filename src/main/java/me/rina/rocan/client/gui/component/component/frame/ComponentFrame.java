package me.rina.rocan.client.gui.component.component.frame;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.gui.component.ComponentClickGUI;
import me.rina.turok.render.opengl.TurokShaderGL;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * @author SrRina
 * @since 27/03/2021 at 00:00
 **/
public class ComponentFrame extends Frame {
    private ComponentClickGUI master;
    private Component component;

    private boolean isMouseClickedLeft;
    private boolean isMouseClickedRight;
    private boolean isMouseClickedMiddle;

    private boolean isStarted = true;
    private boolean isSelected;

    private float dragX;
    private float dragY;

    private ComponentPopupFrame popup;

    /* Flags. */
    public Flag flagMouse = Flag.MOUSE_NOT_OVER;

    public ComponentFrame(ComponentClickGUI master, Component component) {
        super(component.getTag());

        this.master = master;
        this.component = component;

        this.popup = new ComponentPopupFrame(this.master, this);
    }

    public void setPopup(ComponentPopupFrame popup) {
        this.popup = popup;
    }

    public ComponentPopupFrame getPopup() {
        return popup;
    }

    public ComponentClickGUI getMaster() {
        return master;
    }

    public Component getComponent() {
        return component;
    }

    public boolean isMouseClickedLeft() {
        return isMouseClickedLeft;
    }

    public void setMouseClickedRight(boolean mouseClickedRight) {
        isMouseClickedRight = mouseClickedRight;
    }

    public boolean isMouseClickedMiddle() {
        return isMouseClickedMiddle;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void onClose() {
        this.popup.onClose();

        this.isMouseClickedLeft = false;
        this.isMouseClickedRight = false;
        this.isMouseClickedMiddle = false;
        this.component.setDragging(false);
    }

    @Override
    public void onCustomClose() {
        this.popup.onCustomClose();

        this.isMouseClickedLeft = false;
        this.isMouseClickedRight = false;
        this.isMouseClickedMiddle = false;
        this.component.setDragging(false);
    }

    @Override
    public void onOpen() {
        this.popup.onOpen();

        this.isMouseClickedLeft = false;
        this.isMouseClickedRight = false;
        this.component.setDragging(false);
        this.isMouseClickedMiddle = false;
    }

    @Override
    public void onCustomOpen() {
        this.popup.onCustomOpen();

        this.isMouseClickedLeft = false;
        this.isMouseClickedRight = false;
        this.component.setDragging(false);
        this.isMouseClickedMiddle = false;
    }

    @Override
    public void onKeyboard(char character, int key) {
        if (this.master.isCancelledToCloseGUI()) {
            this.master.setCancelledToCloseGUI(false);
        }

        this.popup.onKeyboard(character, key);

        if (this.isSelected) {
            if (key == Keyboard.KEY_ESCAPE) {
                this.isSelected = false;
                this.isMouseClickedRight = false;
                this.isMouseClickedLeft = false;

                if (this.master.isCancelledToCloseGUI()) {
                    this.master.setCancelledToCloseGUI(false);
                }
            } else if (key == Keyboard.KEY_DELETE) {
                this.component.setEnabled(false);
                this.popup.setOpened(false);

                this.isSelected = false;
                this.isMouseClickedRight = false;
                this.isMouseClickedLeft = false;

                if (this.master.isCancelledToCloseGUI()) {
                    this.master.setCancelledToCloseGUI(false);
                }
            }
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        this.popup.onCustomKeyboard(character, key);
    }

    @Override
    public void onMouseReleased(int button) {
        this.popup.onMouseReleased(button);

        if (this.isMouseClickedLeft) {
            this.isMouseClickedLeft = false;
        }

        if (this.isMouseClickedRight) {
            this.isMouseClickedRight = false;
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (!this.component.isEnabled()) {
            return;
        }

        if (this.master.isCancelledToCloseGUI()) {
            this.master.setCancelledToCloseGUI(false);
        }

        if (!(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
            if (!this.master.isPopUpOpened() && !this.master.isMouseOverComponent()) {
                if (!this.master.isPostSelected() || (this.flagMouse == Flag.MOUSE_OVER && !this.isSelected && this.master.isPostSelected())) {
                    this.master.doResetAllPopUps();
                    this.master.setPostSelected(false);
                    this.master.getRectSelected().set(12000, 12000, 0, 0);
                }
            }
        }

        // TODO: Fix the select in this shit!!!! good night (from rina past!)
        if ((button == 0 || button == 1) && (this.flagMouse == Flag.MOUSE_OVER || (this.isSelected && (((this.master.getFrameOver() != null && this.master.getFrameOver().getRect().collideWithRect(this.master.getRectSelected()) || (this.master.isPostSelected() && this.master.getFrameOver() != null && this.master.getFrameOver() instanceof ComponentFrame && ((ComponentFrame) this.master.getFrameOver()).isSelected()))) || ((this.master.getFrameOver() != null && this.master.getFrameOver() instanceof ComponentFrame && ((ComponentFrame) this.master.getFrameOver()).isSelected()) && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))))))) {
            if (!this.isSelected) {
                this.isSelected = true;
            }

            if (button == 0) {
                this.dragX = this.master.getMouse().getX() - this.component.getRect().getX();
                this.dragY = this.master.getMouse().getY() - this.component.getRect().getY();

                this.isMouseClickedLeft = true;
            } else {
                this.isMouseClickedRight = true;
            }
        }

        this.popup.onMouseClicked(button);
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        if (this.component.isEnabled()) {
            TurokShaderGL.drawSolidRect(this.component.getRect(), new int[] {0, 0, 0, 50});

            if (this.flagMouse == Flag.MOUSE_OVER && !this.master.isMouseClickedSelect() && !this.isMouseClickedLeft && !this.master.isPopUpOpened()) {
                TurokShaderGL.drawSolidRect(this.component.getRect(), new int[] {190, 190, 190, 50});
            }

            if (this.master.isMouseClickedSelect() && this.master.isState()) {
                if (!(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
                    this.isSelected = this.master.getRectSelected().collideWithRect(this.rect);
                } else {
                    if (!this.isSelected) {
                        this.isSelected = this.master.getRectSelected().collideWithRect(this.rect);
                    }
                }
            }

            if (this.isSelected && !this.popup.isOpened()) {
                TurokShaderGL.drawSolidRect(this.component.getRect(), new int[] {45, 95, 241, 100});

                this.master.setCancelledToCloseGUI(true);
            }

            this.component.onRenderHUD(this.master.getPartialTicks());

            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);

            GlStateManager.enableBlend();

            GL11.glPopMatrix();

            GlStateManager.enableCull();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();

            if (this.isMouseClickedLeft && this.master.isState()) {
                this.component.getRect().setX(this.master.getMouse().getX() - this.dragX);
                this.component.getRect().setY(this.master.getMouse().getY() - this.dragY);

                this.component.setDragging(true);
            } else {
                this.component.setDragging(false);
            }
        }

        this.component.applyCollision(Rocan.getMinecraft());
        this.rect.copy(this.component.getRect());

        this.flagMouse = Flag.MOUSE_NOT_OVER;

        this.popup.onRender();
    }

    @Override
    public void onCustomRender() {
        this.popup.onCustomRender();

        if (this.master.flagMouseRectBlocking == Flag.MOUSE_NOT_OVER && this.component.isEnabled()) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        } else {
            this.flagMouse = Flag.MOUSE_NOT_OVER;
        }
    }
}
