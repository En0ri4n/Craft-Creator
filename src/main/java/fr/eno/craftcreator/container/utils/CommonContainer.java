package fr.eno.craftcreator.container.utils;

import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class CommonContainer extends VanillaCommonContainer
{
    protected final MultiScreenRecipeCreatorTile tile;

    public CommonContainer(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf byteBuf)
    {
        super(pMenuType, pContainerId);
        this.tile = (MultiScreenRecipeCreatorTile) inventory.player.level.getBlockEntity(byteBuf.readBlockPos());
    }

    public abstract SupportedMods getMod();

    public MultiScreenRecipeCreatorTile getTile()
    {
        return tile;
    }
}