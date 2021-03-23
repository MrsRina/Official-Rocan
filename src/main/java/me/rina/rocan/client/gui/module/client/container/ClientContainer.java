package me.rina.rocan.client.gui.module.client.container;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

import java.awt.*;

/**
 * @author SrRina
 * @since 22/01/2021 at 20:34
 **/
public class ClientContainer extends Container {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleContainer moduleContainer;

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    private boolean isUnselected;
    private boolean isLocked;

    private TurokRect scrollRect = new TurokRect("Scroll", 0, 0);
    private TurokRect realRect = new TurokRect("Real Rect", 0, 0);

    public Flag flagMouse = Flag.MOUSE_NOT_OVER;
    public Flag flagMouseModule = Flag.MOUSE_NOT_OVER;
    public Flag flagMouseReal = Flag.MOUSE_NOT_OVER;

    public ClientContainer(ModuleClickGUI master, MotherFrame frame) {
        super("client-container-user");

        this.master = master;
        this.frame = frame;

        this.init();
    }

    public void init() {
    }

    public void setScrollRect(TurokRect scrollRect) {
        this.scrollRect = scrollRect;
    }

    public TurokRect getScrollRect() {
        return scrollRect;
    }

    public void setRealRect(TurokRect realRect) {
        this.realRect = realRect;
    }

    public TurokRect getRealRect() {
        return realRect;
    }

    public void setModuleContainer(ModuleContainer moduleContainer) {
        this.moduleContainer = moduleContainer;
    }

    public ModuleContainer getModuleContainer() {
        return moduleContainer;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetWidth(float offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public float getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetHeight(float offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public float getOffsetHeight() {
        return offsetHeight;
    }

    public float getWidthScale() {
        float currentScaleX = (5 * this.frame.getScale());
        float scale = (2 * this.frame.getScale());

        return (this.frame.getRect().getWidth() - this.moduleContainer.getRect().getWidth() - currentScaleX - scale + 1);
    }

    public void setUnselected(boolean unselected) {
        this.isUnselected = unselected;
    }

    public boolean isUnselected() {
        return isUnselected;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onOpen() {

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

    }

    @Override
    public void onMouseClicked(int button) {

    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        if (this.moduleContainer != null) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;
            this.flagMouseReal = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MOUSE_OVER : Flag.MOUSE_NOT_OVER;

            // Background of container.
            TurokShaderGL.drawSolidRect(this.rect, new int[] {Rocan.getWrapper().colorContainerBackground[0], Rocan.getWrapper().colorContainerBackground[1], Rocan.getWrapper().colorContainerBackground[2], Rocan.getWrapper().colorContainerBackground[3]});

            if (this.isUnselected) {
                this.rect.setX((this.moduleContainer.getRect().getX() + this.moduleContainer.getRect().getWidth()) + (2 * this.frame.getScale()));
                this.rect.setY(this.moduleContainer.getRect().getY());

                this.scrollRect.setX(this.rect.getX());
                this.scrollRect.setY(TurokMath.lerp(this.scrollRect.getY(), this.rect.getY() + this.offsetY, this.master.getPartialTicks()));

                float realScrollHeight = 1;

                this.realRect.setX(this.rect.getX());
                this.realRect.setY(this.rect.getY() + realScrollHeight);

                this.realRect.setWidth(this.rect.getWidth());
                this.realRect.setHeight(this.rect.getHeight() - realScrollHeight);

                this.rect.setWidth(this.getWidthScale());
                this.rect.setHeight(this.moduleContainer.getHeightScale());
            } else {
                this.rect.setWidth(0);
                this.rect.setHeight(0);
            }

            if (this.moduleContainer.isModuleOpen() == false) {
                if (this.flagMouseModule == Flag.MOUSE_NOT_OVER) {
                    this.isUnselected = true;
                } else {
                    this.isUnselected = false;
                }
            } else {
                this.isUnselected = false;
            }

            this.flagMouseModule = Flag.MOUSE_NOT_OVER;
        }
    }

    @Override
    public void onCustomRender() {

    }
}