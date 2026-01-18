package net.AsherRoland.BoCCustom.indexing;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.AsherRoland.BoCCustom.BocCustom;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlock;
import net.minecraft.tags.BlockTags;
import org.slf4j.Logger;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class SoPRegistry {

    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        LOGGER.info("SoPAllBlocks class loaded");
    }

    public static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(BocCustom.MODID);

    static {
        REGISTRATE.setCreativeTab(SoPCreativeModeTabs.BEST_OF_CREATE);
    }

    public static final BlockEntry<RecyclingBlock> RECYCLING_BLOCK =
            REGISTRATE.block("recycling_block", RecyclingBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(p -> p.noOcclusion())
                    .item()
                    .onRegister(block -> LOGGER.info("Recycling block registered"))
                    .transform(customItemModel())
                    .tag(BlockTags.MINEABLE_WITH_AXE)
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .register();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_WIRE =
            REGISTRATE.item("incomplete_wire", SequencedAssemblyItem::new)
                    .onRegister(block -> LOGGER.info("Incomplete wire registered"))
                    .register();


    public static void register() {}


}
