package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.api.BlockUtils;
import fr.eno.craftcreator.tileentity.ThermalRecipeCreatorTile;
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
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ThermalRecipeCreatorBlock extends RecipeCreatorBlock
{
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public ThermalRecipeCreatorBlock()
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
    protected ActionResultType onBlockUsed(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
    {
        return BlockUtils.openMenu(worldIn, pos, playerIn, ThermalRecipeCreatorTile.class);
    }

    @Override
    protected TileEntity getTileEntity(BlockPos pos, BlockState state)
    {
        return new ThermalRecipeCreatorTile();
    }
}
