package me.rina.rocan.api.component.management;

import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.setting.Setting;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 23/03/2021 at 15:38
 **/
public class ComponentManager implements ISLClass {
    public static ComponentManager INSTANCE;

    private ArrayList<Component> componentList;

    public ComponentManager() {
        INSTANCE = this;

        this.componentList = new ArrayList<>();
    }

    public void setComponentList(ArrayList<Component> componentList) {
        this.componentList = componentList;
    }

    public ArrayList<Component> getComponentList() {
        return componentList;
    }

    public void registry(Component component) {
        try {
            for (Field fields : component.getClass().getDeclaredFields()) {
                if (Setting.class.isAssignableFrom(fields.getType())) {
                    if (!fields.isAccessible()) {
                        fields.setAccessible(true);
                    }

                    final Setting settingDeclared = (Setting) fields.get(component);

                    component.registry(settingDeclared);
                }
            }

            this.componentList.add(component);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void unregister(Component component) {
        if (get(component.getClass()) != null) {
            this.componentList.remove(component);
        }
    }

    public static Component get(Class<?> clazz) {
        for (Component components : ComponentManager.INSTANCE.getComponentList()) {
            if (components.getClass() == clazz) {
                return components;
            }
        }

        return null;
    }

    public static Component get(String tag) {
        for (Component components : ComponentManager.INSTANCE.getComponentList()) {
            if (components.getTag().equalsIgnoreCase(tag)) {
                return components;
            }
        }

        return null;
    }

    @Override
    public void onSave() {

    }

    @Override
    public void onLoad() {

    }
}
