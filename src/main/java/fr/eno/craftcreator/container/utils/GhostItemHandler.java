package fr.eno.craftcreator.container.utils;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

public class GhostItemHandler implements IItemHandler
{
    private final NonNullList<ItemStack> ghostSlots;

    public GhostItemHandler(int size)
    {
        this.ghostSlots = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int getSlots()
    {
        return this.ghostSlots.size();
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return this.ghostSlots.get(slotIndex);
    }

    @Override
    public ItemStack insertItem(int slotIndex, ItemStack stack, boolean simulate)
    {
        return this.ghostSlots.set(slotIndex, stack);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        return ItemStackHelper.getAndSplit(this.ghostSlots, slot, amount);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return true;
    }
}
