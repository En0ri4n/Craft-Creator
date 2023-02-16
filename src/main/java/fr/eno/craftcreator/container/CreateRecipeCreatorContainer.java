package fr.eno.craftcreator.container;


import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class CreateRecipeCreatorContainer extends CommonContainer
{
    public CreateRecipeCreatorContainer(int pContainerId, Inventory inventory, FriendlyByteBuf byteBuf)
    {
        super(InitContainers.CREATE_RECIPE_CREATOR.get(), pContainerId, inventory, byteBuf);
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
