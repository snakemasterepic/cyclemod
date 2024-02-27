package com.snakemasterepic.cyclemod.event;

import com.snakemasterepic.cyclemod.Config;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = "cyclemod", bus = Bus.FORGE)
public class DragonHeadDrop
{

    @SubscribeEvent
    public static void dragonDropHead(
            LivingDropsEvent event)
    {
        if (Config.ENDER_DRAGON_DROPS_HEAD
                && event.getEntity() instanceof EnderDragon dragon
                && event.getSource().getEntity() instanceof Creeper creeper
                && creeper.isPowered()) {
            event.getDrops().add(
                    new ItemEntity(
                            dragon.level(),
                            dragon.getX(),
                            dragon.getY(),
                            dragon.getZ(),
                            new ItemStack(
                                    Items.DRAGON_HEAD)));
        }
    }
}
