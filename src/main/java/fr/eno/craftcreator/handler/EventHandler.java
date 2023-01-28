package fr.eno.craftcreator.handler;

import com.mojang.blaze3d.platform.InputConstants;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.ModSelectionScreen;
import fr.eno.craftcreator.utils.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class EventHandler
{
    @SubscribeEvent
    public static void onKey(TickEvent.ClientTickEvent e)
    {
        if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_K))
        {
            if(Minecraft.getInstance().screen == null)
                Minecraft.getInstance().setScreen(new ModSelectionScreen());
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getPlayer();

        MutableComponent issueMsg = Utilities.createComponentUrlOpener(References.getTranslate("message.join_issue"), "https://github.com/En0ri4n/Craft-Creator/issues");

        player.sendMessage(References.getTranslate("message.join"), player.getUUID());
        player.sendMessage(issueMsg, player.getUUID());
    }
}
