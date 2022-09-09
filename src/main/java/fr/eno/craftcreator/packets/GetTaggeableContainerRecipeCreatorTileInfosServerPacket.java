package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.TaggeableInventoryContainerTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class GetTaggeableContainerRecipeCreatorTileInfosServerPacket
{
    private final BlockPos pos;
    private final int windowId;

    public GetTaggeableContainerRecipeCreatorTileInfosServerPacket(BlockPos pos, int windowId)
    {
        this.pos = pos;
        this.windowId = windowId;
    }

    public static void encode(GetTaggeableContainerRecipeCreatorTileInfosServerPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeInt(msg.windowId);
    }

    public static GetTaggeableContainerRecipeCreatorTileInfosServerPacket decode(PacketBuffer packetBuffer)
    {
        return new GetTaggeableContainerRecipeCreatorTileInfosServerPacket(packetBuffer.readBlockPos(), packetBuffer.readInt());
    }

    public static class ServerHandler
    {
        public static void handle(GetTaggeableContainerRecipeCreatorTileInfosServerPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerWorld world = ctx.get().getSender().getServerWorld();

            TileEntity tileEntity = world.getTileEntity(msg.pos);

            if(tileEntity instanceof TaggeableInventoryContainerTileEntity)
            {
                TaggeableInventoryContainerTileEntity tile = (TaggeableInventoryContainerTileEntity) tileEntity;
                InitPackets.getNetWork().send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new GetTaggeableContainerRecipeCreatorTileInfosClientPacket(msg.windowId, tile.getTaggedSlots()));
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
