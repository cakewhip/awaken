package com.kqp.awaken.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.item.trinket.AwakenTrinketItem;
import com.kqp.awaken.trinket.AwakenSlots;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.InputStream;
import java.io.InputStreamReader;

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
        InputStream inputStream = EntityFeatureGroup.class.getClassLoader().getResourceAsStream(TRINKETS_JSON_PATH);

        if (inputStream == null) {
            throw new RuntimeException("Awaken trinkets JSON file could not be found!");
        }

        InputStreamReader reader = new InputStreamReader(inputStream);

        JsonObject trinketListJsonObject = GSON.fromJson(reader, JsonObject.class);
        trinketListJsonObject.entrySet().forEach((entry) -> {
            String trinketName = entry.getKey();

            if (!trinketName.equals("__comment")) {
                JsonObject trinketJsonObject = entry.getValue().getAsJsonObject();

                String trinketGroup = trinketJsonObject.get("trinket_group").getAsString();
                String trinketSlot = trinketJsonObject.get("trinket_slot").getAsString();
                int durability = trinketJsonObject.get("durability").getAsInt();

                AwakenTrinketItem item = new AwakenTrinketItem(trinketGroup, trinketSlot, durability);

                // Add default entity feature group if it exists
                EntityFeatureGroup.fromJson(trinketName).ifPresent(item.getEntityFeatureGroups()::add);

                register(trinketName, item);
            }
        });
    }

    public static Item register(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), item);

        return item;
    }

    private static final Gson GSON = new GsonBuilder().create();
    private static final String TRINKETS_JSON_PATH = String.format("data/%s/static/%s.json", Awaken.MOD_ID, "trinkets");
}
