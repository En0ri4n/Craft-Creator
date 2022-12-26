package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.vanilla.TaggeableInventoryContainerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UpdateTaggeableContainerRecipeCreatorTilePacket
{
    private final BlockPos pos;
    private final Map<Integer, ResourceLocation> taggedSlots;

    public UpdateTaggeableContainerRecipeCreatorTilePacket(BlockPos pos, Map<Integer, ResourceLocation> taggedSlots)
    {
        this.pos = pos;
        this.taggedSlots = taggedSlots;
    }

    public static void encode(UpdateTaggeableContainerRecipeCreatorTilePacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeInt(msg.taggedSlots.size());

        for(Integer slot : msg.taggedSlots.keySet())
        {
            packetBuffer.writeInt(slot);
            packetBuffer.writeResourceLocation(msg.taggedSlots.get(slot));
        }
    }

    public static UpdateTaggeableContainerRecipeCreatorTilePacket decode(FriendlyByteBuf packetBuffer)
    {
        BlockPos pos = packetBuffer.readBlockPos();
        Map<Integer, ResourceLocation> taggedSlots = new HashMap<>();
        int size = packetBuffer.readInt();

        for(int i = 0; i < size; i++)
        {
            taggedSlots.put(packetBuffer.readInt(), packetBuffer.readResourceLocation());
        }

        return new UpdateTaggeableContainerRecipeCreatorTilePacket(pos, taggedSlots);
    }

    public static class ServerHandler
    {
        public static void handle(UpdateTaggeableContainerRecipeCreatorTilePacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerLevel world = ctx.get().getSender().getLevel();

            BlockEntity tileEntity = world.getBlockEntity(msg.pos);

            if(tileEntity instanceof TaggeableInventoryContainerTileEntity tile)
            {
                tile.setTaggedSlots(msg.taggedSlots);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
