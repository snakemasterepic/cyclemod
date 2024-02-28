package com.snakemasterepic.cyclemod.data.snifferloot;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Structures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

/*

Template for structure

{
  "structure": "",
  "lootTables": [
    {
      "name": "",
      "weight": 0
    }
  ]
}

 */

public class SnifferLoot
{
    private final String structure;

    private final List<LootTableWeight> tables;

    public SnifferLoot(String structure, List<LootTableWeight> tables)
    {
        this.structure = structure;
        this.tables = tables;
    }

    public String getStructure()
    {
        return this.structure;
    }

    public List<LootTableWeight> getTables()
    {
        return this.tables;
    }

    public static final Codec<SnifferLoot> CODEC = RecordCodecBuilder
            .create(
                    instance -> instance
                            .group(
                                    Codec.STRING.fieldOf("structure").forGetter(SnifferLoot::getStructure),
                                    LootTableWeight.CODEC
                                            .listOf()
                                            .fieldOf("lootTables")
                                            .forGetter(SnifferLoot::getTables))
                            .apply(instance, SnifferLoot::new));

    public boolean inStructure(ServerLevel world, BlockPos headPos)
    {
        return world
                .structureManager()
                .getStructureWithPieceAt(
                        headPos,
                        ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(this.structure)))
                .isValid();
    }

    public void giveDiggingLoot(ServerLevel world, BlockPos headPos, Vec3 headVector, Entity sniffer)
    {
        if (this.inStructure(world, headPos)) {
            ResourceLocation lootTableLocation = this.chooseLootTable(world);

            if (lootTableLocation == null) {
                return;
            }

            LootTable lootTable = world.getServer().getLootData().getLootTable(lootTableLocation);

            LootParams params = new LootParams.Builder(world)
                    .withParameter(LootContextParams.ORIGIN, headVector)
                    .withParameter(LootContextParams.THIS_ENTITY, sniffer)
                    .create(LootContextParamSets.GIFT);

            List<ItemStack> roll = lootTable.getRandomItems(params);

            if (roll.size() > 0) {
                ItemStack selectedItemStack = roll.get(world.getRandom().nextInt(roll.size()));

                ItemEntity itementity = new ItemEntity(
                        world,
                        headPos.getX(),
                        headPos.getY(),
                        headPos.getZ(),
                        selectedItemStack);

                itementity.setDefaultPickUpDelay();
                world.addFreshEntity(itementity);
            }
        }
    }

    public void checkArchaeology(ServerLevel world, BlockPos headPos)
    {
        if (this.inStructure(world, headPos)) {
            Block headBlock = world.getBlockState(headPos).getBlock();

            ResourceLocation lootTable = this.chooseLootTable(world);

            if (lootTable == null) {
                return;
            }
            
            if (headBlock == Blocks.SAND) {
                world.setBlockAndUpdate(headPos, Blocks.SUSPICIOUS_SAND.defaultBlockState());
            } else if (headBlock == Blocks.GRAVEL) {
                world.setBlockAndUpdate(headPos, Blocks.SUSPICIOUS_GRAVEL.defaultBlockState());
            } else {
                return;
            }

            world
                    .getBlockEntity(headPos, BlockEntityType.BRUSHABLE_BLOCK)
                    .ifPresent(be -> be.setLootTable(lootTable, world.getRandom().nextLong()));
        }
    }

    private ResourceLocation chooseLootTable(ServerLevel world)
    {
        int totalWeight = this.tables.stream().map(table -> table.getWeight()).reduce(0, (x, y) -> x + y);

        if (totalWeight == 0) {
            return null;
        }

        int choice = world.getRandom().nextInt(totalWeight);
        int index = 0;

        do {
            choice -= this.tables.get(index).getWeight();
            index++;
        } while (choice > 0 && index < this.tables.size());

        index--;
        return new ResourceLocation(this.tables.get(index).getName());
    }

    public static class LootTableWeight
    {
        private final String name;
        private final int weight;

        public LootTableWeight(String name, int weight)
        {
            this.name = name;
            this.weight = weight;
        }

        public String getName()
        {
            return this.name;
        }

        public int getWeight()
        {
            return this.weight;
        }

        public static final Codec<LootTableWeight> CODEC = RecordCodecBuilder
                .create(
                        instance -> instance
                                .group(
                                        Codec.STRING.fieldOf("name").forGetter(LootTableWeight::getName),
                                        Codec.INT.fieldOf("weight").forGetter(LootTableWeight::getWeight))
                                .apply(instance, LootTableWeight::new));
    }
}
