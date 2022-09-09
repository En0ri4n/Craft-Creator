package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.SmithingTableRecipeCreatorTile;
import net.minecraft.block.BlockState;
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

import javax.annotation.Nonnull;

public class SmithingTableRecipeCreatorBlock extends RecipeCreatorBlock
{
	public SmithingTableRecipeCreatorBlock()
	{
	}
	
	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			
			if(tileentity instanceof SmithingTableRecipeCreatorTile)
			{
				SmithingTableRecipeCreatorTile tile = (SmithingTableRecipeCreatorTile) tileentity;

				NetworkHooks.openGui((ServerPlayerEntity) player, tile, pos);
                return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.CONSUME;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new SmithingTableRecipeCreatorTile();
	}
}