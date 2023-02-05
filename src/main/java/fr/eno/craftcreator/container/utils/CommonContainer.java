package fr.eno.craftcreator.container.utils;

import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

//TODO: resolve shift-clicking items not working
public abstract class CommonContainer extends VanillaCommonContainer
{
    protected final MultiScreenRecipeCreatorTile tile;

    public CommonContainer(ContainerType<?> pMenuType, int pContainerId, PlayerInventory inventory, PacketBuffer byteBuf)
    {
        super(pMenuType, pContainerId);
        this.tile = (MultiScreenRecipeCreatorTile) inventory.player.level.getBlockEntity(byteBuf.readBlockPos());
    }

    public abstract SupportedMods getMod();

    public MultiScreenRecipeCreatorTile getTile()
    {
        return tile;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn)
    {
        return true;
    }

    /**
     * Active slots
     *
     * @param active if true, the slots will be activated
     */
    public void activeSlots(boolean active)
    {
        for(Slot slot : slots)
            if(slot instanceof SimpleSlotItemHandler) ((SimpleSlotItemHandler) slot).setActive(active);
    }


    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
    {
        return super.quickMoveStack(playerIn, index);
        /*
        ModRecipeCreator mod = ModRecipeCreator.byName(tile.getCurrentRecipeType());

        int slotIndexStop = this.slots.size() - playerIn.inventory.getContainerSize();

        ItemStack itemstack = null;
        Slot slot = this.slots.get(index);

        if(slot != null && slot.hasItem() && slot.isActive())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if(index < slotIndexStop)
            {
                if(!this.moveItemStackTo(itemstack1, mod.getSlots().get(0).getIndex(), mod.getSlots().get(0).getIndex() + mod.getSlots().size(), false))
                {
                    return null;
                }
            }
            else if(!this.moveItemStackTo(itemstack1, slotIndexStop, playerIn.inventory.getContainerSize(), false))
            {
                return null;
            }

            if(itemstack1.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }
        }

        return itemstack;*/
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
    {
        boolean flag = false;
        int i = startIndex;
        if(reverseDirection) i = endIndex - 1;

        if(stack.isStackable())
        {
            while(stack.getCount() > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex))
            {
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                int maxLimit = Math.min(stack.getMaxStackSize(), slot.getMaxStackSize());

                if(itemstack != ItemStack.EMPTY && areItemStacksEqual(stack, itemstack))
                {
                    int j = itemstack.getCount() + stack.getCount();
                    if(j <= maxLimit)
                    {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;

                    }
                    else if(itemstack.getCount() < maxLimit)
                    {
                        stack.setCount(stack.getCount() - maxLimit - itemstack.getCount());
                        itemstack.setCount(maxLimit);
                        slot.setChanged();
                        flag = true;
                    }
                }
                if(reverseDirection)
                {
                    --i;
                }
                else ++i;
            }
        }
        if(stack.getCount() > 0)
        {
            if(reverseDirection)
            {
                i = endIndex - 1;
            }
            else i = startIndex;

            while(!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)
            {
                Slot slot1 = (Slot) this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();

                if(itemstack1 == ItemStack.EMPTY && slot1.mayPlace(stack))
                { // Forge: Make sure to respect isItemValid in the slot.
                    if(stack.getCount() <= slot1.getMaxStackSize())
                    {
                        slot1.set(stack.copy());
                        slot1.setChanged();
                        stack.setCount(0);
                        flag = true;
                        break;
                    }
                    else
                    {
                        itemstack1 = stack.copy();
                        stack.setCount(stack.getCount() - slot1.getMaxStackSize());
                        itemstack1.setCount(slot1.getMaxStackSize());
                        slot1.set(itemstack1);
                        slot1.setChanged();
                        flag = true;
                    }
                }
                if(reverseDirection)
                {
                    --i;
                }
                else ++i;
            }
        }
        return flag;
    }

    private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
    {
        return stackB.getItem() == stackA.getItem() && (stackA.getTag() == stackB.getTag()) && ItemStack.tagMatches(stackA, stackB);
    }
}