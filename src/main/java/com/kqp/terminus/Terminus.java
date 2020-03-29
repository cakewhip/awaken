package com.kqp.terminus;

import com.kqp.terminus.block.CraftingBlock;
import com.kqp.terminus.block.TerminusAnvilBlock;
import com.kqp.terminus.client.container.TerminusCraftingContainer;
import com.kqp.terminus.data.TerminusDataBlockEntity;
import com.kqp.terminus.data.TerminusWorldProperties;
import com.kqp.terminus.entity.RaptorChickenEntity;
import com.kqp.terminus.group.BlockStats;
import com.kqp.terminus.group.MaterialGroup;
import com.kqp.terminus.group.OreGroup;
import com.kqp.terminus.item.pickaxe.EscapePlanItem;
import com.kqp.terminus.item.TerminusArmorMaterial;
import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.bow.FlameBowItem;
import com.kqp.terminus.item.bow.StatusEffectBowItem;
import com.kqp.terminus.item.shovel.ArchaeologistSpadeItem;
import com.kqp.terminus.item.sword.AtlanteanSabreItem;
import com.kqp.terminus.item.sword.EnderianCutlassItem;
import com.kqp.terminus.item.sword.JangKatanaItem;
import com.kqp.terminus.item.sword.StatusEffectSwordItem;
import com.kqp.terminus.item.tool.TerminusAxeItem;
import com.kqp.terminus.item.tool.TerminusShovelItem;
import com.kqp.terminus.loot.LootTableHelper;
import com.kqp.terminus.loot.TerminusRarity;
import com.kqp.terminus.recipe.RecipeType;
import com.kqp.terminus.util.TimeUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main entry point for Terminus.
 *
 * TODO: add mana awakening
 */
