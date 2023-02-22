package fr.eno.craftcreator.container.base;

import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.slot.DefinedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.slot.utils.DefinedPositionnedSlot;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class CommonContainer extends VanillaCommonContainer
{
    protected final MultiScreenRecipeCreatorTile tile;
    
    public CommonContainer(MenuType<?> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf byteBuf)
    {
        super(pMenuType, pContainerId);
        this.tile = (MultiScreenRecipeCreatorTile) inventory.player.level.getBlockEntity(byteBuf.readBlockPos());
    }

    protected void addSlots(List<PositionnedSlot> slots)
    {
        for(PositionnedSlot positionnedSlot : slots)
        {
            int x = positionnedSlot.getxPos();
            int y = positionnedSlot.getyPos();
            int index = positionnedSlot.getIndex();

            if(positionnedSlot instanceof DefinedPositionnedSlot)
                this.addSlot(new DefinedSlot(tile, index, x, y, ((DefinedPositionnedSlot) positionnedSlot)::isItemValid));
            else
                this.addSlot(new SimpleSlotItemHandler(tile, index, x, y));
        }
    }

    public abstract SupportedMods getMod();
    
    public abstract int getContainerSize();
    
    public MultiScreenRecipeCreatorTile getTile()
    {
        return tile;
    }

    @Override
    public boolean stillValid(Player playerIn)
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
    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        ModRecipeCreator mod = ModRecipeCreator.byName(tile.getCurrentRecipeType());
        
        if(slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            if(index < getContainerSize())
            {
                if(!this.moveItemStackTo(itemstack1, getContainerSize(), this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.moveItemStackTo(itemstack1, mod.getSlots().stream().map(PositionnedSlot::getIndex).min(Integer::compareTo).orElse(0), mod.getSlots().stream().map(PositionnedSlot::getIndex).max(Integer::compareTo).orElse(getContainerSize()) + 1, false))
            {
                return ItemStack.EMPTY;
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
        
        return itemstack;
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
                
                if(itemstack != ItemStack.EMPTY && ItemStack.isSame(stack, itemstack))
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
                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                
                if(itemstack1 == ItemStack.EMPTY && slot1.mayPlace(stack)) // Forge: Make sure to respect isItemValid in the slot.
                {
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
}