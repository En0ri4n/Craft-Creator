package fr.eno.craftcreator.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class LockedSlot extends SimpleSlotItemHandler
{	
	public LockedSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition, ItemStack stack)
	{
		super(inventoryIn, index, xPosition, yPosition);
		mayPlace(stack);
	}

	@Override
	public boolean mayPickup(Player playerIn)
	{
		return false;
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack stack)
	{
		return super.mayPlace(stack);
	}
}