package com.snakemasterepic.cyclemod.event;

import com.snakemasterepic.cyclemod.Config;

import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent.FluidPlaceBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = "cyclemod", bus = Bus.FORGE)
public class DeepslateGenerator
{
    @SubscribeEvent
    public static void deepslateGenerator(FluidPlaceBlockEvent event)
    {
        if (Config.DEEPSLATE_GENERATOR && event.getLevel().getBiome(event.getPos()).is(Biomes.DEEP_DARK)) {
            if (event.getNewState().getBlock() == Blocks.COBBLESTONE) {
                event.setNewState(Blocks.COBBLED_DEEPSLATE.defaultBlockState());
            }
            
            if (event.getNewState().getBlock() == Blocks.STONE) {
                event.setNewState(Blocks.DEEPSLATE.defaultBlockState());
            }
        }
    }
}
