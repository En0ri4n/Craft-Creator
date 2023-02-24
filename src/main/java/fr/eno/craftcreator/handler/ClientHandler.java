package fr.eno.craftcreator.handler;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.screen.RecipeManagerScreen;
import fr.eno.craftcreator.screen.TutorialScreen;
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
