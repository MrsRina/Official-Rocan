package me.rina.rocan.api.module.management;

import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 15/11/20 at 5:30pm
 */
public class ModuleManager implements ISLClass {
    public static ModuleManager INSTANCE;

    private ArrayList<Module> moduleList;

    public ModuleManager() {
        INSTANCE = this;

        this.moduleList = new ArrayList<>();
    }

    public void setModuleList(ArrayList<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public ArrayList<Module> getModuleList() {
        return moduleList;
    }

    public void onKeyPressed(int keyCode) {
        for (Module modules : this.getModuleList()) {
            modules.onKeyPressed(keyCode);
        }
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

    public void unregister(Module module) {
        if (get(module.getClass()) != null) {
            this.moduleList.remove(module);
        }
    }

    public static Module get(Class<?> clazz) {
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

    public static ArrayList<Module> get(ModuleCategory category) {
        ArrayList<Module> moduleListRequested = new ArrayList<>();

        for (Module modules : INSTANCE.getModuleList()) {
            if (modules.getCategory() == category) {
                moduleListRequested.add(modules);
            }
        }

        return moduleListRequested;
    }

    /**
     * Current method to reload and refresh event listener.
     */
    public static void reload() {
        for (Module modules : INSTANCE.getModuleList()) {
            modules.onReload();
        }
    }

    public static void refresh() {
        for (Module modules : INSTANCE.getModuleList()) {
            modules.onSetting();
        }
    }

    @Override
    public void onSave() {
        for (Module modules : this.getModuleList()) {
            modules.onSave();
        }
    }

    @Override
    public void onLoad() {
        for (Module modules : this.getModuleList()) {
            modules.onLoad();
        }
    }
}
