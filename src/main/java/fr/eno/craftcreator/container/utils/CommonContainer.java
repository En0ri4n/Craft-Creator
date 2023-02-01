package fr.eno.craftcreator.container.utils;

import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

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
}