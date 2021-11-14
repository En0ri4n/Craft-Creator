package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.network.*;

import javax.annotation.*;

public class BotaniaRecipeCreatorBlock extends RecipeCreatorBlock
{
    private static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), makeCuboidShape(1.0D, 1.0D, 1.0D, 15.0D, 8.0D, 15.0D), IBooleanFunction.ONLY_FIRST);

    public BotaniaRecipeCreatorBlock()
    {
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
    {
        if(!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if(tileentity instanceof BotaniaRecipeCreatorTile)
            {
                BotaniaRecipeCreatorTile tile = (BotaniaRecipeCreatorTile) tileentity;

                NetworkHooks.openGui((ServerPlayerEntity) player, tile, pos);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.CONSUME;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new BotaniaRecipeCreatorTile();
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context)
    {
        return SHAPE;
    }
}
