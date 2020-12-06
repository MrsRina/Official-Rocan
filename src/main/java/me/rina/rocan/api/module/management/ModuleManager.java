package me.rina.rocan.api.module.management;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.client.module.ModuleExample;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 15/11/20 at 5:30pm
 */
public class ModuleManager {
    public static ModuleManager INSTANCE;

    private ArrayList<Module> moduleList;

    public ModuleManager() {
        INSTANCE = this;

        this.moduleList = new ArrayList<>();
    }

    public void registry(Module module) {
        try {
            for (Field fields : module.getClass().getDeclaredFields()) {
                if (Setting.class.isAssignableFrom(fields.getType())) {
                    if (!fields.isAccessible()) {
                        fields.setAccessible(true);
                    }

                    final Setting settingDeclared = (Setting) fields.get(module);

                    module.registry(settingDeclared);
                }
            }

            this.moduleList.add(module);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void setModuleList(ArrayList<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public ArrayList<Module> getModuleList() {
        return moduleList;
    }

    public void onUpdateModuleList() {
        for (Module modules : this.getModuleList()) {
            if (modules.isEnabled()) {
                modules.onUpdate();
            }
        }
    }

    public void onKeyBinding(int keyCode) {
        for (Module modules : this.getModuleList()) {
            if (modules.getKeyCode() == keyCode) {
                modules.toggle();
            }
        }
    }

    /*
     * Tools.
     */
    public static Module get(Class clazz) {
        for (Module modules : ModuleManager.INSTANCE.getModuleList()) {
            if (modules.getClass() == clazz) {
                return modules;
            }
        }

        return null;
    }

    public static Module get(String tag) {
        for (Module modules : ModuleManager.INSTANCE.getModuleList()) {
            if (modules.getTag().equalsIgnoreCase(tag)) {
                return modules;
            }
        }

        return null;
    }
}
