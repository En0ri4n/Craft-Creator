package fr.eno.craftcreator.test;

import cofh.core.item.FluidContainerItem;
import cofh.thermal.core.init.TCoreFluids;
import cofh.thermal.expansion.block.entity.machine.MachineBottlerTile;
import com.mojang.blaze3d.platform.InputConstants;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.ModSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BucketItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
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
            }
        }
    }
}
