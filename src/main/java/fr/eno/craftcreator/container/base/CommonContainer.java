package fr.eno.craftcreator.container.base;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
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
}