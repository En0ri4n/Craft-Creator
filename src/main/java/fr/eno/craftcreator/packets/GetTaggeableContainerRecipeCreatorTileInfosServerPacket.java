package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.vanilla.TaggeableInventoryContainerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

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

    public static void encode(GetTaggeableContainerRecipeCreatorTileInfosServerPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeInt(msg.windowId);
    }

    public static GetTaggeableContainerRecipeCreatorTileInfosServerPacket decode(FriendlyByteBuf packetBuffer)
    {
        return new GetTaggeableContainerRecipeCreatorTileInfosServerPacket(packetBuffer.readBlockPos(), packetBuffer.readInt());
    }

    public static class ServerHandler
    {
        public static void handle(GetTaggeableContainerRecipeCreatorTileInfosServerPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerLevel world = ctx.get().getSender().getLevel();

            BlockEntity tileEntity = world.getBlockEntity(msg.pos);

            if(tileEntity instanceof TaggeableInventoryContainerTileEntity tile)
            {
                InitPackets.getNetWork().send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new GetTaggeableContainerRecipeCreatorTileInfosClientPacket(msg.windowId, tile.getTaggedSlots()));
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
