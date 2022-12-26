package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.MultiScreenRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class GetModRecipeCreatorScreenIndexPacket
{
    private final BlockPos pos;

    public GetModRecipeCreatorScreenIndexPacket(BlockPos pos)
    {
        this.pos = pos;
    }

    public static void encode(GetModRecipeCreatorScreenIndexPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static GetModRecipeCreatorScreenIndexPacket decode(FriendlyByteBuf packetBuffer)
    {
        BlockPos pos = packetBuffer.readBlockPos();

        return new GetModRecipeCreatorScreenIndexPacket(pos);
    }

    public static class ServerHandler
    {
        public static void handle(GetModRecipeCreatorScreenIndexPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerLevel world = ctx.get().getSender().getLevel();

            BlockEntity tileentity = world.getBlockEntity(msg.pos);

            if(tileentity instanceof MultiScreenRecipeCreatorTile tile)
            {
                InitPackets.getNetWork().send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new SetModRecipeCreatorScreenIndexClientPacket(tile.getScreenIndex()));
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
