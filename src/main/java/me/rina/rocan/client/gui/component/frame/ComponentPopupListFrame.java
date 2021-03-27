package me.rina.rocan.client.gui.component.frame;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.client.gui.component.ComponentClickGUI;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;

/**
 * @author SrRina
 * @since 27/03/2021 at 00:05
 **/
public class ComponentPopupListFrame extends Frame {
    private ComponentClickGUI master;

    private boolean isOpened;
    private boolean isMouseClickedLeft;

    private int maximumHeight;

    public ComponentPopupListFrame(ComponentClickGUI master) {
        super("Mother");

        this.master = master;

        this.rect.setWidth(100);

        // We don't set the height direct.
        this.maximumHeight = 100;
    }

    public ComponentClickGUI getMaster() {
        return master;
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

    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.isMouseClickedLeft) {
            this.rect.setX(this.master.getRectBlocking().getX());
            this.rect.setY(this.master.getRectBlocking().getY());

            this.isOpened = true;
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.master.flagMouseRectBlocking == Flag.MOUSE_NOT_OVER && button == 1) {
            this.master.resetRectBlocking();

            this.isOpened = false;
            this.isMouseClickedLeft = true;
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        if (this.isOpened) {
            this.rect.setHeight(TurokMath.lerp(this.rect.getHeight(), this.maximumHeight, this.master.getPartialTicks()));

            this.master.getRectBlocking().setWidth(this.rect.getWidth());
            this.master.getRectBlocking().setHeight(this.rect.getHeight());
        }

        // Draw background.
        TurokShaderGL.drawSolidRect(this.rect, Rocan.getWrapper().colorFrameBackground);
    }

    @Override
    public void onCustomRender() {

    }
}
