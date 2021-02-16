package me.rina.rocan.api.module;

import com.google.gson.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.*;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.mixin.interfaces.IMinecraft;
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
public class Module implements ISLClass {
    public Minecraft mc = Rocan.MC;

    private String name = getResgistry().name();
    private String tag = getResgistry().tag();

    private String description = getResgistry().description();
    private ModuleCategory category = getResgistry().category();

    private String status;
    private ArrayList<Setting> settingList;

    private ValueBind keyBinding = new ValueBind("Key Bind", "KeyBind", "Key bind to active or disable module.", -1);

    private ValueBoolean showModuleEnableList = new ValueBoolean("Show Module Enabled List", "ShowModuleEnabledList", "Show in component Module Enabled List", true);
    private ValueBoolean toggleMessage = new ValueBoolean("Toggle Message", "ToggleMessage", "Alert if is toggled.", false);

    public me.rina.rocan.mixin.interfaces.IMinecraft imc = (IMinecraft) Minecraft.getMinecraft();

    /*
     * Frustum camera for render in 3D space.
     */
    public ICamera camera = new Frustum();

    public Module() {
        // We need registry the currents pre settings at own Module class.
        this.registry(keyBinding);
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setEnabled(boolean enabled) {
        if (this.keyBinding.getState() != enabled) {
            this.keyBinding.setState(enabled);

            this.onReload();
        }
    }

    public boolean isEnabled() {
        return keyBinding.getState();
    }

    public void setKeyCode(int key) {
        this.keyBinding.setKeyCode(key);
    }

    public int getKeyCode() {
        return keyBinding.getKeyCode();
    }

    public void setSettingList(ArrayList<Setting> settingList) {
        this.settingList = settingList;
    }

    public ArrayList<Setting> getSettingList() {
        return settingList;
    }

    public Registry getResgistry() {
        Registry details = null;

        if (getClass().isAnnotationPresent(Registry.class)) {
            details = getClass().getAnnotation(Registry.class);
        }

        return details;
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

    public void onKeyPressed(int key) {
        for (Setting settings : this.settingList) {
            if (settings instanceof ValueBind) {
                ValueBind settingValueBind = (ValueBind) settings;

                if (settingValueBind.getKeyCode() == key) {
                    settingValueBind.setState(!settingValueBind.getState());

                    if (settingValueBind.getClass() == this.keyBinding.getClass()) {
                        this.onReload();
                    }
                }
            }
        }
    }

    public void toggle() {
        this.setEnabled(!this.keyBinding.getState());
    }

    public void onReload() {
        if (this.keyBinding.getState()) {
            this.setEnabled();
        } else {
            this.setDisabled();
        }
    }

    public void setEnabled() {
        this.keyBinding.setState(true);

        if (toggleMessage.getValue()) {
            this.print(ChatFormatting.GREEN + "Enabled");
        }

        this.onEnable();

        Rocan.getPomeloEventManager().addEventListener(this);
    }

    public void setDisabled() {
        this.keyBinding.setState(false);

        if (toggleMessage.getValue()) {
            this.print(ChatFormatting.RED + "Disabled");
        }

        this.onDisable();

        Rocan.getPomeloEventManager().removeEventListener(this);
    }

    public void print(String message) {
        ChatUtil.print(ChatFormatting.GRAY + this.name + " " + ChatFormatting.WHITE + message);
    }

    public void onEnable() {}
    public void onDisable() {}

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

                if (settings instanceof ValueBind) {
                    ValueBind settingValueBind = (ValueBind) settings;

                    // We create a object json to save key and state.
                    JsonObject valueBindObject = new JsonObject();

                    valueBindObject.add("key", new JsonPrimitive(settingValueBind.getKeyCode()));
                    valueBindObject.add("state", new JsonPrimitive(settingValueBind.getState()));

                    jsonSettingList.add(settingValueBind.getTag(), valueBindObject);
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

            JsonObject mainJson = JsonParser.parseReader(new InputStreamReader(file)).getAsJsonObject();

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

                    if (settings instanceof ValueBind) {
                        ValueBind settingValueBind = (ValueBind) settings;

                        // Get the values from bind as json object.
                        JsonObject valueBindObject = jsonSettingList.get(settingValueBind.getTag()).getAsJsonObject();

                        if (valueBindObject.get("key") != null) {
                            settingValueBind.setKeyCode(valueBindObject.get("key").getAsInt());
                        }

                        if (valueBindObject.get("state") != null) {
                            settingValueBind.setState(valueBindObject.get("state").getAsBoolean());
                        }
                    }
                }
            }

            file.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
