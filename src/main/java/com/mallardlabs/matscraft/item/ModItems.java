package com.mallardlabs.matscraft.item;

import com.mallardlabs.matscraft.MatsCraft;
import com.mallardlabs.matscraft.item.custom.ChainsawItem;
import com.mallardlabs.matscraft.sound.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item MATS = registerItem("mats", new Item(new Item.Settings()));
    public static final Item MATT_LUONGO = registerItem("matt_luongo", new Item(new Item.Settings()));
    public static final Item LOWPOLYDUCK = registerItem("lowpolyduck", new Item(new Item.Settings()));

    public static final Item CHAINSAW = registerItem("chainsaw", new ChainsawItem(new Item.Settings().maxDamage(32)));

    public static final Item BAR_BRAWL_MUSIC_DISC = registerItem("bar_brawl_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.BAR_BRAWL_KEY).maxCount(1)));
    public static final Item BRENDAN_MEZONG_MUSIC_DISC = registerItem("brendan_mezong_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.BRENDAN_MEZONG_KEY).maxCount(1)));
    public static final Item NANNDO_MEMEMEMEZO_MUSIC_DISC = registerItem("nanndo_memememezo_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.NANNDO_MEMEMEMEZO_KEY).maxCount(1)));

    private static Item registerItem (String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MatsCraft.MOD_ID, name), item);
    }

    private static void customIngredients(FabricItemGroupEntries entries) {
        entries.add(MATS);
        entries.add(MATT_LUONGO);
    }

    public static void registerModItems () {
        MatsCraft.LOGGER.info("Registering Mod Items for " + MatsCraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::customIngredients);
    }
}
