package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.tileentity.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.server.*;
import net.minecraftforge.fml.network.*;

import java.util.function.*;

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
