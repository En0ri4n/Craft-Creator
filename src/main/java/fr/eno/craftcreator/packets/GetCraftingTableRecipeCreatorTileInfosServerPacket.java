package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.CraftingTableRecipeCreatorTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class GetCraftingTableRecipeCreatorTileInfosServerPacket
{
    private final BlockPos pos;
    private final int windowId;

    public GetCraftingTableRecipeCreatorTileInfosServerPacket(BlockPos pos, int windowId)
    {
        this.pos = pos;
        this.windowId = windowId;
    }

    public static void encode(GetCraftingTableRecipeCreatorTileInfosServerPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeInt(msg.windowId);
    }

    public static GetCraftingTableRecipeCreatorTileInfosServerPacket decode(PacketBuffer packetBuffer)
    {
        return new GetCraftingTableRecipeCreatorTileInfosServerPacket(packetBuffer.readBlockPos(), packetBuffer.readInt());
    }

    public static class ServerHandler
    {
        public static void handle(GetCraftingTableRecipeCreatorTileInfosServerPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerWorld world = ctx.get().getSender().getServerWorld();

            TileEntity tileEntity = world.getTileEntity(msg.pos);

            if(tileEntity instanceof CraftingTableRecipeCreatorTile)
            {
                CraftingTableRecipeCreatorTile tile = (CraftingTableRecipeCreatorTile) tileEntity;
                InitPackets.getNetWork().send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new GetCraftingTableRecipeCreatorTileInfosClientPacket(msg.windowId, tile.isShapedRecipe()));
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
