package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.server.*;
import net.minecraftforge.fml.network.*;

import java.util.function.*;

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
