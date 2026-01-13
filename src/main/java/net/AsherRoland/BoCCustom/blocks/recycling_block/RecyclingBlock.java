package net.AsherRoland.BoCCustom.blocks.recycling_block;


import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.AsherRoland.BoCCustom.indexing.SoPAllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction.Axis;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_AXIS;

public class RecyclingBlock extends HorizontalKineticBlock implements IBE<RecyclingBlockEntity> {

    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        LOGGER.info("RecyclingBlock class loaded");
    }

    public RecyclingBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState());
    }

//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        Direction prefferedSide = getPreferredHorizontalFacing(context);
//        if (prefferedSide != null)
//            return defaultBlockState().setValue(HORIZONTAL_FACING, prefferedSide);
//        return super.getStateForPlacement(context);
//    }

    public Axis getRotationAxis(BlockState state) {
        return state.getValue(HorizontalDirectionalBlock.FACING).getAxis();
    }


    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face ) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        return face == facing.getClockWise() || face == facing.getCounterClockWise();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }


    @Override
    public Class<RecyclingBlockEntity> getBlockEntityClass() {
        return RecyclingBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RecyclingBlockEntity> getBlockEntityType() {
        return SoPAllBlockEntityTypes.RECYCLING_BLOCK.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return SoPAllBlockEntityTypes.RECYCLING_BLOCK.create(pos, state);
    }
}
