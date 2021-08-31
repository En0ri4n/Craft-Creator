package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.CraftingTableRecipeCreatorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.stream.*;

public class CraftingTableRecipeCreatorBlock extends Block
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
		super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(99999F));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager)
	{
		return true;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
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
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
}