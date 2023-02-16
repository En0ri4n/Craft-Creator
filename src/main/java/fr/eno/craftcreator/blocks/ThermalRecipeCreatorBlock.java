package fr.eno.craftcreator.blocks;


import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.ThermalRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;

public class ThermalRecipeCreatorBlock extends RecipeCreatorBlock
{
    @Override
    protected boolean hasFacing()
    {
        return true;
    }

    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Player playerIn, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit)
    {
        return BlockUtils.openMenu(SupportedMods.THERMAL, worldIn, pos, playerIn, ThermalRecipeCreatorTile.class);
    }

    @Override
    protected BlockEntity getTileEntity(BlockPos pos, BlockState state)
    {
        return new ThermalRecipeCreatorTile(pos, state);
    }
}
