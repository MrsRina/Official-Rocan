package me.rina.rocan.api.gui.element.widget;

import me.rina.rocan.api.gui.element.Abstract;
import me.rina.rocan.api.gui.impl.ImplementElement;
import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 07/12/20 at 01:41am
 */
public class Widget extends Abstract implements ImplementElement {
    protected TurokRect rect;

    protected float offsetX;
    protected float offsetY;

    public Widget(String tag) {
        this.rect = new TurokRect(tag, 0, 0);
    }

    public void setRect(TurokRect rect) {
        this.rect = rect;
    }

    @Override
    public TurokRect getRect() {
        return rect;
    }

    @Override
    public float getOffsetX() {
        return offsetX;
    }

    @Override
    public float getOffsetY() {
        return offsetY;
    }

    @Override
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    @Override
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
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

    }

    @Override
    public void onCustomRender() {

    }
}
