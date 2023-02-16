package fr.eno.craftcreator.container.slot;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class LockedSlot extends SimpleSlotItemHandler
{
	private final ItemStack lockedStack;

	public LockedSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition, ItemStack lockedStack)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.lockedStack = lockedStack;
	}

	@Override
	protected boolean canPickup(Player playerIn)
	{
		return false;
	}

	@Nonnull
	@Override
	public ItemStack getItem()
	{
		return this.lockedStack;
	}
}