package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BotaniaRecipeCreatorBlock extends RecipeCreatorBlock
{
    private static final VoxelShape SHAPE = join(box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
            box(1.0D, 1.0D, 1.0D, 15.0D, 8.0D, 15.0D), IBooleanFunction.ONLY_FIRST);

    @Override
    public VoxelShape getShape()
    {
        return SHAPE;
    }

    @Override
    public ActionResultType use(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
    {
        return openMenu(SupportedMods.BOTANIA, worldIn, pos, playerIn, BotaniaRecipeCreatorTile.class);
    }

    @Override
    protected TileEntity getTileEntity(BlockState state)
    {
        return new BotaniaRecipeCreatorTile();
    }
}
