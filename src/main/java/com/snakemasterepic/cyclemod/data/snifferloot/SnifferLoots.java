package com.snakemasterepic.cyclemod.data.snifferloot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.snakemasterepic.cyclemod.CycleMod;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SnifferLoots extends SimpleJsonResourceReloadListener
{
    public static final SnifferLoots STRUCTURE_LOOTS = new SnifferLoots(new Gson(), "snifferstructureloots");
    public static final SnifferLoots ARCHAEOLOGY_LOOTS = new SnifferLoots(new Gson(), "snifferarchaeologyloots");
    
    private final List<SnifferLoot> data;

    private SnifferLoots(Gson gson, String directory)
    {
        super(gson, directory);
        data = new ArrayList<>();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> entries, ResourceManager resourceManager,
            ProfilerFiller profiler)
    {
        this.data.clear();
        for (Entry<ResourceLocation, JsonElement> entry : entries.entrySet()) {
            JsonElement value = entry.getValue();
            SnifferLoot.CODEC.decode(JsonOps.INSTANCE, value).get().ifLeft(pair -> this.data.add(pair.getFirst()))
                    .ifRight(error -> CycleMod.LOGGER.error(error.toString()));
        }

        CycleMod.LOGGER.info(data.size() + " sniffer structure loots loaded");
    }
    
    public boolean isInStructure(ServerLevel world, BlockPos headPos)
    {
        return this.data.stream().anyMatch(ssl -> ssl.inStructure(world, headPos));
    }
    
    public void giveDiggingLoot(ServerLevel world, BlockPos headPos, Vec3 headVector, Entity sniffer)
    {
        this.data.forEach(ssl -> ssl.giveDiggingLoot(world, headPos, headVector, sniffer));
    }
    
    public void runArchaeology(ServerLevel world, BlockPos headPos)
    {
        this.data.forEach(ssl -> ssl.checkArchaeology(world, headPos));
    }
}
