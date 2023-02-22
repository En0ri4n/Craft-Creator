package fr.eno.craftcreator.container;


import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class ThermalRecipeCreatorContainer extends CommonContainer
{
    public ThermalRecipeCreatorContainer(int containerId, Inventory inventory, FriendlyByteBuf packet)
    {
        super(InitContainers.THERMAL_RECIPE_CREATOR.get(), containerId, inventory, packet);

        addSlots(SlotHelper.THERMAL_SLOTS);

        this.bindInventory(inventory, 60, 90);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.THERMAL;
    }
    
    @Override
    public int getContainerSize()
    {
        return SlotHelper.THERMAL_SLOTS_SIZE;
    }
}
