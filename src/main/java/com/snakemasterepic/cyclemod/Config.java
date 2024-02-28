package com.snakemasterepic.cyclemod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@Mod.EventBusSubscriber(modid = CycleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue _ENDERMAN_SPAWN_WITH_BLOCK = BUILDER
            .comment("Whether to allow endermen to spawn with a block").define("endermanSpawnWithBlock", true);

    private static final ModConfigSpec.DoubleValue _ENDERMAN_SPAWN_WITH_BLOCK_CHANCE = BUILDER
            .comment("The probability of a spawned enderman having a block")
            .defineInRange("endermanSpawnWithBlockChance", 0.25, 0, 1);

    private static final ModConfigSpec.DoubleValue _ENDERMAN_SPAWN_WITH_BLOCK_CHANCE_ENDERMAN_HEAVY_BIOMES = BUILDER
            .comment("The probability of a spawned enderman having a block in an enderman-heavy biome")
            .defineInRange("endermanSpawnWithBlockChanceEndermanHeavyBiome", 0.02, 0, 1);
    
    private static final ModConfigSpec.BooleanValue _ENDER_DRAGON_DROPS_HEAD = BUILDER
            .comment("Whether the ender dragon drops its head if killed by a charged creeper")
            .define("enderDragonDropsHead", true);
    
    private static final ModConfigSpec.BooleanValue _BONE_MEAL_SAND = BUILDER
            .comment("Whether bone mean can be used on sand to get dead bushes")
            .define("boneMealSand", true);

    private static final ModConfigSpec.BooleanValue _CAVE_SPIDERS_MAKE_COBWEBS = BUILDER
            .comment("Whether cave spiders produce cobwebs")
            .define("caveSpidersMakeCobwebs", true);
    
    private static final ModConfigSpec.BooleanValue _GUARDIAN_TO_ELDER_GUARDIAN_CONVERSION = BUILDER
            .comment("Whether guardians turn into elder guardians when struck by lightning.")
            .define("guardianToElderGuardianConversion", true);
    
    private static final ModConfigSpec.BooleanValue _SNIFFER_LOOT = BUILDER
            .comment("Whether sniffers sniff loot out of structures")
            .define("snifferLoot", true);
    
    private static final ModConfigSpec.BooleanValue _SNIFFER_ARCHAEOLOGY = BUILDER
            .comment("Wehether sniffers sniff out more suspicious sand/gravel")
            .define("snifferArchaeology", true);
    
    private static final ModConfigSpec.DoubleValue _SNIFFER_ARCHAEOLOGY_SPEED = BUILDER
            .comment("How fast sniffers sniff out more suspicious sand/gravel")
            .defineInRange("snifferArchaeologySpeed", 0.1, 0, 1);
    
    private static final ModConfigSpec.BooleanValue _DEEPSLATE_GENERATOR = BUILDER
            .comment("Whether (cobbled) deepslate generators exist (Same as (cobble)stone generators but in the deep dark.)")
            .define("deepslateGenerator", true);
    
    private static final ModConfigSpec.BooleanValue _TUFF_GENERATOR = BUILDER
            .comment("Whether tuff generatrs from flowing lava next to blue ice and on top of soul sand.")
            .define("tuffGenerator", true);
    
    private static final ModConfigSpec.BooleanValue _TRADE_SPORE_BLOSSOM = BUILDER
            .comment("Whether the wandering trader sells spore blossoms")
            .define("tradeSporeBlossoms", true);
    
    private static final ModConfigSpec.BooleanValue _ELDER_GUARDIAN_DROPS_HOTS = BUILDER
            .comment("Whether elder guardians drop heart of the sea when killed by magic")
            .define("elderGuardianDropsHOTS", true);
    
    static final ModConfigSpec SPEC = BUILDER.build();
    
    public static boolean ENDERMAN_SPAWN_WITH_BLOCK;
    public static double ENDERMAN_SPAWN_WITH_BLOCK_CHANCE;
    public static double ENDERMAN_SPAWN_WITH_BLOCK_CHANCE_ENDERMAN_HEAVY_BIOMES;
    public static boolean ENDER_DRAGON_DROPS_HEAD;
    public static boolean BONE_MEAL_SAND;
    public static boolean CAVE_SPIDERS_MAKE_COBWEBS;
    public static boolean GUARDIAN_TO_ELDER_GUARDIAN_CONVERSION;
    public static boolean SNIFFER_LOOT;
    public static boolean SNIFFER_ARCHAEOLOGY;
    public static double SNIFFER_ARCHAEOLOGY_SPEED;
    public static boolean DEEPSLATE_GENERATOR;
    public static boolean TUFF_GENERATOR;
    public static boolean TRADE_SPORE_BLOSSOM;
    public static boolean ELDER_GUARDIAN_DROPS_HOTS;
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        ENDERMAN_SPAWN_WITH_BLOCK = _ENDERMAN_SPAWN_WITH_BLOCK.get();
        ENDERMAN_SPAWN_WITH_BLOCK_CHANCE = _ENDERMAN_SPAWN_WITH_BLOCK_CHANCE.get();
        ENDERMAN_SPAWN_WITH_BLOCK_CHANCE_ENDERMAN_HEAVY_BIOMES = _ENDERMAN_SPAWN_WITH_BLOCK_CHANCE_ENDERMAN_HEAVY_BIOMES.get();
        ENDER_DRAGON_DROPS_HEAD = _ENDER_DRAGON_DROPS_HEAD.get();
        BONE_MEAL_SAND = _BONE_MEAL_SAND.get();
        CAVE_SPIDERS_MAKE_COBWEBS = _CAVE_SPIDERS_MAKE_COBWEBS.get();
        GUARDIAN_TO_ELDER_GUARDIAN_CONVERSION = _GUARDIAN_TO_ELDER_GUARDIAN_CONVERSION.get();
        SNIFFER_LOOT = _SNIFFER_LOOT.get();
        SNIFFER_ARCHAEOLOGY = _SNIFFER_ARCHAEOLOGY.get();
        SNIFFER_ARCHAEOLOGY_SPEED = _SNIFFER_ARCHAEOLOGY_SPEED.get();
        DEEPSLATE_GENERATOR = _DEEPSLATE_GENERATOR.get();
        TUFF_GENERATOR = _TUFF_GENERATOR.get();
        TRADE_SPORE_BLOSSOM = _TRADE_SPORE_BLOSSOM.get();
        ELDER_GUARDIAN_DROPS_HOTS = _ELDER_GUARDIAN_DROPS_HOTS.get();
    }
}