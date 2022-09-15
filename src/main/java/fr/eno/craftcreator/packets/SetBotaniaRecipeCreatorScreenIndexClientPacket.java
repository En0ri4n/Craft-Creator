package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.screen.BotaniaRecipeCreatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetBotaniaRecipeCreatorScreenIndexClientPacket
{
    private final int index;

    public SetBotaniaRecipeCreatorScreenIndexClientPacket(int index)
    {
        this.index = index;
    }

    public static void encode(SetBotaniaRecipeCreatorScreenIndexClientPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeInt(msg.index);
    }

    public static SetBotaniaRecipeCreatorScreenIndexClientPacket decode(FriendlyByteBuf packetBuffer)
    {
        int index = packetBuffer.readInt();

        return new SetBotaniaRecipeCreatorScreenIndexClientPacket(index);
    }

    public static void clientHandle(SetBotaniaRecipeCreatorScreenIndexClientPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        Screen screen = Minecraft.getInstance().screen;

        if(screen instanceof BotaniaRecipeCreatorScreen)
        {
            BotaniaRecipeCreatorScreen botaniaRecipeCreatorScreen = (BotaniaRecipeCreatorScreen) screen;
            botaniaRecipeCreatorScreen.setCurrentScreenIndex(msg.index);
        }

        ctx.get().setPacketHandled(true);
    }
}
