package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.server.*;
import net.minecraftforge.fml.network.*;

import java.util.*;
import java.util.function.*;

public class OpenGuiPacket
{
    private final UUID uuid;
    private final BlockPos pos;

    public OpenGuiPacket(UUID uuid, BlockPos pos)
    {
        this.uuid = uuid;
        this.pos = pos;
    }

    public static void encode(OpenGuiPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeUniqueId(msg.uuid);
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static OpenGuiPacket decode(PacketBuffer packetBuffer)
    {
        UUID uuid = packetBuffer.readUniqueId();
        BlockPos pos = packetBuffer.readBlockPos();

        return new OpenGuiPacket(uuid, pos);
    }

    public static class ServerHandler
    {
        public static void handle(OpenGuiPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerWorld world = ctx.get().getSender().getServerWorld();

            TileEntity tileEntity = world.getTileEntity(msg.pos);

            if(tileEntity instanceof BotaniaRecipeCreatorTile)
            {
                BotaniaRecipeCreatorTile tile = (BotaniaRecipeCreatorTile) tileEntity;
                NetworkHooks.openGui(ctx.get().getSender(), tile, msg.pos);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
