package me.rina.rocan.api.social.management;

import com.google.gson.*;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.social.Social;
import me.rina.rocan.api.social.type.SocialType;
import me.rina.turok.util.TurokClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 22/01/2021 at 17:13
 **/
public class SocialManager implements ISLClass {
    public static SocialManager INSTANCE;

    private ArrayList<Social> socialList;

    public SocialManager() {
        INSTANCE = this;

        this.socialList = new ArrayList<>();
    }

    public void setSocialList(ArrayList<Social> socialList) {
        this.socialList = socialList;
    }

    public ArrayList<Social> getSocialList() {
        return socialList;
    }

    public void registry(Social social) {
        this.socialList.add(social);
    }

    public void unregister(Social social) {
        if (get(social.getClass()) != null) {
            this.socialList.remove(social);
        }
    }

    public static Social get(Class<?> clazz) {
        for (Social socials : INSTANCE.getSocialList()) {
            if (socials.getClass() == clazz) {
                return socials;
            }
        }

        return null;
    }

    public static Social get(String name) {
        for (Social socials : INSTANCE.getSocialList()) {
            if (socials.getName().equalsIgnoreCase(name)) {
                return socials;
            }
        }

        return null;
    }

    @Override
    public void onSave() {
        try {
            String pathFolder = Rocan.PATH_CONFIG + "/";
            String pathFile = pathFolder + "Social" + ".json";

            Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

            if (Files.exists(Paths.get(pathFolder)) == false) {
                Files.createDirectories(Paths.get(pathFolder));
            }

            if (Files.exists(Paths.get(pathFile))) {
                java.io.File file = new java.io.File(pathFile);
                file.delete();
            }

            Files.createFile(Paths.get(pathFile));

            JsonParser jsonParser = new JsonParser();
            JsonArray mainJson = new JsonArray();

            for (Social socials : this.socialList) {
                JsonObject socialJson = new JsonObject();

                socialJson.add("name", new JsonPrimitive(socials.getName()));

                if (socials.getType() != null) {
                    socialJson.add("type", new JsonPrimitive(socials.getType().toString()));
                }

                mainJson.add(socialJson);
            }

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
            String pathFile = pathFolder + "Social" + ".json";

            if (Files.exists(Paths.get(pathFile)) == false) {
                return;
            }

            JsonParser jsonParser = new JsonParser();
            InputStream file = Files.newInputStream(Paths.get(pathFile));
            JsonArray mainJson = jsonParser.parse(new InputStreamReader(file)).getAsJsonArray();

            for (JsonElement element : mainJson) {
                JsonObject socialJson = element.getAsJsonObject();

                if (socialJson.get("name") == null) {
                    continue;
                }

                Social social = new Social(socialJson.get("name").getAsString());

                if (socialJson.get("user") != null) {
                    SocialType enumRequested = (SocialType) TurokClass.getEnumByName(SocialType.UNKNOWN, socialJson.get("user").getAsString());

                    // Set type using the enum.name().
                    social.setType(enumRequested != null ? enumRequested : SocialType.UNKNOWN);

                    // Add in social list!
                    Rocan.getSocialManager().registry(social);
                }
            }

            file.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
