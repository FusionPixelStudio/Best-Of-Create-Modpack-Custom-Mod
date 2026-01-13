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

    protected void renderSafe(KineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (VisualizationManager.supportsVisualization(be.getLevel())) return;
        BlockState blockState = be.getBlockState();
        BlockPos pos = be.getBlockPos();

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        int packedLightmapCoords = LevelRenderer.getLightColor(be.getLevel(), pos);
        SuperByteBuffer shaft =  CachedBuffers.partial(AllPartialModels.RECYCLER_GRINDER, blockState);
        Direction.Axis axis = getRotationAxisOf(be);

        shaft
                .rotateCentered(axis == Direction.Axis.Z ? 0 : 90*(float)Math.PI/180f, Direction.UP)
                .translate(0, 4f/16f, 0)
                .rotateCentered(getAngleForBe(be, pos, axis), Direction.NORTH)
                .light(packedLightmapCoords)
                .renderInto(ms, vb);
    }

    public RecyclingBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected SuperByteBuffer getRotatedModel(RecyclingBlockEntity be, BlockState state) {
        return CachedBuffers.partial(AllPartialModels.RECYCLER_GRINDER, state);
    }
}
