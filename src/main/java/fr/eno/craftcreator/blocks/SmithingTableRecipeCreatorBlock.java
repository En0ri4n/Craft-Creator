package fr.eno.craftcreator.blocks;

import fr.eno.craftcreator.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.particle.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.network.*;

import javax.annotation.*;

public class SmithingTableRecipeCreatorBlock extends Block
{
	public SmithingTableRecipeCreatorBlock()
	{
		super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(99999F));
	}

	@Override
	public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager)
	{
		return true;
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
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
}