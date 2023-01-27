package fr.eno.craftcreator.tileentity.vanilla;

import fr.eno.craftcreator.container.CraftingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CraftingTableRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
	private boolean shapedRecipe;

	public CraftingTableRecipeCreatorTile(BlockPos pWorldPosition, BlockState pBlockState)
	{
		super(InitTileEntities.CRAFTING_TABLE_RECIPE_CREATOR.get(), pWorldPosition, pBlockState, SlotHelper.CRAFTING_TABLE_SLOTS_SIZE);
	}

	@Override
	public void setData(String dataName, Object data)
	{
		super.setData(dataName, data);
		if(dataName.equals("shaped"))
			setShapedRecipe((boolean) data);
	}

	@Override
	public Object getData(String dataName)
	{
		if(dataName.equals("shaped"))
			return isShapedRecipe();

		return super.getData(dataName);
	}

	@Override
	public void load(@NotNull CompoundTag compound)
	{
		super.load(compound);

		if(compound.contains("isShapedRecipe"))
			this.shapedRecipe = compound.getBoolean("isShapedRecipe");
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag compoundTag)
	{
		compoundTag.putBoolean("isShapedRecipe", this.shapedRecipe);

		super.saveAdditional(compoundTag);
	}

	@Nonnull
	@Override
	public Component getDisplayName()
	{
		return new TextComponent("container.crafting_table_recipe_creator.title");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player player)
	{
		return new CraftingTableRecipeCreatorContainer(id, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(getBlockPos()));
	}

	public boolean isShapedRecipe()
	{
		return shapedRecipe;
	}

	public void setShapedRecipe(boolean shapedRecipe)
	{
		this.shapedRecipe = shapedRecipe;
		this.setChanged();
	}
}