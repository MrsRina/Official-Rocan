package me.rina.rocan.api.setting.value;

import me.rina.rocan.api.setting.Setting;

/**
 * @author SrRina
 * @since 20/01/2021 at 09:55
 **/
public class ValueEnum extends Setting {
    private Enum<?> value;

    public ValueEnum(String name, String tag, String description, Enum<?> value) {
        super(name, tag, description);

        this.value = value;
    }

    public void setValue(Enum<?> value) {
        this.value = value;
    }

    public Enum<?> getValue() {
        return value;
    }
}