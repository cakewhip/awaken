package com.kqp.awaken.entity.mob;

import com.kqp.awaken.init.AwakenEntities;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

/**
 * Spider sac entity class.
 */
public class SpiderSacEntity extends HostileEntity {
    private static final int TIME_TO_HATCH = 4 * 20;

    public boolean hatching = false;
    public int hatchTime = 0;

    public SpiderSacEntity(World world) {
        super(AwakenEntities.SPIDER_SAC, world);
    }

    public static DefaultAttributeContainer.Builder createSpiderSacAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source == DamageSource.ON_FIRE) {
            amount *= 4.0F;
        }

        return super.damage(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        if (!hatching) {
            PlayerEntity player = this.world.getClosestPlayer(this, 8F);

            if (player != null) {
                hatching = true;
                hatchTime = TIME_TO_HATCH;
            }
        } else {
            if (hatchTime <= 0 && !this.removed) {
                int count = 5 + this.random.nextInt(3);

                for (int i = 0; i < count; i++) {
                    EntityType spider = this.random.nextBoolean() ? EntityType.SPIDER : EntityType.CAVE_SPIDER;

                    spider.spawn(this.world, null, null, null, this.getBlockPos(), SpawnType.NATURAL, false, false);
                }

                this.remove();
            } else {
                hatchTime--;
            }
        }
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        if (tag.contains("Hatching")) {
            this.hatching = tag.getBoolean("Hatching");
        }

        if (tag.contains("HatchTime")) {
            this.hatchTime = tag.getInt("HatchTime");
        }
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        tag.putBoolean("Hatching", this.hatching);
        tag.putInt("HatchTime", this.hatchTime);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    @Override
    public void refreshPositionAndAngles(double x, double y, double z, float yaw, float pitch) {
        super.refreshPositionAndAngles(x, y, z, yaw, pitch);
    }
}
