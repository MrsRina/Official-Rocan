package me.rina.rocan.client.gui.module;

import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.minecraft.TurokGUI;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokMath;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * @author SrRina
 * @since 07/12/20 at 11:44am
 */
@TurokGUI.GUI(name = "Rocan", author = "SrRina")
public class ModuleClickGUI extends TurokGUI {
    private MotherFrame motherFrame;

    private int effectAlphaBackground;

    private boolean isOpened;
    private boolean isPositionBack;
    private boolean isCanceledCloseGUI;

    protected me.rina.rocan.client.module.client.ModuleClickGUI moduleClientGUI;

    @Override
    public void init() {
        this.motherFrame = new MotherFrame(this);

        // To turn off when you close the GUI.
        this.moduleClientGUI = (me.rina.rocan.client.module.client.ModuleClickGUI) ModuleManager.get(me.rina.rocan.client.module.client.ModuleClickGUI.class);
    }

    public void setMotherFrame(MotherFrame motherFrame) {
        this.motherFrame = motherFrame;
    }

    public MotherFrame getMotherFrame() {
        return motherFrame;
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

        this.motherFrame.onOpen();
    }

    @Override
    public void onClose() {
        this.moduleClientGUI.setDisabled();

        this.isOpened = false;

        this.motherFrame.onClose();
    }

    @Override
    public void onKeyboard(char character, int key) {
        this.motherFrame.onKeyboard(character, key);

        if (this.isCanceledCloseGUI) {
            this.motherFrame.onCustomKeyboard(character, key);
        } else {
            if (key == Keyboard.KEY_ESCAPE) {
                if (me.rina.rocan.client.module.client.ModuleClickGUI.closeAnimation.getValue()) {
                    this.isOpened = false;
                } else {
                    this.isOpened = false;

                    this.closeGUI();
                }
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
        this.motherFrame.onMouseReleased(button);
    }

    @Override
    public void onMouseClicked(int button) {
        this.motherFrame.onMouseClicked(button);
    }

    @Override
    public void onRender() {
        this.effectAlphaBackground = (int) TurokMath.lerp(this.effectAlphaBackground, me.rina.rocan.client.module.client.ModuleClickGUI.drawDefaultMinecraftBackground.getValue() ? 190 : 0, this.partialTicks);

        // Draw the default background of Minecraft.
        TurokShaderGL.drawSolidRect(0, 0, this.display.getWidth(), this.display.getHeight(), new int[] {20, 20, 20, this.effectAlphaBackground});

        // Update onRender of mother frame.
        this.motherFrame.onRender();

        float calculatedScaledX = (this.display.getScaledWidth() / 2) - (this.motherFrame.getRect().getWidth() / this.motherFrame.getScale());

        // Make the current animation and smooth close.
        if (this.isOpened) {
            if (this.isPositionBack) {
                // We need save the current position.
                this.motherFrame.setSaveX(this.motherFrame.getRect().getX());
                this.motherFrame.setSaveY(this.motherFrame.getRect().getY());
            } else {
                // Use interpolation to set the current position, this make a smooth move but its ok.
                this.motherFrame.getRect().setX((int) TurokMath.lerp(this.motherFrame.getRect().getX(), this.motherFrame.getSaveX(), partialTicks));
                this.motherFrame.getRect().setY((int) TurokMath.lerp(this.motherFrame.getRect().getY(), this.motherFrame.getSaveY(), partialTicks));

                if (this.motherFrame.getRect().getDistance(this.motherFrame.getSaveX() + this.motherFrame.getRect().getWidth(), this.motherFrame.getSaveY() + this.motherFrame.getRect().getHeight()) <= 50) {
                    this.motherFrame.getRect().setX(this.motherFrame.getSaveX());
                    this.motherFrame.getRect().setY(this.motherFrame.getSaveY());

                    this.isPositionBack = true;
                }
            }
        } else {
            float frameWidthScaled = this.motherFrame.getRect().getWidth() + 1;

            this.motherFrame.getRect().setX((int) TurokMath.lerp(this.motherFrame.getRect().getX(), TurokMath.negative(frameWidthScaled) - 50, partialTicks));

            if (this.motherFrame.getRect().getX() <= TurokMath.negative(frameWidthScaled)) {
                this.mouse.setCursorPos(this.display.getScaledWidth() / 2, this.display.getScaledHeight() / 2);

                // Make the own TurokGUI close the GUI.
                this.closeGUI();

                this.mouse.setCursorPos(this.display.getScaledWidth() / 2, this.display.getScaledHeight() / 2);
            }
        }
    }
}