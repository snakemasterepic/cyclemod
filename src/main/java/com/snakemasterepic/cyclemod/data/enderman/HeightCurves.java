package com.snakemasterepic.cyclemod.data.enderman;

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
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class HeightCurves extends SimpleJsonResourceReloadListener
{
    public static final HeightCurves CURVES = new HeightCurves(new Gson(), "endermanblocks");

    private final List<HeightCurve> data;

    private HeightCurves(Gson gson, String directory)
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
            HeightCurve.CODEC.decode(JsonOps.INSTANCE, value).get().ifLeft(pair -> this.data.add(pair.getFirst()))
                    .ifRight(error -> CycleMod.LOGGER.error(error.toString()));
        }

        CycleMod.LOGGER.info(data.size() + " height curves loaded");
    }

    public Block chooseBlock(Level world, BlockPos spawnPos)
    {
        List<Double> weights = this.data.stream().map(curve -> curve.getWeight(world, spawnPos)).toList();
        double totalWeight = weights.stream().reduce(0.0, (x, y) -> x + y);

        if (totalWeight == 0) {
            return null;
        }

        double randomScore = Math.random() * totalWeight;
        int index = 0;

        do {
            randomScore -= weights.get(index);
            index++;
        } while (index < weights.size() && randomScore > 0);

        // Index was incremented to 1 past where we need to stop, so use index-1.
        return this.data.get(index - 1).getBlock();
    }
}
