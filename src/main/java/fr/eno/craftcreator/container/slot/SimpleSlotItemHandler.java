package fr.eno.craftcreator.container.slot;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SimpleSlotItemHandler extends CustomSlotItemHandler
{
    public SimpleSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    protected boolean canPickup(Player playerIn)
    {
        return true;
    }

    @Override
    protected boolean canPlace(ItemStack stack)
    {
        return true;
    }
}