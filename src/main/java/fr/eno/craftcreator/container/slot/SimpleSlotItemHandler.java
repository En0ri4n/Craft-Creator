package fr.eno.craftcreator.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SimpleSlotItemHandler extends SlotItemHandler
{
    private boolean isActive;

    public SimpleSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.isActive = false;
    }

    @Override
    public boolean mayPickup(Player playerIn)
    {
        return super.mayPickup(playerIn);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack)
    {
        return super.mayPlace(stack);
    }

    @Override
    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }
}
