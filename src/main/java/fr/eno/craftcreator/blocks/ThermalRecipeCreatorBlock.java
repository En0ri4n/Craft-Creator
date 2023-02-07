package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.ThermalRecipeCreatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ThermalRecipeCreatorBlock extends RecipeCreatorBlock
{
    @Override
    protected boolean hasFacing()
    {
        return true;
    }

    @Override
    public ActionResultType use(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
    {
        return BlockUtils.openMenu(SupportedMods.THERMAL, worldIn, pos, playerIn, ThermalRecipeCreatorTile.class);
    }

    @Override
    protected TileEntity getTileEntity(BlockPos pos, BlockState state)
    {
        return new ThermalRecipeCreatorTile();
    }
}
