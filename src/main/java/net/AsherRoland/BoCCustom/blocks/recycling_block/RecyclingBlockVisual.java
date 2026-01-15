package net.AsherRoland.BoCCustom.blocks.recycling_block;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.AsherRoland.BoCCustom.rendering.AllPartialModels;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


public class RecyclingBlockVisual extends KineticBlockEntityVisual<RecyclingBlockEntity> {

    private final RotatingInstance grinder;

    public RecyclingBlockVisual(VisualizationContext context, RecyclingBlockEntity be, float partialTick) {
        super(context, be, partialTick);

        this.grinder = instancerProvider()
                .instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.RECYCLER_GRINDER))
                .createInstance();

        grinder.setup(be)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.NORTH, blockState.getValue(RecyclingBlock.HORIZONTAL_FACING).getClockWise().getAxis())
                .setRotationAxis(blockState.getValue(RecyclingBlock.HORIZONTAL_FACING).getClockWise().getAxis())
                .setChanged();

    }

    @Override
    public void update(float v) {
        grinder.setup(blockEntity, blockState.getValue(RecyclingBlock.HORIZONTAL_FACING).getClockWise().getAxis(), blockEntity.getSpeed())
                .setChanged();
    }

    @Override
    protected void _delete() {
        grinder.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(grinder);
    }

    @Override
    public void updateLight(float partialTick) {
        this.relight(this.pos, this.grinder);
    }
}
