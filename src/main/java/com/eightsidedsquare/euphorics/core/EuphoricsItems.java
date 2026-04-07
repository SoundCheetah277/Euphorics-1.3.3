package com.eightsidedsquare.euphorics.core;

import java.util.*;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.eightsidedsquare.euphorics.common.item.*;
import net.minecraft.*;

public class EuphoricsItems
{
    private static final Map<Item, Identifier> ITEMS;
    public static final Item RAW_ENDUST;
    public static final Item CRYSTALLIZED_ENDUST;
    public static final Item SHADE_SPAWN_EGG;

    public static void init() {
        EuphoricsItems.ITEMS.keySet().forEach(item -> Registry.register((Registry)Registry.ITEM, (Identifier)EuphoricsItems.ITEMS.get(item), (Object)item));
    }

    private static <T extends Item> T create(final String name, final T item) {
        EuphoricsItems.ITEMS.put(item, EuphoricsMod.id(name));
        return item;
    }

    static {
        ITEMS = new LinkedHashMap<Item, Identifier>();
        RAW_ENDUST = create("raw_endust", (Item)new RawEndustItem(EuphoricsBlocks.RAW_ENDUST_LAYER, new Item.Settings().group(ItemGroup.MISC)));
        CRYSTALLIZED_ENDUST = create("crystallized_endust", new CrystallizedEndustItem(new Item.Settings().group(ItemGroup.MISC)));
        SHADE_SPAWN_EGG = create("shade_spawn_egg", (Item)new SpawnEggItem((EntityType)EuphoricsEntities.SHADE, 3221342, 7688859, new Item.Settings().group(ItemGroup.MISC)));
    }
}
