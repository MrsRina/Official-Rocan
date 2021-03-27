package me.rina.rocan.client.gui.component.frame;

import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.gui.frame.Frame;
import me.rina.rocan.client.gui.component.ComponentClickGUI;

/**
 * @author SrRina
 * @since 27/03/2021 at 00:00
 **/
public class ComponentPopupFrame extends Frame {
    private ComponentClickGUI master;
    private Component component;

    private boolean isOpened;

    public ComponentPopupFrame(ComponentClickGUI master, Component component) {
        super(component.getTag());

        this.master = master;
        this.component = component;
    }

    public ComponentClickGUI getMaster() {
        return master;
    }

    public Component getComponent() {
        return component;
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

    }

    @Override
    public void onMouseClicked(int button) {

    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        if (this.isOpened) {
            // We need get the current click on space... but for it... well...
            this.rect.setX(this.master.getRectBlocking().getX());
            this.rect.setY(this.master.getRectBlocking().getY());

            this.master.getRectBlocking().setWidth(this.rect.getWidth());
            this.master.getRectBlocking().setHeight(this.rect.getHeight());
        }
    }

    @Override
    public void onCustomRender() {

    }
}
