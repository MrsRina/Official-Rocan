package me.rina.rocan.api.preset.management;

import com.google.gson.*;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.preset.Preset;
import me.rina.rocan.api.social.Social;
import me.rina.rocan.api.social.type.SocialType;
import me.rina.turok.util.TurokClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 23/01/2021 at 14:00
 **/
public class PresetManager implements ISLClass {
    public static PresetManager INSTANCE;

    private ArrayList<Preset> presetList;
    private Preset currentPreset;

    public PresetManager() {
        INSTANCE = this;

        this.presetList = new ArrayList<>();
    }

    public void registry(Preset preset) {
        this.presetList.add(preset);
    }

    public void unregister(Preset preset) {
        if (get(preset.getClass()) != null) {
            this.presetList.remove(preset);
        }
    }

    public void setPresetList(ArrayList<Preset> presetList) {
        this.presetList = presetList;
    }

    public ArrayList<Preset> getPresetList() {
        return presetList;
    }

    public void setCurrentPreset(Preset currentPreset) {
        this.currentPreset = currentPreset;
    }

    public Preset getCurrentPreset() {
        return currentPreset;
    }

    public static Preset get(Class<?> clazz) {
        for (Preset presets : INSTANCE.getPresetList()) {
            if (presets.getClass() == clazz) {
                return presets;
            }
        }

        return null;
    }

    public static Preset get(String name) {
        for (Preset presets : INSTANCE.getPresetList()) {
            if (presets.getName().equalsIgnoreCase(name)) {
                return presets;
            }
        }

        return null;
    }

    public static void finish() {
        Rocan.getModuleManager().onSave();
        Rocan.getSocialManager().onSave();

        INSTANCE.onSave();
    }

    public static void reload() {
        if (INSTANCE.currentPreset != null) {
            INSTANCE.currentPreset.onLoad();
        }

        Rocan.getModuleManager().onLoad();
        Rocan.getSocialManager().onLoad();
    }

    @Override
    public void onSave() {
        try {
            String pathFolder = Rocan.PATH_CONFIG + "/";
            String pathFile = pathFolder + "Preset" + ".json";

            Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

            if (Files.exists(Paths.get(pathFolder)) == false) {
                Files.createDirectories(Paths.get(pathFolder));
            }

            if (Files.exists(Paths.get(pathFile))) {
                java.io.File file = new java.io.File(pathFile);
                file.delete();
            } else {
                Files.createFile(Paths.get(pathFile));
            }

            JsonObject mainJson = new JsonObject();

            mainJson.add("current", new JsonPrimitive(this.currentPreset.getName()));

            for (Preset presets : this.presetList) {
                JsonObject presetJson = new JsonObject();

                presetJson.add("name", new JsonPrimitive(presets.getName()));
                presetJson.add("data", new JsonPrimitive(presets.getData()));
                presetJson.add("path", new JsonPrimitive(presets.getData()));

                mainJson.add(presets.getName(), presetJson);
            }

            String stringJson = gsonBuilder.toJson(new JsonParser().parse(mainJson.toString()));

            OutputStreamWriter fileOutputStream = new OutputStreamWriter(new FileOutputStream(pathFile), "UTF-8");

            fileOutputStream.write(stringJson);
            fileOutputStream.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
        try {
            String pathFolder = Rocan.PATH_CONFIG + "/";
            String pathFile = pathFolder + "Preset" + ".json";

            if (Files.exists(Paths.get(pathFile)) == false) {
                return;
            }

            InputStream file = Files.newInputStream(Paths.get(pathFile));

            JsonObject mainJson = new JsonParser().parse(new InputStreamReader(file)).getAsJsonObject();
            JsonArray mainJsonArray = new JsonParser().parse(new InputStreamReader(file)).getAsJsonArray();

            String currentPresetName = "";

            if (mainJson.get("current") != null) {
                currentPresetName = mainJson.get("current").getAsString();
            }

            for (JsonElement element : mainJsonArray) {
                JsonObject presetJson = element.getAsJsonObject();

                if (presetJson.get("name") == null) {
                    continue;
                }

                Preset preset = new Preset(presetJson.get("name").getAsString(), "");

                if (presetJson.get("data") != null) {
                    preset.setData(presetJson.get("data").getAsString());
                }

                if (presetJson.get("path") != null) {
                    preset.setPath(presetJson.get("path").getAsString());
                }

                if (preset.getName().equalsIgnoreCase(currentPresetName)) {
                    this.currentPreset = preset;
                }

                this.registry(preset);
            }

            file.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
