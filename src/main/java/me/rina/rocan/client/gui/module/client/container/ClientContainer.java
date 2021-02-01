package me.rina.rocan.client.gui.module.client.container;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.client.widget.SearchModuleWidget;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;

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

    private SearchModuleWidget searchModuleWidget;

    public Flag flagMouse = Flag.MouseNotOver;
    public Flag flagMouseModule = Flag.MouseNotOver;
    public Flag flagMouseReal = Flag.MouseNotOver;

    public ClientContainer(ModuleClickGUI master, MotherFrame frame) {
        super("client-container-user");

        this.master = master;
        this.frame = frame;

        this.init();
    }

    public void init() {
        this.searchModuleWidget = new SearchModuleWidget(this.master, this.frame, this);
        this.searchModuleWidget.setOffsetY(1);
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
        this.searchModuleWidget.onClose();
    }

    @Override
    public void onOpen() {
        this.searchModuleWidget.onOpen();
    }

    @Override
    public void onKeyboard(char character, int key) {
        this.searchModuleWidget.onKeyboard(character, key);
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        this.searchModuleWidget.onCustomKeyboard(character, key);
    }

    @Override
    public void onMouseReleased(int button) {
        this.searchModuleWidget.onMouseReleased(button);
    }

    @Override
    public void onCustomMouseReleased(int button) {
        this.searchModuleWidget.onCustomMouseReleased(button);
    }

    @Override
    public void onMouseClicked(int button) {
        this.searchModuleWidget.onMouseClicked(button);
    }

    @Override
    public void onCustomMouseClicked(int button) {
        this.searchModuleWidget.onCustomMouseClicked(button);
    }

    @Override
    public void onRender() {
        if (this.moduleContainer != null) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
            this.flagMouseReal = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

            // Background of container.
            TurokRenderGL.color(Rocan.getWrapperGUI().colorContainerBackground[0], Rocan.getWrapperGUI().colorContainerBackground[1], Rocan.getWrapperGUI().colorContainerBackground[2], Rocan.getWrapperGUI().colorContainerBackground[3]);
            TurokRenderGL.drawSolidRect(this.rect);

            // Render the search widget of course.
            this.searchModuleWidget.onRender();

            if (this.isUnselected) {
                this.rect.setX((this.moduleContainer.getRect().getX() + this.moduleContainer.getRect().getWidth()) + (2 * this.frame.getScale()));
                this.rect.setY(this.moduleContainer.getRect().getY());

                this.scrollRect.setX(this.rect.getX());
                this.scrollRect.setY(TurokMath.lerp(this.scrollRect.getY(), this.rect.getY() + this.offsetY, this.master.getPartialTicks()));

                float realScrollHeight = this.searchModuleWidget.getRect().getHeight() + 1;

                this.realRect.setX(this.rect.getX());
                this.realRect.setY(this.rect.getY() + realScrollHeight);

                this.realRect.setWidth(this.rect.getWidth());
                this.realRect.setHeight(this.rect.getHeight() - realScrollHeight);

                this.rect.setWidth(this.getWidthScale());
                this.rect.setHeight(this.moduleContainer.getRect().getHeight());
            } else {
                this.rect.setWidth(0);
                this.rect.setHeight(0);
            }

            if (this.moduleContainer.isModuleOpen() == false) {
                if (this.flagMouseModule == Flag.MouseNotOver) {
                    this.isUnselected = true;
                } else {
                    this.isUnselected = false;
                }
            } else {
                this.isUnselected = false;
            }

            this.flagMouseModule = Flag.MouseNotOver;
        }
    }

    @Override
    public void onCustomRender() {
        // We need render here, cause there is a backend time.
        this.searchModuleWidget.onCustomRender();
    }
}