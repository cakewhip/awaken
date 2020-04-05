package com.kqp.awaken;

import com.kqp.awaken.block.CraftingBlock;
import com.kqp.awaken.block.AwakenAnvilBlock;
import com.kqp.awaken.client.container.AwakenCraftingContainer;
import com.kqp.awaken.data.AwakenDataBlockEntity;
import com.kqp.awaken.data.AwakenWorldData;
import com.kqp.awaken.entity.DireWolfEntity;
import com.kqp.awaken.entity.RaptorChickenEntity;
import com.kqp.awaken.entity.attribute.TEntityAttributes;
import com.kqp.awaken.group.ArmorGroup;
import com.kqp.awaken.group.BlockStats;
import com.kqp.awaken.group.ToolGroup;
import com.kqp.awaken.group.OreGroup;
import com.kqp.awaken.item.effect.Equippable;
import com.kqp.awaken.item.effect.SetBonusEquippable;
import com.kqp.awaken.item.effect.SpecialItemRegistry;
import com.kqp.awaken.item.pickaxe.EscapePlanItem;
import com.kqp.awaken.item.AwakenArmorMaterial;
import com.kqp.awaken.item.AwakenToolMaterial;
import com.kqp.awaken.item.bow.FlameBowItem;
import com.kqp.awaken.item.bow.StatusEffectBowItem;
import com.kqp.awaken.item.shovel.ArchaeologistSpadeItem;
import com.kqp.awaken.item.sword.AtlanteanSabreItem;
import com.kqp.awaken.item.sword.EnderianCutlassItem;
import com.kqp.awaken.item.sword.JangKatanaItem;
import com.kqp.awaken.item.sword.StatusEffectSwordItem;
import com.kqp.awaken.item.tool.AwakenAxeItem;
import com.kqp.awaken.loot.LootTableHelper;
import com.kqp.awaken.loot.AwakenRarity;
import com.kqp.awaken.recipe.RecipeType;
import com.kqp.awaken.util.TimeUtil;
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
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main entry point for Awaken.
 *
 * TODO: add mana awakening
 */
