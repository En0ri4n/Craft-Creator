package fr.eno.craftcreator.container.utils;

import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return this.ghostSlots.get(slotIndex);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slotIndex, @NotNull ItemStack stack, boolean simulate)
    {
        return this.ghostSlots.set(slotIndex, stack);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        return ContainerHelper.removeItem(this.ghostSlots, slot, amount);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        return true;
    }
}
