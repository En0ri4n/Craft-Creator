package fr.eno.craftcreator.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author En0ri4n <br>
 *
 * Class with useful methods for server
 */
public class ServerUtils
{
    /**
     * Get the server player from the context
     *
     * @param ctx The context
     * @return The server player
     * @see NetworkEvent.Context#getSender()
     */
    public static ServerPlayer getServerPlayer(Supplier<NetworkEvent.Context> ctx)
    {
        return ctx.get().getSender();
    }

    /**
     * Get the server level from the context
     * @param ctx The context
     * @return The server level
     *
     * @see ServerPlayer#getLevel()
     */
    public static ServerLevel getServerLevel(Supplier<NetworkEvent.Context> ctx)
    {
        return getServerPlayer(ctx).getLevel();
    }

    /**
     * Get the block entity from the context and the position
     * @param ctx The context
     * @param pos The position
     * @return The block entity
     * @see ServerLevel#getBlockEntity(BlockPos)
     */
    public static BlockEntity getBlockEntity(Supplier<NetworkEvent.Context> ctx, BlockPos pos)
    {
        return getServerLevel(ctx).getBlockEntity(pos);
    }
}
