package com.snakemasterepic.cyclemod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.snakemasterepic.cyclemod.data.enderman.HeightCurves;
import com.snakemasterepic.cyclemod.data.snifferloot.SnifferLoots;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry.InteractionInformation;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CycleMod.MODID)
public class CycleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cyclemod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation ENDERMAN_BLOCK_LOCATION = new ResourceLocation(MODID, "endermanblocks");

    // The constructor for the mod class is the first code that is run when your mod
    // is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and
    // pass them in automatically.
    public CycleMod(IEventBus modEventBus)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod)
        // to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in
        // this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

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
                        NeoForgeMod.LAVA_TYPE.value(),
                        new InteractionInformation(
                                (level, currentPos, relativePos, currentState) -> Config.TUFF_GENERATOR
                                        && level.getBlockState(currentPos.below()).is(Blocks.SOUL_SAND)
                                        && level.getBlockState(relativePos).is(Blocks.BLUE_ICE),
                                Blocks.TUFF.defaultBlockState()));
    }
}
