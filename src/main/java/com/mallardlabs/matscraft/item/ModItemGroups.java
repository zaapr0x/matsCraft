package com.mallardlabs.matscraft.item;

import com.mallardlabs.matscraft.MatsCraft;
import com.mallardlabs.matscraft.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup MATSFI_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(MatsCraft.MOD_ID, "matsfi_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.MATS))
                    .displayName(Text.translatable("itemgroup.matscraft.matsfi_items"))
                    .entries((displayContext, entries) -> {
                        entries.add((ModItems.MATS));
                        entries.add((ModItems.MATT_LUONGO));
                        entries.add((ModItems.LOWPOLYDUCK));

                        entries.add(ModItems.CHAINSAW);

                        entries.add(ModItems.BAR_BRAWL_MUSIC_DISC);
                        entries.add(ModItems.BRENDAN_MEZONG_MUSIC_DISC);
                        entries.add(ModItems.NANNDO_MEMEMEMEZO_MUSIC_DISC);

                    }).build());

    public static final ItemGroup MATS_ORE_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(MatsCraft.MOD_ID, "mats_ore_blocks"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.COMMON_MATS_ORE))
                    .displayName(Text.translatable("itemgroup.matscraft.mats_ore_blocks"))
                    .entries((displayContext, entries) -> {
                        entries.add((ModBlocks.COMMON_MATS_ORE));
                        entries.add((ModBlocks.UNCOMMON_MATS_ORE));
                        entries.add((ModBlocks.RARE_MATS_ORE));
                        entries.add((ModBlocks.EPIC_MATS_ORE));
                        entries.add((ModBlocks.LEGENDARY_MATS_ORE));

                        entries.add((ModBlocks.MAGIC_BLOCK));

                    }).build());

    public static void registerItemGroups() {
        MatsCraft.LOGGER.info("Registering Item Groups for " + MatsCraft.MOD_ID);
    }
}
