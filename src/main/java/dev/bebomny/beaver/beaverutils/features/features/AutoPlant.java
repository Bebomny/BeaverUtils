package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.AutoPlantConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.AutoPlantMenu;
import dev.bebomny.beaver.beaverutils.features.KeyOnOffFeature;
import dev.bebomny.beaver.beaverutils.helpers.BlockUtils;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class AutoPlant extends KeyOnOffFeature {

    private final AutoPlantConfig autoPlantConfig = config.autoPlantConfig;
    private final List<Item> seeds = new ArrayList<>();


    public AutoPlant() {
        super("AutoPlant");

        addActivationKeybinding(GLFW.GLFW_KEY_UNKNOWN);
        setEnableConfig(autoPlantConfig);
        setOptionsMenu(new AutoPlantMenu());

//        if(config.generalConfig.autoEnable)
//            setEnabled(autoPlantConfig.enabled);

        initializeSeeds();

        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
        PlayerBlockBreakEvents.BEFORE.register(this::beforeBlockBreak);
        PlayerBlockBreakEvents.AFTER.register(this::afterBlockBreak);
    }

    private boolean beforeBlockBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState state, @Nullable BlockEntity blockEntity) {
        return !isEnabled() || !autoPlantConfig.preventBreakingNotFullyGrownCrops;
    }

    private void afterBlockBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (state.getBlock() instanceof CropBlock) {
            //state.get(IntProperty.of("AGE", 0, 7));
            int age = ((CropBlock) state.getBlock()).getAge(state);
            notifier.newNotification(
                    Notification.builder(
                                    Text.translatable("feature.auto_plant.age_text", BlockUtils.getBlockName(state.getBlock()), age))
                            .parent(Text.translatable("feature.auto_plant.text"))
                            .duration(120)
                            .build());
        }
    }

    private void onUpdate(MinecraftClient client) {
        if (client.player == null)
            return;

        if (!isEnabled())
            return;


        for (int y = -1; y <= 0; y++) {
            for (int x = -(autoPlantConfig.plantRadius); x <= autoPlantConfig.plantRadius; x++) {
                for (int z = -(autoPlantConfig.plantRadius); z <= autoPlantConfig.plantRadius; z++) {
                    if (tryPlant(client, client.player.getBlockPos().add(x, y, z)))
                        return;
                }
            }
        }
    }

    public boolean tryPlant(MinecraftClient client, BlockPos pos) {
        BlockState blockState = client.world.getBlockState(pos);
        if (blockState.getBlock() instanceof FarmlandBlock) {
            BlockState blockStateUp = client.world.getBlockState(pos.up());
            if (blockStateUp.getBlock() instanceof AirBlock) {
                return tryUseSeed(client, pos, Hand.MAIN_HAND) || tryUseSeed(client, pos, Hand.OFF_HAND);
            }
        }
        return false;
    }

    public boolean tryUseSeed(MinecraftClient client, BlockPos pos, Hand hand) {
        Item item = client.player.getStackInHand(hand).getItem();
        if (seeds.contains(item)) {
            Vec3d blockPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            BlockHitResult hit = new BlockHitResult(blockPos, Direction.UP, pos, false);
            client.interactionManager.interactBlock(client.player, hand, hit);

            notifier.newNotification(
                    Notification.builder(Text.translatable("feature.auto_plant.plant_success.text"))
                            .parent(Text.translatable("feature.auto_plant.text"))
                            .build()
            );

            return true;
        }
        return false;
    }

//    public void changeMode() {
//        setMode(autoPlantConfig.mode == Mode.DontLookAt ? Mode.LookAt
//                : autoPlantConfig.mode == Mode.LookAt ? Mode.LookAtWithInterpolation
//                : autoPlantConfig.mode == Mode.LookAtWithInterpolation ? Mode.OnlyReplant
//                : autoPlantConfig.mode == Mode.OnlyReplant ? Mode.DontLookAt
//                : Mode.OnlyReplant);
//    }

    public int getPlantRadius() {
        return autoPlantConfig.plantRadius;
    }

    public void setPlantRadius(int newRadius) {
        autoPlantConfig.plantRadius = newRadius;
    }

    public Mode getMode() {
        return autoPlantConfig.mode;
    }

    public void setMode(Mode newMode) {
        autoPlantConfig.mode = newMode;
    }

    public void initializeSeeds() {
        seeds.add(Items.WHEAT_SEEDS);
        seeds.add(Items.BEETROOT_SEEDS);
        seeds.add(Items.POTATO);
        seeds.add(Items.CARROT);
        seeds.add(Items.MELON_SEEDS);
        seeds.add(Items.PUMPKIN_SEEDS);
    }

    public enum Mode {
        DONTLOOKAT(0, "DontLookAt"),
        SNAPLOOKAT(1, "SnapLookAt"),
        LOOKATWITHINTERPOLATION(2, "LookAtWithInterpolation"),
        ONLYREPLANT(3, "OnlyReplant");

        private final int id;
        private final String name;

        Mode(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public Text getCompleteText() {
            return Text.of("Mode: " + this.getName());
        }

        public Text getText() {
            return Text.of(this.getName());
        }

        public String getName() {
            return this.name;
        }
    }
}
