package net.AsherRoland.BoCCustom.blocks.recycling_block;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class RecyclingBlockRenderer extends KineticBlockEntityRenderer<RecyclingBlockEntity> {

    public RecyclingBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(RecyclingBlockEntity be, BlockState state) {
        return super.getRotatedModel(be, state);
    }
}
