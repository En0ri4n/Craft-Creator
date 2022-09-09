package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.screen.BotaniaRecipeCreatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetBotaniaRecipeCreatorScreenIndexClientPacket
{
    private final int index;

    public SetBotaniaRecipeCreatorScreenIndexClientPacket(int index)
    {
        this.index = index;
    }

    public static void encode(SetBotaniaRecipeCreatorScreenIndexClientPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeInt(msg.index);
    }

    public static SetBotaniaRecipeCreatorScreenIndexClientPacket decode(PacketBuffer packetBuffer)
    {
        int index = packetBuffer.readInt();

        return new SetBotaniaRecipeCreatorScreenIndexClientPacket(index);
    }

    public static void clientHandle(SetBotaniaRecipeCreatorScreenIndexClientPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        Screen screen = Minecraft.getInstance().currentScreen;

        if(screen instanceof BotaniaRecipeCreatorScreen)
        {
            BotaniaRecipeCreatorScreen botaniaRecipeCreatorScreen = (BotaniaRecipeCreatorScreen) screen;
            botaniaRecipeCreatorScreen.setCurrentScreenIndex(msg.index);
        }

        ctx.get().setPacketHandled(true);
    }
}
