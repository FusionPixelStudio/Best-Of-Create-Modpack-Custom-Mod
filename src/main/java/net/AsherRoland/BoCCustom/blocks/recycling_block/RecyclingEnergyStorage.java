package net.AsherRoland.BoCCustom.blocks.recycling_block;

import net.AsherRoland.BoCCustom.BocLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;

public class RecyclingEnergyStorage extends EnergyStorage {
    public RecyclingEnergyStorage(int capacity) {
        super(capacity);
    }

    public RecyclingEnergyStorage(int capacity, int maxReceive) {
        super(capacity, maxReceive, 0);
    }

    public RecyclingEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
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
        int used = Math.min(amount, getEnergyStored());
        extractEnergy(used, false);
        return used;
    }

    public int produce(int amount) {
        int added = Math.min(amount, getMaxEnergyStored() - getEnergyStored());
        receiveEnergy(added, false);
        return added;
    }

    public int getSpace() {
        return getMaxEnergyStored() - getEnergyStored();
    }

    public void storedEnergyTooltip(List<Component> tooltip){
        BocLang.translate("tooltip.recycler.energy_stats").forGoggles(tooltip);

        BocLang.number(this.getEnergyStored())
                .add(BocLang.text("/"))
                .add(BocLang.number(this.getMaxEnergyStored()))
                .add(BocLang.text(" FE"))
                .style(ChatFormatting.AQUA)
                .space()
                .add(BocLang.translate("tooltip.recycler.energy_stored")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

    }

    public static void energyConsumptionTooltip(List<Component> tooltip, int consumption){
        BocLang.number(consumption)
                .add(BocLang.text(" FE/tick"))
                .style(ChatFormatting.AQUA)
                .space()
                .add(BocLang.translate("tooltip.recycler.energy_consumption")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
    }
}
