package fr.eno.craftcreator.container.slot.utils;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class DefinedPositionnedSlot extends PositionnedSlot
{
    private final Predicate<ItemStack> isItemValid;

    public DefinedPositionnedSlot(int index, int xPos, int yPos, Predicate<ItemStack> isItemValid)
    {
        super(index, xPos, yPos);
        this.isItemValid = isItemValid;
    }

    public boolean isItemValid(ItemStack stack)
    {
        return isItemValid.test(stack);
    }
}
