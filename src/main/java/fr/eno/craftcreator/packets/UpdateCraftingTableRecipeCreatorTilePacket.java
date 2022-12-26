package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.vanilla.CraftingTableRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateCraftingTableRecipeCreatorTilePacket
{
    private final BlockPos pos;
    private final boolean shapedRecipe;

    public UpdateCraftingTableRecipeCreatorTilePacket(BlockPos pos, boolean shapedRecipe)
    {
        this.pos = pos;
        this.shapedRecipe = shapedRecipe;
    }

    public static void encode(UpdateCraftingTableRecipeCreatorTilePacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeBoolean(msg.shapedRecipe);
    }

    public static UpdateCraftingTableRecipeCreatorTilePacket decode(FriendlyByteBuf packetBuffer)
    {
        BlockPos pos = packetBuffer.readBlockPos();
        boolean shapedRecipe = packetBuffer.readBoolean();

        return new UpdateCraftingTableRecipeCreatorTilePacket(pos, shapedRecipe);
    }

    public static class ServerHandler
    {
        public static void handle(UpdateCraftingTableRecipeCreatorTilePacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerLevel world = ctx.get().getSender().getLevel();

            BlockEntity tileEntity = world.getBlockEntity(msg.pos);

            if(tileEntity instanceof CraftingTableRecipeCreatorTile tile)
            {
                tile.setShapedRecipe(msg.shapedRecipe);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
