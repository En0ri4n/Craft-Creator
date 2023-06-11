package fr.eno.craftcreator.handler;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.utils.CommonUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class CommonHandler
{
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        ITextComponent issueMsg = CommonUtils.createComponentUrlOpener(References.getTranslate("message.join_issue"), "https://github.com/En0ri4n/Craft-Creator/issues");

        CommonUtils.sendMessage(event.getPlayer(), References.getTranslate("message.join", new TranslationTextComponent(ClientUtils.KEY_OPEN_RECIPES_MENU.getKey().getDisplayName().getString()).getString(), new TranslationTextComponent(ClientUtils.KEY_OPEN_TUTORIAL.getKey().getDisplayName().getString()).getString()));
        CommonUtils.sendMessage(event.getPlayer(), issueMsg);
    }
}
