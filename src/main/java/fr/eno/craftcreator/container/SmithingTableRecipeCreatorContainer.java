package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.SmithingTableRecipeCreatorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

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