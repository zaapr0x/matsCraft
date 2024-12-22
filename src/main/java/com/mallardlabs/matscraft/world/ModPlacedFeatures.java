package com.mallardlabs.matscraft.world;

import com.mallardlabs.matscraft.MatsCraft;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> COMMON_MATS_ORE_PLACED_KEY = registerKey("common_mats_ore_placed");
    public static final RegistryKey<PlacedFeature> UNCOMMON_MATS_ORE_PLACED_KEY = registerKey("uncommon_mats_ore_placed");
    public static final RegistryKey<PlacedFeature> RARE_MATS_ORE_PLACED_KEY = registerKey("rare_mats_ore_placed");
    public static final RegistryKey<PlacedFeature> EPIC_MATS_ORE_PLACED_KEY = registerKey("epic_mats_ore_placed");
    public static final RegistryKey<PlacedFeature> LEGENDARY_MATS_ORE_PLACED_KEY = registerKey("legendary_mats_ore_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, COMMON_MATS_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.COMMON_MATS_ORE_KEY),
                ModOrePlacement.modifiersWithCount(30, HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, UNCOMMON_MATS_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.UNCOMMON_MATS_ORE_KEY),
                ModOrePlacement.modifiersWithCount(30, HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, RARE_MATS_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.RARE_MATS_ORE_KEY),
                ModOrePlacement.modifiersWithCount(30, HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, EPIC_MATS_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.EPIC_MATS_ORE_KEY),
                ModOrePlacement.modifiersWithCount(30, HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, LEGENDARY_MATS_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.LEGENDARY_MATS_ORE_KEY),
                ModOrePlacement.modifiersWithCount(30, HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));

    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MatsCraft.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                                                                   RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                                                                   PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}