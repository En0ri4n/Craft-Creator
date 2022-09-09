package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.TaggeableInventoryContainerTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

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

    public static void encode(UpdateTaggeableContainerRecipeCreatorTilePacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeInt(msg.taggedSlots.size());

        for(Integer slot : msg.taggedSlots.keySet())
        {
            packetBuffer.writeInt(slot);
            packetBuffer.writeResourceLocation(msg.taggedSlots.get(slot));
        }
    }

    public static UpdateTaggeableContainerRecipeCreatorTilePacket decode(PacketBuffer packetBuffer)
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
            ServerWorld world = ctx.get().getSender().getServerWorld();

            TileEntity tileEntity = world.getTileEntity(msg.pos);

            if(tileEntity instanceof TaggeableInventoryContainerTileEntity)
            {
                TaggeableInventoryContainerTileEntity tile = (TaggeableInventoryContainerTileEntity) tileEntity;
                tile.setTaggedSlots(msg.taggedSlots);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
