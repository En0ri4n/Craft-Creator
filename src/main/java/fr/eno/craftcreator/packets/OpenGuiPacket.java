package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.Objects;
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

    public static void encode(OpenGuiPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeUUID(msg.uuid);
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static OpenGuiPacket decode(FriendlyByteBuf packetBuffer)
    {
        UUID uuid = packetBuffer.readUUID();
        BlockPos pos = packetBuffer.readBlockPos();

        return new OpenGuiPacket(uuid, pos);
    }

    public static class ServerHandler
    {
        public static void handle(OpenGuiPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerLevel world = ctx.get().getSender().getLevel();

            BlockEntity tileEntity = world.getBlockEntity(msg.pos);

            if(tileEntity instanceof BotaniaRecipeCreatorTile tile)
            {
                NetworkHooks.openGui(Objects.requireNonNull(ctx.get().getSender()), tile, msg.pos);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
