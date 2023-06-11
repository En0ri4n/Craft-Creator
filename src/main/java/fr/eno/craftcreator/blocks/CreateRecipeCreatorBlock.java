package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.CreateRecipeCreatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class CreateRecipeCreatorBlock extends RecipeCreatorBlock
{
    private static final VoxelShape SHAPE = Stream.of(create(0.0625, 0.70625, 0.0625, 0.9375, 1, 0.9375),
                    create(0, 0, 0.0625, 1, 0.48125, 0.9375),
                    create(0.1875, 0.515625, 0.1875, 0.8125, 0.671875, 0.8125),
                    create(0.3125, 0.478125, 0.3125, 0.6875, 0.70625, 0.6875),
                    create(0.0625, 0, 0, 0.9375, 0.48125, 0.0625),
                    create(0.0625, 0, 0.9375, 0.9375, 0.48125, 1),
                    create(-0.0625, 0.5, 0.40625, 1.0625, 0.6875, 0.59375),
                    create(-0.0625, 0.5, 0.40625, 1.0625, 0.6875, 0.59375),
                    create(-0.0625, 0.5, 0.40625, 1.0625, 0.6875, 0.59375),
                    create(0.40625, 0.5, -0.0625, 0.59375, 0.6875, 1.0625))
            .reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    @Override
    protected VoxelShape getShape()
    {
        return SHAPE;
    }

    @Override
    public ActionResultType use(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
    {
        return openMenu(SupportedMods.CREATE, worldIn, pos, playerIn, CreateRecipeCreatorTile.class);
    }

    @Override
    protected TileEntity getTileEntity(BlockState state)
    {
        return new CreateRecipeCreatorTile();
    }
}
