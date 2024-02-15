package com.snakemasterepic.cyclemod.data.enderman;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;

/*
 * JSON template for height curve
 * 
{
  "block": "",
  "dimensions": [],
  "biomes": [],
  "canSeeSky": false,
  "points": [
    {
      "yLevel": -64,
      "weight": 0
    }, {
      "yLevel": 319,
      "weight": 0
    }
  ]
}
 */

public class HeightCurve
{
    private final Block block;
    private final List<String> dimensions;
    private final List<String> biomes;
    private final Optional<Boolean> canSeeSky;
    private final List<CurvePoint> points;

    public HeightCurve(Block block, List<String> dimensions, List<String> biomes, Optional<Boolean> canSeeSky)
    {
        this.block = block;
        this.dimensions = dimensions;
        this.biomes = biomes;
        this.canSeeSky = canSeeSky;
        this.points = new ArrayList<>();
    }

    public HeightCurve(
            Block block,
            List<String> dimensions,
            List<String> biomes,
            Optional<Boolean> canSeeSky,
            List<CurvePoint> points)
    {
        this.block = block;
        this.dimensions = dimensions;
        this.biomes = biomes;
        this.canSeeSky = canSeeSky;
        this.points = points;
    }

    public Block getBlock()
    {
        return this.block;
    }

    public List<String> getDimensions()
    {
        return this.dimensions;
    }

    public List<String> getBiomes()
    {
        return this.biomes;
    }

    public Optional<Boolean> getCanSeeSky()
    {
        return this.canSeeSky;
    }

    public List<CurvePoint> getPoints()
    {
        return this.points;
    }

    public void addPoint(int yLevel, double weight)
    {
        int index = 0;
        while (index < points.size()) {
            int result = yLevel - this.points.get(index).getYLevel();
            if (result == 0) {
                throw new IllegalStateException("Duplicate y-level");
            } else if (result < 0) {
                break;
            }
            index++;
        }

        points.add(index, new CurvePoint(yLevel, weight));
    }

    public double getWeight(Level world, BlockPos spawnPos)
    {
        if (this.matchesPositon(world, spawnPos)) {
            return this.interpolateWeight(spawnPos.getY());
        } else {
            return 0;
        }
    }

    private boolean matchesPositon(Level world, BlockPos spawnPos)
    {
        if (this.dimensions.size() > 0 && !this.dimensions.contains(world.dimension().location().toString())) {
            return false;
        }

        if (this.biomes.size() > 0
                && !this.biomes
                        .contains(world.getBiome(spawnPos).unwrapKey().orElse(Biomes.THE_VOID).location().toString())) {
            return false;
        }

        if (!this.canSeeSky.map(canSeeSky -> canSeeSky.equals(world.canSeeSky(spawnPos))).orElse(true)) {
            return false;
        }

        return true;
    }

    private double interpolateWeight(int yLevel)
    {
        int count = this.points.size();
        if (count == 0 || yLevel < this.points.get(0).yLevel || yLevel > this.points.get(count - 1).yLevel) {
            return 0;
        }

        int min = 0;
        int max = count - 1;

        while (max - min > 1) {
            int mid = (min + max) / 2;
            CurvePoint midPoint = this.points.get(mid);

            if (midPoint.getYLevel() == yLevel) {
                return midPoint.weight;
            } else if (midPoint.getYLevel() > yLevel) {
                max = mid;
            } else {
                min = mid;
            }
        }

        CurvePoint low = this.points.get(min);
        CurvePoint high = this.points.get(max);

        double delta = high.getYLevel() - low.getYLevel();

        return high.getWeight() * (yLevel - low.getYLevel()) / delta
                + low.getWeight() * (high.getYLevel() - yLevel) / delta;
    }

    public static final Codec<HeightCurve> CODEC = RecordCodecBuilder
            .create(
                    instance -> instance
                            .group(
                                    BuiltInRegistries.BLOCK
                                            .byNameCodec()
                                            .fieldOf("block")
                                            .forGetter(HeightCurve::getBlock),
                                    Codec.STRING
                                            .listOf()
                                            .optionalFieldOf("dimensions", new ArrayList<>())
                                            .forGetter(HeightCurve::getDimensions),
                                    Codec.STRING
                                            .listOf()
                                            .optionalFieldOf("biomes", new ArrayList<>())
                                            .forGetter(HeightCurve::getBiomes),
                                    Codec.BOOL.optionalFieldOf("canSeeSky").forGetter(HeightCurve::getCanSeeSky),
                                    CurvePoint.CODEC.listOf().fieldOf("points").forGetter(HeightCurve::getPoints))
                            .apply(instance, HeightCurve::new));

    public static class CurvePoint
    {
        private final int yLevel;
        private final double weight;

        public CurvePoint(int yLevel, double weight)
        {
            this.yLevel = yLevel;
            this.weight = weight;
        }

        public int getYLevel()
        {
            return this.yLevel;
        }

        public double getWeight()
        {
            return this.weight;
        }

        public static final Codec<CurvePoint> CODEC = RecordCodecBuilder
                .create(
                        instance -> instance
                                .group(
                                        Codec.INT.fieldOf("yLevel").forGetter(CurvePoint::getYLevel),
                                        Codec.DOUBLE.fieldOf("weight").forGetter(CurvePoint::getWeight))
                                .apply(instance, CurvePoint::new));
    }
}
