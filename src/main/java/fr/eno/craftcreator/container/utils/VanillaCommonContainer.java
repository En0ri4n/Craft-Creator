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
    public boolean canInteractWith(PlayerEntity playerIn)
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
        for(Slot slot : inventorySlots)
            if(slot instanceof SimpleSlotItemHandler)
                ((SimpleSlotItemHandler) slot).setActive(active);
    }



    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        int playerInvIndexStart = player.inventory.getSizeInventory();
        ItemStack retStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if(slot.getHasStack())
        {
            final ItemStack stack = slot.getStack();
            retStack = stack.copy();

            final int size = this.inventorySlots.size() - playerInvIndexStart;
            if(index < size)
            {
                if(!mergeItemStack(stack, size, this.inventorySlots.size(), false)) return ItemStack.EMPTY;
            }
            else if(!mergeItemStack(stack, 0, size, false)) return ItemStack.EMPTY;

            if(stack.isEmpty() || stack.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if(stack.getCount() == retStack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, stack);
        }

        return retStack;
    }
}