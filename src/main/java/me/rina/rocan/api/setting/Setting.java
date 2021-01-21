package me.rina.rocan.api.setting;

import me.rina.rocan.api.setting.impl.SettingStructure;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public class Setting implements SettingStructure {
    private String name;
    private String tag;
    private String description;

    /**
     * Set if render or no the setting, when its faLse the setting won't be manageable,
     * to works you need refresh at GUI later changed.
     */
    private boolean enabled = true;

    public Setting(String name, String tag, String description) {
        this.name = name;
        this.tag = tag;
        this.description = description;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}