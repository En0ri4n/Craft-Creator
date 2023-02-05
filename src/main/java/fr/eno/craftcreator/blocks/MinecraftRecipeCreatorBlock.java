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
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.or(shape, VoxelShapes.box(1D / 16D, 0D, 0D, 15D / 16D, 14D / 16D, 1D / 16D));
		shape = VoxelShapes.or(shape, VoxelShapes.box(0D, 0D, 1D / 16D, 1D, 14D / 16D, 15D / 16D));
		shape = VoxelShapes.or(shape, VoxelShapes.box(1D / 16D, 0D, 15D / 16D, 15D / 16D, 14D / 16D, 1D));
		shape = VoxelShapes.or(shape, VoxelShapes.box(15D / 16D, 0D, 1D / 16D, 1D, 14D / 16D, 15D / 16D));
		shape = VoxelShapes.or(shape, VoxelShapes.box(1D / 16D, 13D / 16D, 1D / 16D, 15D / 16D, 15D / 16D, 15D / 16D));
		return shape;
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