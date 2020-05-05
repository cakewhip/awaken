package com.kqp.awaken.item;

import com.kqp.awaken.data.AwakenConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BossSpawnerItem extends Item {
    public final EntityType<?> type;

    public BossSpawnerItem(EntityType<?> type) {
        super(new Item.Settings().group(ItemGroup.MISC).maxCount(1));

        this.type = type;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        boolean success = false;

        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (!world.isClient) {
            if (context.getSide() == Direction.UP) {
                BlockPos blockPos = context.getBlockPos();

                if (isValidSpawnBlock(world, blockPos)) {
                    if (canSpawn(world)) {
                        ItemStack itemStack = context.getStack();
                        itemStack.decrement(1);

                        spawnEntity(world, blockPos);

                        return ActionResult.SUCCESS;
                    } else {
                        player.sendMessage(new TranslatableText("item.boss_spawner.wrong_time"), true);
                    }
                } else {
                    player.sendMessage(new TranslatableText("item.boss_spawner.no_space"), true);
                }
            } else {
                player.sendMessage(new TranslatableText("item.boss_spawner.not_top"), true);
            }
        }

        return ActionResult.FAIL;
    }

    private void spawnEntity(World world, BlockPos blockPos) {
        type.spawn(world, null, null, null, blockPos, SpawnType.MOB_SUMMONED, true, false);
    }

    private boolean isValidSpawnBlock(World world, BlockPos blockPos) {
        for (int i = -8; i < 8; i++) {
            for (int j = -8; j < 8; j++) {
                for (int k = 1; k < 8; k++) {
                    BlockPos checkBlockPos = blockPos.add(i, k, j);
                    BlockState blockState = world.getBlockState(checkBlockPos);
                    if (!blockState.getBlock().isTranslucent(blockState, world, checkBlockPos)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean canSpawn(World world) {
        long time = world.getTimeOfDay() % 24000;

        return time >= AwakenConfig.VALID_BOSS_TIME_START && time < AwakenConfig.VALID_BOSS_TIME_END;
    }
}
