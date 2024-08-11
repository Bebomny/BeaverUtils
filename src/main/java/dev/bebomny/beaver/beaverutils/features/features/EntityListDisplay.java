package dev.bebomny.beaver.beaverutils.features.features;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.bebomny.beaver.beaverutils.configuration.config.EntityListDisplayConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.EntityListDisplayMenu;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EntityListDisplay extends SimpleOnOffFeature {

    private static final Identifier GATO = Identifier.of("beaverutils", "gato.png");
    private static final Identifier ARROWS = Identifier.of("beaverutils", "arrows.png");
    private final EntityListDisplayConfig entityListDisplayConfig = config.entityListDisplayConfig;
    private int entryHeight = 16;
    private List<Entity> entityList;
    private List<EntityListEntry> entityListEntries;

    public EntityListDisplay() {
        super("EntityDisplay");

        setEnableConfig(entityListDisplayConfig);
        setOptionsMenu(new EntityListDisplayMenu());


        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
        //Update on world(client?) tick to safe resources by not building a new list on every frame
        //Do something different with players
        //DebugHud;
        //client.player.getWorld().getTickManager().getTickRate()

        this.entryHeight = Math.max(9 + 2 + 2, entryHeight);
        this.entityList = new ArrayList<>();
        this.entityListEntries = new ArrayList<>();

    }

    //TODO somehow render a table to the screen with all entities listed
    private void onUpdate(MinecraftClient client) {
        if (client.player == null)
            return;

        if (!isEnabled())
            return;

        Box playerBox = new Box(client.player.getBlockPos()).expand(entityListDisplayConfig.searchRadius);

        //TODO: Add a blacklist here, in the predicate
        entityList = client.player
                .getWorld()
                .getEntitiesByClass(
                        Entity.class, playerBox,
                        entity -> true);

        this.entityListEntries = mapEntitiesToEntityEntryList(entityList);

    }

    public void onHudRenderInit(DrawContext context, RenderTickCounter tickCounter) {
        //see {@link PlayerListHud} for rendering details
        //get all entities in render distance
        //client.player.getServer().getWorlds().
        //TODO: Add a total at the top(as an entry?) If an entry then set entity as null and check it?

        if (!isEnabled())
            return;

        if (client.textRenderer == null)
            return;

        Queue<EntityListEntry> entityListEntryQueue = new PriorityQueue<>();
        entityListEntryQueue.addAll(entityListEntries);

        //int widestEntityName = entityListEntries.stream().map(EntityListEntry::getNameWidth).reduce(Math::min).orElse(entityListDisplayConfig.minNameColumnWidth);
        int nameColumnWidth = Math.max(
                entityListDisplayConfig.minNameColumnWidth,
                client.textRenderer.getWidth(Text.translatable("feature.entity_list_display.title.entity_name")));
        int countColumnWidth = Math.max(
                entityListDisplayConfig.minCountColumnWidth,
                client.textRenderer.getWidth(Text.translatable("feature.entity_list_display.title.entity_count")));
        int viewportCountColumnWidth = Math.max(
                entityListDisplayConfig.minViewportCountColumnWidth,
                client.textRenderer.getWidth(Text.translatable("feature.entity_list_display.title.entity_viewport_count")));
        int distanceToNearestColumnWidth = Math.max(
                entityListDisplayConfig.minDistanceColumnWidth,
                client.textRenderer.getWidth(Text.translatable("feature.entity_list_display.title.entity_distance_to_nearest")));

        for (EntityListEntry entry : this.entityListEntries) {
            nameColumnWidth = Math.max(nameColumnWidth, entry.getNameWidth() + 4);
            countColumnWidth = Math.max(countColumnWidth, entry.getCountTextWidth() + 4);
            viewportCountColumnWidth = Math.max(viewportCountColumnWidth, entry.getViewportCountTextWidth() + 4);
            distanceToNearestColumnWidth = Math.max(distanceToNearestColumnWidth, entry.getDistanceToNearestTextWidth() + 4);
        }

        this.entryHeight = Math.max(client.textRenderer.fontHeight + 2 + 2, 20);

        int textBackgroundColor = this.client.options.getTextBackgroundColor(553648127);
        int textColor = 16777215;

        RenderSystem.enableBlend();
        int entryPosY = entityListDisplayConfig.onScreenPosY; // - (entryHeight - (int) Math.floor(client.textRenderer.fontHeight / 2D)) // + (this.entryHeight * i)
        int entryPosX = entityListDisplayConfig.onScreenPosX;

        //context.drawHorizontalLine(entryPosX, entryPosX + nameColumnWidth + countColumnWidth + distanceToNearestColumnWidth, entryPosY, 0xFFFFFFFF); //13911217

        context.fill(
                entryPosX - 4,
                entryPosY,
                entryPosX + nameColumnWidth + countColumnWidth + distanceToNearestColumnWidth + 4, // + viewportCountColumnWidth
                entryPosY + entryHeight + (entryHeight * entityListEntryQueue.size()),
                Integer.MIN_VALUE);

        //context.fill(entryPosX, entryPosY, entryPosX - 100, entryPosY - 100, 0xFFFFFFFF);

        //TODO: add direction
        //TODO: highlight the nearest entity - a box? an arrow above its head?

        //159x160
        int catOffset = 19;
        context.drawTexture(GATO, entryPosX, entryPosY + 1, 0, 0, 16, 16, 16, 16);

        entryPosY += 5;

        context.drawTextWithShadow(client.textRenderer,
                Text.translatable("feature.entity_list_display.title.entity_name"),
                entryPosX + catOffset, entryPosY, textColor);

        context.drawTextWithShadow(client.textRenderer,
                Text.translatable("feature.entity_list_display.title.entity_count"),
                entryPosX + nameColumnWidth, entryPosY, textColor);

        context.drawTextWithShadow(client.textRenderer,
                Text.translatable("feature.entity_list_display.title.entity_distance_to_nearest"),
                entryPosX + nameColumnWidth + countColumnWidth, entryPosY, textColor);

        context.drawHorizontalLine(entryPosX - 2, entryPosX + nameColumnWidth + countColumnWidth + distanceToNearestColumnWidth + 2, entryPosY + (this.entryHeight / 2) + 4, 0x6FAFAFAF); //13911217
        entryPosY += this.entryHeight;


        for (int i = 0; !entityListEntryQueue.isEmpty(); i++) {
            EntityListEntry listEntry = entityListEntryQueue.poll();

            context.drawTextWithShadow(client.textRenderer, listEntry.getEntityName(), entryPosX, entryPosY + (this.entryHeight * i), textColor);
            context.drawTextWithShadow(client.textRenderer, listEntry.getCountText(), entryPosX + nameColumnWidth, entryPosY + (this.entryHeight * i), textColor);
            context.drawTextWithShadow(client.textRenderer, listEntry.getDistanceToNearestText(), entryPosX + nameColumnWidth + countColumnWidth, entryPosY + (this.entryHeight * i), textColor);


        }
    }

    private List<EntityListEntry> mapEntitiesToEntityEntryList(List<Entity> entities) {

        Map<EntityType<?>, EntityListEntry> entityEntryMap = new HashMap<>();

        for (Entity entity : entities) {
            //If this entity isn't in the list
            EntityType<?> type = entity.getType();

            entityEntryMap.computeIfAbsent(type, entityType -> new EntityListEntry(entity)).updateEntry(entity);


//            float distanceToEntity;
//            if(client.player != null)
//                distanceToEntity = client.player.distanceTo(entity);
//            else
//                distanceToEntity = Float.MAX_VALUE;
//
//            //TODO: Ray cast to see if the entity is in the player's viewport?
//
//            entityEntryMap.computeIfAbsent(type, entityType -> {
//                Text name = entityType.getName();
//
//                return new EntityListEntry(
//                        name,
//                        0, -1,
//                        distanceToEntity,
//                        entity.getClass(), type);
//            }).updateCountAndDistance(distanceToEntity);


//            if(!entityEntryMap.containsKey(type)) { //entityEntryList.stream().filter(entityEntry -> entityEntry.entityClass() == entity.getClass()).count() == 0
//
//                Text entityName = type.getName();
//
//
//
//                EntityEntry newEntityEntry = new EntityEntry(
//                        entityName,
//                        1, -1,
//                        distanceToEntity,
//                        entity.getClass(), type);
//
//                entityEntryMap.put(type, newEntityEntry);
//            } else {
//                EntityEntry entityEntry = entityEntryMap.remove(type);
//                entityEntry.incrementCount();
//
//                if(distanceToEntity < entityEntry.getDistanceToNearest())
//                    entityEntry.setDistanceToNearest(distanceToEntity);
//
//                entityEntryMap.put(type, entityEntry);
//            }
        }

        return new ArrayList<>(entityEntryMap.values());
    }

    //TODO:
    // Allow to pin entities to the list, if that entity is missing show "missing" or something similar
    //
    //Name      | Count | Viewport Count | Nearest [m] | ???
    // Villager |   34  |        4       |     12 m    |
    @Environment(EnvType.CLIENT)
    public class EntityListEntry implements Comparable<EntityListEntry> {

        final Text entityName;
        final Class<? extends Entity> entityClass;
        final EntityType<?> entityType;
        int count;
        int viewportCount;
        float distanceToNearest = Float.MAX_VALUE;
        Entity closestEntity;
        Entity chosenEntity; //TODO: make sure that the entity is still alive
        List<Entity> entityList;

        public EntityListEntry(Entity newEntity) {
            this.entityType = newEntity.getType();
            this.entityName = entityType.getName();
            this.entityClass = newEntity.getClass();

            this.count = 0;
            this.viewportCount = -1;
            this.distanceToNearest = 42069;
            this.entityList = new ArrayList<>();
//            this.entityList.add(newEntity);
//
//            float distanceToPlayer = client.player != null ? client.player.distanceTo(newEntity) : Float.MAX_VALUE;
//
//            if(distanceToPlayer < this.getDistanceToNearest())
//                this.setDistanceToNearest(distanceToPlayer);

        }

        public EntityListEntry(Text entityName, int count, int viewportCount, float distanceToNearest, Class<? extends Entity> entityClass, EntityType<?> entityType) {
            this.entityName = entityName;

            this.count = count;
            this.viewportCount = viewportCount;
            this.distanceToNearest = distanceToNearest;

            this.entityClass = entityClass;
            this.entityType = entityType;

            this.entityList = new ArrayList<>();
        }


        public void updateEntry(Entity newEntity) {
            this.entityList.add(newEntity);
            this.count++;

            //LOGGER.atInfo().log("Trying to get distance for: " + newEntity.getName() + " | and of type: " + newEntity.getType().getName());
            float distanceToPlayer = client.player != null ? client.player.distanceTo(newEntity) : Float.MAX_VALUE;

            if (distanceToPlayer < this.getDistanceToNearest()) {
                this.setDistanceToNearest(distanceToPlayer);
                this.closestEntity = newEntity;
            }

            //TODO: Ray cast to see if the entity is in the player's viewport?
        }

        public Text getEntityName() {
            return entityName;
        }

        public int getNameWidth() {
            return client.textRenderer.getWidth(this.getEntityName());
        }

        public Text getCountText() {
            return Text.literal(String.format("%d", this.getCount()));
        }

        public int getCountTextWidth() {
            return client.textRenderer.getWidth(this.getCountText());
        }

        public Text getViewportCountText() {
            return Text.literal(String.format("%d", this.getViewportCount()));
        }

        public int getViewportCountTextWidth() {
            return client.textRenderer.getWidth(this.getViewportCountText());
        }

        public Text getDistanceToNearestText() {
            BlockPos closestEntityPos = this.closestEntity.getBlockPos();
            return Text.literal(String.format(
                    "%.1fm (x:%d | y:%d | z:%d)",
                    this.getDistanceToNearest(),
                    closestEntityPos.getX(), closestEntityPos.getY(), closestEntityPos.getZ()));
        }

        public int getDistanceToNearestTextWidth() {
            return client.textRenderer.getWidth(this.getDistanceToNearestText());
        }

        public int getCount() {
            return count;
        }

        public int getViewportCount() {
            return viewportCount;
        }

        public void setViewportCount(int viewportCount) {
            this.viewportCount = viewportCount;
        }

        public float getDistanceToNearest() {
            return distanceToNearest;
        }

        public void setDistanceToNearest(float distanceToNearest) {
            this.distanceToNearest = distanceToNearest;
        }

        public Class<? extends Entity> getEntityClass() {
            return entityClass;
        }

        public EntityType<?> getEntityType() {
            return entityType;
        }

        @Override
        public int compareTo(@NotNull EntityListDisplay.EntityListEntry o) {
            //TODO: Test this after implementing a button for customization
            return switch (entityListDisplayConfig.entrySortBy) {
                case NAME -> o.entityName.getString().compareTo(this.entityName.getString());
                case NAME_REVERSED -> -o.entityName.getString().compareTo(this.entityName.getString());
                case COUNT -> o.count - this.count;
                case COUNT_REVERSED -> this.count - o.count;
                case VIEWPORT_COUNT -> o.viewportCount - this.viewportCount;
                case VIEWPORT_COUNT_REVERSED -> this.viewportCount - o.viewportCount;
                case DISTANCE -> (int) (o.distanceToNearest - this.distanceToNearest);
                case DISTANCE_REVERSED -> (int) (this.distanceToNearest - o.distanceToNearest);
            };
        }
    }

    public enum EntrySortBy {
        NAME,
        NAME_REVERSED,
        COUNT,
        COUNT_REVERSED,
        VIEWPORT_COUNT,
        VIEWPORT_COUNT_REVERSED,
        DISTANCE,
        DISTANCE_REVERSED;
    }
}
