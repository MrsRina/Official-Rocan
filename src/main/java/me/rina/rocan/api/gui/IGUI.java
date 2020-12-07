package me.rina.rocan.api.gui;

import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 07/12/20 at 11:39am
 */
public interface IGUI {
    public TurokRect getRect();

    public void onKeyboard(char character, int key);
    public void onCustomKeyboard(char character, int key);

    public void onMouseReleased(int button);
    public void onCustomMouseReleased(int button);

    public void onMouseClicked(int button);
    public void onCustomMouseClicked(int button);

    public void onRender();
    public void onCustomRender();
}