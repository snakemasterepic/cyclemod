package com.snakemasterepic.cyclemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snakemasterepic.cyclemod.Config;
import com.snakemasterepic.cyclemod.data.snifferloot.SnifferLoots;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.phys.Vec3;

@Mixin(targets="net.minecraft.world.entity.animal.sniffer.SnifferAi$Scenting")
public abstract class SnifferAiMixin
{
    @Inject(method="stop", at = @At("RETURN"))
    public void onMakeSuspiciousBlock(ServerLevel world, Sniffer sniffer, long time, CallbackInfo callbackInfo)
    {
        if (Config.SNIFFER_ARCHAEOLOGY) {
            Vec3 headVec = sniffer.position().add(sniffer.getForward().scale(2.25));
            BlockPos headPos = BlockPos.containing(headVec.x(), sniffer.getY() -0.8F, headVec.z());
            SnifferLoots.ARCHAEOLOGY_LOOTS.runArchaeology(world, headPos);
        }
    }
}
