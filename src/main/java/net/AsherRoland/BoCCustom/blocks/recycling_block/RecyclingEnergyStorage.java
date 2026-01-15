package net.AsherRoland.BoCCustom.blocks.recycling_block;

import net.AsherRoland.BoCCustom.BocLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;

public class RecyclingEnergyStorage extends EnergyStorage {
//    public RecyclingEnergyStorage(int capacity) {
//        super(capacity);
//    }
//
//    public RecyclingEnergyStorage(int capacity, int maxReceive) {
//        super(capacity, maxReceive, 0);
//    }

    private final RecyclingBlockEntity blockEntity;

    public RecyclingEnergyStorage(int capacity, int maxReceive, int maxExtract, RecyclingBlockEntity blockEntity) {
        super(capacity, maxReceive, maxExtract, 0);
        this.blockEntity = blockEntity;
    }

    // NBT read/write
    public CompoundTag write(CompoundTag nbt, String key) {
        nbt.putInt("energy_" + key, getEnergyStored());
        return nbt;
    }

    public void read(CompoundTag nbt, String key) {
        this.energy = nbt.getInt("energy_" + key); // directly assign
    }

    // Direct energy manipulation for internal processing
    public int consume(int amount) {
        int used = Math.min(amount, energy);
        energy -= used;
        blockEntity.sendData();
        return used;
    }

    public int produce(int amount) {
        int added = Math.min(amount, getMaxEnergyStored() - getEnergyStored());
        receiveEnergy(added, false);
        return added;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);
        if (received > 0 && !simulate) {
            blockEntity.setChanged();
        }
        return received;
    }

    public int getSpace() {
        return getMaxEnergyStored() - getEnergyStored();
    }

    public void energyConsumptionTooltip(List<Component> tooltip, int consumption){
        BocLang.translate("tooltip.recycler.energy_stats").space().style(ChatFormatting.GOLD).forGoggles(tooltip);

        BocLang.number(this.getEnergyStored() > 0 ? consumption : 0)
                .add(BocLang.text(" FE/tick"))
                .style(ChatFormatting.AQUA)
                .space()
                .add(BocLang.translate("tooltip.recycler.energy_consumption")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
    }
}
