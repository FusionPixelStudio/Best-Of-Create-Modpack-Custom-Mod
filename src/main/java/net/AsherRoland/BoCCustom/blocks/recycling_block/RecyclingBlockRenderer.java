package net.AsherRoland.BoCCustom.blocks.recycling_block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.AsherRoland.BoCCustom.rendering.AllPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class RecyclingBlockRenderer extends KineticBlockEntityRenderer {

    public static final Logger LOGGER = LogUtils.getLogger();
    static {
        LOGGER.info("RecyclingBlockRenderer class loaded");
    }

    protected BlockState getRenderedBlockState(RecyclingBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }

    public RecyclingBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected SuperByteBuffer getRotatedModel(RecyclingBlockEntity be, BlockState state) {
        return CachedBuffers.partial(AllPartialModels.RECYCLER_GRINDER, state);
    }
}
