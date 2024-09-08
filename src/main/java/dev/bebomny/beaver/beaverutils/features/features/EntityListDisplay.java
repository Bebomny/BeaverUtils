package dev.bebomny.beaver.beaverutils.features.features;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.bebomny.beaver.beaverutils.configuration.config.EntityListDisplayConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.EntityListDisplayMenu;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import dev.bebomny.beaver.beaverutils.helpers.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class EntityListDisplay extends SimpleOnOffFeature {

    private static final Identifier GATO = Identifier.of("beaverutils", "gato.png");
    private static final Identifier ARROWS = Identifier.of("beaverutils", "arrows.png");
    private final EntityListDisplayConfig entityListDisplayConfig = config.entityListDisplayConfig;

    //TODO: I shouldn't have 3 lists basically containing the same stuff
    private final Map<EntityType<?>, EntityListEntry> entityEntryMap;
    private List<Entity> entityList;
    private List<EntityListEntry> entityListEntries;

    // Time measurement
    public List<Long> timeMeasurements = new ArrayList<>();
    public int measurementCountLimit = 100; // in ticks 20 = 1 second of measurements
    private int entryHeight = 16;

    //Drawing
    private final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(new BufferAllocator(256));



    // On each tick update all entries ->
    // 1. on first tick initiate all entryLists
    // 2. then on every tick check if there are any new entities and add them into the list
    // 3. remove any that have no entities
    // 4. check if any new entities of already existing entryLists spawned if so then add them into there corresponding lists
    // 5. remove entities if they are dead (Beware of possible null values there)
    // 6. at the end recalculate all counts, distances, names and coordinates
    //
    // this allows for persisting states of entities, and provides better performance with caching

    public EntityListDisplay() {
        super("EntityDisplay");

        setEnableConfig(entityListDisplayConfig);
        setOptionsMenu(new EntityListDisplayMenu());


        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
        HudRenderCallback.EVENT.register(this::onHudRenderInit);
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(this::onRenderWorld);

        //Update on world(client?) tick to safe resources by not building a new list on every frame
        //Do something different with players
        //DebugHud;
        //client.player.getWorld().getTickManager().getTickRate()

        this.entryHeight = Math.max(9 + 2 + 2, entryHeight);
        this.entityList = new ArrayList<>();
        this.entityListEntries = new ArrayList<>();
        this.entityEntryMap = new HashMap<>();

    }


    //TODO somehow render a table to the screen with all entities listed
    private void onUpdate(MinecraftClient client) {
        if (client.player == null)
            return;

        if (!isEnabled())
            return;

        ///Time calculation
        long calculationStart = System.nanoTime();

        Box playerBox = new Box(client.player.getBlockPos()).expand(entityListDisplayConfig.searchRadius);

        //TODO: Consider item entities, they are currently not handled correctly
        // cause go entity.isAlive() checks instead of item related

        //TODO: Add a blacklist here, in the predicate
        // client.world.getEntities() ???????
        this.entityList = client.player
                .getWorld()
                .getEntitiesByClass(
                        Entity.class, playerBox,
                        entity -> true);

        ///////TODO: Use completable futures? at least try I guess
        // Add new Entries to the list - Done
        // updateEntries - Done
        // - Check if any entities where killed - Done
        // - calculate distance
        // - choose the chosen entity
        // remove any EntityListEntries if the


        for (Entity entity : entityList) {
            EntityType<?> type = entity.getType();

            this.entityEntryMap.computeIfAbsent(type, entityType -> new EntityListEntry(entity))
                    .updateEntryWithEntity(entity);
        }

        this.entityListEntries = new ArrayList<>(entityEntryMap.values());

        this.entityListEntries.forEach(EntityListEntry::updateEntry);
        this.entityListEntries.removeIf(EntityListEntry::isAbsent);

        //Time calculation
        long calculationEnd = System.nanoTime();
        if (timeMeasurements.size() >= measurementCountLimit) {
            timeMeasurements.removeFirst();
        }
        timeMeasurements.addLast(calculationEnd - calculationStart);


    }

    public void onHudRenderInit(DrawContext context, RenderTickCounter tickCounter) {
        //see {@link PlayerListHud} for rendering details
        //get all entities in render distance
        //client.player.getServer().getWorlds().
        //TODO: Add a total at the top(as an entry?) If an entry then set entity as null and check it?
        //TODO: Put as much stuff from here to tick updates? Or maybe not, the render thread could fetch some some stuff async and it wouldnt look nice :?

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

    private void onRenderWorld(WorldRenderContext worldRenderContext) {
        MatrixStack matrixStack = worldRenderContext.matrixStack();

        //Draw conditions
        // - EntityListDisplay enabled
        // - The rendering stack is available
        // - The player exists
        if (!isEnabled() || (matrixStack == null) || client.player == null)
            return;

        //OpenGL settings
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();

        matrixStack.push(); // Stage 1 - All outlines, textures and text

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();

        for (EntityListEntry entry : entityListEntries) {
            Entity entity = entry.getClosestEntity();

            //skip the player - for now all players so its easier to debug
            //TODO: dont skip other players and display their nickname above their head
            if (entity instanceof PlayerEntity)
                continue;

            matrixStack.push(); // Stage 2 - Each individual entity entry

            Box entityBox = entity.getBoundingBox();

            //We lerp the position of the entity to allow for smooth animation
            // then translate the box to not start at the center of the entity
            // adjusted to start at one of the corners of the entity's bounding box
            Vec3d entityPos = entity.getLerpedPos(worldRenderContext.tickCounter().getTickDelta(true));
            Vec3d entityPosCorner = entityPos.subtract(entityBox.getLengthX() / 2, 0, entityBox.getLengthZ() / 2);

            //Translate entity's bounding box according to the camera position and move it to entities position
            matrixStack.translate(entityPosCorner.getX() - camera.getPos().x, entityPosCorner.getY() - camera.getPos().y, entityPosCorner.getZ() - camera.getPos().z);

            //Whatever happened here it works? -
            // The bounding boxes are already translated by the line above into their correct places
            // - the whole matrix is moved to the entity's position
            // so no need to further move them according to their own position
            // we set their position to 0,0,0 on the matrix, so it renders in the correct place and isn't offset twice
            entityBox = entityBox.offset(new Vec3d(entityBox.minX, entityBox.minY, entityBox.minZ).negate());


            matrixStack.push(); // Stage 3.1 - Bounding Box outline
            RenderUtils.drawOutlinedBox(entityBox, matrixStack);
            //RenderUtils.drawOutlinedBoxRainbow(entityBox, matrixStack);
            matrixStack.pop(); // Stage 3.1 - Bounding Box outline

            // TODO: Adjust the pitch
            Vec3d target = client.player.getPos(); //client.player.getPos()
            double d = target.x - entityPos.x;
//            double e = target.y - entityBoxCenterPos.y;
            double f = target.z - entityPos.z;
//            double g = Math.sqrt(d * d + f * f);
//            float pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 57.2957763671875)));
            float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875 + 90.0F));

            matrixStack.push(); // Stage 3.2 - Textures and Text
            Vec3d center = entityBox.getCenter();
            matrixStack.translate(center.getX(), entityBox.getLengthY(), center.getZ());

            //Rotate the matrix plain to face the player
            matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(yaw));

            //Draw textures
            //RenderUtils.drawTexture(ARROWS, 8, 4, matrixStack); //4
            //RenderUtils.drawTexture(GATO, 1, 1, matrixStack);


            //Name tags
            //TODO:
            // `client.options.getGuiScale().getValue()` doesnt quite work right
            // maybe make a custom scale setting?
            double scale = 1; // for testing = 1
            matrixStack.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);
            matrixStack.translate(0, -10, 0);

            //TODO: Change whats displayed based on:
            // Item -> item name
            // Player -> Nickname
            // This could be done in the `getEntityName()` method in the `EntityListEntry` class instead of here
            Text text = entry.getEntityName();//Text.literal("Test Test Test");
            int opacity = (int) (client.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
            float halfWidth = ((float) client.textRenderer.getWidth(text) / 2);

            client.textRenderer.draw(
                    text, -halfWidth, 0f,
                    553648127, false,
                    matrixStack.peek().getPositionMatrix(), immediate,
                    TextRenderer.TextLayerType.SEE_THROUGH,
                    opacity, 0xf000f0);
            immediate.draw();

            client.textRenderer.draw(
                    text, -halfWidth, 0f,
                    -1, false,
                    matrixStack.peek().getPositionMatrix(), immediate,
                    TextRenderer.TextLayerType.SEE_THROUGH,
                    0, 0xf000f0);
            immediate.draw();


            matrixStack.pop(); // Stage 3.2 - Textures and Text
            matrixStack.pop(); // Stage 2 - Each individual entity entry
        }

        matrixStack.pop(); // Stage 1 - All outlines, textures and text

        // OpenGL setting resets
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public enum EntrySortBy {
        NAME,
        NAME_REVERSED,
        COUNT,
        COUNT_REVERSED,
        VIEWPORT_COUNT,
        VIEWPORT_COUNT_REVERSED,
        DISTANCE,
        DISTANCE_REVERSED
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
        List<Entity> entryEntityList;

        public EntityListEntry(Entity newEntity) {
            this.entityType = newEntity.getType();
            this.entityName = entityType.getName();
            this.entityClass = newEntity.getClass();

            this.count = 0;
            this.viewportCount = -1;
            this.distanceToNearest = Float.MAX_VALUE;
            this.closestEntity = null;
            this.chosenEntity = null;
            this.entryEntityList = new ArrayList<>();
        }

        public EntityListEntry(Text entityName, int count, int viewportCount, float distanceToNearest, Class<? extends Entity> entityClass, EntityType<?> entityType) {
            this.entityName = entityName;

            this.count = count;
            this.viewportCount = viewportCount;
            this.distanceToNearest = distanceToNearest;

            this.entityClass = entityClass;
            this.entityType = entityType;

            this.entryEntityList = new ArrayList<>();
        }


        public void updateEntry() {
            entryEntityList.removeIf(entity -> {
                if ((!entity.isAlive()) || !entityList.contains(entity)) {
                    if (entity == this.chosenEntity)
                        this.chosenEntity = null;
                    if (entity == this.closestEntity) {
                        this.closestEntity = null;
                        this.distanceToNearest = Float.MAX_VALUE;
                    }
                    return true;
                }
                return false;
            });

            //Recalculate distance and other stuff here
            this.count = entryEntityList.size();

            if (client.player == null)
                return;

//            this.chosenEntity = null;
//            this.closestEntity = null;


            //Nearest entity
            for (Entity entity : entryEntityList) {
                float distanceToPlayer = client.player.distanceTo(entity);

                if (distanceToPlayer < distanceToNearest) {
                    this.closestEntity = entity;
                }
            }

            if (this.closestEntity != null)
                this.setDistanceToNearest(client.player.distanceTo(this.closestEntity));

            //Chosen entity isn't set on first tick
            if (chosenEntity == null)
                this.chosenEntity = closestEntity;


        }

        public void updateEntryWithEntity(Entity entity) {
            if (entryEntityList.contains(entity))
                return;


            this.entryEntityList.add(entity);
            //this.count++;

            //TODO: calculate distance in EntityListEntry::updateEntry
//            float distanceToPlayer = client.player != null ? client.player.distanceTo(newEntity) : Float.MAX_VALUE;
//
//            if (distanceToPlayer < this.getDistanceToNearest()) {
//                this.setDistanceToNearest(distanceToPlayer);
//                this.closestEntity = newEntity;
//                if(chosenEntity == null)
//                    this.chosenEntity = closestEntity;
//            }

            //TODO: Ray cast to see if the entity is in the player's viewport?
        }

        public boolean isAbsent() {
            return entryEntityList.isEmpty();
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
            if (this.closestEntity == null)
                return Text.literal("Missing");
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

        public Entity getClosestEntity() {
            return closestEntity;
        }

        public Entity getChosenEntity() {
            return chosenEntity;
        }

        public List<Entity> getEntryEntityList() {
            return entryEntityList;
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
}
