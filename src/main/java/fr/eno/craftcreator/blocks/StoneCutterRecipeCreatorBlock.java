package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.vanilla.StoneCutterRecipeCreatorTile;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoneCutterRecipeCreatorBlock extends RecipeCreatorBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	
	public StoneCutterRecipeCreatorBlock()
	{
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext)
	{
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
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
			
			if(tileentity instanceof StoneCutterRecipeCreatorTile tile)
			{
				NetworkHooks.openGui((ServerPlayer) pPlayer, tile, pPos);
				
				return InteractionResult.SUCCESS;
			}
		}
		
		return InteractionResult.CONSUME;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState)
	{
		return new StoneCutterRecipeCreatorTile(pPos, pState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(FACING);
	}
}