package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.tileentity.vanilla.StoneCutterRecipeCreatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class StoneCutterRecipeCreatorBlock extends RecipeCreatorBlock
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = BlockUtils.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);

    public StoneCutterRecipeCreatorBlock()
    {
    }

    @Override
    protected Map<DirectionProperty, Direction> getStates()
    {
		Map<DirectionProperty, Direction> map = new HashMap<>();
		map.put(FACING, Direction.NORTH);
		return map;
    }

    @Override
    public VoxelShape getShape()
    {
        return SHAPE;
    }

    @Override
    protected ActionResultType onBlockUsed(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
    {
        return BlockUtils.openMenu(worldIn, pos, playerIn, StoneCutterRecipeCreatorTile.class);
    }

    @Override
    protected TileEntity getTileEntity(BlockPos pos, BlockState state)
    {
        return new StoneCutterRecipeCreatorTile();
    }
}