package me.rina.rocan.client.gui.component;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.gui.component.component.frame.ComponentFrame;
import me.rina.rocan.client.gui.component.component.frame.ComponentPopupListFrame;
import me.rina.rocan.client.module.client.ModuleHUD;
import me.rina.turok.minecraft.TurokGUI;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 26/03/2021 at 23:50
 **/
@TurokGUI.GUI(name = "Rocan HUD", author = "SrRina")
public class ComponentClickGUI extends TurokGUI {
    private final TurokRect rectBlocking = new TurokRect(0, 0);
    private final TurokRect rectSelected = new TurokRect(0, 0);

    private ArrayList<Frame> loadedFrameList;
    private int effectAlphaBackground;

    /* The current frame over. */
    private Frame frameOver;

    private boolean state;
    private boolean postSelected;

    private boolean isPopUpOpened;
    private boolean isCancelledToCloseGUI;

    private boolean isMouseOverComponent;
    private boolean isMouseClickedSelect;

    private boolean isMouseClickedRight;

    private int saveX;
    private int saveY;

    /* Flags. */
    public Flag flagMouseRectBlocking = Flag.MOUSE_NOT_OVER;

    @Override
    public void init() {
        this.loadedFrameList = new ArrayList<>();

        for (Component components : Rocan.getComponentManager().getComponentList()) {
            ComponentFrame componentFrame = new ComponentFrame(this, components);

            this.loadedFrameList.add(componentFrame);
        }

        this.loadedFrameList.add(new ComponentPopupListFrame(this));
    }

