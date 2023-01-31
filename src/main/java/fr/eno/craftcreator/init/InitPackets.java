package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataClientPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InitPackets
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static SimpleChannel network = null;
    private static int packetId;

    public static void initNetwork()
    {
        if(network != null)
        {
            throw new RuntimeException("Network has been already initialized !");
        }

        network = NetworkRegistry.ChannelBuilder
                .named(References.getLoc("main_channel"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();
        packetId = 0;
    }

    private static SimpleChannel getNetWork()
    {
        if(network == null)
        {
            throw new RuntimeException("Network hasn't been initialized !");
        }

        return network;
    }

    public static void registerMessages()
    {
        registerServerMessage(RetrieveRecipeCreatorTileDataServerPacket.class, RetrieveRecipeCreatorTileDataServerPacket::encode, RetrieveRecipeCreatorTileDataServerPacket::decode, RetrieveRecipeCreatorTileDataServerPacket.ServerHandler::handle);

        registerClientMessage(UpdateRecipeCreatorTileDataClientPacket.class, UpdateRecipeCreatorTileDataClientPacket::encode, UpdateRecipeCreatorTileDataClientPacket::decode, UpdateRecipeCreatorTileDataClientPacket.ClientHandler::handle);
        registerServerMessage(UpdateRecipeCreatorTileDataServerPacket.class, UpdateRecipeCreatorTileDataServerPacket::encode, UpdateRecipeCreatorTileDataServerPacket::decode, UpdateRecipeCreatorTileDataServerPacket.ServerHandler::handle);
    }

    private static Optional<NetworkDirection> distClient() { return Optional.of(NetworkDirection.PLAY_TO_CLIENT); }
    private static Optional<NetworkDirection> distServer() { return Optional.of(NetworkDirection.PLAY_TO_SERVER); }

    private static <MSG> void registerClientMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
    {
        network.registerMessage(packetId++, messageType, encoder, decoder, messageConsumer, distClient());
    }

    private static <MSG> void registerServerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
    {
        network.registerMessage(packetId++, messageType, encoder, decoder, messageConsumer, distServer());
    }

    public static class NetworkHelper
    {
        public static <MSG> void sendToPlayer(ServerPlayerEntity player, MSG message)
        {
            getNetWork().send(PacketDistributor.PLAYER.with(() -> player), message);
        }

        public static <MSG> void sendToServer(MSG message)
        {
            getNetWork().send(PacketDistributor.SERVER.noArg(), message);
        }
    }

    public enum PacketDataType
    {
        INT,
        INT_ARRAY,
        STRING,
        BOOLEAN,
        FLOAT,
        DOUBLE,
        MAP_INT_STRING
    }
}
