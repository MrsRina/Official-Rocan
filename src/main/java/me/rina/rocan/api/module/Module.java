package me.rina.rocan.api.module;

import com.google.gson.*;
import com.google.gson.internal.LazilyParsedNumber;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISavableLoadable;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.setting.value.ValueString;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.turok.util.TurokClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public class Module implements ISavableLoadable {
    public Minecraft mc = Rocan.MC;

    private String name, tag, description;
    private String infoHUDEnabledList;

    private ModuleCategory category;

    private boolean isEnabled;
    private int keyCode;

    private ArrayList<Setting> settingList;

    public static ValueBoolean showModuleEnableList = new ValueBoolean("Show Module Enabled List", "ShowModuleEnabledList", "Show in component Module Enabled List", true);
    public static ValueBoolean toggleMessage = new ValueBoolean("Toggle Message", "ToggleMessage", "Send a message when enable or disable.", true);

    /*
     * Frustum camera for render in 3D space.
     */
    public ICamera camera = new Frustum();

    public Module(String name, String tag, String description, ModuleCategory category) {
        this.name = name;
        this.tag = tag;

        this.description = description;
        this.infoHUDEnabledList = null;

        this.category = category;

        this.isEnabled = false;
        this.keyCode = -1;

        // We need registry the currents two settings at own Module.
        this.registry(showModuleEnableList);
        this.registry(toggleMessage);
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

    public void setInfoHUDEnabledList(String infoHUDEnabledList) {
        this.infoHUDEnabledList = infoHUDEnabledList;
    }

    public String getInfoHUDEnabledList() {
        return infoHUDEnabledList;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setEnabled(boolean enabled) {
        if (this.isEnabled != enabled) {
            this.isEnabled = enabled;

            this.onReload();
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void registry(Setting setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList<>();
        }

        this.settingList.add(setting);
    }

    public void unregister(Setting setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList<>();
        } else {
            if (this.get(setting.getClass()) != null) {
                this.settingList.remove(setting);
            }
        }
    }

    public void setSettingList(ArrayList<Setting> settingList) {
        this.settingList = settingList;
    }

    public ArrayList<Setting> getSettingList() {
        return settingList;
    }

    public Setting get(Class<?> clazz) {
        for (Setting settings : this.settingList) {
            if (settings.getClass() == clazz) {
                return settings;
            }
        }

        return null;
    }

    public Setting get(String tag) {
        for (Setting settings : this.settingList) {
            if (settings.getTag().equalsIgnoreCase(tag)) {
                return settings;
            }
        }

        return null;
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled);
    }

    public void onReload() {
        if (this.isEnabled) {
            this.setEnabled();
        } else {
            this.setDisabled();
        }
    }

    public void setEnabled() {
        this.isEnabled = true;

        if (toggleMessage.getValue()) {
            ChatUtil.print(this.tag + " enabled.");
        }

        Rocan.EVENT_BUS.register(this);

        this.onEnable();
    }

    public void setDisabled() {
        this.isEnabled = false;

        if (toggleMessage.getValue()) {
            ChatUtil.print(this.tag + " disabled.");
        }

        Rocan.EVENT_BUS.remove(this);

        this.onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    public void onRender2D() {}
    public void onRender3D() {}

    /**
     * The current context of setting enums.
     */
    public void onSetting() {}

    @Override
    public void onSave() {
        try {
            String pathFolder = Rocan.PATH_CONFIG + "/module/" + this.category.name().toLowerCase() + "/";
            String pathFile = pathFolder + this.tag + ".json";

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

            mainJson.add("enabled", new JsonPrimitive(this.isEnabled));

            JsonObject jsonSettingList = new JsonObject();

            for (Setting settings : this.settingList) {
                if (settings instanceof ValueBoolean) {
                    ValueBoolean settingValueBoolean = (ValueBoolean) settings;

                    jsonSettingList.add(settingValueBoolean.getTag(), new JsonPrimitive(settingValueBoolean.getValue()));
                }

                if (settings instanceof ValueNumber) {
                    ValueNumber settingValueNumber = (ValueNumber) settings;

                    jsonSettingList.add(settingValueNumber.getTag(), new JsonPrimitive(settingValueNumber.getValue()));
                }

                if (settings instanceof ValueEnum) {
                    ValueEnum settingValueEnum = (ValueEnum) settings;

                    jsonSettingList.add(settingValueEnum.getTag(), new JsonPrimitive(settingValueEnum.getValue().name()));
                }

                if (settings instanceof ValueString) {
                    ValueString settingValueString = (ValueString) settings;

                    jsonSettingList.add(settingValueString.getTag(), new JsonPrimitive(settingValueString.getValue()));

                }
            }

            mainJson.add("settings", jsonSettingList);

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
            String pathFolder = Rocan.PATH_CONFIG + "/module/" + this.category.name().toLowerCase() + "/";
            String pathFile = pathFolder + this.tag + ".json";

            if (Files.exists(Paths.get(pathFile)) == false) {
                return;
            }

            InputStream file = Files.newInputStream(Paths.get(pathFile));

            JsonObject mainJson = new JsonParser().parse(new InputStreamReader(file)).getAsJsonObject();

            if (mainJson.get("enabled") != null) {
                this.setEnabled(mainJson.get("enabled").getAsBoolean());
            }

            if (mainJson.get("settings") != null) {
                JsonObject jsonSettingList = mainJson.get("settings").getAsJsonObject();

                for (Setting settings : this.settingList) {
                    if (jsonSettingList.get(settings.getTag()) == null) {
                        continue;
                    }

                    if (settings instanceof ValueBoolean) {
                        ValueBoolean settingValueBoolean = (ValueBoolean) settings;

                        settingValueBoolean.setValue(jsonSettingList.get(settings.getTag()).getAsBoolean());
                    }

                    if (settings instanceof ValueNumber) {
                        ValueNumber settingValueNumber = (ValueNumber) settings;

                        if (jsonSettingList.get(settings.getTag()).getAsNumber() instanceof Float) {
                            settingValueNumber.setValue(jsonSettingList.get(settings.getTag()).getAsFloat());
                        }

                        if (jsonSettingList.get(settings.getTag()).getAsNumber() instanceof Double) {
                            settingValueNumber.setValue(jsonSettingList.get(settings.getTag()).getAsDouble());
                        }

                        if (jsonSettingList.get(settings.getTag()).getAsNumber() instanceof Integer) {
                            settingValueNumber.setValue(jsonSettingList.get(settings.getTag()).getAsInt());
                        }
                    }

                    if (settings instanceof ValueEnum) {
                        ValueEnum settingValueEnum = (ValueEnum) settings;

                        settingValueEnum.setValue(TurokClass.getEnumByName(settingValueEnum.getValue(), jsonSettingList.get(settings.getTag()).getAsString()));
                    }
                }
            }

            file.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
