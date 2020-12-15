package me.rina.rocan.api;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 06/12/20 at 01:04am
 */
public interface ISLClass {
    /*
     * Make the class savable;
     */
    public void onSave();

    /*
     * Make the class loadable;
     */
    public void onLoad();

    /*
     * Save classes using ISLClass;
     */
    public static void onReloadSave(Object object) {
        if (!(object instanceof ArrayList<?>)) {
            return;
        }

        ArrayList<?> objectList = (ArrayList<?>) object;

        for (Object objects : objectList) {
            if (objects instanceof ISLClass) {
                ISLClass _ISLClass = (ISLClass) objects;

                _ISLClass.onSave();
            }
        }
    }

    /*
     * Load classes using ISLClass;
     */
    public static void onReloadLoad(Object object) {
        if (!(object instanceof ArrayList<?>)) {
            return;
        }

        ArrayList<?> objectList = (ArrayList<?>) object;

        for (Object objects : objectList) {
            if (objects instanceof ISLClass) {
                ISLClass _ISLClass = (ISLClass) objects;

                _ISLClass.onLoad();
            }
        }
    }
}