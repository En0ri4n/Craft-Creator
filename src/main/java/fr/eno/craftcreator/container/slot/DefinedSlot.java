package fr.eno.craftcreator.container.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

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
    public boolean mayPlace(@NotNull ItemStack stack)
    {
        return isItemValid.test(stack);
    }
}
