package fr.eno.craftcreatorapi.init;

import fr.eno.craftcreatorapi.utils.Identifier;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class InitNetworkBase
{
    protected static final String PROTOCOL_VERSION = Integer.toString(1);
    private final Identifier networkId;
    private static int packetId;

    public void init()
    {
        registerPackets();
    }

    public abstract void registerPackets();
}
