package net.AsherRoland.BoCCustom.indexing;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.AsherRoland.BoCCustom.BocCustom;
import net.AsherRoland.BoCCustom.block.custom.recycling_block.RecyclingBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {
//    public static final DeferredRegister<Block> BLOCKS =
//            DeferredRegister.create(ForgeRegistries.BLOCKS, BocCustom.MODID);

//    Register blocks here
//    public static final RegistryObject<Block> SAPPHIRE_BLOCK = registerBlock("sapphire_block",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(BocCustom.MODID);

    public static final BlockEntry<RecyclingBlock> RECYCLING_BLOCK = BocCustom.REGISTRATE.block("recycling_block", RecyclingBlock::new)
            .initialProperties(SharedProperties::stone)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .transform(customItemModel())
            .register();


//    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
//        RegistryObject<T> toReturn = BLOCKS.register(name, block);
//        registerBlockItem(name, toReturn);
//        return toReturn;
//    }
//
//    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
//        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
//    }

    public static void register() {

    }

}
