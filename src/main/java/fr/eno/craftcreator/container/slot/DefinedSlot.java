package fr.eno.craftcreator.container.slot;

import net.minecraft.item.*;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.function.*;

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
