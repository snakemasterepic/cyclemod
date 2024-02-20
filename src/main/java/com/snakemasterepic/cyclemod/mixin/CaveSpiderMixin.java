package com.snakemasterepic.cyclemod.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.snakemasterepic.cyclemod.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

@Mixin(CaveSpider.class)
public abstract class CaveSpiderMixin extends Spider
{

    public CaveSpiderMixin(EntityType<? extends Spider> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }
    
    @Override
    public void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(10, new MakeCobwebGoal(this));
    }
    
    private static class MakeCobwebGoal extends Goal
    {
        private final Spider caveSpider;

        public MakeCobwebGoal(Spider caveSpider)
        {
            this.caveSpider = caveSpider;
        }
        
        @Override
        public boolean canUse()
        {
            return caveSpider.getTarget() == null;
        }

        @Override
        public void tick()
        {
            if (Config.CAVE_SPIDERS_MAKE_COBWEBS) {
                Level world = this.caveSpider.level();
                RandomSource rng = world.getRandom();
                if (rng.nextInt(1000) == 0) {
                    BlockPos spiderPos = this.caveSpider.getOnPos().above();
                    if (world.getBlockState(spiderPos).getBlock() == Blocks.AIR) {
                        world.setBlockAndUpdate(spiderPos, Blocks.COBWEB.defaultBlockState());
                }
                
            }}
        }
    }
}
