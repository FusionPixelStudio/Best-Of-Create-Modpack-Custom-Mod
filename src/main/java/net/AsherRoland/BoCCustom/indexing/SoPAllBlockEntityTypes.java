package net.AsherRoland.BoCCustom.indexing;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlockEntity;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlockRenderer;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlockVisual;

public class SoPAllBlockEntityTypes {

    private static final CreateRegistrate REGISTRATE = SoPAllBlocks.REGISTRATE;

    public static final BlockEntityEntry<RecyclingBlockEntity> RECYCLING_BLOCK = REGISTRATE
                    .blockEntity("recycling_block", RecyclingBlockEntity::new)
                    .visual(() -> RecyclingBlockVisual::new)
                    .validBlocks(SoPAllBlocks.RECYCLING_BLOCK)
                    .renderer(() -> RecyclingBlockRenderer::new)
                    .register();

    public static void register() {}

}