public class Terminus implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

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
            TEntities.init();
            TNetworking.init();
            LootTableHelper.init();
            initCallbacks();
        }, (time) -> Terminus.info("Terminus load took " + time + "ms"));
    }

    private static void initCallbacks() {
        info("Initializing callbacks");

        WorldTickCallback.EVENT.register((world) -> {
            if (!world.isClient) {
                world = world;

                if (worldProperties != null) {
                    worldProperties.tick();
                }
            }
        });

        LootTableLoadingCallback.EVENT.register(new LootTableHelper());
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
        public static final Item ATLANTEAN_SABRE = new AtlanteanSabreItem().setRarity(TerminusRarity.EPIC);
        public static final Item ASHEN_BLADE = new StatusEffectSwordItem(TerminusToolMaterial.PHASE_0_SWORD, StatusEffects.WITHER, 4 * 20, 1).setRarity(TerminusRarity.EPIC);
        public static final Item GLACIAL_SHARD = new StatusEffectSwordItem(TerminusToolMaterial.PHASE_0_SWORD, StatusEffects.SLOWNESS, 4 * 20, 1).setRarity(TerminusRarity.EPIC);
        public static final Item ENDERIAN_CUTLASS = new EnderianCutlassItem().setRarity(TerminusRarity.EPIC);
        public static final Item JANG_KATANA = new JangKatanaItem().setRarity(TerminusRarity.FABLED);

        public static final Item CINDERED_BOW = new FlameBowItem(4.0D, false).setRarity(TerminusRarity.RARE);
        public static final Item SLIMEY_BOW = new StatusEffectBowItem(3.0D, false, StatusEffects.SLOWNESS, 2 * 20, 1).setRarity(TerminusRarity.RARE);
        public static final Item RAIDERS_AXE = new TerminusAxeItem(TerminusToolMaterial.PHASE_0_SPECIAL).setRarity(TerminusRarity.UNCOMMON);
        public static final Item ESCAPE_PLAN = new EscapePlanItem().setRarity(TerminusRarity.UNCOMMON);
        public static final Item ARCHAEOLOGIST_SPADE = new ArchaeologistSpadeItem().setRarity(TerminusRarity.UNCOMMON);
        public static final Item RUSTY_SHANK = new StatusEffectSwordItem(TerminusToolMaterial.PHASE_0_SPECIAL, StatusEffects.POISON, 8 * 20, 0).setRarity(TerminusRarity.UNCOMMON);

        //TODO: add unbreakable elytra using post-awakened materials (Dragon Bone Wings)

        public static final Item CELESTIAL_STEEL_INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
        public static final Item RAPTOR_CHICKEN_EGG = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

        public static void init() {
            info("Initializing items");

            register(ATLANTEAN_SABRE, "atlantean_sabre");
            register(ASHEN_BLADE, "ashen_blade");
            register(GLACIAL_SHARD, "glacial_shard");
            register(ENDERIAN_CUTLASS, "enderian_cutlass");
            register(JANG_KATANA, "jang_katana");

            register(CINDERED_BOW, "cindered_bow");
            register(SLIMEY_BOW, "slimey_bow");
            register(RAIDERS_AXE, "raiders_axe");
            register(ESCAPE_PLAN, "escape_plan");
            register(ARCHAEOLOGIST_SPADE, "archaeologist_spade");
            register(RUSTY_SHANK, "rusty_shank");

            register(CELESTIAL_STEEL_INGOT, "celestial_steel_ingot");
            register(RAPTOR_CHICKEN_EGG, "raptor_chicken_egg");
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

    public static class TEntities {
        public static final EntityType<RaptorChickenEntity> RAPTOR_CHICKEN = Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(MOD_ID, "raptor_chicken"),
                FabricEntityTypeBuilder.create(EntityCategory.MONSTER, RaptorChickenEntity::new).size(EntityDimensions.fixed(0.95F, 1.65F)).build()
        );

        public static void init() {
            info("Initializing entities");

            Registry.register(Registry.ITEM, new Identifier(MOD_ID, "raptor_chicken_spawn_egg"), new SpawnEggItem(RAPTOR_CHICKEN, 0x9C0202, 0x610000, new Item.Settings().group(ItemGroup.MISC)));
        }

        //TODO: add killer bunny (https://minecraft.gamepedia.com/Rabbit#The_Killer_Bunny)
    }

    public static class TNetworking {
        public static final Identifier SYNC_SCROLLBAR_ID = new Identifier(Terminus.MOD_ID, "sync_scrollbar");
        public static final Identifier SYNC_RESULTS_ID = new Identifier(Terminus.MOD_ID, "sync_results");
        public static final Identifier SYNC_RESULT_SLOT_ID = new Identifier(Terminus.MOD_ID, "sync_result_slot");
        public static final Identifier OPEN_CRAFTING_C2S_ID = new Identifier(Terminus.MOD_ID, "open_crafting_c2s");
        public static final Identifier OPEN_CRAFTING_S2C_ID = new Identifier(Terminus.MOD_ID, "open_crafting_s2c");
        public static final Identifier CLOSE_CRAFTING_C2S_ID = new Identifier(Terminus.MOD_ID, "close_crafting_c2s");
        public static final Identifier CLOSE_CRAFTING_S2C_ID = new Identifier(Terminus.MOD_ID, "close_crafting_s2c");

        public static void init() {
            info("Initializing networking");

            // Gets the client's scroll bar position and updates the server-side container
            ServerSidePacketRegistry.INSTANCE.register(SYNC_SCROLLBAR_ID, (packetContext, data) -> {
                float scrollPosition = data.readFloat();

                packetContext.getTaskQueue().execute(() -> {
                    if (packetContext.getPlayer().container instanceof TerminusCraftingContainer) {
                        ((TerminusCraftingContainer) packetContext.getPlayer().container).scrollItems(scrollPosition);
                    }
                });
            });

            // Packets for the server to coordinate the navigation between the inventory and the crafting screen
            {
                ServerSidePacketRegistry.INSTANCE.register(OPEN_CRAFTING_C2S_ID, ((packetContext, packetByteBuf) -> {
                    int syncId = packetByteBuf.readInt();
                    double mouseX = packetByteBuf.readDouble();
                    double mouseY = packetByteBuf.readDouble();

                    packetContext.getTaskQueue().execute(() -> {
                        ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                        player.closeContainer();

                        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                        buf.writeInt(syncId);
                        buf.writeDouble(mouseX);
                        buf.writeDouble(mouseY);

                        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, OPEN_CRAFTING_S2C_ID, buf);

                        player.container = new TerminusCraftingContainer(syncId, player.inventory);
                    });
                }));

                ServerSidePacketRegistry.INSTANCE.register(CLOSE_CRAFTING_C2S_ID, ((packetContext, packetByteBuf) -> {
                    double mouseX = packetByteBuf.readDouble();
                    double mouseY = packetByteBuf.readDouble();

                    packetContext.getTaskQueue().execute(() -> {
                        ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                        player.closeContainer();

                        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                        buf.writeDouble(mouseX);
                        buf.writeDouble(mouseY);

                        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CLOSE_CRAFTING_S2C_ID, buf);
                    });
                }));
            }
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