package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.vanilla.FurnaceRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class FurnaceRecipeCreatorBlock extends RecipeCreatorBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final VoxelShape SHAPE = Stream.of(
			Block.box(1, 0, 1, 15, 15, 15),
			Block.box(13, 0, 0, 16, 14, 1),
			Block.box(3, 5, 0, 13, 9, 1),
			Block.box(2, 15, 2, 14, 16, 14),
			Block.box(2, 15, 1, 14, 16, 2),
			Block.box(14, 15, 2, 15, 16, 14),
			Block.box(2, 15, 14, 14, 16, 15),
			Block.box(1, 15, 2, 2, 16, 14),
			Block.box(15, 0, 1, 16, 14, 15),
			Block.box(15, 14, 2, 16, 15, 14),
			Block.box(0, 14, 2, 1, 15, 14),
			Block.box(0, 0, 1, 1, 14, 15),
			Block.box(0, 0, 15, 16, 14, 16),
			Block.box(0, 0, 0, 3, 14, 1),
			Block.box(2, 14, 15, 14, 15, 16),
			Block.box(3, 0, 0, 13, 1, 1),
			Block.box(11, 4, 0, 13, 5, 1),
			Block.box(3, 4, 0, 5, 5, 1),
			Block.box(3, 3, 0, 4, 4, 1),
			Block.box(12, 3, 0, 13, 4, 1),
			Block.box(3, 12, 0, 13, 14, 1),
			Block.box(2, 14, 0, 14, 15, 1),
			Block.box(12, 11, 0, 13, 12, 1),
			Block.box(3, 11, 0, 4, 12, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	public FurnaceRecipeCreatorBlock()
	{
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return SHAPE;
	}

	@NotNull
	@Override
	public InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit)
	{
		if(!pLevel.isClientSide)
		{
			BlockEntity tileentity = pLevel.getBlockEntity(pPos);

			if(tileentity instanceof FurnaceRecipeCreatorTile tile)
			{

				NetworkHooks.openGui((ServerPlayer) pPlayer, tile, pPos);

				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.CONSUME;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext)
	{
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState)
	{
		return new FurnaceRecipeCreatorTile(pPos, pState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(FACING);
		super.createBlockStateDefinition(pBuilder);
	}
}