package com.mallardlabs.matscraft.util;

import com.mallardlabs.matscraft.MatsCraft;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(MatsCraft.MOD_ID, name));
        }

    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS =
                createTag("transformable_items");

        public static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(MatsCraft.MOD_ID, name));
        }
    }
}
