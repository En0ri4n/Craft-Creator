package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.VanillaCommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.vanilla.SmithingTableRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class SmithingTableRecipeCreatorContainer extends VanillaCommonContainer
{
	public SmithingTableRecipeCreatorContainer(int windowId, Inventory playerInventory, FriendlyByteBuf packet)
	{
		super(InitContainers.SMITHING_TABLE_RECIPE_CREATOR.get(), windowId);
		SmithingTableRecipeCreatorTile tile = (SmithingTableRecipeCreatorTile) playerInventory.player.level.getBlockEntity(packet.readBlockPos());
		int index = 0;

		this.addSlot(new SimpleSlotItemHandler(tile, index++, 27, 47));
		this.addSlot(new SimpleSlotItemHandler(tile, index++, 76, 47));
		this.addSlot(new SimpleSlotItemHandler(tile, index, 134, 47));
		
		this.bindPlayerInventory(playerInventory);
	}
}