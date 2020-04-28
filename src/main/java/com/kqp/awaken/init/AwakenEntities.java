package com.kqp.awaken.init;

import com.kqp.awaken.entity.AbominationEntity;
import com.kqp.awaken.entity.DireWolfEntity;
import com.kqp.awaken.entity.RaptorChickenEntity;
import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import net.fabricmc.fabric.api.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenEntities {
    // TODO: add spawning
    // TODO: add killer bunny (https://minecraft.gamepedia.com/Rabbit#The_Killer_Bunny)

    public static final EntityType<RaptorChickenEntity> RAPTOR_CHICKEN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "raptor_chicken"),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, RaptorChickenEntity::new).size(EntityDimensions.fixed(0.95F, 1.65F)).build()
    );

    public static final EntityType<DireWolfEntity> DIRE_WOLF = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "dire_wolf"),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, DireWolfEntity::new).size(EntityDimensions.fixed(1.5F, 1F)).build()
    );

    public static final EntityType<AbominationEntity> ABOMINATION = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "abomination"),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, AbominationEntity::new).size(EntityDimensions.fixed(0.6F * 3F, 1.95F * 3F)).build()
    );

    public static void init() {
        Awaken.info("Initializing entities");

        // Custom Attributes
        {
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "ranged_damage"), AwakenEntityAttributes.RANGED_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "bow_damage"), AwakenEntityAttributes.BOW_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "crossbow_damage"), AwakenEntityAttributes.CROSSBOW_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "trident_damage"), AwakenEntityAttributes.TRIDENT_DAMAGE);

            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "melee_damage"), AwakenEntityAttributes.MELEE_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "sword_damage"), AwakenEntityAttributes.SWORD_DAMAGE);
            Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "axe_damage"), AwakenEntityAttributes.AXE_DAMAGE);
        }

        // Phase 2 Mobs
        {
            register(RAPTOR_CHICKEN, 0x9C0202, 0x610000, RaptorChickenEntity.createRaptorChickenAttributes());
            register(DIRE_WOLF, 0xD6E9FF, 0x97ADCC, DireWolfEntity.createDireWolfAttributes());
            register(ABOMINATION, 0xFFFFFF, 0x000000, AbominationEntity.createAbominationAttributes());
        }
    }

    private static <T extends LivingEntity> void register(EntityType<T> type, int primaryColor, int secondaryColor, DefaultAttributeContainer.Builder attributeBuilder) {
        Registry.register(Registry.ITEM, new Identifier(EntityType.getId(type).toString() + "_spawn_egg"),
                new SpawnEggItem(type, primaryColor, secondaryColor, new Item.Settings().group(ItemGroup.MISC))
        );

        FabricDefaultAttributeRegistry.register(type, attributeBuilder);
    }
}
