package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.tileentity.vanilla.FurnaceRecipeCreatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class FurnaceRecipeCreatorBlock extends RecipeCreatorBlock
{
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE = Stream.of(
			BlockUtils.box(1, 0, 1, 15, 15, 15),
			BlockUtils.box(13, 0, 0, 16, 14, 1),
			BlockUtils.box(3, 5, 0, 13, 9, 1),
			BlockUtils.box(2, 15, 2, 14, 16, 14),
			BlockUtils.box(2, 15, 1, 14, 16, 2),
			BlockUtils.box(14, 15, 2, 15, 16, 14),
			BlockUtils.box(2, 15, 14, 14, 16, 15),
			BlockUtils.box(1, 15, 2, 2, 16, 14),
			BlockUtils.box(15, 0, 1, 16, 14, 15),
			BlockUtils.box(15, 14, 2, 16, 15, 14),
			BlockUtils.box(0, 14, 2, 1, 15, 14),
			BlockUtils.box(0, 0, 1, 1, 14, 15),
			BlockUtils.box(0, 0, 15, 16, 14, 16),
			BlockUtils.box(0, 0, 0, 3, 14, 1),
			BlockUtils.box(2, 14, 15, 14, 15, 16),
			BlockUtils.box(3, 0, 0, 13, 1, 1),
			BlockUtils.box(11, 4, 0, 13, 5, 1),
			BlockUtils.box(3, 4, 0, 5, 5, 1),
			BlockUtils.box(3, 3, 0, 4, 4, 1),
			BlockUtils.box(12, 3, 0, 13, 4, 1),
			BlockUtils.box(3, 12, 0, 13, 14, 1),
			BlockUtils.box(2, 14, 0, 14, 15, 1),
			BlockUtils.box(12, 11, 0, 13, 12, 1),
			BlockUtils.box(3, 11, 0, 4, 12, 1)
	).reduce((v1, v2) -> BlockUtils.join(v1, v2, IBooleanFunction.OR)).get();

	public FurnaceRecipeCreatorBlock()
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
		return BlockUtils.openMenu(worldIn, pos, playerIn, FurnaceRecipeCreatorTile.class);
	}

	@Override
	protected TileEntity getTileEntity(BlockPos pos, BlockState state)
	{
		return new FurnaceRecipeCreatorTile();
	}
}