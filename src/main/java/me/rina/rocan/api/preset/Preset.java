package me.rina.rocan.api.preset;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISavableLoadable;
import me.rina.rocan.api.util.file.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author SrRina
 * @since 22/01/2021 at 21:01
 **/
public class Preset implements ISavableLoadable {
    private String name;
    private String data;
    private String path;

    public Preset(String name, String data) {
        this.name = name;
        this.data = data;
        this.path = Rocan.PATH_CONFIG + "/preset/" + name + ".zip";
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
        try {
            Rocan.getModuleManager().onSave();
            Rocan.getSocialManager().onSave();

            String pathFolder = Rocan.PATH_CONFIG + "/preset/";
            String pathModule = Rocan.PATH_CONFIG + "/module/";

            if (Files.exists(Paths.get(pathFolder)) == false) {
                Files.createDirectories(Paths.get(pathFolder));
            }

            FileUtil.compactZipFolder(pathModule, this.path);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
        try {
            String pathFolder = Rocan.PATH_CONFIG + "/preset/";
            String pathModule = Rocan.PATH_CONFIG + "/module/";

            if (Files.exists(Paths.get(pathFolder)) == false) {
                Files.createDirectories(Paths.get(pathFolder));
            }

            if (Files.exists(Paths.get(this.path)) == false) {
                return;
            }

            FileUtil.extractZipFolder(this.path, pathModule);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}