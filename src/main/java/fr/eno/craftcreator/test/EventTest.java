package fr.eno.craftcreator.test;

import com.mojang.blaze3d.platform.InputConstants;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.ModSelectionScreen;
import fr.eno.craftcreator.utils.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class EventTest
{
    @SubscribeEvent
    public static void onKey(TickEvent.ClientTickEvent e)
    {
        if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_K))
        {
            if(Minecraft.getInstance().screen == null)
                Minecraft.getInstance().setScreen(new ModSelectionScreen());
        }
    }

    @SubscribeEvent
    public static void onClick(PlayerInteractEvent event)
    {
        if(event.getPlayer().isCrouching() && event.getHand() == InteractionHand.OFF_HAND && !event.getWorld().isClientSide)
        {
            if(event.getItemStack().getItem() instanceof BucketItem bucket)
            {
                System.out.println(bucket.getFluid().getRegistryName());

                onPlayerJoin(new PlayerEvent.PlayerLoggedInEvent(event.getPlayer()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getPlayer();

        MutableComponent issueMsg = Utilities.createComponentUrlOpener(References.getTranslate("world.on_join_message_issue"), "https://github.com/En0ri4n/Craft-Creator/issues");

        player.sendMessage(References.getTranslate("world.on_join_message"), player.getUUID());
        player.sendMessage(issueMsg, player.getUUID());
    }
}
