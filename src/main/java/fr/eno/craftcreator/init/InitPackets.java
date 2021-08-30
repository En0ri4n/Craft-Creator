package fr.eno.craftcreator.init;

import fr.eno.craftcreator.*;
import fr.eno.craftcreator.packets.*;
import net.minecraftforge.fml.network.*;
import net.minecraftforge.fml.network.simple.*;

import java.util.*;

public class InitPackets
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static SimpleChannel network = null;

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
    }

    public static SimpleChannel getNetWork()
    {
        if(network == null)
        {
            throw new RuntimeException("Network hasn't been initialized !");
        }

        return network;
    }

    public static void registerMessages()
    {
        int id = 0;

        network.registerMessage(id++, UpdateCraftingTableRecipeCreatorTilePacket.class, UpdateCraftingTableRecipeCreatorTilePacket::encode, UpdateCraftingTableRecipeCreatorTilePacket::decode, UpdateCraftingTableRecipeCreatorTilePacket.ServerHandler::handle, distServer());
        network.registerMessage(id++, GetCraftingTableRecipeCreatorTileInfosServerPacket.class, GetCraftingTableRecipeCreatorTileInfosServerPacket::encode, GetCraftingTableRecipeCreatorTileInfosServerPacket::decode, GetCraftingTableRecipeCreatorTileInfosServerPacket.ServerHandler::handle, distServer());
        network.registerMessage(id, GetCraftingTableRecipeCreatorTileInfosClientPacket.class, GetCraftingTableRecipeCreatorTileInfosClientPacket::encode, GetCraftingTableRecipeCreatorTileInfosClientPacket::decode, GetCraftingTableRecipeCreatorTileInfosClientPacket.ClientHandler::handle, distClient());
    }

    private static Optional<NetworkDirection> distClient() { return Optional.of(NetworkDirection.PLAY_TO_CLIENT); }
    private static Optional<NetworkDirection> distServer() { return Optional.of(NetworkDirection.PLAY_TO_SERVER); }
}
