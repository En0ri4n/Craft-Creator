package fr.eno.craftcreator.blocks;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class RecipeCreatorBlock extends Block implements EntityBlock
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

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
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public abstract InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult);

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
    {
        return getShape();
    }

    protected abstract BlockEntity getTileEntity(BlockPos pos, BlockState state);

    protected VoxelShape getShape()
    {
        return Shapes.block();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState stateHolder = this.defaultBlockState();
        if(hasFacing())
            stateHolder = stateHolder.setValue(FACING, context.getHorizontalDirection().getOpposite());
        return stateHolder;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return getTileEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder)
    {
        if(hasFacing())
            stateBuilder.add(FACING);
    }
}