public class Awaken implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "awaken";
    public static final String MOD_NAME = "Awaken";

    public static AwakenWorldData worldProperties;
    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        info("Initializing Awaken");

        TimeUtil.profile(() -> {
            Groups.init();
            TBlocks.init();
            TItems.init();
            TContainers.init();
            TEntities.init();
            TNetworking.init();
            LootTableHelper.init();
            initCallbacks();
        }, (time) -> Awaken.info("Awaken load took " + time + "ms"));
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

        LootTableLoadingCallback.EVENT.register(LootTableHelper::onLootTableLoading);
    }

    public static class Groups {
        public static ArmorGroup WITHER_SCALE_ARMOR;

        public static OreGroup SALVIUM, VALERIUM;
        public static ArmorGroup SALVIUM_ARMOR, VALERIUM_ARMOR;

        public static OreGroup SUNSTONE, MOONSTONE;

        public static ToolGroup CELESTIAL_STEEL_TOOLS;
        public static ArmorGroup CELESTIAL_STEEL_ARMOR;

        public static void init() {
            info("Initializing ore groups");

            // Phase 1
            {
                WITHER_SCALE_ARMOR = new ArmorGroup("wither_scale", AwakenArmorMaterial.WITHER_SCALE);
            }

            // Phase 2
            {
                SALVIUM = new OreGroup(
                        "salvium",
                        new BlockStats(25.0F, 6.0F, 0),
                        false,
                        "ingot"
                );

                VALERIUM = new OreGroup(
                        "valerium",
                        new BlockStats(25.0F, 6.0F, 0),
                        false,
                        "ingot"
                );

                SALVIUM_ARMOR = new ArmorGroup("salvium", AwakenArmorMaterial.SALVIUM, "Set bonus: 15% extra ranged damage");
                SpecialItemRegistry.EQUIPPABLE_ARMOR.put(SALVIUM_ARMOR.CHESTPLATE, new SetBonusEquippable()
                        .addEntityAttributeModifier(TEntityAttributes.RANGED_DAMAGE, "salvium_set_bonus", 1000D, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                );

                VALERIUM_ARMOR = new ArmorGroup("valerium", AwakenArmorMaterial.VALERIUM, "Set bonus: 15% extra melee damage");
                SpecialItemRegistry.EQUIPPABLE_ARMOR.put(VALERIUM_ARMOR.CHESTPLATE, new SetBonusEquippable()
                    .addEntityAttributeModifier(EntityAttributes.ATTACK_DAMAGE, "valerium_set_bonus", 1.15D, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                );
            }

            // Phase 3
            {
                SUNSTONE = new OreGroup(
                        "sunstone",
                        new BlockStats(25.0F, 6.0F, 6),
                        true,
                        "fragment"
                );

                MOONSTONE = new OreGroup(
                        "moonstone",
                        new BlockStats(25.0F, 6.0F, 6),
                        true,
                        "fragment"
                );

                CELESTIAL_STEEL_TOOLS = new ToolGroup("celestial_steel", AwakenToolMaterial.CELESTIAL_STEEL);
                CELESTIAL_STEEL_ARMOR = new ArmorGroup("celestial_steel", AwakenArmorMaterial.CELESTIAL_STEEL);
            }
        }
    }

    public static class TBlocks {
        public static BlockEntityType<AwakenDataBlockEntity> AWAKEN_DATA_BE_TYPE;

        public static final Block CELESTIAL_ALTAR_BLOCK = new CraftingBlock(
                FabricBlockSettings.of(Material.STONE).strength(35.0F, 12.0F).lightLevel(4).build(),
                RecipeType.CELESTIAL_ALTAR,
                RecipeType.CRAFTING_TABLE
        );

        public static final Block CELESTIAL_STEEL_ANVIL_BLOCK = new AwakenAnvilBlock(
                FabricBlockSettings.of(Material.METAL).strength(35.0F, 24.0F).lightLevel(4).build(),
                RecipeType.CELESTIAL_STEEL_ANVIL,
                RecipeType.ANVIL
        );

        public static void init() {
            info("Initializing blocks");

            register(CELESTIAL_ALTAR_BLOCK, "celestial_altar");
            register(CELESTIAL_STEEL_ANVIL_BLOCK, "celestial_steel_anvil");

            AWAKEN_DATA_BE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, Awaken.MOD_ID + "awaken_data", BlockEntityType.Builder.create(AwakenDataBlockEntity::new, Blocks.BEDROCK).build(null));
        }

        public static void register(Block block, String name) {
            Registry.register(Registry.BLOCK, new Identifier(Awaken.MOD_ID, name), block);
            Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        }
    }

    public static class TItems {
        public static final Item ENDER_DRAGON_SCALE = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
        public static final Item WITHER_RIB = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

        public static final Item CINDERED_BOW = new FlameBowItem(4.0D, false).setRarity(AwakenRarity.RARE);
        public static final Item SLIMEY_BOW = new StatusEffectBowItem(3.0D, false, StatusEffects.SLOWNESS, 2 * 20, 1).setRarity(AwakenRarity.RARE);
        public static final Item RAIDERS_AXE = new AwakenAxeItem(AwakenToolMaterial.PHASE_0_SPECIAL).setRarity(AwakenRarity.UNCOMMON);
        public static final Item ESCAPE_PLAN = new EscapePlanItem().setRarity(AwakenRarity.UNCOMMON);
        public static final Item ARCHAEOLOGIST_SPADE = new ArchaeologistSpadeItem().setRarity(AwakenRarity.UNCOMMON);
        public static final Item RUSTY_SHANK = new StatusEffectSwordItem(AwakenToolMaterial.PHASE_0_SPECIAL, StatusEffects.POISON, 8 * 20, 0).setRarity(AwakenRarity.UNCOMMON);

        public static final Item ATLANTEAN_SABRE = new AtlanteanSabreItem().setRarity(AwakenRarity.EPIC);
        public static final Item ASHEN_BLADE = new StatusEffectSwordItem(AwakenToolMaterial.PHASE_0_SWORD, StatusEffects.WITHER, 4 * 20, 1).setRarity(AwakenRarity.EPIC);
        public static final Item GLACIAL_SHARD = new StatusEffectSwordItem(AwakenToolMaterial.PHASE_0_SWORD, StatusEffects.SLOWNESS, 4 * 20, 1).setRarity(AwakenRarity.EPIC);
        public static final Item ENDERIAN_CUTLASS = new EnderianCutlassItem().setRarity(AwakenRarity.EPIC);
        public static final Item JANG_KATANA = new JangKatanaItem().setRarity(AwakenRarity.FABLED);

        // TODO: add unbreakable elytra using post-awakened materials (Dragon Bone Wings)

        public static final Item CELESTIAL_STEEL_INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
        public static final Item RAPTOR_CHICKEN_EGG = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

        public static void init() {
            info("Initializing items");

            // Phase 1
            {
                // Reagents
                register(ENDER_DRAGON_SCALE, "ender_dragon_scale");
                register(WITHER_RIB, "wither_rib");

                // Special Tools
                register(CINDERED_BOW, "cindered_bow");
                register(SLIMEY_BOW, "slimey_bow");
                register(RAIDERS_AXE, "raiders_axe");
                register(ESCAPE_PLAN, "escape_plan");
                register(ARCHAEOLOGIST_SPADE, "archaeologist_spade");
                register(RUSTY_SHANK, "rusty_shank");

                // Special Swords
                register(ATLANTEAN_SABRE, "atlantean_sabre");
                register(ASHEN_BLADE, "ashen_blade");
                register(GLACIAL_SHARD, "glacial_shard");
                register(ENDERIAN_CUTLASS, "enderian_cutlass");
                register(JANG_KATANA, "jang_katana");
            }

            register(CELESTIAL_STEEL_INGOT, "celestial_steel_ingot");
            register(RAPTOR_CHICKEN_EGG, "raptor_chicken_egg");
        }

        public static void register(Item item, String name) {
            Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), item);
        }
    }

    public static class TContainers {
        public static void init() {
            info("Initializing containers");
        }
    }

    public static class TEntities {
        // TODO: add spawning

        public static final EntityType<RaptorChickenEntity> RAPTOR_CHICKEN = Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(MOD_ID, "raptor_chicken"),
                FabricEntityTypeBuilder.create(EntityCategory.MONSTER, RaptorChickenEntity::new).size(EntityDimensions.fixed(0.95F, 1.65F)).build()
        );

        public static final EntityType<DireWolfEntity> DIRE_WOLF = Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(MOD_ID, "dire_wolf"),
                FabricEntityTypeBuilder.create(EntityCategory.MONSTER, DireWolfEntity::new).size(EntityDimensions.fixed(1.5F, 1F)).build()
        );

        public static void init() {
            info("Initializing entities");

            Registry.register(Registry.ITEM, new Identifier(MOD_ID, "raptor_chicken_spawn_egg"), new SpawnEggItem(RAPTOR_CHICKEN, 0x9C0202, 0x610000, new Item.Settings().group(ItemGroup.MISC)));
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, "dire_wolf_spawn_egg"), new SpawnEggItem(DIRE_WOLF, 0xD6E9FF, 0x97ADCC, new Item.Settings().group(ItemGroup.MISC)));
        }

        // TODO: add killer bunny (https://minecraft.gamepedia.com/Rabbit#The_Killer_Bunny)
    }

    public static class TNetworking {
        // TODO: move C2S IDs to client entry point
        // TODO: label directions on variables (see open/close crafting IDs)
        public static final Identifier SYNC_CRAFTING_SCROLLBAR_ID = new Identifier(Awaken.MOD_ID, "sync_crafting_scrollbar");
        public static final Identifier SYNC_CRAFTING_RESULTS_ID = new Identifier(Awaken.MOD_ID, "sync_crafting_results");
        public static final Identifier SYNC_CRAFTING_RESULT_SLOT_ID = new Identifier(Awaken.MOD_ID, "sync_crafting_result_slot");
        public static final Identifier SYNC_LOOK_UP_SCROLLBAR_ID = new Identifier(Awaken.MOD_ID, "sync_look_up_scrollbar");
        public static final Identifier SYNC_LOOK_UP_RESULTS_ID = new Identifier(Awaken.MOD_ID, "sync_look_up_results");
        public static final Identifier SYNC_LOOK_UP_RESULT_SLOT_ID = new Identifier(Awaken.MOD_ID, "sync_look_up_result_slot");


        public static final Identifier OPEN_CRAFTING_C2S_ID = new Identifier(Awaken.MOD_ID, "open_crafting_c2s");
        public static final Identifier OPEN_CRAFTING_S2C_ID = new Identifier(Awaken.MOD_ID, "open_crafting_s2c");
        public static final Identifier CLOSE_CRAFTING_C2S_ID = new Identifier(Awaken.MOD_ID, "close_crafting_c2s");
        public static final Identifier CLOSE_CRAFTING_S2C_ID = new Identifier(Awaken.MOD_ID, "close_crafting_s2c");

        public static void init() {
            info("Initializing networking");

            // Gets the client's scroll bar position and updates the server-side container
            ServerSidePacketRegistry.INSTANCE.register(SYNC_CRAFTING_SCROLLBAR_ID, (packetContext, data) -> {
                float scrollPosition = data.readFloat();

                packetContext.getTaskQueue().execute(() -> {
                    if (packetContext.getPlayer().container instanceof AwakenCraftingContainer) {
                        ((AwakenCraftingContainer) packetContext.getPlayer().container).scrollOutputs(scrollPosition);
                    }
                });
            });

            ServerSidePacketRegistry.INSTANCE.register(SYNC_LOOK_UP_SCROLLBAR_ID, (packetContext, data) -> {
                float scrollPosition = data.readFloat();

                packetContext.getTaskQueue().execute(() -> {
                    if (packetContext.getPlayer().container instanceof AwakenCraftingContainer) {
                        ((AwakenCraftingContainer) packetContext.getPlayer().container).scrollLookUpResults(scrollPosition);
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

                        player.container = new AwakenCraftingContainer(syncId, player.inventory);
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