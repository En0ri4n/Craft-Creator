package fr.eno.craftcreator.container.utils;

import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.MultiScreenRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class CommonContainer extends AbstractContainerMenu
{
    protected final MultiScreenRecipeCreatorTile tile;

    public CommonContainer(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf packet)
    {
        super(pMenuType, pContainerId);
        this.tile = (MultiScreenRecipeCreatorTile) inventory.player.level.getBlockEntity(packet.readBlockPos());
    }

    public abstract SupportedMods getMod();

    @Override
    public boolean stillValid(Player pPlayer)
    {
        return true;
    }

    protected void bindPlayerInventory(Inventory playerInventory)
    {
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        var retStack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if(slot.hasItem())
        {
            final ItemStack stack = slot.getItem();
            retStack = stack.copy();

            final int size = this.slots.size() - player.getInventory().getContainerSize();
            if(index < size)
            {
                if(!moveItemStackTo(stack, 0, this.slots.size(), false)) return ItemStack.EMPTY;
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

    public MultiScreenRecipeCreatorTile getTile()
    {
        return tile;
    }
}