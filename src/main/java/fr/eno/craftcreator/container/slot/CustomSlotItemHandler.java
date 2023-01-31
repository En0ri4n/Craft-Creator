package fr.eno.craftcreator.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public abstract class CustomSlotItemHandler extends SlotItemHandler
{
    private boolean isActive;

    public CustomSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.isActive = false;
    }

    protected abstract boolean canPickup(PlayerEntity playerIn);

    protected abstract boolean canPlace(ItemStack stack);

    @Override
    public boolean canTakeStack(PlayerEntity playerIn)
    {
        return canPickup(playerIn);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return canPlace(stack);
    }

    @Override
    public boolean isEnabled()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }
}
