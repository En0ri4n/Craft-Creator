package fr.eno.craftcreator.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public abstract class RecipeCreatorBlock extends BaseEntityBlock
{
    public RecipeCreatorBlock()
    {
        super(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.STONE).strength(999999F));
    }

    @Nonnull
    @Override
    public abstract InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit);

    @Nonnull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState pState)
    {
        return RenderShape.MODEL;
    }
    @org.jetbrains.annotations.Nullable
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState);
}
