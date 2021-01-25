package me.rina.rocan.api;

/**
 * @author SrRina
 * @since 06/12/20 at 01:04am
 */
public interface ISLClass {
    /**
     * Make the class savable;
     */
    public void onSave();

    /**
     * Make the class loadable;
     */
    public void onLoad();
}