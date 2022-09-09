package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.FurnaceRecipeCreatorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

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