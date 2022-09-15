package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.CraftingTableRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

public class CraftingTableRecipeCreatorContainer extends CommonContainer
{
	private final CraftingTableRecipeCreatorTile tile;

	public CraftingTableRecipeCreatorContainer(int containerId, Inventory inventory, FriendlyByteBuf packet)
	{
		super(InitContainers.CRAFTING_TABLE_RECIPE_CREATOR.get(), containerId);
		this.tile = (CraftingTableRecipeCreatorTile) inventory.player.level.getBlockEntity(packet.readBlockPos());
		int index = 0;
		
		for (int x = 0; x < 3; ++x)
		{
			for (int y = 0; y < 3; ++y)
			{
				this.addSlot(new SlotItemHandler(tile, index++, 30 + y * 18, 17 + x * 18));
			}
		}
		
		this.addSlot(new SlotItemHandler(tile, index, 124, 35));
		
		this.bindPlayerInventory(inventory);
	}

	public CraftingTableRecipeCreatorTile getTile()
	{
		return tile;
	}
}