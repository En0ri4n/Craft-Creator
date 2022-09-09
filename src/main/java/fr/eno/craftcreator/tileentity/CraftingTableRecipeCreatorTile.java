package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.container.CraftingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CraftingTableRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
	private boolean shapedRecipe;

	public CraftingTableRecipeCreatorTile()
	{
		super(InitTileEntities.CRAFTING_TABLE_RECIPE_CREATOR.get(), 10);
	}

	@Override
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound)
	{
		super.read(state, compound);

		if(compound.contains("isShapedRecipe"))
			this.shapedRecipe = compound.getBoolean("isShapedRecipe");
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT compound)
	{
		compound.putBoolean("isShapedRecipe", this.shapedRecipe);

		return super.write(compound);
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("container.crafting_table_recipe_creator.title");
	}

	@Nullable
	@Override
	public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player)
	{
		return new CraftingTableRecipeCreatorContainer(id, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getPos()));
	}

	public boolean isShapedRecipe()
	{
		return shapedRecipe;
	}

	public void setShapedRecipe(boolean shapedRecipe)
	{
		this.shapedRecipe = shapedRecipe;
		this.markDirty();
	}
}