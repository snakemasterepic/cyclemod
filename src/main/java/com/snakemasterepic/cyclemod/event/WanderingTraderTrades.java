package com.snakemasterepic.cyclemod.event;

import java.util.List;

import com.snakemasterepic.cyclemod.Config;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = "cyclemod", bus = Bus.FORGE)
public class WanderingTraderTrades
{
    @SubscribeEvent
    public static void addSporeBlossomTrade(WandererTradesEvent event)
    {
        if (Config.TRADE_SPORE_BLOSSOM) {
            List<VillagerTrades.ItemListing> trades = event.getRareTrades();
            trades.add(new BasicItemListing(4, new ItemStack(Items.SPORE_BLOSSOM), 6, 1));
        }
    }
}
