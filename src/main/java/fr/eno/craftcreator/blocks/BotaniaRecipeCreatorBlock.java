package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
