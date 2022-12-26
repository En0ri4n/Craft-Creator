package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.MultiScreenRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetModRecipeCreatorScreenIndexServerPacket
{
    private final int index;
    private final BlockPos pos;

    public SetModRecipeCreatorScreenIndexServerPacket(int index, BlockPos pos)
    {
        this.index = index;
        this.pos = pos;
    }

    public static void encode(SetModRecipeCreatorScreenIndexServerPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeInt(msg.index);
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static SetModRecipeCreatorScreenIndexServerPacket decode(FriendlyByteBuf packetBuffer)
    {
        int index = packetBuffer.readInt();
        BlockPos pos = packetBuffer.readBlockPos();

        return new SetModRecipeCreatorScreenIndexServerPacket(index, pos);
    }

    public static void serverHandle(SetModRecipeCreatorScreenIndexServerPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ServerLevel world = ctx.get().getSender().getLevel();

        BlockEntity tileentity = world.getBlockEntity(msg.pos);

        if(tileentity instanceof MultiScreenRecipeCreatorTile tile)
        {
            tile.setScreenIndex(msg.index);
        }

        ctx.get().setPacketHandled(true);
    }
}
