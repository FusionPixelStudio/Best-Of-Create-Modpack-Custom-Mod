package net.AsherRoland.BoCCustom.blocks.recycling_block;

import com.mojang.logging.LogUtils;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import net.AsherRoland.BoCCustom.BocLang;
import net.AsherRoland.BoCCustom.client;
import net.AsherRoland.BoCCustom.network.ModNetworking;
import net.AsherRoland.BoCCustom.network.TotalRecycledPacket;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.List;

public class RecyclingBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public static final Logger LOGGER = LogUtils.getLogger();
    static {
        LOGGER.info("RecyclingBlockEntity class loaded");
    }

    private final ItemStackHandler inventory = new ItemStackHandler(2) {

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // Slot 0 = input only
            if (slot == 0) return true;

            // Slot 1 = output only (no manual insertion)
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (!(level instanceof ServerLevel)) return;
            setChanged();
        }
    };

    private final LazyOptional<IItemHandler> lazyItems =
            LazyOptional.of(() -> inventory);


    private int accumulatedItems = 0;
    private int lockedItemsPerGold = -1;
    private int cachedItemsPerGold = 0; // default for tooltip

    private final RecyclingEnergyStorage energy;
    private final LazyOptional<IEnergyStorage> lazyEnergy;


    private static final int CAPACITY = 20_000;    // Max energy
    private static final int MAX_INPUT = 500;     // Max input per tick
    private boolean active = false;

    public int timer = 120;

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            if (active && isRotatingCorrectly() && isSpeedRequirementFulfilled()) {
                spawnParticles();
            }
        }
        if (!(level instanceof ServerLevel serverLevel)) return;

        ItemStack input = inventory.getStackInSlot(0);
        ItemStack output = inventory.getStackInSlot(1);

        boolean hasInput = !input.isEmpty();
        boolean outputHasSpace = output.isEmpty() || output.getCount() < 64;
        boolean mechanicsValid = isRotatingCorrectly() && isSpeedRequirementFulfilled();

        if (!mechanicsValid || !outputHasSpace) {
            active = false;
            return;
        }

        if (hasInput) {
            active = true;
            timer = Math.max(timer, 20);
        }
        else {
            if (timer > 0) {
                timer--;
            } else {
                active = false;
                sendData();
                return;
            }
        }

        if (!active) return;

        int requiredEnergy = getEnergyConsumptionRate();
        int consumed = energy.consume(requiredEnergy);

        RecyclingBlockSavedData data = RecyclingBlockSavedData.get(serverLevel);
        data.addRecycledItems(inventory.getStackInSlot(0).getCount());

        // Send packet to all players tracking this chunk
        ModNetworking.CHANNEL.send(
                PacketDistributor.TRACKING_CHUNK.with(() -> serverLevel.getChunkAt(getBlockPos())),
                new TotalRecycledPacket(data.getTotalItemsRecycled())
        );

        ensureLockedItemsPerGold(serverLevel);

        if (!input.isEmpty() && (output.isEmpty() || output.getCount() < 64)) {
            int toTake = Math.min(
                    input.getCount(),
                    lockedItemsPerGold - accumulatedItems
            );

            input.shrink(toTake);
            accumulatedItems += toTake;

            inventory.setStackInSlot(0, ItemStack.EMPTY);
            setChanged();
        }

        processRecycling(consumed);
    }

    private void processRecycling(int consumedFE) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        ItemStack output = inventory.getStackInSlot(1);

        // Stop if output slot is full
        if (!output.isEmpty() && output.getCount() >= 64) return;

        // Get multipliers for gold efficiency
        float goldMultiplier = getEnergyMultiplier(consumedFE);

        int effectiveItemsPerGold =
                Math.max(1, Math.round(lockedItemsPerGold / goldMultiplier));

        cachedItemsPerGold = effectiveItemsPerGold;

        // Process as much gold as possible
        while (accumulatedItems >= effectiveItemsPerGold) {
            accumulatedItems -= effectiveItemsPerGold;

            // Add gold to output
            if (output.isEmpty()) {
                inventory.setStackInSlot(1, new ItemStack(Items.GOLD_INGOT, 1));
                output = inventory.getStackInSlot(1);
            } else {
                output.grow(1);
            }

            lockedItemsPerGold = -1;

            setChanged();

            // Stop if output reaches 64
            if (output.getCount() >= 64) break;
        }
    }

    private void ensureLockedItemsPerGold(ServerLevel level) {
        if (lockedItemsPerGold <= 0) {
            lockedItemsPerGold = getItemsPerGold(level);
        }
    }

    private int getItemsPerGold(ServerLevel level) {

        long total = RecyclingBlockSavedData.get(level).getTotalItemsRecycled();

        int baseCost = 128;
        int maxCost = 64*50000;

        double rawScaling = Math.pow(total / 128f, 0.6);
        int stacks = (int) Math.floor(rawScaling);
        int scaling = stacks * 64;

        return Math.min(baseCost + scaling, maxCost);
    }

    private float getEnergyMultiplier(int consumedFE) {
        if (consumedFE <= 0) return 1f;

        // Every 100 FE = +1x speed
        return 1f + (consumedFE / 100f);
    }

    /** How much FE to consume per tick, can scale with speed */
    public int getEnergyConsumptionRate() {
        float speed = Math.abs(getSpeed()); // Use rotational speed if desired
        return Math.max(10, (int)(speed * 2)); // Example formula: min 10 FE, scales with speed
    }

    /** Expose FE input capability */
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return lazyEnergy.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.DOWN) {
                // Only expose slot 1 for bottom
                IItemHandler outputOnly = new IItemHandler() {
                    @Override
                    public int getSlots() { return 2; }

                    @Override
                    public ItemStack getStackInSlot(int slot) {
                        return slot == 1 ? inventory.getStackInSlot(1) : ItemStack.EMPTY;
                    }

                    @Override
                    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                        return stack; // don't allow inserting from bottom
                    }

                    @Override
                    public ItemStack extractItem(int slot, int amount, boolean simulate) {
                        if (slot == 1) return inventory.extractItem(slot, amount, simulate);
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public int getSlotLimit(int slot) { return 64; }

                    @Override
                    public boolean isItemValid(int slot, ItemStack stack) {
                        return false; // bottom doesn't accept items
                    }
                };
                return LazyOptional.of(() -> outputOnly).cast();
            }
            return lazyItems.cast(); // other sides use full access
        }
        return super.getCapability(cap, side);
    }

    /** NBT persistence */
    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        energy.read(compound, "main");
        active = compound.getBoolean("active");
        accumulatedItems = compound.getInt("Accumulated");
        cachedItemsPerGold = compound.getInt("Rate");
        inventory.deserializeNBT(compound.getCompound("Inventory"));

        lockedItemsPerGold = -1;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        energy.write(compound, "main");
        compound.putBoolean("active", active);
        compound.putInt("Accumulated", accumulatedItems);
        compound.putInt("Rate", cachedItemsPerGold);
        compound.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void remove() {
        lazyEnergy.invalidate();
        active = false;
        super.remove();
    }

    @Override
    public void invalidate() {
        super.invalidate();
//        invalidateCapabilities();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inventory);
    }

    public void spawnParticles() {
        if (!(level instanceof net.minecraft.client.multiplayer.ClientLevel clientLevel))
            return;

        BlockParticleOption data =
                new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GRAVEL.defaultBlockState());
        Vec3 center = VecHelper.getCenterOf(worldPosition)
                .add(0, 0.2f, 0);

        float speed = Math.signum(getSpeed());
        float angle = speed > 0 ? 25 : -25;

        Vec3 motion = new Vec3(0, 0, 0.15f);
        motion = VecHelper.rotate(motion, angle, Direction.Axis.Y);
        motion = VecHelper.offsetRandomly(motion, level.random, 0.02f);

        level.addParticle(
                data,
                center.x,
                center.y,
                center.z,
                motion.x,
                motion.y,
                motion.z
        );
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

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        energy.energyConsumptionTooltip(tooltip, active ? getEnergyConsumptionRate() : 0);

        if (active) {
            BocLang.text(" ").forGoggles(tooltip);
            BocLang.translate("tooltip.recycler.processing")
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip);

            // Show output slot
            BocLang.translate("tooltip.recycler.output")
                    .space()
                    .add(BocLang.number(inventory.getStackInSlot(1).getCount()))
                    .space()
                    .add(BocLang.translate("tooltip.recycler.output_detail"))
                    .style(ChatFormatting.YELLOW)
                    .forGoggles(tooltip);

            // Show progress toward next gold
            BocLang.translate("tooltip.recycler.progress")
                    .space()
                    .add(BocLang.number(accumulatedItems))
                    .add(BocLang.text(" / "))
                    .add(BocLang.number(cachedItemsPerGold))
                    .space()
                    .add(BocLang.translate("tooltip.recycler.progress_detail"))
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip);


                BocLang.translate("tooltip.recycler.total_recycled")
                        .space()
                        .add(BocLang.number(client.ClientRecyclingData.totalItemsRecycled))
                        .space()
                        .add(BocLang.translate("tooltip.recycler.recycled_detail"))
                        .style(ChatFormatting.GREEN)
                        .forGoggles(tooltip);

        }

        if (!isRotatingCorrectly()) {
            BocLang.text(" ").forGoggles(tooltip);
            BocLang.translate("tooltip.recycler.direction")
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip);
            BocLang.translate("tooltip.recycler.wrong_direction")
                    .forGoggles(tooltip);
        }

        BocLang.text(" ").forGoggles(tooltip);
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        return true;
    }

    @Override
    public float calculateStressApplied() {
        return 2f;
    }

    public RecyclingBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.energy = new RecyclingEnergyStorage(CAPACITY, MAX_INPUT, 0, this);
        this.lazyEnergy = LazyOptional.of(() -> energy);

    }

}
