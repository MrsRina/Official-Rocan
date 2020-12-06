package me.rina.rocan.api.module;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.util.chat.ChatUtil;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public class Module {
    private String name;
    private String tag;

    private String description;
    private String infoHUDEnabledList;

    private ModuleCategory category;

    private boolean isEnabled;
    private int keyCode;

    private ArrayList<Setting> settingList;

    public static Setting showModuleEnableList = new Setting("Show Module Enabled List", "ShowModuleEnabledList", "Show in component Module Enabled List", true);
    public static Setting toggleMessage = new Setting("Toggle Message", "ToggleMessage", "Send a message when enable or disable.", true);

    public Module(String name, String tag, String description, ModuleCategory category) {
        this.name = name;
        this.tag = tag;

        this.description = description;
        this.infoHUDEnabledList = null;

        this.category = category;

        this.isEnabled = false;
        this.keyCode = -1;

        this.registry(showModuleEnableList);
        this.registry(toggleMessage);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setInfoHUDEnabledList(String infoHUDEnabledList) {
        this.infoHUDEnabledList = infoHUDEnabledList;
    }

    public String getInfoHUDEnabledList() {
        return infoHUDEnabledList;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setEnabled(boolean enabled) {
        if (isEnabled != enabled) {
            this.isEnabled = isEnabled;

            this.onReload();
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void registry(Setting setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList<>();
        }

        this.settingList.add(setting);
    }

    public Setting get(Class clazz) {
        for (Setting settings : this.settingList) {
            if (settings.getClass() == clazz) {
                return settings;
            }
        }

        return null;
    }

    public Setting get(String tag) {
        for (Setting settings : this.settingList) {
            if (settings.getTag().equalsIgnoreCase(tag)) {
                return settings;
            }
        }

        return null;
    }

    /*
     * Tools.
     */
    public void toggle() {
        this.setEnabled(!this.isEnabled);
    }

    public void onReload() {
        if (this.isEnabled) {
            this.setEnabled();
        } else {
            this.setDisabled();
        }
    }

    public void setEnabled() {
        this.isEnabled = true;

        if ((boolean) toggleMessage.getValue()) {
            ChatUtil.print(this.tag + " enabled.");
        }

        Rocan.EVENT_BUS.addEventListener(this);

        this.onEnable();
    }

    public void setDisabled() {
        this.isEnabled = false;

        if ((boolean) toggleMessage.getValue()) {
            ChatUtil.print(this.tag + " disabled.");
        }

        Rocan.EVENT_BUS.removeEventListener(this);

        this.onDisable();
    }

    /*
     * Overrides.
     */
    public void onUpdate() {}

    protected void onEnable() {}
    protected void onDisable() {}
}
