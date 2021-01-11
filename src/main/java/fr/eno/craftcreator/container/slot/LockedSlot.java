package fr.eno.craftcreator.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class LockedSlot extends Slot
{	
	public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, ItemStack stack)
	{
		super(inventoryIn, index, xPosition, yPosition);
		putStack(stack);
	}
	
	@Override
	public boolean canTakeStack(PlayerEntity playerIn)
	{
		return false;
	}
	
	@Override
	public void putStack(ItemStack stack) { super.putStack(stack); }
}