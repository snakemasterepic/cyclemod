package com.snakemasterepic.cyclemod.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snakemasterepic.cyclemod.event.EndermanSpawnWithBlock;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

// The purpose of this mixin is to allow endermen that spawn with blocks to despawn prior
// to placing their blocks down.
@Mixin(EnderMan.class)
public abstract class EndermanMixin extends Monster
{

    protected EndermanMixin(EntityType<? extends EnderMan> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    @Inject(method = "requiresCustomPersistence", at = @At("RETURN"), cancellable = true)
    private void canDespawnIfHoldingBlockSpawnedWith(CallbackInfoReturnable<Boolean> cir)
    {
        if (this.getPersistentData().getBoolean(EndermanSpawnWithBlock.SPAWN_WITH_BLOCK_KEY)) {
            cir.setReturnValue(super.requiresCustomPersistence());
        }
    }

    // Once an enderman places the block it spawns with in the world, we need to
    // ensure that it will not despawn should it pick up another block.
    @Inject(method = "setCarriedBlock", at = @At("RETURN"))
    private void removeSpawnWithBlockKey(@Nullable BlockState block,CallbackInfo ci)
    {
        if (block == null) {
            this.getPersistentData().putBoolean(EndermanSpawnWithBlock.SPAWN_WITH_BLOCK_KEY, false);
        }
    }
}
