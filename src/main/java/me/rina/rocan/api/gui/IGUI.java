package me.rina.rocan.api.gui;

import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 07/12/20 at 11:39am
 */
public interface IGUI {
    /**
     * Return the current rect of implemented class.
     *
     * @return
     */
    public TurokRect getRect();

    /**
     * Method for keyboard events.
     *
     * @param character - Typed char on keyboard.
     * @param key       - Typed int on keyboard.
     */
    public void onKeyboard(char character, int key);

    /**
     * Custom method for keyboard.
     *
     * @param character - Typed char on keyboard.
     * @param key      -  Typed int on keyboard.
     */
    public void onCustomKeyboard(char character, int key);

    /**
     * Method for mouse button up event.
     *
     * @param button - Current click of mouse.
     */
    public void onMouseReleased(int button);

    /**
     * Custom method for mouse button up event.
     *
     * @param button - Current int click of mouse.
     */
    public void onCustomMouseReleased(int button);

    /**
     * Method for mouse button down event.
     *
     * @param button - Current int click of mouse.
     */
    public void onMouseClicked(int button);

    /**
     * Custom method for mouse button down event.
     *
     * @param button - Current int click of mouse.
     */
    public void onCustomMouseClicked(int button);

    /**
     * A method to update render at implemented class.
     */
    public void onRender();

    /**
     * A custom method to update render at implemeted class.
     */
    public void onCustomRender();
}