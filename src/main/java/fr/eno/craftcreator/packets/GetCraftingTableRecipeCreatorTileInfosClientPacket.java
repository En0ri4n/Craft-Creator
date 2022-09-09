package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.screen.CraftingTableRecipeCreatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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

    public static void encode(GetCraftingTableRecipeCreatorTileInfosClientPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeInt(msg.windowId);
        packetBuffer.writeBoolean(msg.shapedRecipe);
    }

    public static GetCraftingTableRecipeCreatorTileInfosClientPacket decode(PacketBuffer packetBuffer)
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

            if(mc.currentScreen instanceof CraftingTableRecipeCreatorScreen)
            {
                CraftingTableRecipeCreatorScreen screen = (CraftingTableRecipeCreatorScreen) mc.currentScreen;

                if(screen.getContainer().windowId == msg.windowId)
                {
                    screen.setShaped(msg.shapedRecipe);
                }
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
