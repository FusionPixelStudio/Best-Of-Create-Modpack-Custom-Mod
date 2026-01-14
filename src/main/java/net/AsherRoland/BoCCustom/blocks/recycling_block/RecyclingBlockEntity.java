package net.AsherRoland.BoCCustom.blocks.recycling_block;

import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.AsherRoland.BoCCustom.BocLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.slf4j.Logger;

import java.util.List;

public class RecyclingBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public static final Logger LOGGER = LogUtils.getLogger();
    static {
        LOGGER.info("RecyclingBlockEntity class loaded");
    }

    @Override
    public void tick() {
        super.tick();

        // Normal processing logic continues here
    }

    public Direction getRotationDirectionRelativeToFront() {
        float speed = getSpeed();
        if (speed == 0)
            return getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        Direction front = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        speed = convertToDirection(speed, front);

        return speed > 0 ? front : front.getOpposite();
    }

    public boolean isRotatingCorrectly() {
        Direction dir = getRotationDirectionRelativeToFront();
        return dir == getBlockState().getValue(HorizontalDirectionalBlock.FACING);
    }

    public boolean canProcess() {
        return isRotatingCorrectly() && isSpeedRequirementFulfilled();
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean sneaking) {

        BocLang.translate("tooltip.recycler.header")
                .forGoggles(tooltip);
        if (!isRotatingCorrectly()) {
            BocLang.translate("tooltip.recycler.wrong_direction")
                    .style(ChatFormatting.RED)
                    .forGoggles(tooltip);
            return true;
        }

        return true;
    }

    public RecyclingBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

}
