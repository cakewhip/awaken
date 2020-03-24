package com.kqp.terminus;

import com.kqp.terminus.block.CelestialAltarBlock;
import com.kqp.terminus.client.container.CelestialAltarContainer;
import com.kqp.terminus.data.TerminusDataBlockEntity;
import com.kqp.terminus.data.TerminusWorldProperties;
import com.kqp.terminus.oregroup.BlockStats;
import com.kqp.terminus.oregroup.OreGroup;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.BlockContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.BiFunction;

public class Terminus implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_NAME = "Terminus";

    public static TerminusWorldProperties worldProperties;
    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        info("Initializing Terminus");

        OreGroups.init();

        TBlocks.init();

        TItems.init();

        TContainers.init();

        initCallbacks();
    }

    private void initCallbacks() {
        info("Initializing callbacks");

        WorldTickCallback.EVENT.register((world) -> {
            if (!world.isClient) {
                world = (ServerWorld) world;

                if (worldProperties != null) {
                    worldProperties.tick();
                }
            }
        });
    }

    public static class OreGroups {

        public static OreGroup SUNSTONE, MOONSTONE;

        public static void init() {
            info("Initializing ore groups");

            SUNSTONE = new OreGroup(
                    "sunstone",
                    new BlockStats(25.0F, 6.0F, 6), "fragment", true);
            MOONSTONE = new OreGroup(
                    "moonstone",
                    new BlockStats(25.0F, 6.0F, 6), "fragment", true);
        }
    }

    public static class TBlocks {
        public static BlockEntityType<TerminusDataBlockEntity> TERMINUS_DATA_BE_TYPE;

        public static final Block CELESTIAL_ALTAR_BLOCK = new CelestialAltarBlock();

        public static void init() {
            info("Initializing blocks");

            register(CELESTIAL_ALTAR_BLOCK, "celestial_altar");

            TERMINUS_DATA_BE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "terminus:terminus_data", BlockEntityType.Builder.create(TerminusDataBlockEntity::new, Blocks.BEDROCK).build(null));
        }

        public static void register(Block block, String name) {
            Registry.register(Registry.BLOCK, new Identifier("terminus", name), block);
            Registry.register(Registry.ITEM, new Identifier("terminus", name), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        }
    }

    public static class TItems {
        public static void init() {
            info("Initializing items");
        }

        public static void register(Item item, String name) {
            Registry.register(Registry.ITEM, new Identifier("terminus", name), item);
        }
    }

    public static class TContainers {
        public static final Identifier CELESTIAL_ALTAR_ID = new Identifier("terminus", "celestial_altar");
        public static final String CELESTIAL_ALTAR_TRANSLATION_KEY = Util.createTranslationKey("container", CELESTIAL_ALTAR_ID);

        public static void init() {
            info("Initializing containers");

            ContainerProviderRegistry.INSTANCE.registerFactory(CELESTIAL_ALTAR_ID, (syncId, identifier, player, buf) -> {
                final World world = player.world;
                final BlockPos pos = buf.readBlockPos();

                return world.getBlockState(pos).createContainerFactory(world, pos).createMenu(syncId, player.inventory, player);
            });
        }
    }

    public static void info(String message) {
        LOGGER.log(Level.INFO, "[" + MOD_NAME + "] " + message);
    }

    public static void warn(String message) {
        LOGGER.log(Level.WARN, "[" + MOD_NAME + "] " + message);
    }

    public static void error(String message) {
        LOGGER.log(Level.ERROR, "[" + MOD_NAME + "] " + message);
    }
}