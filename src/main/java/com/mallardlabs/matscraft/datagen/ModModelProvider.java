package com.mallardlabs.matscraft.datagen;

import com.mallardlabs.matscraft.block.ModBlocks;
import com.mallardlabs.matscraft.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.COMMON_MATS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.UNCOMMON_MATS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RARE_MATS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.EPIC_MATS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LEGENDARY_MATS_ORE);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MAGIC_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.MATS, Models.GENERATED);
        itemModelGenerator.register(ModItems.MATT_LUONGO, Models.GENERATED);
        itemModelGenerator.register(ModItems.LOWPOLYDUCK, Models.GENERATED);

        itemModelGenerator.register(ModItems.CHAINSAW, Models.GENERATED);

        itemModelGenerator.register(ModItems.BAR_BRAWL_MUSIC_DISC, Models.GENERATED);
        itemModelGenerator.register(ModItems.BRENDAN_MEZONG_MUSIC_DISC, Models.GENERATED);
        itemModelGenerator.register(ModItems.NANNDO_MEMEMEMEZO_MUSIC_DISC, Models.GENERATED);
    }
}
