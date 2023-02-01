package fr.eno.craftcreator.tileentity.vanilla;

import fr.eno.craftcreator.container.FurnaceRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class FurnaceRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
	public FurnaceRecipeCreatorTile()
	{
		super(InitTileEntities.FURNACE_RECIPE_CREATOR.get(), SlotHelper.FURNACE_SLOTS_SIZE);
	}

	@Override
	public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player)
	{
		return new FurnaceRecipeCreatorContainer(id, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getBlockPos()));
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("container.furnace_recipe_creator.title");
	}
}