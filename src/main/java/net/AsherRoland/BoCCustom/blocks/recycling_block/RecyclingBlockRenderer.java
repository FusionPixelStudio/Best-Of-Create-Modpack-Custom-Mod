package net.AsherRoland.BoCCustom.blocks.recycling_block;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.AsherRoland.BoCCustom.rendering.AllPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class RecyclingBlockRenderer extends KineticBlockEntityRenderer<RecyclingBlockEntity> {

    public static final Logger LOGGER = LogUtils.getLogger();
    static {
        LOGGER.info("RecyclingBlockRenderer class loaded");
    }

    public RecyclingBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(RecyclingBlockEntity be, BlockState state) {
        return CachedBuffers.partial(AllPartialModels.RECYCLER_GRINDER, state);
    }
}
