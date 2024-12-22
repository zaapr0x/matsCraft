package com.mallardlabs.matscraft.world;

import com.mallardlabs.matscraft.MatsCraft;
import com.mallardlabs.matscraft.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> COMMON_MATS_ORE_KEY = registerKey("common_mats_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> UNCOMMON_MATS_ORE_KEY = registerKey("uncommon_mats_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> RARE_MATS_ORE_KEY = registerKey("rare_mats_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> EPIC_MATS_ORE_KEY = registerKey("epic_mats_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> LEGENDARY_MATS_ORE_KEY = registerKey("legendary_mats_ore");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

        RuleTest stoneReplaceables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> overworldCommonMatsOre =
                List.of(OreFeatureConfig.createTarget(stoneReplaceables, ModBlocks.COMMON_MATS_ORE.getDefaultState()));

        List<OreFeatureConfig.Target> overworldUncommonMatsOre =
                List.of(OreFeatureConfig.createTarget(stoneReplaceables, ModBlocks.UNCOMMON_MATS_ORE.getDefaultState()));

        List<OreFeatureConfig.Target> overworldRareMatsOre =
                List.of(OreFeatureConfig.createTarget(stoneReplaceables, ModBlocks.RARE_MATS_ORE.getDefaultState()));

        List<OreFeatureConfig.Target> overworldEpicMatsOre =
                List.of(OreFeatureConfig.createTarget(stoneReplaceables, ModBlocks.EPIC_MATS_ORE.getDefaultState()));

        List<OreFeatureConfig.Target> overworldLegendaryMatsOre =
                List.of(OreFeatureConfig.createTarget(stoneReplaceables, ModBlocks.LEGENDARY_MATS_ORE.getDefaultState()));

        register(context, COMMON_MATS_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldCommonMatsOre, 12));
        register(context, UNCOMMON_MATS_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldUncommonMatsOre, 12));
        register(context, RARE_MATS_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldRareMatsOre, 12));
        register(context, EPIC_MATS_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldEpicMatsOre, 12));
        register(context, LEGENDARY_MATS_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldLegendaryMatsOre, 12));


    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MatsCraft.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}