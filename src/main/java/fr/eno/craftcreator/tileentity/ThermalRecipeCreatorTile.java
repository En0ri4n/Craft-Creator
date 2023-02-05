package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ThermalRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    public ThermalRecipeCreatorTile()
    {
        super(SupportedMods.THERMAL, InitTileEntities.THERMAL_RECIPE_CREATOR.get(), SlotHelper.THERMAL_SLOTS_SIZE);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent(References.getTranslate("tile.thermal_recipe_creator.name").getString());
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory inventory, PlayerEntity player)
    {
        return new ThermalRecipeCreatorContainer(containerId, inventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getBlockPos()));
    }
}
