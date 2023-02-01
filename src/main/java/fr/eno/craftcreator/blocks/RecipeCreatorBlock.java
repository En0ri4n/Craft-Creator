package fr.eno.craftcreator.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
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
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public abstract class RecipeCreatorBlock extends Block
{
    public RecipeCreatorBlock()
    {
        super(Block.Properties.of(Material.METAL).sound(SoundType.STONE).strength(999999F));
        BlockState stateHolder = this.defaultBlockState();
        getStates().forEach(stateHolder::setValue);
        this.registerDefaultState(stateHolder);
    }

    protected Map<DirectionProperty, Direction> getStates()
    {
        return new HashMap<>();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState p_149645_1_)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_)
    {
        return onBlockUsed(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
    {
        return getShape();
    }

    protected abstract TileEntity getTileEntity(BlockPos pos, BlockState state);

    protected abstract ActionResultType onBlockUsed(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit);

    protected VoxelShape getShape()
    {
        return VoxelShapes.block();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState stateHolder = this.defaultBlockState();
        getStates().forEach(stateHolder::setValue);
        return stateHolder;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return getTileEntity(null, state);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder)
    {
        getStates().keySet().forEach(stateBuilder::add);
    }
}
