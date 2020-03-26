package com.kqp.terminus;

import com.kqp.terminus.block.CelestialAltarBlock;
import com.kqp.terminus.client.container.CelestialAltarContainer;
import com.kqp.terminus.data.TerminusDataBlockEntity;
import com.kqp.terminus.data.TerminusWorldProperties;
import com.kqp.terminus.group.BlockStats;
import com.kqp.terminus.group.MaterialGroup;
import com.kqp.terminus.group.OreGroup;
import com.kqp.terminus.item.TerminusArmorMaterial;
import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.recipe.TerminusRecipes;
import com.kqp.terminus.util.TimeUtil;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
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

public class Terminus implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_NAME = "Terminus";

    public static TerminusWorldProperties worldProperties;
    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        info("Initializing Terminus");

        TimeUtil.profile(() -> {
            Groups.init();
            TBlocks.init();
            TItems.init();
            TContainers.init();
            TNetworking.init();
            initCallbacks();
            TerminusRecipes.init();
        }, (time) -> Terminus.info("Terminus load took " + time + "ms"));
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

    public static class Groups {
        public static OreGroup SUNSTONE, MOONSTONE;
        public static MaterialGroup CELESTIAL;

        public static void init() {
            info("Initializing ore groups");

            SUNSTONE = new OreGroup(
                    "sunstone",
                    new BlockStats(25.0F, 6.0F, 6), "fragment");
            MOONSTONE = new OreGroup(
                    "moonstone",
                    new BlockStats(25.0F, 6.0F, 6), "fragment");
            CELESTIAL = new MaterialGroup("celestial_steel", TerminusToolMaterial.CELESTIAL, TerminusArmorMaterial.CELESTIAL);
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
        public static final Item CELESTIAL_STEEL_INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

        public static void init() {
            info("Initializing items");

            register(CELESTIAL_STEEL_INGOT, "celestial_steel_ingot");
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

    public static class TNetworking {
        public static final Identifier SYNC_SCROLLBAR_ID = new Identifier("terminus", "sync_scrollbar");
        public static final Identifier SYNC_RESULTS_ID = new Identifier("terminus", "sync_results");
        public static final Identifier SYNC_RESULT_SLOT_ID = new Identifier("terminus", "sync_result_slot");

        public static void init() {
            info("Initializing networking");

            ServerSidePacketRegistry.INSTANCE.register(SYNC_SCROLLBAR_ID, (packetContext, data) -> {
                float scrollPosition = data.readFloat();

                packetContext.getTaskQueue().execute(() -> {
                    if (packetContext.getPlayer().container instanceof CelestialAltarContainer) {
                        ((CelestialAltarContainer) packetContext.getPlayer().container).scrollItems(scrollPosition);
                    }
                });
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