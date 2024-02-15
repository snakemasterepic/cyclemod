package com.snakemasterepic.cyclemod.event;

import java.util.List;

import com.snakemasterepic.cyclemod.Config;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

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
