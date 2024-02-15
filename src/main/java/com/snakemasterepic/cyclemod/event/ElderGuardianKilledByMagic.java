package com.snakemasterepic.cyclemod.event;

import com.snakemasterepic.cyclemod.Config;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

@Mod.EventBusSubscriber(modid = "cyclemod", bus = Bus.FORGE)
public class ElderGuardianKilledByMagic
{
    @SubscribeEvent
    public static void elderGuardianDropHeartOfTheSee(LivingDropsEvent event)
    {
        if (Config.ELDER_GUARDIAN_DROPS_HOTS
                && event.getEntity() instanceof ElderGuardian elderGuardian
                && event.getSource().is(DamageTypes.MAGIC)) {
            event
                    .getDrops()
                    .add(
                            new ItemEntity(
                                    elderGuardian.level(),
                                    elderGuardian.getX(),
                                    elderGuardian.getY(),
                                    elderGuardian.getZ(),
                                    new ItemStack(Items.HEART_OF_THE_SEA)));
        }
    }
}
