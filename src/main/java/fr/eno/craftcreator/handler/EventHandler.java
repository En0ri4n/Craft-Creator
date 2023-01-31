package fr.eno.craftcreator.handler;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.screen.ModSelectionScreen;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class EventHandler
{
    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent e)
    {
        if(ClientUtils.KEY_OPEN_RECIPES_MENU.isKeyDown() && ClientUtils.getCurrentScreen() == null)
            ClientUtils.openScreen(new ModSelectionScreen());
    }

    @SubscribeEvent
    public static void onPlayerJoin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event)
    {
        ITextComponent issueMsg = Utils.createComponentUrlOpener(References.getTranslate("message.join_issue"), "https://github.com/En0ri4n/Craft-Creator/issues");

        ClientUtils.sendMessage(event.getPlayer(), References.getTranslate("message.join", new TranslationTextComponent(ClientUtils.KEY_OPEN_RECIPES_MENU.getKey().getTranslationKey()).getString()));
        ClientUtils.sendMessage(event.getPlayer(), issueMsg);
    }
}
