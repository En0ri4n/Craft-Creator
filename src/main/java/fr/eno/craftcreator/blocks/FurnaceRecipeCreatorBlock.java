package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.StateContainer.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.network.*;

import javax.annotation.*;
import java.util.stream.*;

public class FurnaceRecipeCreatorBlock extends RecipeCreatorBlock
{
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE = Stream.of(
			Block.makeCuboidShape(1, 0, 1, 15, 15, 15),
			Block.makeCuboidShape(13, 0, 0, 16, 14, 1),
			Block.makeCuboidShape(3, 5, 0, 13, 9, 1),
			Block.makeCuboidShape(2, 15, 2, 14, 16, 14),
			Block.makeCuboidShape(2, 15, 1, 14, 16, 2),
			Block.makeCuboidShape(14, 15, 2, 15, 16, 14),
			Block.makeCuboidShape(2, 15, 14, 14, 16, 15),
			Block.makeCuboidShape(1, 15, 2, 2, 16, 14),
			Block.makeCuboidShape(15, 0, 1, 16, 14, 15),
			Block.makeCuboidShape(15, 14, 2, 16, 15, 14),
			Block.makeCuboidShape(0, 14, 2, 1, 15, 14),
			Block.makeCuboidShape(0, 0, 1, 1, 14, 15),
			Block.makeCuboidShape(0, 0, 15, 16, 14, 16),
			Block.makeCuboidShape(0, 0, 0, 3, 14, 1),
			Block.makeCuboidShape(2, 14, 15, 14, 15, 16),
			Block.makeCuboidShape(3, 0, 0, 13, 1, 1),
			Block.makeCuboidShape(11, 4, 0, 13, 5, 1),
			Block.makeCuboidShape(3, 4, 0, 5, 5, 1),
			Block.makeCuboidShape(3, 3, 0, 4, 4, 1),
			Block.makeCuboidShape(12, 3, 0, 13, 4, 1),
			Block.makeCuboidShape(3, 12, 0, 13, 14, 1),
			Block.makeCuboidShape(2, 14, 0, 14, 15, 1),
			Block.makeCuboidShape(12, 11, 0, 13, 12, 1),
			Block.makeCuboidShape(3, 11, 0, 4, 12, 1)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

	public FurnaceRecipeCreatorBlock()
	{
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context)
	{
		return SHAPE;
	}

	@Nonnull
	@Override
	public ActionResultType onBlockActivated(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if(tileentity instanceof FurnaceRecipeCreatorTile)
			{
				FurnaceRecipeCreatorTile tile = (FurnaceRecipeCreatorTile) tileentity;

				NetworkHooks.openGui((ServerPlayerEntity) player, tile, pos);

				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.CONSUME;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new FurnaceRecipeCreatorTile();
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}