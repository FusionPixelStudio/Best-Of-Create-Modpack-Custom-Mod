package net.AsherRoland.BoCCustom.Archived.datagen.loot;

//import net.AsherRoland.BoCCustom.indexing.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
//        Add Block LootTables

//        this.dropSelf(ModBlocks.SAPPHIRE_BLOCK.get());
//        this.dropSelf(ModBlocks.RAW_SAPPHIRE_BLOCK.get());
//        this.dropSelf(ModBlocks.SOUND_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