    public void resetRectBlocking() {
        this.rectBlocking.setX(10000);
        this.rectBlocking.setY(10000);
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isState() {
        return state;
    }

    public TurokRect getRectBlocking() {
        return rectBlocking;
    }

    public TurokRect getRectSelected() {
        return rectSelected;
    }

    public void setMouseClickedSelect(boolean mouseClickedSelect) {
        isMouseClickedSelect = mouseClickedSelect;
    }

    public boolean isMouseClickedSelect() {
        return isMouseClickedSelect;
    }

    public void setPopUpOpened(boolean popUpOpened) {
        isPopUpOpened = popUpOpened;
    }

    public boolean isPopUpOpened() {
        return isPopUpOpened;
    }

    public void setMouseOverComponent(boolean mouseOverComponent) {
        isMouseOverComponent = mouseOverComponent;
    }

    public void setPostSelected(boolean postSelected) {
        this.postSelected = postSelected;
    }

    public boolean isPostSelected() {
        return postSelected;
    }

    public boolean isMouseOverComponent() {
        return isMouseOverComponent;
    }

    public void setCancelledToCloseGUI(boolean cancelledToCloseGUI) {
        isCancelledToCloseGUI = cancelledToCloseGUI;
    }

    public boolean isCancelledToCloseGUI() {
        return isCancelledToCloseGUI;
    }

    public boolean isMouseClickedRight() {
        return isMouseClickedRight;
    }

    public Frame getFrameOver() {
        return frameOver;
    }

    public void refresh(Frame frame) {
        if (frame != null) {
            this.loadedFrameList.remove(frame);
            this.loadedFrameList.add(frame);
        }
    }

    @Override
    public boolean pauseGameWhenActive() {
        return false;
    }

    @Override
    public void onOpen() {
        for (Frame frames : this.loadedFrameList) {
            frames.onOpen();
            frames.onCustomOpen();
        }

        this.state = true;
    }

    @Override
    public void onClose() {
        for (Frame frames : this.loadedFrameList) {
            frames.onClose();
            frames.onCustomClose();
        }

        this.isMouseClickedRight = false;
        this.isMouseClickedSelect = false;

        ModuleHUD moduleHUD = (ModuleHUD) ModuleManager.get(ModuleHUD.class);
        moduleHUD.setDisabled();
    }

    @Override
    public void onKeyboard(char character, int key) {
        if (this.isCancelledToCloseGUI) {
            if (key == Keyboard.KEY_ESCAPE && this.isMouseClickedSelect) {
                this.isMouseClickedSelect = false;
            }

            for (Frame frames : this.loadedFrameList) {
                frames.onKeyboard(character, key);
                frames.onCustomKeyboard(character, key);
            }
        } else {
            if (key == Keyboard.KEY_ESCAPE) {
                this.state = false;
            }
        }

        if ((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.doSelectAllComponents();

            this.postSelected = true;
        }
    }

    @Override
    public void onMouseClicked(int button) {
        if (!this.state) {
            return;
        }

        if (button == 1) {
            this.isMouseClickedRight = true;
        }

        if (!this.isMouseOverComponent && !this.isPopUpOpened && (button == 0 || this.isMouseClickedRight) && this.flagMouseRectBlocking == Flag.MOUSE_NOT_OVER) {
            this.saveX = this.mouse.getX();
            this.saveY = this.mouse.getY();

            this.isMouseClickedSelect = true;
            this.postSelected = true;
        } else {
            if (this.isMouseOverComponent && !this.postSelected) {
                this.rectSelected.set(12000, 12000, 0, 0);
            }
        }

        for (Frame frames : this.loadedFrameList) {
            frames.onMouseClicked(button);
            frames.onCustomMouseClicked(button);
        }
    }

    @Override
    public void onMouseReleased(int button) {
        if (!this.state) {
            return;
        }

        for (Frame frames : this.loadedFrameList) {
            frames.onMouseReleased(button);
            frames.onCustomMouseReleased(button);
        }

        if (this.isMouseClickedSelect) {
            this.isMouseClickedSelect = false;
        }

        if (this.isMouseClickedRight) {
            this.isMouseClickedRight = false;
        }
    }

    @Override
    public void onRender() {
        // Draw default background.
        TurokShaderGL.drawSolidRect(0, 0, this.display.getWidth(), this.display.getHeight(), new int[] {20, 20, 20, this.effectAlphaBackground});

        this.frameOver = null;
        this.isMouseOverComponent = false;

        for (Frame frames : this.loadedFrameList) {
            frames.onRender();

            boolean accept = false;

            if (frames instanceof ComponentFrame && !this.isPopUpOpened && ((((ComponentFrame) frames).getComponent().isEnabled() && frames.getRect().collideWithMouse(this.mouse)) || (((ComponentFrame) frames).getPopup().isOpened() && ((ComponentFrame) frames).getPopup().getRect().collideWithMouse(this.mouse)))) {
                accept = true;
            }

            if (frames instanceof ComponentPopupListFrame && ((ComponentPopupListFrame) frames).isOpened() && frames.getRect().collideWithMouse(this.mouse)) {
                accept = true;
            }

            if (accept) {
                this.frameOver = frames;
            }
        }

        if (this.frameOver != null) {
            this.frameOver.onCustomRender();
            this.refresh(this.frameOver);
            this.isMouseOverComponent = true;
        } else {
            this.isMouseOverComponent = false;
        }

        if (this.isMouseClickedSelect && this.state) {
            float x = saveX;
            float y = saveY;
            float w = this.mouse.getX() - saveX;
            float h = this.mouse.getY() - saveY;

            if (this.mouse.getX() < saveX) {
                x = this.mouse.getX();
                w = saveX - x;
            }

            if (this.mouse.getY() < saveY) {
                y = this.mouse.getY();
                h = saveY - y;
            }

            this.rectSelected.set(x, y, w, h);
            this.postSelected = this.isSomeFrameSelected();

            if (w != 0 && h != 0) {
                TurokShaderGL.drawSolidRect(this.rectSelected, new int[]{45, 95, 241, 50});
                TurokShaderGL.drawOutlineRect(this.rectSelected, new int[]{45, 95, 241, 200});
            }
        }

        this.flagMouseRectBlocking = this.rectBlocking.collideWithMouse(this.mouse) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
        this.isPopUpOpened = this.rectBlocking.getX() != 10000;

        if (this.state) {
            this.effectAlphaBackground = (int) TurokMath.lerp(this.effectAlphaBackground, 190, this.partialTicks);
        } else {
            this.effectAlphaBackground = (int) TurokMath.lerp(this.effectAlphaBackground, 0, this.partialTicks);

            if (this.effectAlphaBackground <= 10) {
                this.closeGUI();
            }
        }
    }

    public void doSelectAllComponents() {
        for (Frame frames : this.loadedFrameList) {
            if (frames instanceof ComponentFrame) {
                ComponentFrame componentFrame = (ComponentFrame) frames;
                componentFrame.setSelected(true);
            }
        }
    }

    public boolean isSomeFrameSelected() {
        boolean isSelected = false;

        for (Frame frames : this.loadedFrameList) {
            if (frames instanceof ComponentFrame) {
                ComponentFrame componentFrame = (ComponentFrame) frames;

                if (componentFrame.isSelected()) {
                    isSelected = true;

                    break;
                }
            }
        }

        return isSelected;
    }

    public void doResetAllPopUps() {
        for (Frame frames : this.loadedFrameList) {
            if (frames instanceof ComponentFrame) {
                ComponentFrame componentFrame = (ComponentFrame) frames;
                componentFrame.setSelected(false);
            }
        }
    }
}
