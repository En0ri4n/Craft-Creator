package fr.eno.craftcreator.container.base;


import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public abstract class VanillaCommonContainer extends AbstractContainerMenu
{
    public VanillaCommonContainer(MenuType<?> pMenuType, int pContainerId)
    {
        super(pMenuType, pContainerId);
    }

    protected void bindInventory(Inventory playerInventory)
    {
        bindInventory(playerInventory, 0, 0);
    }

    protected void bindInventory(Inventory playerInventory, int x, int y)
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
}