package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class SmithingTableRecipeCreatorContainer extends CommonContainer
{
	public SmithingTableRecipeCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
	{
		super(InitContainers.SMITHING_TABLE_RECIPE_CREATOR.get(), windowId, playerInventory, packet);

		for(int i = 0; i < SlotHelper.SMITHING_TABLE_SLOTS_SIZE; i++)
		{
			this.addSlot(new SimpleSlotItemHandler(this.tile, i, SlotHelper.SMITHING_TABLE_SLOTS.get(i).getxPos(), SlotHelper.SMITHING_TABLE_SLOTS.get(i).getyPos()));
		}

		this.bindPlayerInventory(playerInventory);

		activeSlots(true);
	}

	@Override
	public SupportedMods getMod()
	{
		return SupportedMods.MINECRAFT;
	}
}