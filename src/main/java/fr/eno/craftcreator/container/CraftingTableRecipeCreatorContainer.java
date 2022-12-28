package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.VanillaCommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.vanilla.CraftingTableRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class CraftingTableRecipeCreatorContainer extends VanillaCommonContainer
{
	public final CraftingTableRecipeCreatorTile tile;

	public CraftingTableRecipeCreatorContainer(int containerId, Inventory inventory, FriendlyByteBuf packet)
	{
		super(InitContainers.CRAFTING_TABLE_RECIPE_CREATOR.get(), containerId);
		this.tile = (CraftingTableRecipeCreatorTile) inventory.player.level.getBlockEntity(packet.readBlockPos());
		int index = 0;
		
		for (int x = 0; x < 3; ++x)
		{
			for (int y = 0; y < 3; ++y)
			{
				this.addSlot(new SimpleSlotItemHandler(tile, index++, 30 + y * 18, 17 + x * 18));
			}
		}
		
		this.addSlot(new SimpleSlotItemHandler(tile, index, 124, 35));
		
		this.bindPlayerInventory(inventory);
	}
}