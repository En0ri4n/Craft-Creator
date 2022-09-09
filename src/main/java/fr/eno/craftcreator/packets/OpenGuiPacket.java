package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;
import java.util.function.Supplier;

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
