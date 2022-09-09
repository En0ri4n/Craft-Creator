package fr.eno.craftcreator.container.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CommonContainer extends Container
{
	protected final int invenSize;

	public CommonContainer(ContainerType<?> type, int id, int size)
	{
		super(type, id);
		invenSize = size;
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity playerIn)
	{
		return true;
	}

	protected void bindPlayerInventory(PlayerInventory playerInventory)
	{
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k)
		{
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack() && slot.xPos > 0 && slot.yPos > 0)
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < invenSize)
			{
				if (!this.mergeItemStack(itemstack1, invenSize, this.inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, invenSize, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0)
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	protected boolean mergeItemStack(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean flag = false;
		int i = startIndex;
		if (reverseDirection)
			i = endIndex - 1;

		if (stack.isStackable())
		{
			while (stack.getCount() > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex))
			{
				Slot slot = this.inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();
				int maxLimit = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());

				if (!itemstack.isEmpty() && areItemStacksEqual(stack, itemstack))
				{
					int j = itemstack.getCount() + stack.getCount();
					if (j <= maxLimit)
					{
						stack.setCount(0);
						itemstack.setCount(j);
						slot.onSlotChanged();
						flag = true;

					} else if (itemstack.getCount() < maxLimit)
					{
						stack.shrink(maxLimit - itemstack.getCount());
						itemstack.setCount(maxLimit);
						slot.onSlotChanged();
						flag = true;
					}
				}
				if (reverseDirection)
				{
					--i;
				} else
					++i;
			}
		}
		if (stack.getCount() > 0)
		{
			if (reverseDirection)
			{
				i = endIndex - 1;
			} else
				i = startIndex;

			while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)
			{
				Slot slot1 = this.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();

				if (itemstack1.isEmpty() && slot1.isItemValid(stack))
				{ // Forge: Make sure to respect isItemValid in the slot.
					if (stack.getCount() <= slot1.getSlotStackLimit())
					{
						slot1.putStack(stack.copy());
						slot1.onSlotChanged();
						stack.setCount(0);
						flag = true;
						break;
					} else
					{
						itemstack1 = stack.copy();
						stack.shrink(slot1.getSlotStackLimit());
						itemstack1.setCount(slot1.getSlotStackLimit());
						slot1.putStack(itemstack1);
						slot1.onSlotChanged();
						flag = true;
					}
				}
				if (reverseDirection)
				{
					--i;
				} else
					++i;
			}
		}
		return flag;
	}

	private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
	{
		return stackB.getItem() == stackA.getItem() && stackA.areShareTagsEqual(stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}
}