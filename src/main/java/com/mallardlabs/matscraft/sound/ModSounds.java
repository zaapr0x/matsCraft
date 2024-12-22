package com.mallardlabs.matscraft.sound;

import com.mallardlabs.matscraft.MatsCraft;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent CHAINSAW_CUT = registerSoundEvent("chainsaw_cut");
    public static final SoundEvent CHAINSAW_PULL = registerSoundEvent("chainsaw_pull");

    public static final SoundEvent MAGIC_BLOCK_BREAK = registerSoundEvent("magic_block_break");
    public static final SoundEvent MAGIC_BLOCK_STEP = registerSoundEvent("magic_block_step");
    public static final SoundEvent MAGIC_BLOCK_PLACE = registerSoundEvent("magic_block_place");
    public static final SoundEvent MAGIC_BLOCK_HIT = registerSoundEvent("magic_block_hit");
    public static final SoundEvent MAGIC_BLOCK_FALL = registerSoundEvent("magic_block_fall");

    public static final BlockSoundGroup MAGIC_BLOCK_SOUNDS = new BlockSoundGroup(1f, 1f,
            MAGIC_BLOCK_BREAK, MAGIC_BLOCK_STEP, MAGIC_BLOCK_PLACE, MAGIC_BLOCK_HIT, MAGIC_BLOCK_FALL);

    public static final SoundEvent BAR_BRAWL = registerSoundEvent("bar_brawl");
    public static final RegistryKey<JukeboxSong> BAR_BRAWL_KEY = of("bar_brawl");
    public static final SoundEvent BRENDAN_MEZONG = registerSoundEvent("brendan_mezong");
    public static final RegistryKey<JukeboxSong> BRENDAN_MEZONG_KEY = of("brendan_mezong");
    public static final SoundEvent NANNDO_MEMEMEMEZO = registerSoundEvent("nanndo_memememezo");
    public static final RegistryKey<JukeboxSong> NANNDO_MEMEMEMEZO_KEY = of("nanndo_memememezo");


    private static RegistryKey<JukeboxSong> of(String name) {
        return  RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MatsCraft.MOD_ID, name));
    }

    private static SoundEvent registerSoundEvent(String name) {
        return Registry.register(Registries.SOUND_EVENT, Identifier.of(MatsCraft.MOD_ID, name),
                SoundEvent.of(Identifier.of(MatsCraft.MOD_ID, name)));
    }

    public static void registerSounds() {
        MatsCraft.LOGGER.info("Registering Mod Sounds for " + MatsCraft.MOD_ID);
    }
}
