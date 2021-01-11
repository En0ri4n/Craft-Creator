package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.CraftingTableRecipeCreatorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class CraftingTableRecipeCreatorContainer extends CommonContainer
{
	public CraftingTableRecipeCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
	{
		super(InitContainers.CRAFTING_TABLE_RECIPE_CREATOR.get(), windowId, 10);
		CraftingTableRecipeCreatorTile tile = (CraftingTableRecipeCreatorTile) playerInventory.player.world.getTileEntity(packet.readBlockPos());
		int index = 0;
		
		for (int x = 0; x < 3; ++x)
		{
			for (int y = 0; y < 3; ++y)
			{
				this.addSlot(new Slot(tile, index++, 30 + y * 18, 17 + x * 18));
			}
		}
		
		this.addSlot(new Slot(tile, index++, 124, 35));
		
		this.bindPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return true;
	}
}