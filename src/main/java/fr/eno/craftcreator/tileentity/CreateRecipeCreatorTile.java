package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class CreateRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    public CreateRecipeCreatorTile()
    {
        super(SupportedMods.CREATE, InitTileEntities.CREATE_RECIPE_CREATOR.get(), SlotHelper.CREATE_SLOTS_SIZE);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return References.getTranslate("container.create_recipe_creator.title");
    }

    @Nullable
    @Override
    public Container createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity player)
    {
        return new CreateRecipeCreatorContainer(containerId, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getBlockPos()));
    }
}
