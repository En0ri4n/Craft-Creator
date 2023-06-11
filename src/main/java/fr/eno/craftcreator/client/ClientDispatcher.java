package fr.eno.craftcreator.client;

import fr.eno.craftcreator.client.handler.ClientHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;

/**
 * This class is used to register the client handler only on the client side.
 */
public class ClientDispatcher implements DistExecutor.SafeRunnable
{
    private static final ClientDispatcher INSTANCE = new ClientDispatcher();

    @Override
    public void run()
    {
        MinecraftForge.EVENT_BUS.register(new ClientHandler());
    }

    public static ClientDispatcher get()
    {
        return INSTANCE;
    }
}
