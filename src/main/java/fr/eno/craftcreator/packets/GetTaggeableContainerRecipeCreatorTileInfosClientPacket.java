package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.screen.container.TaggeableSlotsContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GetTaggeableContainerRecipeCreatorTileInfosClientPacket
{
    private final int windowId;
    private final Map<Integer, ResourceLocation> taggedSlots;

    public GetTaggeableContainerRecipeCreatorTileInfosClientPacket(int windowId, Map<Integer, ResourceLocation> taggedSlots)
    {
        this.windowId = windowId;
        this.taggedSlots = taggedSlots;
    }

    public static void encode(GetTaggeableContainerRecipeCreatorTileInfosClientPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeInt(msg.windowId);
        packetBuffer.writeInt(msg.taggedSlots.size());

        for(Integer slot : msg.taggedSlots.keySet())
        {
            packetBuffer.writeInt(slot);
            packetBuffer.writeResourceLocation(msg.taggedSlots.get(slot));
        }
    }

    public static GetTaggeableContainerRecipeCreatorTileInfosClientPacket decode(FriendlyByteBuf packetBuffer)
    {
        int windowId = packetBuffer.readInt();
        Map<Integer, ResourceLocation> taggedSlots = new HashMap<>();
        int size = packetBuffer.readInt();

        for(int i = 0; i < size; i++)
        {
            taggedSlots.put(packetBuffer.readInt(), packetBuffer.readResourceLocation());
        }

        return new GetTaggeableContainerRecipeCreatorTileInfosClientPacket(windowId, taggedSlots);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static class ClientHandler
    {
        public static void handle(GetTaggeableContainerRecipeCreatorTileInfosClientPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            Minecraft mc = Minecraft.getInstance();

            if(mc.screen instanceof TaggeableSlotsContainerScreen)
            {
                TaggeableSlotsContainerScreen screen = (TaggeableSlotsContainerScreen) mc.screen;

                if(screen.getMenu().containerId == msg.windowId)
                {
                    screen.setTaggedSlots(msg.taggedSlots);
                }
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
