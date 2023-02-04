package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.MinecraftRecipeCreatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MinecraftRecipeCreatorBlock extends RecipeCreatorBlock
{
	private static final VoxelShape SHAPE = VoxelShapes.block();

	public MinecraftRecipeCreatorBlock()
	{
	}

	@Override
	public VoxelShape getShape()
	{
		return SHAPE;
	}

	@Override
	protected ActionResultType onBlockUsed(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
	{
		return BlockUtils.openMenu(SupportedMods.MINECRAFT, worldIn, pos, playerIn, MinecraftRecipeCreatorTile.class);
	}

	@Override
	protected TileEntity getTileEntity(BlockPos pos, BlockState state)
	{
		return new MinecraftRecipeCreatorTile();
	}
}