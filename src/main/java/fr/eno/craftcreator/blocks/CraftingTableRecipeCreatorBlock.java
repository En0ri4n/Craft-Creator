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
import java.util.stream.*;

public class CraftingTableRecipeCreatorBlock extends RecipeCreatorBlock
{
	private static final VoxelShape SHAPE = Stream.of(
			Block.makeCuboidShape(15, 12, 5, 16, 16, 11),
			Block.makeCuboidShape(14, 12, 4, 15, 16, 12),
			Block.makeCuboidShape(13, 12, 2, 14, 16, 14),
			Block.makeCuboidShape(12, 12, 2, 13, 16, 14),
			Block.makeCuboidShape(11, 12, 1, 12, 16, 15),
			Block.makeCuboidShape(5, 12, 0, 11, 16, 16),
			Block.makeCuboidShape(4, 12, 1, 5, 16, 15),
			Block.makeCuboidShape(3, 12, 2, 4, 16, 14),
			Block.makeCuboidShape(2, 12, 2, 3, 16, 14),
			Block.makeCuboidShape(1, 12, 4, 2, 16, 12),
			Block.makeCuboidShape(0, 12, 5, 1, 16, 11),
			Block.makeCuboidShape(7, 5, 9, 9, 7, 11),
			Block.makeCuboidShape(7, 6, 10, 9, 8, 12),
			Block.makeCuboidShape(7, 7, 11, 9, 9, 13),
			Block.makeCuboidShape(7, 8, 12, 9, 10, 14),
			Block.makeCuboidShape(7, 9, 13, 9, 11, 15),
			Block.makeCuboidShape(7, 10, 14, 9, 12, 16),
			Block.makeCuboidShape(0, 10, 7, 2, 12, 9),
			Block.makeCuboidShape(1, 9, 7, 3, 11, 9),
			Block.makeCuboidShape(2, 8, 7, 4, 10, 9),
			Block.makeCuboidShape(3, 7, 7, 5, 9, 9),
			Block.makeCuboidShape(4, 6, 7, 6, 8, 9),
			Block.makeCuboidShape(5, 5, 7, 7, 7, 9),
			Block.makeCuboidShape(14, 10, 7, 16, 12, 9),
			Block.makeCuboidShape(13, 9, 7, 15, 11, 9),
			Block.makeCuboidShape(12, 8, 7, 14, 10, 9),
			Block.makeCuboidShape(11, 7, 7, 13, 9, 9),
			Block.makeCuboidShape(10, 6, 7, 12, 8, 9),
			Block.makeCuboidShape(9, 5, 7, 11, 7, 9),
			Block.makeCuboidShape(7, 10, 0, 9, 12, 2),
			Block.makeCuboidShape(7, 9, 1, 9, 11, 3),
			Block.makeCuboidShape(7, 8, 2, 9, 10, 4),
			Block.makeCuboidShape(7, 7, 3, 9, 9, 5),
			Block.makeCuboidShape(7, 6, 4, 9, 8, 6),
			Block.makeCuboidShape(7, 5, 5, 9, 7, 7),
			Block.makeCuboidShape(7, 2, 7, 9, 12, 9),
			Block.makeCuboidShape(6, 3, 6, 8, 4, 8),
			Block.makeCuboidShape(5, 3, 5, 7, 4, 7),
			Block.makeCuboidShape(4, 2, 4, 6, 3, 6),
			Block.makeCuboidShape(3, 2, 3, 5, 3, 5),
			Block.makeCuboidShape(2, 1, 2, 4, 2, 4),
			Block.makeCuboidShape(1, 1, 1, 3, 2, 3),
			Block.makeCuboidShape(0, 0, 0, 2, 1, 2),
			Block.makeCuboidShape(8, 3, 6, 10, 4, 8),
			Block.makeCuboidShape(9, 3, 5, 11, 4, 7),
			Block.makeCuboidShape(10, 2, 4, 12, 3, 6),
			Block.makeCuboidShape(11, 2, 3, 13, 3, 5),
			Block.makeCuboidShape(12, 1, 2, 14, 2, 4),
			Block.makeCuboidShape(13, 1, 1, 15, 2, 3),
			Block.makeCuboidShape(14, 0, 0, 16, 1, 2),
			Block.makeCuboidShape(8, 3, 8, 10, 4, 10),
			Block.makeCuboidShape(9, 3, 9, 11, 4, 11),
			Block.makeCuboidShape(10, 2, 10, 12, 3, 12),
			Block.makeCuboidShape(11, 2, 11, 13, 3, 13),
			Block.makeCuboidShape(12, 1, 12, 14, 2, 14),
			Block.makeCuboidShape(13, 1, 13, 15, 2, 15),
			Block.makeCuboidShape(14, 0, 14, 16, 1, 16),
			Block.makeCuboidShape(6, 3, 8, 8, 4, 10),
			Block.makeCuboidShape(5, 3, 9, 7, 4, 11),
			Block.makeCuboidShape(4, 2, 10, 6, 3, 12),
			Block.makeCuboidShape(3, 2, 11, 5, 3, 13),
			Block.makeCuboidShape(2, 1, 12, 4, 2, 14),
			Block.makeCuboidShape(1, 1, 13, 3, 2, 15),
			Block.makeCuboidShape(0, 0, 14, 2, 1, 16)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

	public CraftingTableRecipeCreatorBlock()
	{
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context)
	{
		return SHAPE;
	}
	
	@Nonnull
	@Override
	public ActionResultType onBlockActivated(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			
			if(tileentity instanceof CraftingTableRecipeCreatorTile)
			{
				CraftingTableRecipeCreatorTile tile = (CraftingTableRecipeCreatorTile) tileentity;
				
				NetworkHooks.openGui((ServerPlayerEntity) player, tile, pos);
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.CONSUME;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new CraftingTableRecipeCreatorTile();
	}
}