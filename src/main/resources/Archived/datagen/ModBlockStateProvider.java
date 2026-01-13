package net.AsherRoland.BoCCustom.Archived.datagen;

import net.AsherRoland.BoCCustom.BocCustom;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BocCustom.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
//        Put Standard Blocks from our mod here
//        blockWithItem(ModBlocks.SAPPHIRE_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockingRegistryObject) {
        simpleBlockWithItem(blockingRegistryObject.get(), cubeAll(blockingRegistryObject.get()));
    }
}
