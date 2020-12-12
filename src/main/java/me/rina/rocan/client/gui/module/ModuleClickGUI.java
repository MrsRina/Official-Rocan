package me.rina.rocan.client.gui.module;

import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokMath;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * @author SrRina
 * @since 07/12/20 at 11:44am
 */
public class ModuleClickGUI extends GuiScreen {
    protected TurokDisplay display;
    protected TurokMouse mouse;

    private MotherFrame motherFrame;

    protected float partialTicks;

    private boolean isOpened;
    private boolean isCanceledCloseGUI;

    public ModuleClickGUI() {
        TurokRenderGL.init();

        this.init();
    }

    public void init() {
        this.motherFrame = new MotherFrame(this);
    }

    protected void setDisplay(TurokDisplay display) {
        this.display = display;
    }

    public TurokDisplay getDisplay() {
        return display;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    protected void setMouse(TurokMouse mouse) {
        this.mouse = mouse;
    }

    public TurokMouse getMouse() {
        return mouse;
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

    @Override
    public void initGui() {
        this.isOpened = true;
    }

    @Override
    public void onGuiClosed() {
        this.isOpened = false;
    }

    @Override
    public void keyTyped(char character, int key) {
        if (this.isCanceledCloseGUI) {
            this.motherFrame.onKeyboard(character, key);
        } else {
            if (key == Keyboard.KEY_ESCAPE) {
                this.isOpened = false;
            }
        }
    }

    @Override
    public void mouseReleased(int mousePositionX, int mousePositionY, int button) {
        this.motherFrame.onMouseReleased(button);
    }


    @Override
    public void mouseClicked(int mousePositionX, int mousePositionY, int button) {
        this.motherFrame.onMouseClicked(button);
    }

    @Override
    public void drawScreen(int mousePositionX, int mousePositionY, float partialTicks) {
        this.partialTicks = partialTicks;

        this.display = new TurokDisplay(mc);
        this.mouse = new TurokMouse(mousePositionX, mousePositionY);

        TurokRenderGL.init(this.display);
        TurokRenderGL.init(this.mouse);

        /*
         * We need fix the scale;
         */
        TurokRenderGL.autoScale();
        TurokRenderGL.disable(GL11.GL_TEXTURE_2D);

        TurokRenderGL.pushMatrix();

        this.motherFrame.onRender();

        int calculatedScaledX = (this.display.getScaledWidth() - this.motherFrame.getScaleWidth()) /  (this.motherFrame.getScale());

        if (this.isOpened) {
            this.motherFrame.setScaleX((int) TurokMath.linearInterpolation(this.motherFrame.getScaleX(), calculatedScaledX, partialTicks));
        } else {
            this.motherFrame.setScaleX((int) TurokMath.linearInterpolation(this.motherFrame.getScaleX(), TurokMath.negative(this.motherFrame.getRect().getWidth()) - 10, partialTicks));

            if (this.motherFrame.getRect().getX() <= TurokMath.negative(this.motherFrame.getRect().getWidth())) {
                this.onGuiClosed();

                this.mouse.setPos(this.display.getScaledWidth() / 2, this.display.getScaledHeight() / 2);

                mc.displayGuiScreen(null);

                this.mouse.setPos(this.display.getScaledWidth() / 2, this.display.getScaledHeight() / 2);
            }
        }

        TurokRenderGL.popMatrix();

        TurokRenderGL.enable(GL11.GL_TEXTURE_2D);

        TurokRenderGL.disable(GL11.GL_TEXTURE_2D);
        TurokRenderGL.disable(GL11.GL_BLEND);

        TurokRenderGL.enable(GL11.GL_TEXTURE_2D);
        TurokRenderGL.color(255, 255, 255);
    }
}
