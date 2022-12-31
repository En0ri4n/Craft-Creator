package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.screen.container.BotaniaRecipeCreatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetModRecipeCreatorScreenIndexClientPacket
{
    private final int index;

    public SetModRecipeCreatorScreenIndexClientPacket(int index)
    {
        this.index = index;
    }

    public static void encode(SetModRecipeCreatorScreenIndexClientPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeInt(msg.index);
    }

    public static SetModRecipeCreatorScreenIndexClientPacket decode(FriendlyByteBuf packetBuffer)
    {
        int index = packetBuffer.readInt();

        return new SetModRecipeCreatorScreenIndexClientPacket(index);
    }

    public static void clientHandle(SetModRecipeCreatorScreenIndexClientPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        Screen screen = Minecraft.getInstance().screen;

        if(screen instanceof BotaniaRecipeCreatorScreen botaniaRecipeCreatorScreen)
        {
            botaniaRecipeCreatorScreen.setCurrentScreenIndex(msg.index);
        }

        ctx.get().setPacketHandled(true);
    }
}
