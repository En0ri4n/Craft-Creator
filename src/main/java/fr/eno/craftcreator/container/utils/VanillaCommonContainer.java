package fr.eno.craftcreator.container.utils;

import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

//TODO: resolve shift-clicking items not working
public abstract class VanillaCommonContainer extends Container
{
    public VanillaCommonContainer(ContainerType<?> pMenuType, int pContainerId)
    {
        super(pMenuType, pContainerId);
    }
    
    @Override
    public boolean stillValid(PlayerEntity playerIn)
    {
        return true;
    }

    protected void bindPlayerInventory(PlayerInventory playerInventory)
    {
        bindPlayerInventory(playerInventory, 0, 0);
    }

    protected void bindPlayerInventory(PlayerInventory playerInventory, int x, int y)
    {
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, x + 8 + j * 18, y + 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInventory, k, x + 8 + k * 18, y + 142));
        }
    }

    /**
     * Active slots
     * @param active if true, the slots will be activated
     */
    public void activeSlots(boolean active)
    {
        for(Slot slot : slots)
            if(slot instanceof SimpleSlotItemHandler)
                ((SimpleSlotItemHandler) slot).setActive(active);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index)
    {
        int playerInvIndexStart = player.inventory.getContainerSize();
        ItemStack retStack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if(slot.hasItem())
        {
            final ItemStack stack = slot.getItem();
            retStack = stack.copy();

            final int size = this.slots.size() - playerInvIndexStart;
            if(index < size)
            {
                if(!moveItemStackTo(stack, size, this.slots.size(), false)) return ItemStack.EMPTY;
            }
            else if(!moveItemStackTo(stack, 0, size, false)) return ItemStack.EMPTY;

            if(stack.isEmpty() || stack.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if(stack.getCount() == retStack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, stack);
        }

        return retStack;
    }
}