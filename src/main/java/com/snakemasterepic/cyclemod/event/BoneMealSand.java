package com.snakemasterepic.cyclemod.event;

import com.snakemasterepic.cyclemod.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.Event.Result;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;

@Mod.EventBusSubscriber(modid = "cyclemod", bus = Bus.FORGE)
public class BoneMealSand
{

    private static int rollRandom(RandomSource rng)
    {
        return rng.nextIntBetweenInclusive(-1, 1);
    }
    
    @SubscribeEvent
    public static void boneMealSand(
            BonemealEvent event)
    {
        if (Config.BONE_MEAL_SAND
                && event.getBlock().getBlockHolder().is(
                        BlockTags.SAND)
                && event.getLevel() instanceof ServerLevel world) {
            RandomSource rng = world.getRandom();
            BlockPos eventPos = event.getPos();
            
            for (int i = 0; i < 5; i++) {
                int x = rollRandom(rng) + rollRandom(rng) + rollRandom(rng);
                int y = (rollRandom(rng) + rollRandom(rng)) / 2;
                int z = rollRandom(rng) + rollRandom(rng) + rollRandom(rng);
                BlockPos targetPos = new BlockPos(
                        eventPos.getX() + x,
                        eventPos.getY() + y,
                        eventPos.getZ() + z);
                if (world.getBlockState(targetPos).getBlockHolder().is(BlockTags.SAND) && world.getBlockState(targetPos.above()).getBlock() == Blocks.AIR) {
                    world.setBlockAndUpdate(targetPos.above(), Blocks.DEAD_BUSH.defaultBlockState());
                }
            }
            
            event.setResult(Result.ALLOW);
        }
    }
}
