package fr.eno.craftcreator.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerUtils
{
    public static ServerPlayer getServerPlayer(Supplier<NetworkEvent.Context> ctx)
    {
        return ctx.get().getSender();
    }

    public static ServerLevel getServerLevel(Supplier<NetworkEvent.Context> ctx)
    {
        return getServerPlayer(ctx).getLevel();
    }

    public static BlockEntity getBlockEntity(Supplier<NetworkEvent.Context> ctx, BlockPos pos)
    {
        return getServerLevel(ctx).getBlockEntity(pos);
    }
}
