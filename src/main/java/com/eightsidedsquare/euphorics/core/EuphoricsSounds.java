package com.eightsidedsquare.euphorics.core;

import net.minecraft.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class EuphoricsSounds
{
    private static final List<SoundEvent> SOUNDS;
    public static final SoundEvent ITEM_CRYSTALLIZED_ENDUST_SNORT;
    public static final SoundEvent ITEM_PICKAXE_SCRAPE;
    public static final SoundEvent ENTITY_SHADE_GLITCH;
    public static final SoundEvent ENTITY_SHADE_DEATH;
    public static final SoundEvent ENTITY_SHADE_HURT;
    public static final SoundEvent ENTITY_SHADE_ATTACK;
    public static final SoundEvent ENTITY_SHADE_TELEPORT;
    public static final SoundEvent EVENT_BLINK;
    public static final SoundEvent BLOCK_SCRAPED_END_PORTAL_FRAME_REGENERATE;

    public static void init() {
        EuphoricsSounds.SOUNDS.forEach(sound -> Registry.register(Registry.SOUND_EVENT, sound.getId(), sound));
    }

    private static SoundEvent create(final String id) {
        final SoundEvent soundEvent = new SoundEvent(EuphoricsMod.id(id));
        EuphoricsSounds.SOUNDS.add(soundEvent);
        return soundEvent;
    }

    static {
        SOUNDS = new ArrayList<SoundEvent>();
        ITEM_CRYSTALLIZED_ENDUST_SNORT = create("item.crystallized_endust.snort");
        ITEM_PICKAXE_SCRAPE = create("item.pickaxe.scrape");
        ENTITY_SHADE_GLITCH = create("entity.shade.glitch");
        ENTITY_SHADE_DEATH = create("entity.shade.death");
        ENTITY_SHADE_HURT = create("entity.shade.hurt");
        ENTITY_SHADE_ATTACK = create("entity.shade.attack");
        ENTITY_SHADE_TELEPORT = create("entity.shade.teleport");
        EVENT_BLINK = create("event.blink");
        BLOCK_SCRAPED_END_PORTAL_FRAME_REGENERATE = create("block.scraped_end_portal_frame.regenerate");
    }
}
