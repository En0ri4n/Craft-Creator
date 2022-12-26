package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.vanilla.CraftingTableRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Objects;
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

    public static void encode(GetCraftingTableRecipeCreatorTileInfosServerPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeInt(msg.windowId);
    }

    public static GetCraftingTableRecipeCreatorTileInfosServerPacket decode(FriendlyByteBuf packetBuffer)
    {
        return new GetCraftingTableRecipeCreatorTileInfosServerPacket(packetBuffer.readBlockPos(), packetBuffer.readInt());
    }

    public static class ServerHandler
    {
        public static void handle(GetCraftingTableRecipeCreatorTileInfosServerPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerLevel world = Objects.requireNonNull(ctx.get().getSender()).getLevel();

            BlockEntity tileEntity = world.getBlockEntity(msg.pos);

            if(tileEntity instanceof CraftingTableRecipeCreatorTile tile)
            {
                InitPackets.getNetWork().send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new GetCraftingTableRecipeCreatorTileInfosClientPacket(msg.windowId, tile.isShapedRecipe()));
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
