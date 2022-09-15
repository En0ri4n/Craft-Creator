package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.packets.*;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

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
        network.registerMessage(id++, GetCraftingTableRecipeCreatorTileInfosClientPacket.class, GetCraftingTableRecipeCreatorTileInfosClientPacket::encode, GetCraftingTableRecipeCreatorTileInfosClientPacket::decode, GetCraftingTableRecipeCreatorTileInfosClientPacket.ClientHandler::handle, distClient());

        network.registerMessage(id++, OpenGuiPacket.class, OpenGuiPacket::encode, OpenGuiPacket::decode, OpenGuiPacket.ServerHandler::handle, distServer());

        network.registerMessage(id++, GetTaggeableContainerRecipeCreatorTileInfosClientPacket.class, GetTaggeableContainerRecipeCreatorTileInfosClientPacket::encode, GetTaggeableContainerRecipeCreatorTileInfosClientPacket::decode, GetTaggeableContainerRecipeCreatorTileInfosClientPacket.ClientHandler::handle, distClient());
        network.registerMessage(id++, GetTaggeableContainerRecipeCreatorTileInfosServerPacket.class, GetTaggeableContainerRecipeCreatorTileInfosServerPacket::encode, GetTaggeableContainerRecipeCreatorTileInfosServerPacket::decode, GetTaggeableContainerRecipeCreatorTileInfosServerPacket.ServerHandler::handle, distServer());

        network.registerMessage(id, UpdateTaggeableContainerRecipeCreatorTilePacket.class, UpdateTaggeableContainerRecipeCreatorTilePacket::encode, UpdateTaggeableContainerRecipeCreatorTilePacket::decode, UpdateTaggeableContainerRecipeCreatorTilePacket.ServerHandler::handle, distServer());


        if(SupportedMods.isBotaniaLoaded())
        {
            network.registerMessage(id++, SetBotaniaRecipeCreatorScreenIndexServerPacket.class, SetBotaniaRecipeCreatorScreenIndexServerPacket::encode, SetBotaniaRecipeCreatorScreenIndexServerPacket::decode, SetBotaniaRecipeCreatorScreenIndexServerPacket::serverHandle, distServer());
            network.registerMessage(id++, SetBotaniaRecipeCreatorScreenIndexClientPacket.class, SetBotaniaRecipeCreatorScreenIndexClientPacket::encode, SetBotaniaRecipeCreatorScreenIndexClientPacket::decode, SetBotaniaRecipeCreatorScreenIndexClientPacket::clientHandle, distClient());
            network.registerMessage(id, GetBotaniaRecipeCreatorScreenIndexPacket.class, GetBotaniaRecipeCreatorScreenIndexPacket::encode, GetBotaniaRecipeCreatorScreenIndexPacket::decode, GetBotaniaRecipeCreatorScreenIndexPacket.ServerHandler::handle, distServer());
        }
    }

    private static Optional<NetworkDirection> distClient() { return Optional.of(NetworkDirection.PLAY_TO_CLIENT); }
    private static Optional<NetworkDirection> distServer() { return Optional.of(NetworkDirection.PLAY_TO_SERVER); }
}
