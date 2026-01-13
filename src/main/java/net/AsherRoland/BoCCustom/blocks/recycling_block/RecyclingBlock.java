package net.AsherRoland.BoCCustom.blocks.recycling_block;


import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.AsherRoland.BoCCustom.indexing.SoPAllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction.Axis;

public class RecyclingBlock extends HorizontalKineticBlock implements IBE<RecyclingBlockEntity> {

//    public static final DirectionProperty RECYCLING_FACING =
//            HorizontalDirectionalBlock.FACING;

    public RecyclingBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    /* ---------------- KINETICS ---------------- */

    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.X;
    }

    // Shaft connections ONLY on left and right sides
    @Override
    public boolean hasShaftTowards(
            LevelReader world,
            BlockPos pos,
            BlockState state,
            Direction face
    ) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING
        );
        return face == facing.getClockWise()
                || face == facing.getCounterClockWise();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }

    /* ---------------- BLOCK ENTITY ---------------- */

    @Override
    public Class<RecyclingBlockEntity> getBlockEntityClass() {
        return RecyclingBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RecyclingBlockEntity> getBlockEntityType() {
        return SoPAllBlockEntityTypes.RECYCLING_BLOCK.get();
    }

    /* ---------------- STATE ---------------- */

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        super.createBlockStateDefinition(builder);
//        builder.add(RECYCLING_FACING);
    }
}
