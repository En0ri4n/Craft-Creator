package fr.eno.craftcreator.container.utils;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class CommonContainer extends AbstractContainerMenu
{
	public CommonContainer(@Nullable MenuType<?> pMenuType, int pContainerId)
	{
		super(pMenuType, pContainerId);
	}

	@Override
	public boolean stillValid(Player pPlayer)
	{
		return true;
	}

	protected void bindPlayerInventory(Inventory playerInventory)
	{
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k)
		{
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}
}