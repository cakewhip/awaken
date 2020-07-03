package com.kqp.awaken.init;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.kqp.awaken.effect.EntityFeatureGroup;
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

public class AwakenArmor {
    public static void init() {
        loadArmor();
    }

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
                        armorItem.setItemMods(efg);
                    }

                    JsonObject setBonusJsonObject = pieceJsonObject.getAsJsonObject("set_bonus_efg");
                    if (setBonusJsonObject != null) {
                        EntityFeatureGroup efg = EntityFeatureGroup.fromJsonObject(armorItemId.getPath(),
                                setBonusJsonObject);
                        armorItem.setSetMods(efg);
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

    public static Item register(Identifier id, Item item) {
        Registry.register(Registry.ITEM, id, item);

        return item;
    }
}
