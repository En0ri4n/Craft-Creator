package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.SmithingTableRecipeCreatorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class SmithingTableRecipeCreatorContainer extends CommonContainer
{
	public SmithingTableRecipeCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
	{
		super(InitContainers.SMITHING_TABLE_RECIPE_CREATOR.get(), windowId, 10);
		SmithingTableRecipeCreatorTile tile = (SmithingTableRecipeCreatorTile) playerInventory.player.world.getTileEntity(packet.readBlockPos());
		int index = 0;

		this.addSlot(new Slot(tile, index++, 124, 35));
		this.addSlot(new Slot(tile, index++, 150, 35));
		this.addSlot(new Slot(tile, index++, 164, 35));
		
		this.bindPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return true;
	}
}