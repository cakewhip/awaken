package com.kqp.awaken.init;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.item.trinket.AwakenTrinketItem;
import com.kqp.awaken.trinket.AwakenSlots;
import com.kqp.awaken.util.DataUtil;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import io.github.cottonmc.staticdata.StaticData;
import io.github.cottonmc.staticdata.StaticDataItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.Optional;

public class AwakenTrinkets {
    public static void init() {
        addSlots();
        loadTrinkets();
    }

    private static void addSlots() {
        TrinketSlots.addSlot(SlotGroups.HEAD, AwakenSlots.HAT, new Identifier(Awaken.MOD_ID, "textures/item/empty_trinket_slot_hat.png"));
        TrinketSlots.addSlot(SlotGroups.HEAD, Slots.MASK, new Identifier("trinkets", "textures/item/empty_trinket_slot_mask.png"));
        TrinketSlots.addSlot(SlotGroups.HEAD, Slots.NECKLACE, new Identifier("trinkets", "textures/item/empty_trinket_slot_necklace.png"));
        TrinketSlots.addSlot(SlotGroups.CHEST, Slots.BELT, new Identifier("trinkets", "textures/item/empty_trinket_slot_belt.png"));
        TrinketSlots.addSlot(SlotGroups.LEGS, Slots.CHARM, new Identifier("trinkets", "textures/item/empty_trinket_slot_charm.png"));
        TrinketSlots.addSlot(SlotGroups.FEET, Slots.AGLET, new Identifier("trinkets", "textures/item/empty_trinket_slot_aglet.png"));
        TrinketSlots.addSlot(SlotGroups.HAND, Slots.RING, new Identifier("trinkets", "textures/item/empty_trinket_slot_ring.png"));
        TrinketSlots.addSlot(SlotGroups.HAND, Slots.GLOVES, new Identifier("trinkets", "textures/item/empty_trinket_slot_gloves.png"));
        TrinketSlots.addSlot(SlotGroups.OFFHAND, Slots.RING, new Identifier("trinkets", "textures/item/empty_trinket_slot_ring.png"));
    }

    private static void loadTrinkets() {
        ImmutableSet<StaticDataItem> trinketStaticData = StaticData.getAllInDirectory("trinkets");

        trinketStaticData.forEach(staticDataItem -> {
            try {
                Identifier trinketItemId = DataUtil.getStrippedIdentifier(staticDataItem.getIdentifier());
                JsonObject trinketJsonObject = DataUtil.getJsonObject(staticDataItem);

                String trinketGroup = trinketJsonObject.get("trinket_group").getAsString();
                String trinketSlot = trinketJsonObject.get("trinket_slot").getAsString();
                int durability = trinketJsonObject.get("durability").getAsInt();

                AwakenTrinketItem trinketItem = new AwakenTrinketItem(trinketGroup, trinketSlot, durability);

                JsonObject efgJsonObject = trinketJsonObject.get("entity_feature_group").getAsJsonObject();
                EntityFeatureGroup efg = EntityFeatureGroup.fromJsonObject(trinketItemId.getPath(),
                        efgJsonObject);
                trinketItem.getEntityFeatureGroups().add(efg);

                register(trinketItemId, trinketItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static Item register(Identifier id, Item item) {
        Registry.register(Registry.ITEM, id, item);

        return item;
    }
}
