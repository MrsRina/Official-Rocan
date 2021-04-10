package me.rina.rocan.api.preset;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.preset.impl.PresetState;
import me.rina.rocan.api.util.file.FileUtil;
import me.rina.turok.util.TurokGeneric;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author SrRina
 * @since 22/01/2021 at 21:01
 **/
public class Preset {
    private String name;
    private String data;
    private String path;

    private PresetState state;
    private TurokGeneric<Boolean> current;

    public Preset(String name, String path, String data) {
        this.name = name;
        this.data = data;
        this.path = path.toLowerCase();

        this.state = PresetState.OPERABLE;
        this.current = new TurokGeneric<>(false);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setState(PresetState state) {
        this.state = state;
    }

    public PresetState getState() {
        return state;
    }

    public void setCurrent(TurokGeneric<Boolean> current) {
        this.current = current;
    }

    public TurokGeneric<Boolean> getCurrent() {
        return current;
    }

    public void setCurrent(boolean is) {
        this.current.setValue(is);
    }

    public boolean isCurrent() {
        return current.getValue();
    }
}