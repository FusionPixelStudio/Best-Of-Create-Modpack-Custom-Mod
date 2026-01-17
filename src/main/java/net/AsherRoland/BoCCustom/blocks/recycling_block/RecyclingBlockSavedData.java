package net.AsherRoland.BoCCustom.blocks.recycling_block;

import net.AsherRoland.BoCCustom.client;
import net.AsherRoland.BoCCustom.network.TotalRecycledPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;

import static net.AsherRoland.BoCCustom.network.ModNetworking.CHANNEL;

public class RecyclingBlockSavedData extends SavedData {
    private static final String NAME = "boc_recycling_data";

    private long totalItemsRecycled = 0;

    public static RecyclingBlockSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                RecyclingBlockSavedData::load,
                RecyclingBlockSavedData::new,
                NAME
        );
    }

    public static void sendTotalRecycledToClient(ServerLevel serverLevel, long total) {
        serverLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new TotalRecycledPacket(total));
        });
    }

    public void resetTotalItemsRecycled() {
        totalItemsRecycled = 0;
        setDirty();
    }

    public class TotalRecycledPacketHandler {
        public static void handle(TotalRecycledPacket message) {
            client.ClientRecyclingData.totalItemsRecycled = message.total;
        }
    }

    public void addRecycledItems(long amount) {
        totalItemsRecycled += amount;
        setDirty();
    }

    public long getTotalItemsRecycled() {
        return totalItemsRecycled;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putLong("TotalItems", totalItemsRecycled);
        return tag;
    }

    public static RecyclingBlockSavedData load(CompoundTag tag) {
        RecyclingBlockSavedData data = new RecyclingBlockSavedData();
        data.totalItemsRecycled = tag.getLong("TotalItems");
        return data;
    }
}
