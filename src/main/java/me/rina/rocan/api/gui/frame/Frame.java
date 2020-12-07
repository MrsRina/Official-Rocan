package me.rina.rocan.api.gui.frame;

import me.rina.rocan.api.gui.IGUI;
import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 07/12/20 at 01:41am
 */
public class Frame implements IGUI {
    protected TurokRect rect;

    public Frame(String tag) {
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
