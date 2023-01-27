package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.screen.container.ModRecipeCreatorDataScreen;
import fr.eno.craftcreator.utils.ClientUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

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

    public static void encode(UpdateRecipeCreatorTileDataClientPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeUtf(msg.dataName);
        packetBuffer.writeInt(msg.dataType.ordinal());
        packetBuffer.writeBlockPos(msg.pos);

        switch(msg.dataType)
        {
            case INT -> packetBuffer.writeInt((int) msg.data);
            case INT_ARRAY -> packetBuffer.writeVarIntArray((int[]) msg.data);
            case STRING -> packetBuffer.writeUtf((String) msg.data);
            case BOOLEAN -> packetBuffer.writeBoolean((boolean) msg.data);
            case MAP_INT_STRING -> packetBuffer.writeMap((Map<Integer, ResourceLocation>) msg.data, FriendlyByteBuf::writeInt, FriendlyByteBuf::writeResourceLocation);
        }
    }

    public static UpdateRecipeCreatorTileDataClientPacket decode(FriendlyByteBuf packetBuffer)
    {
        String dataName = packetBuffer.readUtf();
        InitPackets.PacketDataType dataType = InitPackets.PacketDataType.values()[packetBuffer.readInt()];
        BlockPos pos = packetBuffer.readBlockPos();
        Object data;

        switch(dataType)
        {
            case INT -> {
                data = packetBuffer.readInt();
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, data);
            }
            case INT_ARRAY -> {
                data = packetBuffer.readVarIntArray();
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, data);
            }
            case STRING -> {
                data = packetBuffer.readUtf();
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, data);
            }
            case BOOLEAN -> {
                data = packetBuffer.readBoolean();
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, data);
            }
            case MAP_INT_STRING -> {
                data = packetBuffer.readMap(FriendlyByteBuf::readInt, FriendlyByteBuf::readResourceLocation);
                return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, data);
            }
            default -> data = -1;
        }

        return new UpdateRecipeCreatorTileDataClientPacket(dataName, pos, dataType, data);
    }

    public static class ClientHandler
    {
        public static void handle(UpdateRecipeCreatorTileDataClientPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            if(ClientUtils.getCurrentScreen() instanceof ModRecipeCreatorDataScreen<?> dataScreen)
            {
                dataScreen.setData(msg.dataName, msg.data);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
