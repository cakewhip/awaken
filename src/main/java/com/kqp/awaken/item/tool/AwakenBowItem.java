package com.kqp.awaken.item.tool;

import com.kqp.awaken.loot.AwakenRarity;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for special bows that don't break.
 */
public abstract class AwakenBowItem extends BowItem {
    public AwakenRarity rarity = AwakenRarity.OKAY;

    /**
     * Base damage, subject to additional damage by arrow used.
     * The vanilla calculates as such: 2 + (powerLevel + 1) * 0.5
     */
    public double baseDamage;

    /**
     * Whether this bow requires ammo.
     */
    public boolean infinity;

    public AwakenBowItem(double baseDamage, boolean infinity) {
        super(new Item.Settings().maxCount(1).maxDamage(-1).group(ItemGroup.COMBAT));

        this.baseDamage = baseDamage;
        this.infinity = infinity;
    }

    @Override
    public void onStoppedUsing(ItemStack itemStack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) user;
            boolean bl = playerEntity.abilities.creativeMode || infinity;

            ItemStack arrowStack = playerEntity.getArrowType(itemStack);
            if (!arrowStack.isEmpty() || bl) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float pullProgress = getPullProgress(this.getMaxUseTime(itemStack) - remainingUseTicks);

                if (pullProgress >= 0.1F) {
                    if (!world.isClient) {
                        PersistentProjectileEntity projectileEntity = createProjectileEntity(arrowStack, world, playerEntity, remainingUseTicks);
                        projectileEntity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, pullProgress * 3.0F, 1.0F);

                        if (pullProgress == 1.0F) {
                            projectileEntity.setCritical(true);
                        }

                        projectileEntity.setDamage(baseDamage);

                        if (infinity || playerEntity.abilities.creativeMode) {
                            projectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }

                        world.spawnEntity(projectileEntity);
                    }

                    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);

                    if (!playerEntity.abilities.creativeMode || !infinity) {
                        arrowStack.decrement(1);
                        if (arrowStack.isEmpty()) {
                            playerEntity.inventory.removeOne(arrowStack);
                        }
                    }

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    /**
     * Instantiate the projectile entity that the bow will fire.
     *
     * @param stack             The stack of arrows used
     * @param world
     * @param user
     * @param remainingUseTicks
     * @return ProjectileEntity
     */
    public abstract PersistentProjectileEntity createProjectileEntity(ItemStack stack, World world, LivingEntity user, int remainingUseTicks);

    public Item setRarity(AwakenRarity rarity) {
        this.rarity = rarity;

        return this;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.get(0).getStyle().setColor(rarity.color);
    }
}
