package com.mallardlabs.matscraft.block;

import com.mallardlabs.matscraft.MatsCraft;
import com.mallardlabs.matscraft.block.custom.MagicBlock;
import com.mallardlabs.matscraft.sound.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block COMMON_MATS_ORE = registerBlock("common_mats_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2,5),
                    AbstractBlock.Settings.create().strength(2f).requiresTool().sounds(BlockSoundGroup.METAL)));

    public static final Block UNCOMMON_MATS_ORE = registerBlock("uncommon_mats_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3,6),
                    AbstractBlock.Settings.create().strength(2f).requiresTool().sounds(BlockSoundGroup.METAL)));

    public static final Block RARE_MATS_ORE = registerBlock("rare_mats_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(4,7),
                    AbstractBlock.Settings.create().strength(3f).requiresTool().sounds(BlockSoundGroup.METAL)));

    public static final Block EPIC_MATS_ORE = registerBlock("epic_mats_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(5,8),
                    AbstractBlock.Settings.create().strength(3f).requiresTool().sounds(BlockSoundGroup.METAL)));

    public static final Block LEGENDARY_MATS_ORE = registerBlock("legendary_mats_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(7,10),
                    AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.METAL)));

    public static final Block MAGIC_BLOCK = registerBlock("magic_block",
            new MagicBlock(AbstractBlock.Settings.create().strength(1f).requiresTool().sounds(ModSounds.MAGIC_BLOCK_SOUNDS)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(MatsCraft.MOD_ID, name), block);
    }

    public static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(MatsCraft.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        MatsCraft.LOGGER.info("Registering Mod Blocks for " + MatsCraft.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.COMMON_MATS_ORE);
            entries.add(ModBlocks.UNCOMMON_MATS_ORE);
            entries.add(ModBlocks.RARE_MATS_ORE);
            entries.add(ModBlocks.EPIC_MATS_ORE);
            entries.add(ModBlocks.LEGENDARY_MATS_ORE);
        });
    }
}
