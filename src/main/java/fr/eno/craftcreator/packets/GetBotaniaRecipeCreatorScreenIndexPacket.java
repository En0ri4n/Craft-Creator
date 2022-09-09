package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class GetBotaniaRecipeCreatorScreenIndexPacket
{
    private final BlockPos pos;

    public GetBotaniaRecipeCreatorScreenIndexPacket(BlockPos pos)
    {
        this.pos = pos;
    }

    public static void encode(GetBotaniaRecipeCreatorScreenIndexPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static GetBotaniaRecipeCreatorScreenIndexPacket decode(PacketBuffer packetBuffer)
    {
        BlockPos pos = packetBuffer.readBlockPos();

        return new GetBotaniaRecipeCreatorScreenIndexPacket(pos);
    }

    public static class ServerHandler
    {
        public static void handle(GetBotaniaRecipeCreatorScreenIndexPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerWorld world = ctx.get().getSender().getServerWorld();

            TileEntity tileentity = world.getTileEntity(msg.pos);

            if(tileentity instanceof BotaniaRecipeCreatorTile)
            {
                BotaniaRecipeCreatorTile tile = (BotaniaRecipeCreatorTile) tileentity;

                InitPackets.getNetWork().send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new SetBotaniaRecipeCreatorScreenIndexClientPacket(tile.getScreenIndex()));            }

            ctx.get().setPacketHandled(true);
        }
    }
}
