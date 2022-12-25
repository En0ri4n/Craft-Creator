package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.SmithingTableRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

public class SmithingTableRecipeCreatorContainer extends CommonContainer
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