package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.StoneCutterRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

public class StoneCutterRecipeCreatorContainer extends CommonContainer
{
	public StoneCutterRecipeCreatorContainer(int windowId, Inventory playerInventory, FriendlyByteBuf packet)
	{
		super(InitContainers.STONE_CUTTER_RECIPE_CREATOR.get(), windowId);
		StoneCutterRecipeCreatorTile tile = (StoneCutterRecipeCreatorTile) playerInventory.player.level.getBlockEntity(packet.readBlockPos());
		int index = 0;

		this.addSlot(new SlotItemHandler(tile, index++, 39, 33));
		this.addSlot(new SlotItemHandler(tile, index, 114, 33));
		
		this.bindPlayerInventory(playerInventory);
	}
}