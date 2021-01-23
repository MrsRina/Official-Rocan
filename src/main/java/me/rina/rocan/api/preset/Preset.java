package me.rina.rocan.api.preset;

import me.rina.rocan.api.ISavableLoadable;

/**
 * @author SrRina
 * @since 22/01/2021 at 21:01
 **/
public class Preset implements ISavableLoadable {
    private String name;
    private String data;
    private String path;

    public Preset(String name, String data, String path) {
        this.name = name;
        this.data = data;
        this.path = path;
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

    @Override
    public void onSave() {

    }

    @Override
    public void onLoad() {

    }
}