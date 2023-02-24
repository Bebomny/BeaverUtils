package dev.bebomny.beaver.beaverutils.client;

import dev.bebomny.beaver.beaverutils.features.*;
import dev.bebomny.beaver.beaverutils.helpers.NotificationHandler;
import dev.bebomny.beaver.beaverutils.helpers.ScreenEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class BeaverUtilsClient implements ClientModInitializer {

    public MinecraftClient client;
    public Logger LOGGER = LoggerFactory.getLogger("BeaverUtils");
    static BeaverUtilsClient INSTANCE;
    public NotificationHandler notifier;

    //features
    public Flight flight;
    public XRay xRay;
    public FullBright fullBright;
    public AutoPlant autoPlant;
    public AutoClicker autoClicker;
    public NoFall noFall;
    public Reach reach;
    public ElytraSpeedControl elytraSpeedControl;
    public AutoTool autoTool;


    @Override
    public void onInitializeClient() {
        if(INSTANCE == null) INSTANCE = this;
        if(this.client == null) this.client = MinecraftClient.getInstance();

        //handlers
        this.notifier = new NotificationHandler(client);
        ScreenEventHandler.register();

        //features
        this.flight = new Flight(client, getInstance());
        this.xRay = new XRay(client, getInstance());
        this.fullBright = new FullBright(client, getInstance());
        this.autoPlant = new AutoPlant(client, getInstance());
        this.autoClicker = new AutoClicker(client, getInstance());
        this.noFall = new NoFall(client, getInstance());
        this.reach = new Reach(client, getInstance());
        this.elytraSpeedControl = new ElytraSpeedControl(client, getInstance());
        this.autoTool = new AutoTool(client, getInstance());

        //register tick events

    }

    public static BeaverUtilsClient getInstance() {
        return INSTANCE;
    }

    public void reloadRenderer() {
        client.worldRenderer.reload();
    }
}
