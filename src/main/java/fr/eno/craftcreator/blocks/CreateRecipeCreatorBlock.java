package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.CreateRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class CreateRecipeCreatorBlock extends RecipeCreatorBlock
{
    private static final VoxelShape SHAPE = Stream.of(BlockUtils.create(0.0625, 0.70625, 0.0625, 0.9375, 1, 0.9375),
                    BlockUtils.create(0, 0, 0.0625, 1, 0.48125, 0.9375),
                    BlockUtils.create(0.1875, 0.515625, 0.1875, 0.8125, 0.671875, 0.8125),
                    BlockUtils.create(0.3125, 0.478125, 0.3125, 0.6875, 0.70625, 0.6875),
                    BlockUtils.create(0.0625, 0, 0, 0.9375, 0.48125, 0.0625),
                    BlockUtils.create(0.0625, 0, 0.9375, 0.9375, 0.48125, 1),
                    BlockUtils.create(-0.0625, 0.5, 0.40625, 1.0625, 0.6875, 0.59375),
                    BlockUtils.create(-0.0625, 0.5, 0.40625, 1.0625, 0.6875, 0.59375),
                    BlockUtils.create(-0.0625, 0.5, 0.40625, 1.0625, 0.6875, 0.59375),
                    BlockUtils.create(0.40625, 0.5, -0.0625, 0.59375, 0.6875, 1.0625))
            .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    @Override
    protected VoxelShape getShape()
    {
        return SHAPE;
    }

    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Player playerIn, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit)
    {
        return BlockUtils.openMenu(SupportedMods.CREATE, worldIn, pos, playerIn, CreateRecipeCreatorTile.class);
    }

    @Override
    protected BlockEntity getTileEntity(BlockPos pos, BlockState state)
    {
        return new CreateRecipeCreatorTile(pos, state);
    }
}
