package me.rina.rocan.api.setting.value;

import me.rina.rocan.api.setting.Setting;

/**
 * @author SrRina
 * @since 20/01/2021 at 09:57
 **/
public class ValueString extends Setting {
    private String value;

    public ValueString(String name, String tag, String description, String value) {
        super(name, tag, description);

        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
