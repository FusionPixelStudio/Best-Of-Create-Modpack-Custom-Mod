package net.AsherRoland.BoCCustom.blocks.recycling_block;

import com.mojang.logging.LogUtils;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.AsherRoland.BoCCustom.BocLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.slf4j.Logger;

import java.util.List;

public class RecyclingBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public static final Logger LOGGER = LogUtils.getLogger();
    static {
        LOGGER.info("RecyclingBlockEntity class loaded");
    }

    private final RecyclingEnergyStorage energy;
    private final LazyOptional<IEnergyStorage> lazyEnergy;


    private static final int CAPACITY = 20_000;    // Max energy
    private static final int MAX_INPUT = 500;     // Max input per tick
    private boolean active = false;

    @Override
    public void tick() {
        super.tick();
        if(level.isClientSide()) return;

        sendData();

        int requiredEnergy = getEnergyConsumptionRate();
        if(!active) {
            if (isRotatingCorrectly() && isSpeedRequirementFulfilled()) {
                active = true;
            }
        }

        if(active) {
            int consumed = energy.consume(requiredEnergy); // Drain FE
            float speedMultiplier = 1f + ((float) consumed / requiredEnergy); // Scale speed
            processWithSpeedMultiplier(speedMultiplier);

            // Stop if not enough energy left
            if(!isRotatingCorrectly() || !isSpeedRequirementFulfilled()) active = false;
        }
    }

    /** Multiply your internal processing by this value */
    private void processWithSpeedMultiplier(float multiplier) {
        // Insert your normal processing logic here, multiplying progress by multiplier
    }

    /** How much FE to consume per tick, can scale with speed */
    public int getEnergyConsumptionRate() {
        float speed = Math.abs(getSpeed()); // Use rotational speed if desired
        return Math.max(10, (int)(speed * 2)); // Example formula: min 10 FE, scales with speed
    }

    /** Expose FE input capability */
    @Override
    public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, Direction side) {
        if(cap == ForgeCapabilities.ENERGY) return lazyEnergy.cast();
        return super.getCapability(cap, side);
    }

    /** NBT persistence */
    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        energy.read(compound, "main");
        active = compound.getBoolean("active");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        energy.write(compound, "main");
        compound.putBoolean("active", active);
    }

    @Override
    public void remove() {
        lazyEnergy.invalidate();
        active = false;
        super.remove();
    }

    public Direction getRotationDirectionRelativeToFront() {
        float speed = getSpeed();
        if (speed == 0)
            return getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        Direction front = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        speed = convertToDirection(speed, front);

        Direction.Axis axis = front.getAxis();

        if (axis == Direction.Axis.Z) {
            return speed > 0 ? front : front.getOpposite();
        } else {
            return speed > 0 ? front.getOpposite() : front;
        }
    }

    public boolean isRotatingCorrectly() {
        Direction dir = getRotationDirectionRelativeToFront();
        return dir == getBlockState().getValue(HorizontalDirectionalBlock.FACING);
    }

    public float calculateStressApplied() {
        float capacity = 128f;
        this.lastStressApplied  = capacity;
        return capacity;
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean sneaking) {

//        BocLang.translate("tooltip.recycler.header")
//                .forGoggles(tooltip);
        if (!isRotatingCorrectly()) {
            BocLang.translate("tooltip.recycler.wrong_direction")
                    .style(ChatFormatting.RED)
                    .forGoggles(tooltip);
        }

        if (energy.getEnergyStored() > 0) {
            energy.storedEnergyTooltip(tooltip);
            energy.energyConsumptionTooltip(tooltip, active ? getEnergyConsumptionRate() : 0);
        }

        return true;
    }

    public RecyclingBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.energy = new RecyclingEnergyStorage(CAPACITY, MAX_INPUT, 0);
        this.lazyEnergy = LazyOptional.of(() -> energy);

    }

}
