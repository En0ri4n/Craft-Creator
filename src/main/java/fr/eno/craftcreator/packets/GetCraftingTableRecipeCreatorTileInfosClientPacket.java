package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.screen.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.network.*;

import java.util.*;
import java.util.function.*;

public class GetCraftingTableRecipeCreatorTileInfosClientPacket
{
    private final int windowId;
    private final boolean shapedRecipe;
    private final Map<Integer, ResourceLocation> taggedSlots;

    public GetCraftingTableRecipeCreatorTileInfosClientPacket(int windowId, boolean shapedRecipe, Map<Integer, ResourceLocation> taggedSlots)
    {
        this.windowId = windowId;
        this.shapedRecipe = shapedRecipe;
        this.taggedSlots = taggedSlots;
    }

    public static void encode(GetCraftingTableRecipeCreatorTileInfosClientPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeInt(msg.windowId);
        packetBuffer.writeBoolean(msg.shapedRecipe);
        packetBuffer.writeInt(msg.taggedSlots.size());

        for(Integer slot : msg.taggedSlots.keySet())
        {
            packetBuffer.writeInt(slot);
            packetBuffer.writeResourceLocation(msg.taggedSlots.get(slot));
        }
    }

    public static GetCraftingTableRecipeCreatorTileInfosClientPacket decode(PacketBuffer packetBuffer)
    {
        int windowId = packetBuffer.readInt();
        boolean shapedRecipe = packetBuffer.readBoolean();
        Map<Integer, ResourceLocation> taggedSlots = new HashMap<>();
        int size = packetBuffer.readInt();

        for(int i = 0; i < size; i++)
        {
            taggedSlots.put(packetBuffer.readInt(), packetBuffer.readResourceLocation());
        }

        return new GetCraftingTableRecipeCreatorTileInfosClientPacket(windowId, shapedRecipe, taggedSlots);
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
                    screen.setInfos(msg.shapedRecipe, msg.taggedSlots);
                }
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
