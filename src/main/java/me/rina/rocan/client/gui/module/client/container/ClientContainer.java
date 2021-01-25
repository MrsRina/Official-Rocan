package me.rina.rocan.client.gui.module.client.container;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.opengl.TurokRenderGL;

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

    public Flag flagMouse = Flag.MouseNotOver;

    public ClientContainer(ModuleClickGUI master, MotherFrame frame) {
        super("client-container-user");

        this.master = master;
        this.frame = frame;
    }

    public void init() {
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
            this.rect.setX((this.moduleContainer.getRect().getX() + this.moduleContainer.getRect().getWidth()) + (2 * this.frame.getScale()));
            this.rect.setY(this.moduleContainer.getRect().getY());

            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

            if (this.moduleContainer.isModuleOpen()) {
                this.rect.setWidth(this.getWidthScale());
                this.rect.setHeight(this.moduleContainer.getRect().getHeight());

                // The fully background rect.
                TurokRenderGL.color(Rocan.getWrapperGUI().colorContainerBackground[0], Rocan.getWrapperGUI().colorContainerBackground[1], Rocan.getWrapperGUI().colorContainerBackground[2], Rocan.getWrapperGUI().colorContainerBackground[3]);
                TurokRenderGL.drawSolidRect(this.rect);
            } else {
                this.rect.setWidth(0);
                this.rect.setHeight(0);
            }
        }
    }

    @Override
    public void onCustomRender() {
    }
}