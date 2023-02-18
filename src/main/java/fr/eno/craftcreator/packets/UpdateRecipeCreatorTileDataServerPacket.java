package fr.eno.craftcreator.packets;


import fr.eno.craftcreator.api.ServerUtils;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.base.InventoryDataContainerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UpdateRecipeCreatorTileDataServerPacket
{
    private final String dataName;
    private final BlockPos pos;
    private final InitPackets.PacketDataType dataType;
    private final Object data;

    public UpdateRecipeCreatorTileDataServerPacket(String dataName, BlockPos pos, InitPackets.PacketDataType dataType, Object data)
    {
        this.dataName = dataName;
        this.pos = pos;
        this.dataType = dataType;
        this.data = data;
    }

    public static void encode(UpdateRecipeCreatorTileDataServerPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeUtf(msg.dataName);
        packetBuffer.writeInt(msg.dataType.ordinal());
        packetBuffer.writeBlockPos(msg.pos);

        switch(msg.dataType)
        {
            case INT:
                packetBuffer.writeInt((int) msg.data);
                break;
            case INT_ARRAY:
                packetBuffer.writeVarIntArray((int[]) msg.data);
                break;
            case DOUBLE_ARRAY:
                double[] doubleArray = (double[]) msg.data;
                packetBuffer.writeInt(doubleArray.length);
                for(double d : doubleArray)
                    packetBuffer.writeDouble(d);
                break;
            case STRING:
                packetBuffer.writeUtf((String) msg.data);
                break;
            case BOOLEAN:
                packetBuffer.writeBoolean((boolean) msg.data);
                break;
            case MAP_INT_RESOURCELOCATION:
                Map<Integer, ResourceLocation> map = (Map<Integer, ResourceLocation>) msg.data;
                packetBuffer.writeInt(map.size());
                map.forEach((index, loc) ->
                {
                    packetBuffer.writeInt(index);
                    packetBuffer.writeResourceLocation(loc);
                });
                break;
        }
    }

    public static UpdateRecipeCreatorTileDataServerPacket decode(FriendlyByteBuf packetBuffer)
    {
        String dataName = packetBuffer.readUtf();
        InitPackets.PacketDataType dataType = InitPackets.PacketDataType.values()[packetBuffer.readInt()];
        BlockPos pos = packetBuffer.readBlockPos();

        switch(dataType)
        {
            case INT:
                return new UpdateRecipeCreatorTileDataServerPacket(dataName, pos, dataType, packetBuffer.readInt());
            case INT_ARRAY:
                return new UpdateRecipeCreatorTileDataServerPacket(dataName, pos, dataType, packetBuffer.readVarIntArray());
            case DOUBLE_ARRAY:
                double[] doubleArray = new double[packetBuffer.readInt()];
                for(int i = 0; i < doubleArray.length; i++)
                    doubleArray[i] = packetBuffer.readDouble();
                return new UpdateRecipeCreatorTileDataServerPacket(dataName, pos, dataType, doubleArray);
            case STRING:
                return new UpdateRecipeCreatorTileDataServerPacket(dataName, pos, dataType, packetBuffer.readUtf());
            case BOOLEAN:
                return new UpdateRecipeCreatorTileDataServerPacket(dataName, pos, dataType, packetBuffer.readBoolean());
            case MAP_INT_RESOURCELOCATION:
                Map<Integer, ResourceLocation> map = new HashMap<>();
                int size = packetBuffer.readInt();
                for(int i = 0; i < size; i++)
                    map.put(packetBuffer.readInt(), packetBuffer.readResourceLocation());
                return new UpdateRecipeCreatorTileDataServerPacket(dataName, pos, dataType, map);
            default:
                return new UpdateRecipeCreatorTileDataServerPacket(dataName, pos, dataType, -1);
        }
    }

    public static class ServerHandler
    {
        /**
         * Handle the packet on the server side, update the data in the SERVER tile entity
         */
        public static void handle(UpdateRecipeCreatorTileDataServerPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            BlockEntity tile = ServerUtils.getBlockEntity(ctx, msg.pos);

            if(tile instanceof InventoryDataContainerTileEntity)
            {
                InventoryDataContainerTileEntity dataContainerTile = (InventoryDataContainerTileEntity) tile;
                dataContainerTile.setData(msg.dataName, msg.data);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
