package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.screen.container.base.ModRecipeCreatorDataScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UpdateRecipeCreatorTileDataClientPacket
{
    private final String dataName;
    private final BlockPos pos;
    private final InitPackets.PacketDataType dataType;
    private final Object data;

    public UpdateRecipeCreatorTileDataClientPacket(String dataName, BlockPos pos, InitPackets.PacketDataType dataType, Object data)
    {
        this.dataName = dataName;
        this.pos = pos;
        this.dataType = dataType;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public static void encode(UpdateRecipeCreatorTileDataClientPacket msg, PacketBuffer packetBuffer)
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
            case STRING:
                packetBuffer.writeUtf((String) msg.data);
                break;
            case BOOLEAN:
                packetBuffer.writeBoolean((boolean) msg.data);
                break;
            case MAP_INT_STRING:
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

    public static UpdateRecipeCreatorTileDataClientPacket decode(PacketBuffer packetBuffer)
    {
        String dataName = packetBuffer.readUtf();
        InitPackets.PacketDataType dataType = InitPackets.PacketDataType.values()[packetBuffer.readInt()];
        BlockPos pos = packetBuffer.readBlockPos();

        switch(dataType)
        {
            case INT:
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, packetBuffer.readInt());
            case INT_ARRAY:
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, packetBuffer.readVarIntArray());
            case STRING:
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, packetBuffer.readUtf());
            case BOOLEAN:
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, packetBuffer.readBoolean());
            case MAP_INT_STRING:
                Map<Integer, ResourceLocation> map = new HashMap<>();
                int size = packetBuffer.readInt();
                for(int i = 0; i < size; i++)
                    map.put(packetBuffer.readInt(), packetBuffer.readResourceLocation());
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, map);
            default:
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, -1);
        }
    }

    public static class ClientHandler
    {
        public static void handle(UpdateRecipeCreatorTileDataClientPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            if(ClientUtils.getCurrentScreen() instanceof ModRecipeCreatorDataScreen<?>)
            {
                ((ModRecipeCreatorDataScreen<?>) ClientUtils.getCurrentScreen()).setData(msg.dataName, msg.data);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
