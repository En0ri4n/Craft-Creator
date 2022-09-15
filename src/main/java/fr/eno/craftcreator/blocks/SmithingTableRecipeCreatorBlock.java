package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.SmithingTableRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmithingTableRecipeCreatorBlock extends RecipeCreatorBlock
{
	public SmithingTableRecipeCreatorBlock()
	{
	}

	@NotNull
	@Override
	public InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit)
	{
		if(!pLevel.isClientSide)
		{
			BlockEntity tileentity = pLevel.getBlockEntity(pPos);
			
			if(tileentity instanceof SmithingTableRecipeCreatorTile tile)
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
		return new SmithingTableRecipeCreatorTile(pPos, pState);
	}
}