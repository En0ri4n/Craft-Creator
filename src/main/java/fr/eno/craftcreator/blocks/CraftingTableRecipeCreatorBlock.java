package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.CraftingTableRecipeCreatorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CraftingTableRecipeCreatorBlock extends Block
{
	public CraftingTableRecipeCreatorBlock()
	{
		super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(99999F));
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