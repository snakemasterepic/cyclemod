package com.snakemasterepic.cyclemod.event;

import com.snakemasterepic.cyclemod.Config;
import com.snakemasterepic.cyclemod.CycleMod;
import com.snakemasterepic.cyclemod.data.enderman.HeightCurves;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = "cyclemod", bus = Bus.FORGE)
public class EndermanSpawnWithBlock
{
    public static final String SPAWN_WITH_BLOCK_KEY = "spawnedWithBlock";

    public static final TagKey<Biome> ENDERMAN_HEAVY = TagKey.create(
            Registries.BIOME,
            new ResourceLocation(
                    CycleMod.MODID,
                    "enderman_heavy"));

    @SubscribeEvent
    public static void onSpawn(
            MobSpawnEvent.FinalizeSpawn event)
    {
        if (Config.ENDERMAN_SPAWN_WITH_BLOCK) {
            Entity entity = event.getEntity();
            if (entity instanceof EnderMan enderman) {
                BlockPos eventPos = enderman.getOnPos();
                Level world = event.getLevel().getLevel();
                Holder<Biome> biome = world.getBiome(
                        eventPos);

                double blockChance = biome.is(
                        ENDERMAN_HEAVY) ? Config.ENDERMAN_SPAWN_WITH_BLOCK_CHANCE_ENDERMAN_HEAVY_BIOMES : Config.ENDERMAN_SPAWN_WITH_BLOCK_CHANCE;

                double roll = Math.random();

                if (roll < blockChance) {
                    Block heldBlock = HeightCurves.CURVES.chooseBlock(
                            world,
                            eventPos.above());

                    if (heldBlock != null) {
                        enderman.getPersistentData().putBoolean(
                                SPAWN_WITH_BLOCK_KEY,
                                true);
                        enderman.setCarriedBlock(
                                heldBlock.defaultBlockState());
                    }
                }
            }
        }

    }
}
