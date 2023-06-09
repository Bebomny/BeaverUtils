package dev.bebomny.beaver.beaverutils.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.Features;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigHandler {

    private final MinecraftClient client;
    private final BeaverUtilsClient beaverUtilsClient;
    private final Logger LOGGER;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
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
            configDirectory.toFile().mkdirs();
        } catch (Exception ignored) {}

        LOGGER.atInfo().log("Creating new config file");
        configFile = new File(configDirectory.toFile(), "config.json");

        if(configFile.exists()) {
            LOGGER.atInfo().log("Entering main If Statement");
            try {
                LOGGER.atInfo().log("Creating Input Stream Reader");
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                if(beaverUtilsClient.features == null)
                    LOGGER.atInfo().log("Features are equal to null");

                LOGGER.atInfo().log("Loading from JSON");
                beaverUtilsClient.features = gson.fromJson(
                        bufferedReader,
                        Features.class
                );

                LOGGER.atInfo().log("Loaded Config from file");
            } catch (IOException e) {
                LOGGER.atError().log("Error at Config load");
                e.printStackTrace();
                LOGGER.atError().log("Exception while reading " + configFile.getName() + ". Will load blank config");
            }
        }

        if(beaverUtilsClient.features == null) {
            // If the config does not exist, generate the default one
            LOGGER.atInfo().log("Creating blank config and saving to file at " + configFile.getPath());
            beaverUtilsClient.features = new Features();
            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            LOGGER.atInfo().log("Saving config file at: " + configFile.toPath());
            configFile.getParentFile().mkdirs();
            if (!configFile.createNewFile())
                LOGGER.atInfo().log("Something went wrong when creating the Config File OR the Config File already exists");
            BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
            writer.write(gson.toJson(beaverUtilsClient.features));
            writer.close();
        } catch (IOException e) {
            LOGGER.atError().log("Could not save config file to " + configFile.getPath(), e);
            e.printStackTrace();
        }
    }

    public void resetConfig() {
        LOGGER.atWarn().log("Resetting Config to defaults");
        beaverUtilsClient.features = new Features();
        saveConfig();
    }
}
