package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.packets.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

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

        // Register packets
        registerPackets();
    }

    private static SimpleChannel getNetWork()
    {
        if(network == null)
        {
            throw new RuntimeException("Network hasn't been initialized !");
        }

        return network;
    }

    public static void registerPackets()
    {
        registerServerMessage(RetrieveRecipeCreatorTileDataServerPacket.class, RetrieveRecipeCreatorTileDataServerPacket::encode, RetrieveRecipeCreatorTileDataServerPacket::decode, RetrieveRecipeCreatorTileDataServerPacket.ServerHandler::handle);
        registerServerMessage(CreateRecipePacket.class, CreateRecipePacket::encode, CreateRecipePacket::decode, CreateRecipePacket.ServerHandler::handle);

        registerClientMessage(UpdateRecipeCreatorTileDataClientPacket.class, UpdateRecipeCreatorTileDataClientPacket::encode, UpdateRecipeCreatorTileDataClientPacket::decode, UpdateRecipeCreatorTileDataClientPacket.ClientHandler::handle);
        registerServerMessage(UpdateRecipeCreatorTileDataServerPacket.class, UpdateRecipeCreatorTileDataServerPacket::encode, UpdateRecipeCreatorTileDataServerPacket::decode, UpdateRecipeCreatorTileDataServerPacket.ServerHandler::handle);

        registerClientMessage(UpdateRecipeListClientPacket.class, UpdateRecipeListClientPacket::encode, UpdateRecipeListClientPacket::decode, UpdateRecipeListClientPacket.ClientHandler::handle);
        registerServerMessage(RetrieveServerRecipesPacket.class, RetrieveServerRecipesPacket::encode, RetrieveServerRecipesPacket::decode, RetrieveServerRecipesPacket.ServerHandler::handle);

        registerServerMessage(RemoveAddedRecipePacket.class, RemoveAddedRecipePacket::encode, RemoveAddedRecipePacket::decode, RemoveAddedRecipePacket.ServerHandler::handle);
        registerServerMessage(RemoveModifiedRecipe.class, RemoveModifiedRecipe::encode, RemoveModifiedRecipe::decode, RemoveModifiedRecipe.ServerHandler::handle);
        registerServerMessage(RemoveRecipePacket.class, RemoveRecipePacket::encode, RemoveRecipePacket::decode, RemoveRecipePacket.ServerHandler::handle);
    }

    private static Optional<NetworkDirection> distClient()
    {
        return Optional.of(NetworkDirection.PLAY_TO_CLIENT);
    }

    private static Optional<NetworkDirection> distServer()
    {
        return Optional.of(NetworkDirection.PLAY_TO_SERVER);
    }

    private static <MSG> void registerClientMessage(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
    {
        network.registerMessage(packetId++, messageType, encoder, decoder, messageConsumer, distClient());
    }

    private static <MSG> void registerServerMessage(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
    {
        network.registerMessage(packetId++, messageType, encoder, decoder, messageConsumer, distServer());
    }

    public static class NetworkHelper
    {
        public static <MSG> void sendToPlayer(ServerPlayer player, MSG message)
        {
            getNetWork().send(PacketDistributor.PLAYER.with(() -> player), message);
        }

        public static <MSG> void sendToServer(MSG message)
        {
            getNetWork().send(PacketDistributor.SERVER.noArg(), message);
        }
    }

    public enum RecipeList
    {
        ADDED_RECIPES,
        MODIFIED_RECIPES
    }

    public enum PacketDataType
    {
        INT,
        INT_ARRAY,
        STRING,
        BOOLEAN,
        DOUBLE_ARRAY,
        MAP_INT_RESOURCELOCATION,
        RECIPES
    }
}
