package fr.eno.craftcreator.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class RecipeCreatorBlock extends Block
{
    public RecipeCreatorBlock()
    {
        super(AbstractBlock.Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(999999F));
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager)
    {
        return true;
    }

    @Nonnull
    @Override
    public abstract ActionResultType onBlockActivated(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit);

    @Nonnull
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isTransparent(@Nonnull BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
}
