package dev.bebomny.beaver.beaverutils.configuration;

import com.google.gson.*;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.LeftMouseClickActionImpl;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.MouseClickAction;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.RightMouseClickActionImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigHandler {

    private final MinecraftClient client;
    private final BeaverUtilsClient beaverUtilsClient;
    private final Logger LOGGER;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Vec3d.class, new Vec3dSerializer())
            .registerTypeAdapter(Vec3d.class, new Vec3dDeserializer())
            .registerTypeAdapter(MouseClickAction.class, new MouseClickActionAdapter())
            .create();
    public final Path configDirectory;
    private File configFile;

    public ConfigHandler() {
        this.client = MinecraftClient.getInstance();
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.LOGGER = beaverUtilsClient.getLogger("ConfigHandler");

        this.configDirectory = client.runDirectory.toPath().resolve("config/BeaverConfigs/BeaverUtils");
        this.configFile = null;
    }

    public void loadConfig() {
        try {
            if (configDirectory.toFile().mkdirs())
                LOGGER.atInfo().log("Creating a config directory");
        } catch (Exception ignored) {}

        configFile = new File(configDirectory.toFile(), "config.json");

        if(configFile.exists()) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                LOGGER.atInfo().log("Loading from JSON");
                beaverUtilsClient.config = gson.fromJson(
                        bufferedReader,
                        Config.class//Features.class
                );

                if(beaverUtilsClient.config != null)
                    LOGGER.atInfo().log("Loaded Config from file");
            } catch (IOException e) {
                LOGGER.atError().log("Error at Config load");
                e.printStackTrace();
                LOGGER.atError().log("Exception while reading " + configFile.getName() + ". Will load blank config");
            }
        }

        if(beaverUtilsClient.config == null) {
            // If the config does not exist, generate the default one
            LOGGER.atInfo().log("Creating blank config and saving to file at " + configFile.getPath());
            //beaverUtilsClient.features = new Features();
            beaverUtilsClient.config = new Config();
            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            configFile.getParentFile().mkdirs();

            if (!configFile.createNewFile())
                LOGGER.atInfo().log("Config File already exists");

            LOGGER.atInfo().log("Saving config file at: " + configFile.toPath());
            BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
            writer.write(gson.toJson(beaverUtilsClient.config));
            writer.close();
        } catch (IOException e) {
            LOGGER.atError().log("Could not save config file to " + configFile.getPath(), e);
            e.printStackTrace();
        }
    }

    public void resetConfig() {
        LOGGER.atWarn().log("Resetting Config to defaults");
        //beaverUtilsClient.features = new Features();
        beaverUtilsClient.config = new Config();
        saveConfig();
    }

    // Vec3d Serializer
    private static class Vec3dSerializer implements JsonSerializer<Vec3d> {
        @Override
        public JsonElement serialize(Vec3d src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("x", src.x);
            jsonObject.addProperty("y", src.y);
            jsonObject.addProperty("z", src.z);
            return jsonObject;
        }
    }

    // Vec3d Deserializer
    private static class Vec3dDeserializer implements JsonDeserializer<Vec3d> {
        @Override
        public Vec3d deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            double x = jsonObject.get("x").getAsDouble();
            double y = jsonObject.get("y").getAsDouble();
            double z = jsonObject.get("z").getAsDouble();
            return new Vec3d(x, y, z);
        }
    }

    public static class MouseClickActionAdapter implements JsonDeserializer<MouseClickAction>, JsonSerializer<MouseClickAction> {

        @Override
        public MouseClickAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String mouseButton = jsonObject.get("mouseButton").getAsString();

            return switch (mouseButton) {
                case "LEFT" -> context.deserialize(jsonObject, LeftMouseClickActionImpl.class);
                case "RIGHT" -> context.deserialize(jsonObject, RightMouseClickActionImpl.class);
                default -> throw new JsonParseException("Unknown type: " + mouseButton);
            };
        }

        @Override
        public JsonElement serialize(MouseClickAction src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = context.serialize(src).getAsJsonObject();
            if (src instanceof LeftMouseClickActionImpl) {
                jsonObject.addProperty("mouseButton", "LEFT");
            } else if (src instanceof RightMouseClickActionImpl) {
                jsonObject.addProperty("mouseButton", "RIGHT");
            }
            return jsonObject;
        }
    }
}
