package fr.eno.craftcreator.test;

import com.mojang.blaze3d.platform.InputConstants;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.ModSelectionScreen;
import net.minecraft.client.Minecraft;
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
        if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_K))
        {
            if(Minecraft.getInstance().screen == null)
                Minecraft.getInstance().setScreen(new ModSelectionScreen());
        }
    }
}
