package me.rina.rocan.client.gui.module;

import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokMath;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

/**
 * @author SrRina
 * @since 07/12/20 at 11:44am
 */
public class ModuleClickGUI extends GuiScreen {
    protected TurokDisplay display;
    protected TurokMouse mouse;

    private MotherFrame motherFrame;

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

    protected void setMouse(TurokMouse mouse) {
        this.mouse = mouse;
    }

    public TurokMouse getMouse() {
        return mouse;
    }

    @Override
    public void drawScreen(int mousePositionX, int mousePositionY, float partialTicks) {
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

        TurokRenderGL.popMatrix();

        TurokRenderGL.enable(GL11.GL_TEXTURE_2D);

        TurokRenderGL.disable(GL11.GL_TEXTURE_2D);
        TurokRenderGL.disable(GL11.GL_BLEND);

        TurokRenderGL.enable(GL11.GL_TEXTURE_2D);
        TurokRenderGL.color(255, 255, 255);
    }
}
