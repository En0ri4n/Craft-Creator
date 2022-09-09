package fr.eno.craftcreator.test;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.ModSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class EventTest
{
    @SubscribeEvent
    public static void onKey(TickEvent.ClientTickEvent e)
    {
        if(InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_K))
        {
            if(Minecraft.getInstance().currentScreen == null)
                Minecraft.getInstance().displayGuiScreen(new ModSelectionScreen());
        }
    }
}
