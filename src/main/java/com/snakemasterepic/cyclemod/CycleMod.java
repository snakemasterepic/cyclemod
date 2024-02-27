package com.snakemasterepic.cyclemod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.snakemasterepic.cyclemod.data.enderman.HeightCurves;
import com.snakemasterepic.cyclemod.data.snifferloot.SnifferLoots;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidInteractionRegistry.InteractionInformation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CycleMod.MODID)
public class CycleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cyclemod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation ENDERMAN_BLOCK_LOCATION = new ResourceLocation(MODID, "endermanblocks");

    public CycleMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod)
        // to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in
        // this class, like onServerStarting() below.
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config
        // file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(HeightCurves.CURVES);
        event.addListener(SnifferLoots.STRUCTURE_LOOTS);
        event.addListener(SnifferLoots.ARCHAEOLOGY_LOOTS);
        
        FluidInteractionRegistry
                .addInteraction(
                        ForgeMod.LAVA_TYPE.get(),
                        new InteractionInformation(
                                (level, currentPos, relativePos, currentState) -> Config.TUFF_GENERATOR
                                        && level.getBlockState(currentPos.below()).is(Blocks.SOUL_SAND)
                                        && level.getBlockState(relativePos).is(Blocks.BLUE_ICE),
                                Blocks.TUFF.defaultBlockState()));
    }
}
