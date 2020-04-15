package com.kqp.awaken.init;

import com.kqp.awaken.entity.DireWolfEntity;
import com.kqp.awaken.entity.RaptorChickenEntity;
import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
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

    public static void init() {
        Awaken.info("Initializing entities");

        Registry.register(Registry.ATTRIBUTES, new Identifier(Awaken.MOD_ID, "ranged_damage"), AwakenEntityAttributes.RANGED_DAMAGE);

        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, "raptor_chicken_spawn_egg"), new SpawnEggItem(RAPTOR_CHICKEN, 0x9C0202, 0x610000, new Item.Settings().group(ItemGroup.MISC)));
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, "dire_wolf_spawn_egg"), new SpawnEggItem(DIRE_WOLF, 0xD6E9FF, 0x97ADCC, new Item.Settings().group(ItemGroup.MISC)));
    }

}
