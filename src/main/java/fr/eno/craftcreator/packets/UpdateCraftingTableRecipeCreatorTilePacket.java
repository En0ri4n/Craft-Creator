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

public class UpdateCraftingTableRecipeCreatorTilePacket
{
    private final BlockPos pos;
    private final boolean shapedRecipe;
    private final Map<Integer, ResourceLocation> taggedSlots;

    public UpdateCraftingTableRecipeCreatorTilePacket(BlockPos pos, boolean shapedRecipe, Map<Integer, ResourceLocation> taggedSlots)
    {
        this.pos = pos;
        this.shapedRecipe = shapedRecipe;
        this.taggedSlots = taggedSlots;
    }

    public static void encode(UpdateCraftingTableRecipeCreatorTilePacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeBoolean(msg.shapedRecipe);
        packetBuffer.writeInt(msg.taggedSlots.size());

        for(Integer slot : msg.taggedSlots.keySet())
        {
            packetBuffer.writeInt(slot);
            packetBuffer.writeResourceLocation(msg.taggedSlots.get(slot));
        }
    }

    public static UpdateCraftingTableRecipeCreatorTilePacket decode(PacketBuffer packetBuffer)
    {
        BlockPos pos = packetBuffer.readBlockPos();
        boolean shapedRecipe = packetBuffer.readBoolean();
        Map<Integer, ResourceLocation> taggedSlots = new HashMap<>();
        int size = packetBuffer.readInt();

        for(int i = 0; i < size; i++)
        {
            taggedSlots.put(packetBuffer.readInt(), packetBuffer.readResourceLocation());
        }

        return new UpdateCraftingTableRecipeCreatorTilePacket(pos, shapedRecipe, taggedSlots);
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
                tile.setTaggedSlots(msg.taggedSlots);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
