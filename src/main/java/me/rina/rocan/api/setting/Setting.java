package me.rina.rocan.api.setting;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public class Setting {
    private String name;
    private String tag;

    private String description;

    private Object value;

    private Object minimum;
    private Object maximum;

    public Setting(String name, String tag, String description, Object value) {
        this.name = name;
        this.tag = tag;

        this.description = description;

        this.value = value;
    }

    public Setting(String name, String tag, String description, Object value, Object minimum, Object maximum) {
        this.name = name;
        this.tag = tag;

        this.description = description;

        this.value = value;

        this.minimum = minimum;
        this.maximum = maximum;
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

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public Class[] getValueClasses() {
        return value.getClass().getClasses();
    }

    public void setMinimum(Object minimum) {
        this.minimum = minimum;
    }

    public Object getMinimum() {
        return minimum;
    }

    public void setMaximum(Object maximum) {
        this.maximum = maximum;
    }

    public Object getMaximum() {
        return maximum;
    }
}