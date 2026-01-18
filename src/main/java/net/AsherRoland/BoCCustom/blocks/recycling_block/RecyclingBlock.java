package net.AsherRoland.BoCCustom.blocks.recycling_block;


import com.mojang.logging.LogUtils;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.AsherRoland.BoCCustom.BocLang;
import net.AsherRoland.BoCCustom.indexing.SoPAllBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction.Axis;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class RecyclingBlock extends HorizontalKineticBlock implements IBE<RecyclingBlockEntity>, IWrenchable {

    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        LOGGER.info("RecyclingBlock class loaded");
    }

    public RecyclingBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState());
    }

    public Axis getRotationAxis(BlockState state) {
        return state.getValue(HorizontalDirectionalBlock.FACING).getClockWise().getAxis();
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
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();

        // Only run on server
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockPos pos = context.getClickedPos();

        // Remove the block
        level.destroyBlock(pos, false);

        // Give the player the block item
        ItemStack stack = new ItemStack(this);

        // If inventory is full, drop it in the world
        if (!player.getInventory().add(stack)) {
            Block.popResource(level, context.getClickedPos(), stack);
        }

        return InteractionResult.SUCCESS;
    }

//    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(BocLang.translate("tooltip.boc_custom.speed").style(ChatFormatting.GRAY).component());
        pTooltip.add(BocLang.text(" ").translate("tooltip.boc_custom.energy_per_tick",
                        formatLong(256)).style(ChatFormatting.AQUA)
                .add(BocLang.text(" ").translate("tooltip.boc_custom.per_rpm", 128).style(ChatFormatting.GRAY)).component());
        pTooltip.add(BocLang.translate("tooltip.boc_custom.stores").style(ChatFormatting.GRAY).component());
        pTooltip.add(BocLang.text(" ").translate("tooltip.boc_custom.energy",
                formatLong(20000)).style(ChatFormatting.AQUA).component());
    }

}
