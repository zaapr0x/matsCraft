package com.mallardlabs.matscraft.datagen;

import com.mallardlabs.matscraft.block.ModBlocks;
import com.mallardlabs.matscraft.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    public ModLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.MAGIC_BLOCK);

        addDrop(ModBlocks.COMMON_MATS_ORE, multipleOreDrops(ModBlocks.COMMON_MATS_ORE, ModItems.MATS, 1, 2));
        addDrop(ModBlocks.UNCOMMON_MATS_ORE, multipleOreDrops(ModBlocks.UNCOMMON_MATS_ORE, ModItems.MATS, 1, 3));
        addDrop(ModBlocks.RARE_MATS_ORE, multipleOreDrops(ModBlocks.RARE_MATS_ORE, ModItems.MATS, 2, 5));
        addDrop(ModBlocks.EPIC_MATS_ORE, multipleOreDrops(ModBlocks.EPIC_MATS_ORE, ModItems.MATS, 5, 10));
        addDrop(ModBlocks.LEGENDARY_MATS_ORE, multipleOreDrops(ModBlocks.LEGENDARY_MATS_ORE, ModItems.MATS, 10, 25));
    }

    public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrops, float maxDrops) {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(drop, (LootPoolEntry.Builder<?>)this.applyExplosionDecay(drop,
                        ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops)))
                                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }
}
