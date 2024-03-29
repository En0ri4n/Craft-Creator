package fr.eno.craftcreator.packets;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.utils.ServerUtils;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.base.InventoryDataContainerTileEntity;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static void encode(UpdateRecipeCreatorTileDataServerPacket msg, PacketBuffer packetBuffer)
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
            case PAIR_VALUE_STRING_JSON_OBJECT_LIST:
                PairValues<String, List<JsonObject>> recipeTypePair = (PairValues<String, List<JsonObject>>) msg.data;
                packetBuffer.writeUtf(recipeTypePair.getFirstValue());
                packetBuffer.writeInt(recipeTypePair.getSecondValue().size());
                recipeTypePair.getSecondValue().forEach(js -> packetBuffer.writeUtf(js.toString()));
                break;
        }
    }

    public static UpdateRecipeCreatorTileDataServerPacket decode(PacketBuffer packetBuffer)
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

            case PAIR_VALUE_STRING_JSON_OBJECT_LIST:
                String recipeType = packetBuffer.readUtf();
                int listSize = packetBuffer.readInt();
                List<JsonObject> jsonList = new ArrayList<>();
                for(int k = 0; k < listSize; k++)
                    jsonList.add(Utils.GSON.fromJson(packetBuffer.readUtf(), JsonObject.class));

                return new UpdateRecipeCreatorTileDataServerPacket(dataName, pos, dataType, PairValues.create(recipeType, jsonList));

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
            TileEntity tile = ServerUtils.getBlockEntity(ctx, msg.pos);

            if(tile instanceof InventoryDataContainerTileEntity)
            {
                InventoryDataContainerTileEntity dataContainerTile = (InventoryDataContainerTileEntity) tile;
                dataContainerTile.setData(msg.dataName, msg.data);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
