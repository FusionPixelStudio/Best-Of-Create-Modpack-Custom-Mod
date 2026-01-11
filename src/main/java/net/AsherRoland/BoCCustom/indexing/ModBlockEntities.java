package net.AsherRoland.BoCCustom.indexing;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.AsherRoland.BoCCustom.BocCustom;
import net.AsherRoland.BoCCustom.block.custom.recycling_block.RecyclingBlockEntity;
import net.AsherRoland.BoCCustom.block.custom.recycling_block.RecyclingBlockRenderer;
import net.AsherRoland.BoCCustom.block.custom.recycling_block.RecyclingBlockVisual;

public class ModBlockEntities {
    public static final BlockEntityEntry<RecyclingBlockEntity> RECYCLING_BLOCK = BocCustom.REGISTRATE
            .blockEntity("recycling_block", RecyclingBlockEntity::new)
            .visual(() -> RecyclingBlockVisual::new)
            .validBlocks(BocCustom.RECYCLING_BLOCK)
            .renderer(() -> RecyclingBlockRenderer::new)
            .register();
}
