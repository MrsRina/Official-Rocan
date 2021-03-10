package me.rina.rocan.client.gui.module;

import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.gui.module.UI.MainFrameUI;
import me.rina.turok.minecraft.TurokGUI;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import org.lwjgl.input.Keyboard;

/**
 * @author SrRina
 * @since 07/12/20 at 11:44am
 */
@TurokGUI.GUI(name = "Rocan", author = "SrRina")
public class ModuleClickGUI extends TurokGUI {
    private int effectAlphaBackground;

    private boolean isOpened;
    private boolean isPositionBack;
    private boolean isCanceledCloseGUI;

    protected me.rina.rocan.client.module.client.ModuleClickGUI moduleClientGUI;
    private MainFrameUI mainFrameUI;

    @Override
    public void init() {
        // To turn off when you close the GUI.
        this.moduleClientGUI = (me.rina.rocan.client.module.client.ModuleClickGUI) ModuleManager.get(me.rina.rocan.client.module.client.ModuleClickGUI.class);

        // Start main frame UI.
        this.mainFrameUI = new MainFrameUI(this);
    }

    public void setCanceledCloseGUI(boolean canceledCloseGUI) {
        isCanceledCloseGUI = canceledCloseGUI;
    }

    public boolean isCanceledCloseGUI() {
        return isCanceledCloseGUI;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setPositionBack(boolean positionBack) {
        isPositionBack = positionBack;
    }

    public boolean isPositionBack() {
        return isPositionBack;
    }

    @Override
    public void onOpen() {
        this.isOpened = true;
        this.isPositionBack = false;

        this.mainFrameUI.onOpen();
    }

    @Override
    public void onClose() {
        this.moduleClientGUI.setDisabled();
        this.isOpened = false;

        this.mainFrameUI.onClose();
    }

    @Override
    public void onKeyboard(char character, int key) {
        this.mainFrameUI.onCustomKeyboard(character, key);

        if (this.isCanceledCloseGUI) {
            this.mainFrameUI.onKeyboard(character, key);
        } else {
            if (key == Keyboard.KEY_ESCAPE) {
                this.closeGUI();
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
        this.mainFrameUI.onMouseReleased(button);
        this.mainFrameUI.onCustomMouseReleased(button);
    }

    @Override
    public void onMouseClicked(int button) {
        this.mainFrameUI.onMouseClicked(button);
        this.mainFrameUI.onCustomMouseClicked(button);
    }

    @Override
    public void onRender() {
        this.effectAlphaBackground = (int) TurokMath.lerp(this.effectAlphaBackground, me.rina.rocan.client.module.client.ModuleClickGUI.drawDefaultMinecraftBackground.getValue() ? 190 : 0, this.partialTicks);

        // Draw the default background of Minecraft.
        TurokShaderGL.drawSolidRect(0, 0, this.display.getWidth(), this.display.getHeight(), new int[] {20, 20, 20, this.effectAlphaBackground});

        // Draw all contexts of main UI frame.
        this.mainFrameUI.onRender();
    }
}