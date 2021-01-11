package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.container.FurnaceRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class FurnaceRecipeCreatorTile extends LockableTileEntity
{
	private final NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);

	public FurnaceRecipeCreatorTile()
	{
		super(InitTileEntities.FURNACE_RECIPE_CREATOR.get());
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.size();
	}

	@Override
	public boolean isEmpty()
	{
		for(ItemStack stack : inventory)
		{
			if(!stack.isEmpty())
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.inventory.set(index, stack);
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return true;
	}

	@Override
	public void clear()
	{
		this.inventory.clear();
	}

	@Override
	protected ITextComponent getDefaultName()
	{
		return new StringTextComponent("container.furnace_recipe_creator.title");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new FurnaceRecipeCreatorContainer(id, player, new PacketBuffer(Unpooled.buffer()).writeBlockPos(pos));
	}
}