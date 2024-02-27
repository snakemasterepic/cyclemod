package com.snakemasterepic.cyclemod.mixin;

import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snakemasterepic.cyclemod.Config;
import com.snakemasterepic.cyclemod.data.snifferloot.SnifferLoots;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

@Mixin(Sniffer.class)
public abstract class SnifferMixin extends Animal
{
    @Shadow
    private static EntityDataAccessor<Integer> DATA_DROP_SEED_AT_TICK;

    protected SnifferMixin(EntityType<? extends Animal> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    @Inject(method = "canDig(Lnet/minecraft/core/BlockPos;)Z", at = @At("RETURN"), cancellable = true)
    public void canDigInStructures(BlockPos headPos, CallbackInfoReturnable<Boolean> callbackInfo)
    {
        if (!Config.SNIFFER_LOOT) {
            return;
        }

        if (this
                .getExploredPositions()
                .anyMatch(globalPos -> GlobalPos.of(this.level().dimension(), headPos).equals(globalPos))) {
            return;
        }

        if (!Optional.ofNullable(this.getNavigation().createPath(headPos, 1)).map(Path::canReach).orElse(false)) {
            return;
        }

        if (this.level() instanceof ServerLevel world) {
            if (SnifferLoots.STRUCTURE_LOOTS.isInStructure(world, headPos)) {
                callbackInfo.setReturnValue(true);
            }
        }
    }

    @Inject(method = "dropSeed", at = @At("HEAD"), cancellable = true)
    private void dropTreasure(CallbackInfo callbackInfo)
    {
        if (!Config.SNIFFER_LOOT) {
            return;
        }

        if (this.entityData.get(DATA_DROP_SEED_AT_TICK) != this.tickCount) {
            return;
        }

        if (this.level() instanceof ServerLevel world) {
            BlockPos headPos = this.getHeadBlock();
            if (SnifferLoots.STRUCTURE_LOOTS.isInStructure(world, headPos)) {
                SnifferLoots.STRUCTURE_LOOTS.giveDiggingLoot(world, headPos, this.getHeadPosition(), this);
                this.playSound(SoundEvents.SNIFFER_DROP_SEED, 1.0F, 1.0F);
                callbackInfo.cancel();
            }
        }
    }

    @Shadow
    private Stream<GlobalPos> getExploredPositions()
    {
        // Private methods cannot be abstract
        throw new IllegalStateException("Mixin failed to shadow getExploredPositions()");
    }

    @Shadow
    private BlockPos getHeadBlock()
    {
        // Private methods cannot be abstract
        throw new IllegalStateException("Mixin failed to shadow getHeadBlock()");
    }

    @Shadow
    private Vec3 getHeadPosition()
    {
        // Private methods cannot be abstract
        throw new IllegalStateException("Mixin failed to shadow getHeadPosition()");
    }
}
