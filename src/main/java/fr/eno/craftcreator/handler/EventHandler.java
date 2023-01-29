package fr.eno.craftcreator.handler;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.ModSelectionScreen;
import fr.eno.craftcreator.utils.ClientUtils;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class EventHandler
{
    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent e)
    {
        if(ClientUtils.KEY_OPEN_RECIPES_MENU.isDown() && ClientUtils.getCurrentScreen() == null)
            ClientUtils.openScreen(new ModSelectionScreen());
    }

    @SubscribeEvent
    public static void onPlayerJoin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getPlayer();

        MutableComponent issueMsg = Utils.createComponentUrlOpener(References.getTranslate("message.join_issue"), "https://github.com/En0ri4n/Craft-Creator/issues");

        player.sendMessage(References.getTranslate("message.join", new TranslatableComponent(ClientUtils.KEY_OPEN_RECIPES_MENU.getKey().getDisplayName().getString()).getString()), player.getUUID());
        player.sendMessage(issueMsg, player.getUUID());
    }
}
