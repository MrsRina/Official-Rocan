package me.rina.rocan.api.module;

import com.google.gson.*;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
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
public class Module implements ISLClass {
    public Minecraft mc = Rocan.MC;

    private String name;
    private String tag;

    private String description;
    private String infoHUDEnabledList;

    private ModuleCategory category;

    private boolean isEnabled;
    private int keyCode;

    private ArrayList<Setting> settingList;

    public static Setting showModuleEnableList = new Setting("Show Module Enabled List", "ShowModuleEnabledList", "Show in component Module Enabled List", true);
    public static Setting toggleMessage = new Setting("Toggle Message", "ToggleMessage", "Send a message when enable or disable.", true);

    /*
     * Basic render;
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

    public Setting get(Class clazz) {
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

    /*
     * Tools.
     */
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

        if ((boolean) toggleMessage.getValue()) {
            ChatUtil.print(this.tag + " enabled.");
        }

        Rocan.EVENT_BUS.register(this);

        this.onEnable();
    }

    public void setDisabled() {
        this.isEnabled = false;

        if ((boolean) toggleMessage.getValue()) {
            ChatUtil.print(this.tag + " disabled.");
        }

        Rocan.EVENT_BUS.remove(this);

        this.onDisable();
    }

    /*
     * Overrides.
     */
    protected void onEnable() {}
    protected void onDisable() {}

    /*
     * Class content;
     */
    @Override
    public void onSave() {
        try {
            String pathFolder = Rocan.PATH_CONFIG + "/module/" + this.category.name().toLowerCase() + "/";
            String pathFile = pathFolder + this.tag + ".json";

            Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

            if (!Files.exists(Paths.get(pathFolder))) {
                Files.createDirectories(Paths.get(pathFolder));
            }

            if (!Files.exists(Paths.get(pathFile))) {
                Files.createFile(Paths.get(pathFile));
            } else {
                java.io.File file = new java.io.File(pathFile);
                file.delete();
            }

            JsonObject mainJson = new JsonObject();

            mainJson.add("enabled", new JsonPrimitive(this.isEnabled));

            JsonObject jsonSettingList = new JsonObject();

            for (Setting settings : this.settingList) {
                if (settings.getValue() instanceof Boolean) {
                    jsonSettingList.add(settings.getTag(), new JsonPrimitive((Boolean) settings.getValue()));
                }

                if (settings.getValue() instanceof Number) {
                    jsonSettingList.add(settings.getTag(), new JsonPrimitive((Number) settings.getValue()));
                }

                if (settings.getValue() instanceof Enum<?>) {
                    jsonSettingList.add(settings.getTag(), new JsonPrimitive(((Enum<?>) settings.getValue()).name()));
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

            if (!Files.exists(Paths.get(pathFile))) {
                return;
            }

            InputStream file = Files.newInputStream(Paths.get(pathFile));

            JsonObject mainJson = new JsonParser().parse(new InputStreamReader(file)).getAsJsonObject();

            if(mainJson.get("enabled") != null) this.setEnabled(mainJson.get("enabled").getAsBoolean());

            if (mainJson.get("settings") != null) {
                JsonObject jsonSettingList = mainJson.get("settings").getAsJsonObject();

                for (Setting settings : this.settingList) {
                    if (jsonSettingList.get(settings.getTag()) == null) {
                        continue;
                    }

                    if (settings.getValue() instanceof Boolean) {
                        settings.setValue(jsonSettingList.get(settings.getTag()).getAsBoolean());
                    }

                    if (settings.getValue() instanceof Number) {
                        settings.setValue(jsonSettingList.get(settings.getTag()).getAsNumber());
                    }

                    if (settings.getValue() instanceof Enum<?>) {
                        settings.setValue(TurokClass.getEnumByName((Enum<?>) settings.getValue(), jsonSettingList.get(settings.getTag()).getAsString()));
                    }
                }
            }

            file.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
