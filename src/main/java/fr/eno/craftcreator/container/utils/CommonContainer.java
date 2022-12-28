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

public abstract class CommonContainer extends VanillaCommonContainer
{
    protected final MultiScreenRecipeCreatorTile tile;

    public CommonContainer(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf packet)
    {
        super(pMenuType, pContainerId);
        this.tile = (MultiScreenRecipeCreatorTile) inventory.player.level.getBlockEntity(packet.readBlockPos());
    }

    public abstract SupportedMods getMod();

    public MultiScreenRecipeCreatorTile getTile()
    {
        return tile;
    }
}