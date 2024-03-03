package com.snakemasterepic.cyclemod.event;

import com.snakemasterepic.cyclemod.Config;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = "cyclemod", bus = Bus.FORGE)
public class GuardianLightningEvent
{

    @SubscribeEvent
    public static void onStruckByLightning(EntityStruckByLightningEvent event)
    {
        if (Config.GUARDIAN_TO_ELDER_GUARDIAN_CONVERSION && event.getEntity() instanceof Guardian && !(event.getEntity() instanceof ElderGuardian)) {
            Entity guardian = event.getEntity();
            ElderGuardian elderGuardian = EntityType.ELDER_GUARDIAN.create(guardian.level());
            elderGuardian.moveTo(guardian.getX(), guardian.getY(), guardian.getZ(), guardian.getYRot(), guardian.getXRot());

            if (guardian.hasCustomName()) {
                elderGuardian.setCustomName(guardian.getCustomName());
                elderGuardian.setCustomNameVisible(guardian.isCustomNameVisible());
            }

            elderGuardian.setPersistenceRequired();
            guardian.level().addFreshEntity(elderGuardian);
            guardian.discard();
        }
    }

}
