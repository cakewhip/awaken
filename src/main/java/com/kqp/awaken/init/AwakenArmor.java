package com.kqp.awaken.init;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.item.armor.ArmorEFGMutator;
import com.kqp.awaken.item.armor.AwakenArmorItem;
import com.kqp.awaken.item.material.AwakenArmorMaterial;
import com.kqp.awaken.util.DataUtil;
import io.github.cottonmc.staticdata.StaticData;
import io.github.cottonmc.staticdata.StaticDataItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads stuff related to armor, like armor items and any overrides.
 */
public class AwakenArmor {
    public static Map<Identifier, ArmorEFGOverride> EFG_OVERRIDES = new HashMap();

    public static void init() {
        loadArmor();
        loadOverrides();
        applyOverrides();
    }

    /**
     * Loads json files from the armor directory.
     */
    private static void loadArmor() {
        ImmutableSet<StaticDataItem> armorStaticData = StaticData.getAllInDirectory("armor");

        armorStaticData.forEach(staticDataItem -> {
            try {
                Identifier armorItemId = DataUtil.getStrippedIdentifier(staticDataItem.getIdentifier());
                JsonObject armorJsonObject = DataUtil.getJsonObject(staticDataItem);

                JsonObject materialJsonObject = armorJsonObject.getAsJsonObject("material");
                AwakenArmorMaterial material = AwakenArmorMaterial.fromJsonObject(armorItemId.getPath(), materialJsonObject);

                JsonObject piecesJsonObject = armorJsonObject.getAsJsonObject("pieces");
                piecesJsonObject.entrySet().forEach(pieceJsonEntry -> {
                    JsonObject pieceJsonObject = pieceJsonEntry.getValue().getAsJsonObject();

                    String slotName = pieceJsonObject.get("slot").getAsString();
                    EquipmentSlot slot = EquipmentSlot.byName(slotName);

                    AwakenArmorItem armorItem = new AwakenArmorItem(material, slot);

                    JsonObject efgJsonObject = pieceJsonObject.getAsJsonObject("bonus_efg");
                    if (efgJsonObject != null) {
                        EntityFeatureGroup efg = EntityFeatureGroup.fromJsonObject(armorItemId.getPath(),
                                efgJsonObject);
                        ((ArmorEFGMutator) armorItem).setBonus(efg);
                    }

                    JsonObject setBonusJsonObject = pieceJsonObject.getAsJsonObject("set_bonus_efg");
                    if (setBonusJsonObject != null) {
                        EntityFeatureGroup efg = EntityFeatureGroup.fromJsonObject(armorItemId.getPath(),
                                setBonusJsonObject);
                        ((ArmorEFGMutator) armorItem).setSetBonus(efg);
                    }

                    Identifier pieceId = new Identifier(
                            armorItemId.getNamespace(),
                            armorItemId.getPath() + "_" + pieceJsonEntry.getKey()
                    );

                    register(pieceId, armorItem);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Loads json files from the armor_overrides directory.
     */
    private static void loadOverrides() {
        ImmutableSet<StaticDataItem> staticData = StaticData.getAllInDirectory("armor_overrides");

        staticData.forEach(staticDataItem -> {
            try {
                Identifier overrideArmorId = DataUtil.getStrippedIdentifier(staticDataItem.getIdentifier());
                JsonObject armorJsonObject = DataUtil.getJsonObject(staticDataItem);

                JsonObject piecesJsonObject = armorJsonObject.getAsJsonObject("pieces");
                piecesJsonObject.entrySet().forEach(pieceJsonEntry -> {
                    String itemId = pieceJsonEntry.getKey();
                    JsonObject pieceJsonObject = pieceJsonEntry.getValue().getAsJsonObject();

                    int priority = pieceJsonObject.get("priority").getAsInt();
                    ArmorEFGOverride overrideObj = new ArmorEFGOverride(priority);

                    JsonObject efgJsonObject = pieceJsonObject.getAsJsonObject("bonus_efg");
                    if (efgJsonObject != null) {
                        EntityFeatureGroup efg = EntityFeatureGroup.fromJsonObject(overrideArmorId.getPath(),
                                efgJsonObject);

                        overrideObj.setBonus(efg);
                    }

                    JsonObject setBonusJsonObject = pieceJsonObject.getAsJsonObject("set_bonus_efg");
                    if (setBonusJsonObject != null) {
                        EntityFeatureGroup efg = EntityFeatureGroup.fromJsonObject(overrideArmorId.getPath(),
                                setBonusJsonObject);

                        overrideObj.setSetBonus(efg);
                    }

                    Identifier identifier = new Identifier(itemId);

                    if (EFG_OVERRIDES.containsKey(identifier)) {
                        if (EFG_OVERRIDES.get(identifier).priority < priority) {
                            EFG_OVERRIDES.replace(identifier, overrideObj);
                        }
                    } else {
                        EFG_OVERRIDES.put(identifier, overrideObj);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void applyOverrides() {
        EFG_OVERRIDES.forEach((id, overrideObj) -> {
            ArmorEFGMutator efgMutator = (ArmorEFGMutator) Registry.ITEM.get(id);
            efgMutator.setBonus(overrideObj.bonus);
            efgMutator.setSetBonus(overrideObj.setBonus);
        });
    }

    public static Item register(Identifier id, Item item) {
        Registry.register(Registry.ITEM, id, item);

        return item;
    }

    /**
     * Data class for armor EFG overrides.
     */
    public static class ArmorEFGOverride {
        public final int priority;
        public EntityFeatureGroup bonus, setBonus;

        ArmorEFGOverride(int priority) {
            this.priority = priority;
        }

        public void setBonus(EntityFeatureGroup bonus) {
            this.bonus = bonus;
        }

        public void setSetBonus(EntityFeatureGroup setBonus) {
            this.setBonus = setBonus;
        }
    }
}
