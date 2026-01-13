package net.AsherRoland.BoCCustom.indexing;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.AsherRoland.BoCCustom.BocCustom;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlock;

public class SoPAllBlocks {

    public static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(BocCustom.MODID);

    public static final BlockEntry<RecyclingBlock> RECYCLING_BLOCK =
            REGISTRATE.block("recycling_block", RecyclingBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.strength(3.5F))
                    .item()
                    .build()
                    .register();

    public static void register() {}


}
