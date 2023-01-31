package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.api.ServerUtils;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.utils.InventoryDataContainerTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RetrieveRecipeCreatorTileDataServerPacket
{
    private final String dataName;
    private final BlockPos pos;
    private final InitPackets.PacketDataType dataType;

    public RetrieveRecipeCreatorTileDataServerPacket(String dataName, BlockPos pos, InitPackets.PacketDataType dataType)
    {
        this.dataName = dataName;
        this.pos = pos;
        this.dataType = dataType;
    }

    public static void encode(RetrieveRecipeCreatorTileDataServerPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeString(msg.dataName);
        packetBuffer.writeInt(msg.dataType.ordinal());
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static RetrieveRecipeCreatorTileDataServerPacket decode(PacketBuffer packetBuffer)
    {
        String dataName = packetBuffer.readString();
        InitPackets.PacketDataType dataType = InitPackets.PacketDataType.values()[packetBuffer.readInt()];
        BlockPos pos = packetBuffer.readBlockPos();
        return new RetrieveRecipeCreatorTileDataServerPacket(dataName, pos, dataType);
    }

    public static class ServerHandler
    {
        public static void handle(RetrieveRecipeCreatorTileDataServerPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            TileEntity tileEntity = ServerUtils.getBlockEntity(ctx, msg.pos);

            if(tileEntity instanceof InventoryDataContainerTileEntity)
            {
                InitPackets.NetworkHelper.sendToPlayer(ServerUtils.getServerPlayer(ctx), new UpdateRecipeCreatorTileDataClientPacket(msg.dataName, msg.pos, msg.dataType, ((InventoryDataContainerTileEntity) tileEntity).getData(msg.dataName)));
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
