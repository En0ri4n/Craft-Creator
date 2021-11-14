package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.server.*;
import net.minecraftforge.fml.network.*;

import java.util.*;
import java.util.function.*;

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
