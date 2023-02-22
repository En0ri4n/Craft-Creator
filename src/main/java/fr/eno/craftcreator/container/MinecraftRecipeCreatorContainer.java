package fr.eno.craftcreator.container;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class MinecraftRecipeCreatorContainer extends CommonContainer
{
    public MinecraftRecipeCreatorContainer(int containerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.MINECRAFT_RECIPE_CREATOR.get(), containerId, inventory, packet);

        addSlots(SlotHelper.MINECRAFT_SLOTS);

        this.bindPlayerInventory(inventory);

        activeSlots(true);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.MINECRAFT;
    }
    
    @Override
    public int getContainerSize()
    {
        return SlotHelper.MINECRAFT_SLOTS_SIZE;
    }
}