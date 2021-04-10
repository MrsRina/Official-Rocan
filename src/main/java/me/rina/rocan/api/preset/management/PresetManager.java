package me.rina.rocan.api.preset.management;

import com.google.gson.*;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.preset.Preset;
import me.rina.rocan.api.preset.impl.PresetState;
import me.rina.turok.util.TurokGeneric;

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

    /**
     * The policy protection for path in Rocan, every file handled and managed at Rocan
     * will pass and verified by policy path.
     */
    private TurokGeneric<String> policyProtection;

    public PresetManager() {
        INSTANCE = this;

        this.presetList = new ArrayList<>();
        this.policyProtection = new TurokGeneric<>("default");
    }

    public void setPolicyProtection(TurokGeneric<String> preset) {
        this.policyProtection = preset;
    }

    public TurokGeneric<String> getPolicyProtection() {
        return policyProtection;
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

    public void setCurrent(Preset preset) {
        for (Preset presets : this.presetList) {
            preset.setCurrent(false);
        }

        for (Preset presets : this.presetList) {
            if (preset.getName().equalsIgnoreCase(preset.getName())) {
                preset.setCurrent(true);

                break;
            }
        }
    }

    public static Preset current() {
        Preset preset = null;

        int countTarget = 0;
        int countTotally = 0;

        for (Preset presets : INSTANCE.presetList) {
            if (presets.isCurrent() && presets.getState() == PresetState.OPERABLE) {
                preset = presets;

                countTarget++;
            }

            countTotally++;
        }

        System.out.println("Rocan verified " + countTotally + " presets but found " + countTarget + " conflicts.");
        System.out.println("Rocan processed " + (preset == null ? "default" : preset.getName()) + " preset.");

        return preset;
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

    public static void shutdown() {
        System.out.println("Rocan is saving.");
        refresh();

        Rocan.getModuleManager().onSave();
        Rocan.getSocialManager().onSave();

        INSTANCE.onSave();

        System.out.println("Rocan saved 2 managers.");
    }

    public static void reload() {
        System.out.println("Rocan preprocess reload.");

        INSTANCE.onLoad();

        refresh();

        Rocan.getModuleManager().onLoad();
        Rocan.getSocialManager().onLoad();

        System.out.println("Rocan processed 2 managers.");
    }

    public static void refresh() {
        Preset preset = current();

        if (preset == null) {
            preset = get("Default");

            if (preset == null) {
                preset = new Preset("Default", "Default", "Unknown");
            }

            INSTANCE.setCurrent(preset);
        }

        String path = Rocan.PATH_CONFIG + "presets/" + preset.getPath();

        INSTANCE.policyProtection.setValue(Files.exists(Paths.get(path)) ? preset.getPath() : "default");
    }

    public static String getPolicyProtectionValue() {
        return Rocan.PATH_CONFIG + "presets/" + INSTANCE.getPolicyProtection().getValue();
    }

    @Override
    public void onSave() {
        try {
            String pathFolder = Rocan.PATH_CONFIG + "/";
            String pathFile = pathFolder + "Preset" + ".json";

            Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();

            if (Files.exists(Paths.get(pathFolder)) == false) {
                Files.createDirectories(Paths.get(pathFolder));
            }

            if (Files.exists(Paths.get(pathFile))) {
                java.io.File file = new java.io.File(pathFile);
                file.delete();
            }

            Files.createFile(Paths.get(pathFile));

            JsonObject mainJson = new JsonObject();
            JsonArray mainJsonArray = new JsonArray();

            for (Preset presets : this.presetList) {
                JsonObject presetJson = new JsonObject();

                presetJson.add("name", new JsonPrimitive(presets.getName()));
                presetJson.add("data", new JsonPrimitive(presets.getData()));
                presetJson.add("path", new JsonPrimitive(presets.getData()));
                presetJson.add("current", new JsonPrimitive(presets.isCurrent()));

                mainJsonArray.add(presetJson);
            }

            mainJson.add("presets", mainJsonArray);

            String stringJson = gsonBuilder.toJson(jsonParser.parse(mainJson.toString()));
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
            JsonParser jsonParser = new JsonParser();
            JsonObject mainJson = jsonParser.parse(new InputStreamReader(file)).getAsJsonObject();

            if (mainJson.get("presets") == null) {
                file.close();

                return;
            }

            JsonArray mainJsonArray = mainJson.get("presets").getAsJsonArray();

            for (JsonElement element : mainJsonArray) {
                JsonObject presetJson = element.getAsJsonObject();

                if (presetJson.get("name") == null || presetJson.get("path") == null) {
                    continue;
                }

                Preset preset = new Preset(presetJson.get("name").getAsString(), presetJson.get("path").getAsString(), "");

                if (presetJson.get("current") != null) {
                    preset.setCurrent(presetJson.get("current").getAsBoolean());
                }

                if (presetJson.get("data") != null) {
                    preset.setData(presetJson.get("data").getAsString());
                }

                this.registry(preset);
            }

            file.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
