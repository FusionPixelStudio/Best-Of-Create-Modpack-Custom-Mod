package net.AsherRoland.BoCCustom.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.AsherRoland.BoCCustom.clientEventData.ClientRecyclingData;

import java.util.function.Supplier;

public class TotalRecycledPacket {
    public final long total;

    public TotalRecycledPacket(long total) {
        this.total = total;
    }

    // Encode → write data to the buffer
    public static void encode(TotalRecycledPacket msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.total);
    }

    // Decode → read data from the buffer
    public static TotalRecycledPacket decode(FriendlyByteBuf buf) {
        return new TotalRecycledPacket(buf.readLong());
    }

    // Handle the packet on the client
    public static void handle(TotalRecycledPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientRecyclingData.totalItemsRecycled = msg.total;
        });
        ctx.get().setPacketHandled(true);
    }
}
