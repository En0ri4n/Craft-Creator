package fr.eno.craftcreator.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class DefinedSlot extends SlotItemHandler
{
    private final Predicate<ItemStack> isItemValid;

    public DefinedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> isItemValid)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.isItemValid = isItemValid;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return isItemValid.test(stack);
    }
}
