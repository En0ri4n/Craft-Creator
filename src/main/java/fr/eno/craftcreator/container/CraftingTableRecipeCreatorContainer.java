package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class CraftingTableRecipeCreatorContainer extends CommonContainer
{
    public CraftingTableRecipeCreatorContainer(int containerId, Inventory inventory, FriendlyByteBuf packet)
    {
        super(InitContainers.CRAFTING_TABLE_RECIPE_CREATOR.get(), containerId, inventory, packet);

        for(int i = 0; i < SlotHelper.CRAFTING_TABLE_SLOTS_SIZE; ++i)
        {
            this.addSlot(new SimpleSlotItemHandler(this.tile, i, SlotHelper.CRAFTING_TABLE_SLOTS.get(i).getxPos(), SlotHelper.CRAFTING_TABLE_SLOTS.get(i).getyPos()));
        }

        this.bindPlayerInventory(inventory);

        activeSlots(true);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.MINECRAFT;
    }
}