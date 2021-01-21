package me.rina.rocan.api.setting.value;

import me.rina.rocan.api.setting.Setting;

/**
 * @author SrRina
 * @since 20/01/2021 at 09:53
 **/
public class ValueNumber extends Setting {
    private Number value;

    private Number minimum;
    private Number maximum;

    public ValueNumber(String name, String tag, String description, Number value, Number minimum, Number maximum) {
        super(name, tag, description);

        this.value = value;

        this.minimum = minimum;
        this.maximum = maximum;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    public void setMinimum(Number minimum) {
        this.minimum = minimum;
    }

    public Number getMinimum() {
        return minimum;
    }

    public void setMaximum(Number maximum) {
        this.maximum = maximum;
    }

    public Number getMaximum() {
        return maximum;
    }
}
