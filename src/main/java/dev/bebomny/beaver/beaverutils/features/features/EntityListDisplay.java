package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.EntityListDisplayConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.EntityListDisplayMenu;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.world.entity.ClientEntityManager;

public class EntityListDisplay extends SimpleOnOffFeature {

    private final EntityListDisplayConfig entityListDisplayConfig = config.entityListDisplayConfig;

    public EntityListDisplay() {
        super("EntityDisplay");

        setOptionsMenu(new EntityListDisplayMenu());


        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
        //Update on world(client?) tick to safe resources by not building a new list on every frame
        //Do something different with players
        //DebugHud;
        //client.player.getWorld().getTickManager().getTickRate()
    }

    //TODO somehow render a table to the screen with all entities
    private void onUpdate(MinecraftClient client) {

    }

    public void onHudRenderInit(DrawContext context, RenderTickCounter tickCounter) {
        //see {@link PlayerListHud} for rendering details
        //get all entities in render distance
        //client.player.getServer().getWorlds().

    }

    @Environment(EnvType.CLIENT)
    public record entityListEntry(Text entityName, int count, int viewportCount, Class<Entity> entityClass) {

        public entityListEntry(Text entityName, int count, int viewportCount, Class<Entity> entityClass) {
            this.entityName = entityName;
            this.count = count;
            this.viewportCount = viewportCount;
            this.entityClass = entityClass;
        }
    }
}
