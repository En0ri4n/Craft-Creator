package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.server.*;
import net.minecraftforge.fml.network.*;

import java.util.function.*;

public class UpdateCraftingTableRecipeCreatorTilePacket
{
    private final BlockPos pos;
    private final boolean shapedRecipe;

    public UpdateCraftingTableRecipeCreatorTilePacket(BlockPos pos, boolean shapedRecipe)
    {
        this.pos = pos;
        this.shapedRecipe = shapedRecipe;
    }

    public static void encode(UpdateCraftingTableRecipeCreatorTilePacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeBoolean(msg.shapedRecipe);
    }

    public static UpdateCraftingTableRecipeCreatorTilePacket decode(PacketBuffer packetBuffer)
    {
        BlockPos pos = packetBuffer.readBlockPos();
        boolean shapedRecipe = packetBuffer.readBoolean();

        return new UpdateCraftingTableRecipeCreatorTilePacket(pos, shapedRecipe);
    }

    public static class ServerHandler
    {
        public static void handle(UpdateCraftingTableRecipeCreatorTilePacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerWorld world = ctx.get().getSender().getServerWorld();

            TileEntity tileEntity = world.getTileEntity(msg.pos);

            if(tileEntity instanceof CraftingTableRecipeCreatorTile)
            {
                CraftingTableRecipeCreatorTile tile = (CraftingTableRecipeCreatorTile) tileEntity;
                tile.setShapedRecipe(msg.shapedRecipe);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
