package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.screen.container.CraftingTableRecipeCreatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GetCraftingTableRecipeCreatorTileInfosClientPacket
{
    private final int windowId;
    private final boolean shapedRecipe;

    public GetCraftingTableRecipeCreatorTileInfosClientPacket(int windowId, boolean shapedRecipe)
    {
        this.windowId = windowId;
        this.shapedRecipe = shapedRecipe;
    }

    public static void encode(GetCraftingTableRecipeCreatorTileInfosClientPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeInt(msg.windowId);
        packetBuffer.writeBoolean(msg.shapedRecipe);
    }

    public static GetCraftingTableRecipeCreatorTileInfosClientPacket decode(FriendlyByteBuf packetBuffer)
    {
        int windowId = packetBuffer.readInt();
        boolean shapedRecipe = packetBuffer.readBoolean();

        return new GetCraftingTableRecipeCreatorTileInfosClientPacket(windowId, shapedRecipe);
    }

    public static class ClientHandler
    {
        public static void handle(GetCraftingTableRecipeCreatorTileInfosClientPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            Minecraft mc = Minecraft.getInstance();

            if(mc.screen instanceof CraftingTableRecipeCreatorScreen)
            {
                CraftingTableRecipeCreatorScreen screen = (CraftingTableRecipeCreatorScreen) mc.screen;

                if(screen.getMenu().containerId == msg.windowId)
                {
                    screen.setShaped(msg.shapedRecipe);
                }
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
