package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class BotaniaRecipeCreatorBlock extends RecipeCreatorBlock
{
    private static final VoxelShape SHAPE = BlockUtils.join(BlockUtils.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
            BlockUtils.box(1.0D, 1.0D, 1.0D, 15.0D, 8.0D, 15.0D), BooleanOp.ONLY_FIRST);

    @Override
    public VoxelShape getShape()
    {
        return SHAPE;
    }

    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Player playerIn, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit)
    {
        return BlockUtils.openMenu(SupportedMods.BOTANIA, worldIn, pos, playerIn, BotaniaRecipeCreatorTile.class);
    }

    @Override
    protected BlockEntity getTileEntity(BlockPos pos, BlockState state)
    {
        return new BotaniaRecipeCreatorTile(pos, state);
    }
}
