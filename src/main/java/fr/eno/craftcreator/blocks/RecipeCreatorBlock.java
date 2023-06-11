package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
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

    /**
     * Create a VoxelShape from a box (from 0 to 16) (Only for compatibilty)
     *
     * @see VoxelShapes#box(double, double, double, double, double, double)
     *
     * @return the VoxelShape with the given size
     */
    public static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return VoxelShapes.box(x1 / 16.0D, y1 / 16.0D, z1 / 16.0D, x2 / 16.0D, y2 / 16.0D, z2 / 16.0D);
    }

    /**
     * Create a VoxelShape from a box (from 0 to 1) (Only for compatibilty)
     *
     * @see VoxelShapes#box(double, double, double, double, double, double)
     *
     * @return the VoxelShape with the given size
     */
    public static VoxelShape create(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return VoxelShapes.box(x1, y1, z1, x2, y2, z2);
    }

    /**
     * Join two VoxelShape with the given IBooleanFunction (Only for compatibilty)
     *
     * @see VoxelShapes#join(VoxelShape, VoxelShape, IBooleanFunction)
     *
     * @return the joined VoxelShape
     */
    public static VoxelShape join(VoxelShape shape1, VoxelShape shape2, IBooleanFunction op)
    {
        return VoxelShapes.join(shape1, shape2, op);
    }

    /**
     * Open the GUI if the mod is loaded and the tile entity is the same as the given one (Only for compatibilty)
     *
     * @param mod the mod to check
     * @param tileEntityClass the class of the tile entity
     * @return the result of the action
     */
    public static ActionResultType openMenu(SupportedMods mod, World level, BlockPos pos, PlayerEntity player, Class<? extends TileEntity> tileEntityClass)
    {
        if(!level.isClientSide && mod.isLoaded())
        {
            TileEntity tileentity = level.getBlockEntity(pos);

            if(CommonUtils.isBlockEntity(tileentity, tileEntityClass))
            {
                NetworkHooks.openGui((ServerPlayerEntity) player, (MultiScreenRecipeCreatorTile) tileentity, pos);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.CONSUME;
    }
}
