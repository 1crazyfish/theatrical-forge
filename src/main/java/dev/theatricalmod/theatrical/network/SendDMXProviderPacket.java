package dev.theatricalmod.theatrical.network;

import dev.theatricalmod.theatrical.TheatricalMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendDMXProviderPacket {

    public SendDMXProviderPacket(BlockPos blockPos, byte[] data) {
        this.blockPos = blockPos;
        this.data = data;
    }

    public SendDMXProviderPacket(FriendlyByteBuf buffer){
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        blockPos = new BlockPos(x, y, z);
        int i = buffer.readInt();
        if(data == null){
            data = new byte[i];
        }
        buffer.readBytes(data);
    }

    private final BlockPos blockPos;
    private byte[] data;

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public byte[] getData() {
        return data;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
        buf.writeInt(data.length);
        buf.writeBytes(data);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> TheatricalMod.proxy.handleProviderDMXUpdate(blockPos, data));
        context.get().setPacketHandled(true);
    }

}
