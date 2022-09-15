package fr.eno.craftcreator.container.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleContainerMenu extends AbstractContainerMenu
{
    protected SimpleContainerMenu(@Nullable MenuType<?> pMenuType, int pContainerId)
    {
        super(pMenuType, pContainerId);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer)
    {
        return true;
    }
}
