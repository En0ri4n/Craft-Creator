package fr.eno.craftcreator.handler;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.screen.RecipeManagerScreen;
import fr.eno.craftcreator.screen.TutorialScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
            ClientUtils.openScreen(new RecipeManagerScreen());

        if(ClientUtils.KEY_OPEN_TUTORIAL.isDown() && ClientUtils.getCurrentScreen() == null)
            ClientUtils.openScreen(new TutorialScreen());
    }

    @SubscribeEvent
    public static void onPlayerJoin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event)
    {
        MutableComponent issueMsg = CommonUtils.createComponentUrlOpener(References.getTranslate("message.join_issue"), "https://github.com/En0ri4n/Craft-Creator/issues");

        ClientUtils.sendMessage(event.getPlayer(), References.getTranslate("message.join", new TranslatableComponent(ClientUtils.KEY_OPEN_RECIPES_MENU.getKey().getDisplayName().getString()).getString(), new TranslatableComponent(ClientUtils.KEY_OPEN_TUTORIAL.getKey().getDisplayName().getString()).getString()));
        ClientUtils.sendMessage(event.getPlayer(), issueMsg);
    }
}
