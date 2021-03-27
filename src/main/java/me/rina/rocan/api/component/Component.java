package me.rina.rocan.api.component;

import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.component.impl.ComponentType;
import me.rina.rocan.api.component.registry.Registry;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.turok.util.TurokRect;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 23/03/2021 at 15:21
 **/
public class Component implements ISLClass {
    private String name = getRegistry().name();
    private String tag = getRegistry().tag();
    private String description = getRegistry().description();

    private ComponentType type = getRegistry().type();
    private ArrayList<Setting> settingList;

    private boolean isEnabled;
    protected TurokRect rect;

    private ValueBoolean settingCustomFont = new ValueBoolean("Custom Font", "CustomFont", "Smooth font to render.", false);
    private ValueBoolean settingShadowFont = new ValueBoolean("Shadow Font", "ShadowFont", "Shadow font to render.", true);
    private ValueBoolean settingRGB = new ValueBoolean("RGB", "RGB", "RGB effect to render, else false HUD module color.", false);

    public Component() {
        this.rect = new TurokRect("unknown", 0, 0);

        if (this.type == ComponentType.TEXT) {
            this.registry(settingCustomFont);
            this.registry(settingShadowFont);
            this.registry(settingRGB);
        }
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

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public ComponentType getType() {
        return type;
    }

    public void setSettingList(ArrayList<Setting> settingList) {
        this.settingList = settingList;
    }

    public ArrayList<Setting> getSettingList() {
        return settingList;
    }

    public void setRect(TurokRect rect) {
        this.rect = rect;
    }

    public TurokRect getRect() {
        return rect;
    }

    public Registry getRegistry() {
        Registry details = null;

        if (getClass().isAnnotationPresent(Registry.class)) {
            details = getClass().getAnnotation(Registry.class);
        }

        return details;
    }

    public void registry(Setting setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList<>();
        }

        this.settingList.add(setting);
    }

    public void unregister(Setting setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList<>();
        } else {
            if (this.get(setting.getClass()) != null) {
                this.settingList.remove(setting);
            }
        }
    }

    public Setting get(Class<?> clazz) {
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

    /**
     * Override renders HUD.
     *
     * @param partialTicks - The partial ticks of Minecraft overlay/GUI;
     */
    public void onRenderHUD(float partialTicks) {}

    @Override
    public void onSave() {

    }

    @Override
    public void onLoad() {

    }
}
