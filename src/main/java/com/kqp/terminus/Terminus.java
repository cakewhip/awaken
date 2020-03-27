package com.kqp.terminus;

import com.kqp.terminus.block.CraftingBlock;
import com.kqp.terminus.block.TerminusAnvilBlock;
import com.kqp.terminus.client.container.TerminusCraftingContainer;
import com.kqp.terminus.data.TerminusDataBlockEntity;
import com.kqp.terminus.data.TerminusWorldProperties;
import com.kqp.terminus.group.BlockStats;
import com.kqp.terminus.group.MaterialGroup;
import com.kqp.terminus.group.OreGroup;
import com.kqp.terminus.item.TerminusArmorMaterial;
import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.sword.EnderianCutlassItem;
import com.kqp.terminus.item.sword.StatusEffectSwordItem;
import com.kqp.terminus.item.sword.AtlanteanSabreItem;
import com.kqp.terminus.item.tool.TerminusSwordItem;
import com.kqp.terminus.recipe.RecipeType;
import com.kqp.terminus.util.TimeUtil;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Terminus implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final Random RANDOM = new Random();

    public static final String MOD_ID = "terminus";
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

        public static final Block CELESTIAL_ALTAR_BLOCK = new CraftingBlock(
                FabricBlockSettings.of(Material.STONE).strength(35.0F, 12.0F).lightLevel(4).build(),
                RecipeType.CELESTIAL_ALTAR,
                RecipeType.CRAFTING_TABLE
        );

        public static final Block CELESTIAL_STEEL_ANVIL_BLOCK = new TerminusAnvilBlock(
                FabricBlockSettings.of(Material.METAL).strength(35.0F, 24.0F).lightLevel(4).build(),
                RecipeType.CELESTIAL_STEEL_ANVIL,
                RecipeType.ANVIL
        );

        public static void init() {
            info("Initializing blocks");

            register(CELESTIAL_ALTAR_BLOCK, "celestial_altar");
            register(CELESTIAL_STEEL_ANVIL_BLOCK, "celestial_steel_anvil");

            TERMINUS_DATA_BE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, Terminus.MOD_ID + "terminus_data", BlockEntityType.Builder.create(TerminusDataBlockEntity::new, Blocks.BEDROCK).build(null));
        }

        public static void register(Block block, String name) {
            Registry.register(Registry.BLOCK, new Identifier(Terminus.MOD_ID, name), block);
            Registry.register(Registry.ITEM, new Identifier(Terminus.MOD_ID, name), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        }
    }

    public static class TItems {
        public static final Item ATLANTEAN_SABRE = new AtlanteanSabreItem();
        public static final Item ASHEN_BLADE = new StatusEffectSwordItem(TerminusToolMaterial.PHASE_0_SWORD, StatusEffects.WITHER, 4 * 20, 2);
        public static final Item GLACIAL_SHARD = new StatusEffectSwordItem(TerminusToolMaterial.PHASE_0_SWORD, StatusEffects.SLOWNESS, 4 * 20, 2);
        public static final Item ENDERIAN_CUTLASS = new EnderianCutlassItem();

        public static final Item CELESTIAL_STEEL_INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

        public static void init() {
            info("Initializing items");

            register(ATLANTEAN_SABRE, "atlantean_sabre");
            register(ASHEN_BLADE, "ashen_blade");
            register(GLACIAL_SHARD, "glacial_shard");
            register(ENDERIAN_CUTLASS, "enderian_cutlass");

            register(CELESTIAL_STEEL_INGOT, "celestial_steel_ingot");
        }

        public static void register(Item item, String name) {
            Registry.register(Registry.ITEM, new Identifier(Terminus.MOD_ID, name), item);
        }
    }

    public static class TContainers {
        public static void init() {
            info("Initializing containers");
        }
    }

    public static class TNetworking {
        public static final Identifier SYNC_SCROLLBAR_ID = new Identifier(Terminus.MOD_ID, "sync_scrollbar");
        public static final Identifier SYNC_RESULTS_ID = new Identifier(Terminus.MOD_ID, "sync_results");
        public static final Identifier SYNC_RESULT_SLOT_ID = new Identifier(Terminus.MOD_ID, "sync_result_slot");
        public static final Identifier OPEN_CRAFTING_ID = new Identifier(Terminus.MOD_ID, "open_crafting");

        public static void init() {
            info("Initializing networking");

            ServerSidePacketRegistry.INSTANCE.register(SYNC_SCROLLBAR_ID, (packetContext, data) -> {
                float scrollPosition = data.readFloat();

                packetContext.getTaskQueue().execute(() -> {
                    if (packetContext.getPlayer().container instanceof TerminusCraftingContainer) {
                        ((TerminusCraftingContainer) packetContext.getPlayer().container).scrollItems(scrollPosition);
                    }
                });
            });

            ServerSidePacketRegistry.INSTANCE.register(OPEN_CRAFTING_ID, ((packetContext, packetByteBuf) -> {
                int syncId = packetByteBuf.readInt();

                packetContext.getTaskQueue().execute(() -> {
                    PlayerEntity player = packetContext.getPlayer();
                    player.container = new TerminusCraftingContainer(syncId, player.inventory);
                });
            }));
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