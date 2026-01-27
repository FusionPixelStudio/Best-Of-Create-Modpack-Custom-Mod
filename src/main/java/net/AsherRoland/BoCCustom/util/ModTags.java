package net.AsherRoland.BoCCustom.util;

import net.AsherRoland.BoCCustom.BocCustom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(BocCustom.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> HELM_BLOCKS = createTag("vs_base_helms");
        public static final TagKey<Item> SAIL_BLOCKS = createTag("vs_sail_colors");
        public static final TagKey<Item> BUOY_BLOCKS = createTag("vs_buoy_colors");


        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(BocCustom.MODID, name));
        }
    }
}
