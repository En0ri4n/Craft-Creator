package fr.eno.craftcreator.packets;


import fr.eno.craftcreator.api.ServerUtils;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.base.InventoryDataContainerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RetrieveRecipeCreatorTileDataServerPacket
{
    private final String dataName;
    private final BlockPos pos;
    private final InitPackets.PacketDataType dataType;
    
    /**
     * Retrieve data from a tile entity on the server side
     *
     * @param dataName The name of the data to update
     * @param pos The position of the tile entity
     * @param dataType The type of the data to update
     */
    public RetrieveRecipeCreatorTileDataServerPacket(String dataName, BlockPos pos, InitPackets.PacketDataType dataType)
    {
        this.dataName = dataName;
        this.pos = pos;
        this.dataType = dataType;
    }

    public static void encode(RetrieveRecipeCreatorTileDataServerPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeUtf(msg.dataName);
        packetBuffer.writeInt(msg.dataType.ordinal());
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static RetrieveRecipeCreatorTileDataServerPacket decode(FriendlyByteBuf packetBuffer)
    {
        String dataName = packetBuffer.readUtf();
        InitPackets.PacketDataType dataType = InitPackets.PacketDataType.values()[packetBuffer.readInt()];
        BlockPos pos = packetBuffer.readBlockPos();
        return new RetrieveRecipeCreatorTileDataServerPacket(dataName, pos, dataType);
    }

    public static class ServerHandler
    {
        /**
         * Handle the packet on the server side and send the data to the client using {@link UpdateRecipeCreatorTileDataClientPacket}
         */
        public static void handle(RetrieveRecipeCreatorTileDataServerPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            BlockEntity tileEntity = ServerUtils.getBlockEntity(ctx, msg.pos);

            if(tileEntity instanceof InventoryDataContainerTileEntity)
            {
                InitPackets.NetworkHelper.sendToPlayer(ServerUtils.getServerPlayer(ctx), new UpdateRecipeCreatorTileDataClientPacket(msg.dataName, msg.pos, msg.dataType, ((InventoryDataContainerTileEntity) tileEntity).getData(msg.dataName)));
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
