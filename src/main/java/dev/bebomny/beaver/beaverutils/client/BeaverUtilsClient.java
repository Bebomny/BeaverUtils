package dev.bebomny.beaver.beaverutils.client;


import dev.bebomny.beaver.beaverutils.configuration.ConfigHandler;
import dev.bebomny.beaver.beaverutils.features.FeatureHandler;
import dev.bebomny.beaver.beaverutils.features.Features;
import dev.bebomny.beaver.beaverutils.helpers.KeyBindingHandler;
import dev.bebomny.beaver.beaverutils.notifications.NotificationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class BeaverUtilsClient implements ClientModInitializer {

    public static BeaverUtilsClient INSTANCE;
    public MinecraftClient client;
    public Logger LOGGER = LoggerFactory.getLogger("BeaverUtils");
    public NotificationHandler notifier;
    public FeatureHandler featureHandler;
    public KeyBindingHandler keyBindingHandler;

    //Configuration
    public ConfigHandler configHandler;

    //Features
    public Features features;

    /* TODO: PRIORITY LEVEL HIGHEST!!! fix notificationHandler(Maybe convert to <String> types from <Text>?, should work fine(I think?)) and rewrite config to external
    TODO: PRIORITY!!! Get configuration Menu up and running
    TODO: Add a possibility to add blocks to xray list through a command
     */

    @Override
    public void onInitializeClient() {
        //Initialization Start
        long startTime = System.currentTimeMillis();

        //Actual Starting Point
        if(INSTANCE == null) INSTANCE = this;
        if(this.client == null) this.client = MinecraftClient.getInstance();

        //TODO: Rewrite from scratch, think of something smart, im dumb
        // @Expose fields to save into config in <Feature> objects
        // somehow make <Feature> objects accessible for ConfigHandler and Mixins
        // make ConfigurationScreen not freeze minecraft
        // Make features actually somewhat readable
        // --------------------------------------------------
        // Maybe dont make a config object, just make a Features class
        // holding all features and expose the necessary fields
        // then give this instance to the GSON serializer (Good option)
        // We need to first create this Features Class then create an instance of it
        // and finally try to save it into a config file\\
        // this will(I think it will) make the features' methods available to other classes and objects
        // so it will solve our main issue and probably look cool
        // lets try that
        // .
        // Steps:
        // 1. Make a Features class, that stores all features
        // 2. Define some placeholder features to test this functionality
        // 3. try to save this to a config file
        // 4. Make a Configuration Screen to work with this design
        // 5. profit
        // .
        // ehhhh, its not working
        // make a completely external config and access needed data directly
        // this will eliminate this bullshit
        // todo! hell nah I am doing it in a completely different way anyway


        //handlers
        this.keyBindingHandler = new KeyBindingHandler();
        this.notifier = new NotificationHandler(client);

        this.configHandler = new ConfigHandler();
        configHandler.loadConfig(); //should be called before initializing Features(This initializes them)

        this.featureHandler = new FeatureHandler();

        features.xRay.printsmh();

        Runtime.getRuntime().addShutdownHook(new Thread(configHandler::saveConfig));

        //Initialization End
        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.atInfo().log("Initialized in "+ elapsedTime + "ms " + "Have a nice kitty Game! ~ BeaverUtils");
    }

    public static BeaverUtilsClient getInstance() {
        return INSTANCE;
    }

    public NotificationHandler getNotifier() {
        return notifier;
    }

    public Logger getLogger(String name) {
        return LoggerFactory.getLogger("BeaverUtils/" + name);
    }
}
