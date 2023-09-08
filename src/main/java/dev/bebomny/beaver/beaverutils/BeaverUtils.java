package dev.bebomny.beaver.beaverutils;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeaverUtils implements ModInitializer {

    public Logger LOGGER = LoggerFactory.getLogger("BeaverUtils");

    @Override
    public void onInitialize() {
        LOGGER.info("[BeaverUtils] Hello, I am a client side mod! I'm afraid of servers!");
    }
}
