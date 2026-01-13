package net.AsherRoland.BoCCustom.indexing;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.AsherRoland.BoCCustom.BocCustom;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlock;
import org.slf4j.Logger;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class SoPAllBlocks {

    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        LOGGER.info("SoPAllBlocks class loaded");
    }

    public static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(BocCustom.MODID);

    public static final BlockEntry<RecyclingBlock> RECYCLING_BLOCK =
            REGISTRATE.block("recycling_block", RecyclingBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(p -> p.noOcclusion())
                    .item()
                    .onRegister(block -> LOGGER.info("Recycling block registered"))
                    .tab(SoPCreativeModeTabs.BEST_OF_CREATE.getKey())
                    .transform(customItemModel())
                    .register();


    public static void register() {}


}
