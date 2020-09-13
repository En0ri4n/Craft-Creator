package fr.eno.craftcreator.container;

import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.tileentity.CraftCreatorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class CraftCreatorContainer extends CommonContainer
{
	public CraftCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
	{
		super(CraftCreator.ModRegistry.CRAFT_CREATOR_CONTAINER.get(), windowId, 10);
		CraftCreatorTile tile = (CraftCreatorTile) playerInventory.player.world.getTileEntity(packet.readBlockPos());
		int index = 0;
		
		this.addSlot(new Slot(tile, index++, 124, 35));

		for (int x = 0; x < 3; ++x)
		{
			for (int y = 0; y < 3; ++y)
			{
				this.addSlot(new Slot(tile, index++, 30 + y * 18, 17 + x * 18));
			}
		}
		
		this.bindPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return true;
	}
}