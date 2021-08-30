package fr.eno.craftcreator.tileentity;

import net.minecraft.inventory.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraftforge.items.*;

import javax.annotation.*;

public abstract class InventoryContainerTileEntity extends TileEntity implements IItemHandler, INamedContainerProvider
{
    private final NonNullList<ItemStack> inventory;

    public InventoryContainerTileEntity(TileEntityType<?> type, int inventorySize)
    {
        super(type);
        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public void read(@Nonnull CompoundNBT compound)
    {
        super.read(compound);
        ItemStackHelper.loadAllItems(compound.getCompound("Inventory"), this.inventory);
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound)
    {
        ItemStackHelper.saveAllItems(compound.getCompound("Inventory"), this.inventory);
        return super.write(compound);
    }

    @Override
    public int getSlots()
    {
        return this.inventory.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return this.inventory.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.inventory.get(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty())
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate)
        {
            if (existing.isEmpty())
            {
                this.inventory.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            }
            else
            {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack)
    {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.inventory.get(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract)
        {
            if (!simulate)
            {
                this.inventory.set(slot, ItemStack.EMPTY);
                return existing;
            }
            else
            {
                return existing.copy();
            }
        }
        else
        {
            if (!simulate)
            {
                this.inventory.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    protected void validateSlotIndex(int slot)
    {
        if (slot < 0 || slot >= inventory.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + inventory.size() + ")");
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return true;
    }
}
