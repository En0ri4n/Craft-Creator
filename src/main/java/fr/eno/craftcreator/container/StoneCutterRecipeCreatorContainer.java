package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.tileentity.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraftforge.items.*;

import javax.annotation.*;

public class StoneCutterRecipeCreatorContainer extends CommonContainer
{
	public StoneCutterRecipeCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
	{
		super(InitContainers.STONE_CUTTER_RECIPE_CREATOR.get(), windowId, 10);
		StoneCutterRecipeCreatorTile tile = (StoneCutterRecipeCreatorTile) playerInventory.player.world.getTileEntity(packet.readBlockPos());
		int index = 0;

		this.addSlot(new SlotItemHandler(tile, index++, 39, 33));
		this.addSlot(new SlotItemHandler(tile, index, 114, 33));
		
		this.bindPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity playerIn)
	{
		return true;
	}
}