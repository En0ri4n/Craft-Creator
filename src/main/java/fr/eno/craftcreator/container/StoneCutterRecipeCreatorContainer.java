package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.StoneCutterRecipeCreatorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

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