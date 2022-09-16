package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class BotaniaRecipeCreatorBlock extends RecipeCreatorBlock
{
    private static final VoxelShape SHAPE = Shapes.join(box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), box(1.0D, 1.0D, 1.0D, 15.0D, 8.0D, 15.0D), BooleanOp.ONLY_FIRST);

    public BotaniaRecipeCreatorBlock()
    {
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit)
    {
        if(!pLevel.isClientSide)
        {
            BlockEntity tileentity = pLevel.getBlockEntity(pPos);

            if(tileentity instanceof BotaniaRecipeCreatorTile tile)
            {
                NetworkHooks.openGui((ServerPlayer) pPlayer, tile, pPos);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState)
    {
        return new BotaniaRecipeCreatorTile(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
    {
        return SHAPE;
    }
}
