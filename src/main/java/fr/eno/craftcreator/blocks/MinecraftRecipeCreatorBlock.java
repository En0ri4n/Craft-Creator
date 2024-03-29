package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.MinecraftRecipeCreatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MinecraftRecipeCreatorBlock extends RecipeCreatorBlock
{
    @Override
    protected boolean hasFacing()
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos blockPos, ISelectionContext selectionContext)
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
    public ActionResultType use(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
    {
        return openMenu(SupportedMods.MINECRAFT, worldIn, pos, playerIn, MinecraftRecipeCreatorTile.class);
    }

    @Override
    protected TileEntity getTileEntity(BlockState state)
    {
        return new MinecraftRecipeCreatorTile();
    }
}