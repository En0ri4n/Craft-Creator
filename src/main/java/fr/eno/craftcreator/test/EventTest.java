package fr.eno.craftcreator.test;

import fr.eno.craftcreator.*;
import fr.eno.craftcreator.screen.*;
import net.minecraft.client.*;
import net.minecraft.client.util.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import org.lwjgl.glfw.*;

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
