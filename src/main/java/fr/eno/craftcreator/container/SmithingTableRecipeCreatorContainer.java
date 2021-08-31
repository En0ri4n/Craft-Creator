package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.tileentity.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraftforge.items.*;

import javax.annotation.*;

public class SmithingTableRecipeCreatorContainer extends CommonContainer
{
	public SmithingTableRecipeCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
	{
		super(InitContainers.SMITHING_TABLE_RECIPE_CREATOR.get(), windowId, 10);
		SmithingTableRecipeCreatorTile tile = (SmithingTableRecipeCreatorTile) playerInventory.player.world.getTileEntity(packet.readBlockPos());
		int index = 0;

		this.addSlot(new SlotItemHandler(tile, index++, 27, 47));
		this.addSlot(new SlotItemHandler(tile, index++, 76, 47));
		this.addSlot(new SlotItemHandler(tile, index, 134, 47));
		
		this.bindPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity playerIn)
	{
		return true;
	}
}