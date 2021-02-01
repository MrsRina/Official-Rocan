package me.rina.rocan.api.setting.value;

import me.rina.rocan.api.setting.Setting;
import net.minecraft.client.Minecraft;

/**
 * @author SrRina
 * @since 01/02/2021 at 12:25
 **/
public class ValueBind extends Setting {
    private boolean state;
    private int keyCode;

    public ValueBind(String name, String tag, String description, int key) {
        super(name, tag, description);

        this.keyCode = key;
    }

    public void setKeyCode(int key) {
        this.keyCode = key;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}
