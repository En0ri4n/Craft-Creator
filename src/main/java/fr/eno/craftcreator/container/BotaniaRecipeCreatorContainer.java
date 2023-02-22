package fr.eno.craftcreator.container;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class BotaniaRecipeCreatorContainer extends CommonContainer
{
    public BotaniaRecipeCreatorContainer(int containerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.BOTANIA_RECIPE_CREATOR.get(), containerId, inventory, packet);

        addSlots(SlotHelper.BOTANIA_SLOTS);

        this.bindPlayerInventory(inventory);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.BOTANIA;
    }
    
    @Override
    public int getContainerSize()
    {
        return SlotHelper.BOTANIA_SLOTS_SIZE;
    }
}
