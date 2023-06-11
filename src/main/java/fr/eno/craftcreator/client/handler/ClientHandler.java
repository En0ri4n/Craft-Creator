package fr.eno.craftcreator.client.handler;

import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.screen.RecipeManagerScreen;
import fr.eno.craftcreator.client.screen.TutorialScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientHandler
{
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e)
    {
        if(ClientUtils.KEY_OPEN_RECIPES_MENU.isDown() && ClientUtils.getCurrentScreen() == null)
            ClientUtils.openScreen(new RecipeManagerScreen());

        if(ClientUtils.KEY_OPEN_TUTORIAL.isDown() && ClientUtils.getCurrentScreen() == null)
            ClientUtils.openScreen(new TutorialScreen());
    }
}
