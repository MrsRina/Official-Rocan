package me.rina.rocan.client.gui.component;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.gui.component.frame.ComponentPopupListFrame;
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
    private TurokRect rectBlocking = new TurokRect(0, 0);
    private boolean state;

    private ArrayList<Frame> loadedFrameList;
    private int effectAlphaBackground;

    /* Flags. */
    public Flag flagMouseRectBlocking = Flag.MOUSE_NOT_OVER;

    @Override
    public void init() {
        this.loadedFrameList = new ArrayList<>();
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

    public void setRectBlocking(TurokRect rectBlocking) {
        this.rectBlocking = rectBlocking;
    }

    public TurokRect getRectBlocking() {
        return rectBlocking;
    }

    @Override
    public boolean pauseGameWhenActive() {
        return false;
    }

    @Override
    public void onOpen() {
        this.state = true;
    }

    @Override
    public void onClose() {
        ModuleHUD moduleHUD = (ModuleHUD) ModuleManager.get(ModuleHUD.class);
        moduleHUD.setDisabled();
    }

    @Override
    public void onKeyboard(char character, int key) {
        if (key == Keyboard.KEY_ESCAPE) {
            this.state = false;
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseClicked(button);
        }
    }

    @Override
    public void onMouseReleased(int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseReleased(button);
        }
    }

    @Override
    public void onRender() {
        // Draw default background.
        TurokShaderGL.drawSolidRect(0, 0, this.display.getWidth(), this.display.getHeight(), new int[] {20, 20, 20, this.effectAlphaBackground});

        for (Frame frames : this.loadedFrameList) {
            frames.onRender();
        }

        this.flagMouseRectBlocking = this.rectBlocking.collideWithMouse(this.mouse) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;

        if (this.state) {
            this.effectAlphaBackground = (int) TurokMath.lerp(this.effectAlphaBackground, 190, this.partialTicks);
        } else {
            this.effectAlphaBackground = (int) TurokMath.lerp(this.effectAlphaBackground, 0, this.partialTicks);

            if (this.effectAlphaBackground <= 10) {
                this.closeGUI();
            }
        }
    }
}
