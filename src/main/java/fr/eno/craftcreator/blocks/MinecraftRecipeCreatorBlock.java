package fr.eno.craftcreator.blocks;


import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.MinecraftRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class MinecraftRecipeCreatorBlock extends RecipeCreatorBlock
{
    @Override
    protected boolean hasFacing()
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
    {
        switch(state.getValue(RecipeCreatorBlock.FACING))
        {
            case NORTH:
                return LecternBlock.SHAPE_NORTH;
            case SOUTH:
                return LecternBlock.SHAPE_SOUTH;
            case EAST:
                return LecternBlock.SHAPE_EAST;
            case WEST:
                return LecternBlock.SHAPE_WEST;
            default:
                return LecternBlock.SHAPE_COMMON;
        }
    }

    @Override
    public InteractionResult use(BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Player playerIn, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit)
    {
        return BlockUtils.openMenu(SupportedMods.MINECRAFT, worldIn, pos, playerIn, MinecraftRecipeCreatorTile.class);
    }

    @Override
    protected BlockEntity getTileEntity(BlockPos pos, BlockState state)
    {
        return new MinecraftRecipeCreatorTile(pos, state);
    }
}