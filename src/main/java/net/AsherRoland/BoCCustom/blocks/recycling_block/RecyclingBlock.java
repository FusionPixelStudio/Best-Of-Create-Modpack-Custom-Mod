package net.AsherRoland.BoCCustom.blocks.recycling_block;


import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.AsherRoland.BoCCustom.BocLang;
import net.AsherRoland.BoCCustom.indexing.SoPAllBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction.Axis;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

import static com.simibubi.create.api.stress.BlockStressValues.getCapacity;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_AXIS;

public class RecyclingBlock extends HorizontalKineticBlock implements IBE<RecyclingBlockEntity> {

    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        LOGGER.info("RecyclingBlock class loaded");
    }

//    public static long getCapacity(int tier) {
//        return (long) (Math.pow(10, tier) * 1000);
//    }

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

    public static final String[] postfixes = new String[] {
            "",
            "K",
            "M",
            "B",
            "T",
            "Q"
    };

    public static String formatLong(long l) {
        int d = 0;
        double number = l;
        while (number >= 1000 && d < postfixes.length) {
            number /= 1000;
            d++;
        }

        if (l >= 1000 && Math.floor(number) != number)
            return String.format("%.1f%s", number, postfixes[d]);

        return (int)number + postfixes[d];
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(BocLang.translate("tooltip.boc_custom.speed").style(ChatFormatting.GRAY).component());
        pTooltip.add(BocLang.text(" ").translate("tooltip.boc_custom.energy_per_tick",
                        formatLong(128)).style(ChatFormatting.AQUA)
                .add(BocLang.text(" ").translate("tooltip.boc_custom.per_rpm", 10).style(ChatFormatting.GRAY)).component());
        pTooltip.add(BocLang.translate("tooltip.boc_custom.stores").style(ChatFormatting.GRAY).component());
        pTooltip.add(BocLang.text(" ").translate("tooltip.boc_custom.energy",
                formatLong(20000)).style(ChatFormatting.AQUA).component());
    }
}
