package me.rina.rocan.api.setting;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public class Setting <S> {
    private String name, tag, description;

    private S value;

    private S minimum;
    private S maximum;

    /*
     * So it is basically the render for GUI,
     * when false won't render the setting,
     * this make the client very pretty and organized.
     */
    private boolean enabled = true;

    public Setting(String name, String tag, String description, S value) {
        this.name = name;
        this.tag = tag;

        this.description = description;

        this.value = value;
    }

    public Setting(String name, String tag, String description, S value, S minimum, S maximum) {
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

    public void setValue(S value) {
        this.value = value;
    }

    public S getValue() {
        return value;
    }

    public void setMinimum(S minimum) {
        this.minimum = minimum;
    }

    public S getMinimum() {
        return minimum;
    }

    public void setMaximum(S maximum) {
        this.maximum = maximum;
    }

    public S getMaximum() {
        return maximum;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}