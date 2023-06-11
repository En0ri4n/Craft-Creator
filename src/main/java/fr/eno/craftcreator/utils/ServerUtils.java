package fr.eno.craftcreator.utils;

import com.mojang.brigadier.context.CommandContext;
import fr.eno.craftcreator.container.base.CommonContainer;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author En0ri4n <br>
 * <p>
 * This class contains some useful methods for the server.
 */
public class ServerUtils
{
    /**
     * Get the container from the context (server)<br>
     * This method must be called after checking that the container is an instance of {@link CommonContainer}
     *
     * @param ctx The context
     * @return The container casted to {@link CommonContainer}
     */
    public static CommonContainer getContainer(Supplier<NetworkEvent.Context> ctx)
    {
        return (CommonContainer) ServerUtils.getServerPlayer(ctx).containerMenu;
    }

    /**
     * Get the server player from the context
     *
     * @param ctx The context
     * @return The server player
     * @see NetworkEvent.Context#getSender()
     */
    public static ServerPlayerEntity getServerPlayer(Supplier<NetworkEvent.Context> ctx)
    {
        return ctx.get().getSender();
    }

    /**
     * Get the server level from the context
     * @param ctx The context
     * @return The server level
     *
     * @see ServerPlayerEntity#getLevel()
     */
    public static ServerWorld getServerLevel(Supplier<NetworkEvent.Context> ctx)
    {
        return getServerPlayer(ctx).getLevel();
    }

    /**
     * Get the block entity from the context and the position
     * @param ctx The context
     * @param pos The position
     * @return The block entity
     * @see ServerWorld#getBlockEntity(BlockPos)
     */
    public static TileEntity getBlockEntity(Supplier<NetworkEvent.Context> ctx, BlockPos pos)
    {
        return getServerLevel(ctx).getBlockEntity(pos);
    }

    /**
     * Perform a command in a specific context (server)
     * @param ctx The context
     * @param command The command
     */
    public static void doCommand(CommandContext<CommandSource> ctx, String command)
    {
        ctx.getSource().getServer().getCommands().performCommand(ctx.getSource(), command);
    }
}
