package fr.eno.craftcreator.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class LockedSlot extends SlotItemHandler
{	
	public LockedSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition, ItemStack stack)
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
	public void putStack(@Nonnull ItemStack stack) { super.putStack(stack); }
}