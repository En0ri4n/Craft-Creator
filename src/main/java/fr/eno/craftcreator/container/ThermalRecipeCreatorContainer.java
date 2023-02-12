package fr.eno.craftcreator.container;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.container.slot.DefinedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.slot.utils.DefinedPositionnedSlot;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ThermalRecipeCreatorContainer extends CommonContainer
{
    public ThermalRecipeCreatorContainer(int containerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.THERMAL_RECIPE_CREATOR.get(), containerId, inventory, packet);

        for(int i = 0; i < SlotHelper.THERMAL_SLOTS_SIZE; i++)
        {
            PositionnedSlot positionnedSlot = SlotHelper.THERMAL_SLOTS.get(i);

            int x = SlotHelper.THERMAL_SLOTS.get(i).getxPos();
            int y = SlotHelper.THERMAL_SLOTS.get(i).getyPos();

            if(positionnedSlot instanceof DefinedPositionnedSlot)
                this.addSlot(new DefinedSlot(tile, i, x, y, ((DefinedPositionnedSlot) positionnedSlot)::isItemValid));
            else
                this.addSlot(new SimpleSlotItemHandler(tile, i, x, y));
        }

        this.bindPlayerInventory(inventory, 60, 90);
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
