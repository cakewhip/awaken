package com.kqp.awaken.item.health;

import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.mixin.player.MaxHealthPersister;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Health increase item-- right-click to increase life.
 * {@link MaxHealthPersister} makes the attributes persist.
 */
public class HealthIncreaseItem extends Item {
    public static final ArrayList<HealthIncreaseItem> HEALTH_INCREASE_ITEMS = new ArrayList();

    public final int extraHearts;
    public final HealthIncreaseItem[] preReqs;
    public final List<EntityAttributeModifier> healthModifiers;

    public HealthIncreaseItem(String name, int extraHearts, HealthIncreaseItem... preReqs) {
        super(new Item.Settings().group(ItemGroup.MISC).maxCount(1));

        HEALTH_INCREASE_ITEMS.add(this);

        this.extraHearts = extraHearts;
        this.preReqs = preReqs;
        this.healthModifiers = new ArrayList();

        for (int i = 0; i < extraHearts; i++) {
            healthModifiers.add(new EntityAttributeModifier(Awaken.MOD_ID + ":" + name + "_" + i, 2D, EntityAttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        EntityAttributeInstance inst = user.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);

        // Fail if prereq items have not been used yet
        for (HealthIncreaseItem item : preReqs) {
            for (EntityAttributeModifier healthModifier : item.healthModifiers) {
                if (!inst.hasModifier(healthModifier)) {
                    return TypedActionResult.fail(itemStack);
                }
            }
        }

        for (int i = 0; i < healthModifiers.size(); i++) {
            EntityAttributeModifier mod = healthModifiers.get(i);

            if (!inst.hasModifier(mod)) {
                inst.addPersistentModifier(mod);
                itemStack.decrement(1);

                return TypedActionResult.success(itemStack);
            }
        }

        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText("Use to increase max health"));
    }
}
