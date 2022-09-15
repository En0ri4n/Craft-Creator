package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class GetBotaniaRecipeCreatorScreenIndexPacket
{
    private final BlockPos pos;

    public GetBotaniaRecipeCreatorScreenIndexPacket(BlockPos pos)
    {
        this.pos = pos;
    }

    public static void encode(GetBotaniaRecipeCreatorScreenIndexPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static GetBotaniaRecipeCreatorScreenIndexPacket decode(FriendlyByteBuf packetBuffer)
    {
        BlockPos pos = packetBuffer.readBlockPos();

        return new GetBotaniaRecipeCreatorScreenIndexPacket(pos);
    }

    public static class ServerHandler
    {
        public static void handle(GetBotaniaRecipeCreatorScreenIndexPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerLevel world = ctx.get().getSender().getLevel();

            BlockEntity tileentity = world.getBlockEntity(msg.pos);

            if(tileentity instanceof BotaniaRecipeCreatorTile)
            {
                BotaniaRecipeCreatorTile tile = (BotaniaRecipeCreatorTile) tileentity;

                InitPackets.getNetWork().send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new SetBotaniaRecipeCreatorScreenIndexClientPacket(tile.getScreenIndex()));            }

            ctx.get().setPacketHandled(true);
        }
    }
}
