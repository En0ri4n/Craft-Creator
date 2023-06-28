package fr.eno.craftcreator.container;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class CreateRecipeCreatorContainer extends CommonContainer
{
    public CreateRecipeCreatorContainer(int pContainerId, PlayerInventory inventory, PacketBuffer byteBuf)
    {
        super(InitContainers.CREATE_RECIPE_CREATOR.get(), pContainerId, inventory, byteBuf);

        bindPlayerInventory(inventory, 40, 90);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.CREATE;
    }

    @Override
    public int getContainerSize()
    {
        return SlotHelper.CREATE_SLOTS_SIZE;
    }
}
