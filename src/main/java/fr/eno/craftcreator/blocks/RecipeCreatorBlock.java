package fr.eno.craftcreator.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class RecipeCreatorBlock extends Block
{
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public RecipeCreatorBlock()
    {
        super(Block.Properties.of(Material.METAL).sound(SoundType.STONE).strength(999999F));
        BlockState stateHolder = this.defaultBlockState();
        if(hasFacing())
            stateHolder = stateHolder.setValue(FACING, Direction.NORTH);
        this.registerDefaultState(stateHolder);
    }

    protected boolean hasFacing()
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public abstract ActionResultType use(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit);

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
    {
        return getShape();
    }

    protected abstract TileEntity getTileEntity(BlockState state);

    protected VoxelShape getShape()
    {
        return VoxelShapes.block();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState stateHolder = this.defaultBlockState();
        if(hasFacing())
            stateHolder = stateHolder.setValue(FACING, context.getHorizontalDirection().getOpposite());
        return stateHolder;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return getTileEntity(state);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder)
    {
        if(hasFacing())
            stateBuilder.add(FACING);
    }
}
