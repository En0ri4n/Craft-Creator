package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.vanilla.CraftingTableRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
public class CraftingTableRecipeCreatorBlock extends RecipeCreatorBlock
{
	private static final VoxelShape SHAPE = Stream.of(
			Block.box(15, 12, 5, 16, 16, 11),
			Block.box(14, 12, 4, 15, 16, 12),
			Block.box(13, 12, 2, 14, 16, 14),
			Block.box(12, 12, 2, 13, 16, 14),
			Block.box(11, 12, 1, 12, 16, 15),
			Block.box(5, 12, 0, 11, 16, 16),
			Block.box(4, 12, 1, 5, 16, 15),
			Block.box(3, 12, 2, 4, 16, 14),
			Block.box(2, 12, 2, 3, 16, 14),
			Block.box(1, 12, 4, 2, 16, 12),
			Block.box(0, 12, 5, 1, 16, 11),
			Block.box(7, 5, 9, 9, 7, 11),
			Block.box(7, 6, 10, 9, 8, 12),
			Block.box(7, 7, 11, 9, 9, 13),
			Block.box(7, 8, 12, 9, 10, 14),
			Block.box(7, 9, 13, 9, 11, 15),
			Block.box(7, 10, 14, 9, 12, 16),
			Block.box(0, 10, 7, 2, 12, 9),
			Block.box(1, 9, 7, 3, 11, 9),
			Block.box(2, 8, 7, 4, 10, 9),
			Block.box(3, 7, 7, 5, 9, 9),
			Block.box(4, 6, 7, 6, 8, 9),
			Block.box(5, 5, 7, 7, 7, 9),
			Block.box(14, 10, 7, 16, 12, 9),
			Block.box(13, 9, 7, 15, 11, 9),
			Block.box(12, 8, 7, 14, 10, 9),
			Block.box(11, 7, 7, 13, 9, 9),
			Block.box(10, 6, 7, 12, 8, 9),
			Block.box(9, 5, 7, 11, 7, 9),
			Block.box(7, 10, 0, 9, 12, 2),
			Block.box(7, 9, 1, 9, 11, 3),
			Block.box(7, 8, 2, 9, 10, 4),
			Block.box(7, 7, 3, 9, 9, 5),
			Block.box(7, 6, 4, 9, 8, 6),
			Block.box(7, 5, 5, 9, 7, 7),
			Block.box(7, 2, 7, 9, 12, 9),
			Block.box(6, 3, 6, 8, 4, 8),
			Block.box(5, 3, 5, 7, 4, 7),
			Block.box(4, 2, 4, 6, 3, 6),
			Block.box(3, 2, 3, 5, 3, 5),
			Block.box(2, 1, 2, 4, 2, 4),
			Block.box(1, 1, 1, 3, 2, 3),
			Block.box(0, 0, 0, 2, 1, 2),
			Block.box(8, 3, 6, 10, 4, 8),
			Block.box(9, 3, 5, 11, 4, 7),
			Block.box(10, 2, 4, 12, 3, 6),
			Block.box(11, 2, 3, 13, 3, 5),
			Block.box(12, 1, 2, 14, 2, 4),
			Block.box(13, 1, 1, 15, 2, 3),
			Block.box(14, 0, 0, 16, 1, 2),
			Block.box(8, 3, 8, 10, 4, 10),
			Block.box(9, 3, 9, 11, 4, 11),
			Block.box(10, 2, 10, 12, 3, 12),
			Block.box(11, 2, 11, 13, 3, 13),
			Block.box(12, 1, 12, 14, 2, 14),
			Block.box(13, 1, 13, 15, 2, 15),
			Block.box(14, 0, 14, 16, 1, 16),
			Block.box(6, 3, 8, 8, 4, 10),
			Block.box(5, 3, 9, 7, 4, 11),
			Block.box(4, 2, 10, 6, 3, 12),
			Block.box(3, 2, 11, 5, 3, 13),
			Block.box(2, 1, 12, 4, 2, 14),
			Block.box(1, 1, 13, 3, 2, 15),
			Block.box(0, 0, 14, 2, 1, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	public CraftingTableRecipeCreatorBlock()
	{
	}

	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext)
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
			
			if(tileentity instanceof CraftingTableRecipeCreatorTile tile)
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
		return new CraftingTableRecipeCreatorTile(pPos, pState);
	}
}