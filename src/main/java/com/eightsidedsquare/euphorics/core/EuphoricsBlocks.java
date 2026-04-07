package com.eightsidedsquare.euphorics.core;

import java.util.*;
import net.minecraft.*;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.eightsidedsquare.euphorics.common.block.*;

public class EuphoricsBlocks
{
    private static final Map<Block, Identifier> BLOCKS;
    private static final Map<Item, Identifier> ITEMS;
    public static final Block WITHERED_SPORE_BLOSSOM;
    public static final Block RAW_ENDUST_LAYER;
    public static final Block SOUL_FIRED_ENDUST;
    public static final Block UNCRYSTALLIZED_ENDUST_BLOCK;
    public static final Block CRYSTALLIZED_ENDUST_BLOCK;
    public static final Block RAW_ENDUST_CAULDRON;
    public static final Block SOUL_FIRED_ENDUST_CAULDRON;
    public static final Block SCRAPED_END_PORTAL_FRAME;

    private static <T extends Block> T create(final String name, final T block, final ItemGroup itemGroup) {
        EuphoricsBlocks.BLOCKS.put(block, EuphoricsMod.id(name));
        if (itemGroup != null) {
            EuphoricsBlocks.ITEMS.put((Item)new BlockItem((Block)block, new Item.Settings().group(itemGroup)), EuphoricsBlocks.BLOCKS.get(block));
        }
        return block;
    }

    public static void init() {
        EuphoricsBlocks.BLOCKS.keySet().forEach(block -> Registry.register((Registry)Registry.BLOCK, (Identifier)EuphoricsBlocks.BLOCKS.get(block), (Object)block));
        EuphoricsBlocks.ITEMS.keySet().forEach(item -> Registry.register((Registry)Registry.ITEM, (Identifier)EuphoricsBlocks.ITEMS.get(item), (Object)item));
    }

    static {
        BLOCKS = new LinkedHashMap<Block, Identifier>();
        ITEMS = new LinkedHashMap<Item, Identifier>();
        WITHERED_SPORE_BLOSSOM = create("withered_spore_blossom", (Block)new WitheredSporeBlossomBlock(AbstractBlock.Settings.copy((AbstractBlock)Blocks.SPORE_BLOSSOM).ticksRandomly()), ItemGroup.DECORATIONS);
        RAW_ENDUST_LAYER = create("raw_endust_layer", (Block)new EndustLayerBlock(AbstractBlock.Settings.of(Material.AGGREGATE, MapColor.LICHEN_GREEN).sounds(BlockSoundGroup.SAND).strength(1.0f)), null);
        SOUL_FIRED_ENDUST = create("soul_fired_endust", new Block(AbstractBlock.Settings.of(Material.AGGREGATE, MapColor.CYAN).strength(1.0f).sounds(BlockSoundGroup.SOUL_SOIL)), ItemGroup.BUILDING_BLOCKS);
        UNCRYSTALLIZED_ENDUST_BLOCK = create("uncrystallized_endust_block", new UncrystallizedEndustBlock(AbstractBlock.Settings.of(Material.SCULK, MapColor.DARK_AQUA).strength(1.0f).sounds(BlockSoundGroup.SCULK)), ItemGroup.BUILDING_BLOCKS);
        CRYSTALLIZED_ENDUST_BLOCK = create("crystallized_endust_block", new Block(AbstractBlock.Settings.of(Material.AMETHYST, MapColor.PURPLE).strength(1.0f).sounds(BlockSoundGroup.AMETHYST_BLOCK)), ItemGroup.BUILDING_BLOCKS);
        RAW_ENDUST_CAULDRON = create("raw_endust_cauldron", (Block)new RawEndustCauldronBlock(AbstractBlock.Settings.copy((AbstractBlock) Blocks.CAULDRON).ticksRandomly()), null);
        SOUL_FIRED_ENDUST_CAULDRON = create("soul_fired_endust_cauldron", (Block)new SoulFiredEndustCauldronBlock(AbstractBlock.Settings.copy((AbstractBlock)Blocks.CAULDRON)), null);
        SCRAPED_END_PORTAL_FRAME = create("scraped_end_portal_frame", (Block)new ScrapedEndPortalFrameBlock(AbstractBlock.Settings.copy((AbstractBlock)Blocks.END_PORTAL_FRAME).ticksRandomly()), null);
    }
}
