package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetBotaniaRecipeCreatorScreenIndexServerPacket
{
    private final int index;
    private final BlockPos pos;

    public SetBotaniaRecipeCreatorScreenIndexServerPacket(int index, BlockPos pos)
    {
        this.index = index;
        this.pos = pos;
    }

    public static void encode(SetBotaniaRecipeCreatorScreenIndexServerPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeInt(msg.index);
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static SetBotaniaRecipeCreatorScreenIndexServerPacket decode(PacketBuffer packetBuffer)
    {
        int index = packetBuffer.readInt();
        BlockPos pos = packetBuffer.readBlockPos();

        return new SetBotaniaRecipeCreatorScreenIndexServerPacket(index, pos);
    }

    public static void serverHandle(SetBotaniaRecipeCreatorScreenIndexServerPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ServerWorld world = ctx.get().getSender().getServerWorld();

        TileEntity tileentity = world.getTileEntity(msg.pos);

        if(tileentity instanceof BotaniaRecipeCreatorTile)
        {
            BotaniaRecipeCreatorTile tile = (BotaniaRecipeCreatorTile) tileentity;
            tile.setScreenIndex(msg.index);
        }

        ctx.get().setPacketHandled(true);
    }
}
