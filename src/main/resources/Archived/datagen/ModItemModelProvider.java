package net.AsherRoland.BoCCustom.Archived.datagen;

import net.AsherRoland.BoCCustom.BocCustom;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BocCustom.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
//        Add Items Here

//        simpleItem(ModItems.SAPPHIRE);
//        simpleItem(ModItems.RAW_SAPPHIRE);
//
//        simpleItem(ModItems.METAL_DETECTOR);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BocCustom.MODID,"item/" + item.getId().getPath()));
    }
}

