package fr.eno.craftcreator.api;

import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockUtils
{
    public static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return VoxelShapes.box(x1 / 16.0D, y1 / 16.0D, z1 / 16.0D, x2 / 16.0D, y2 / 16.0D, z2 / 16.0D);
    }

    public static VoxelShape join(VoxelShape shape1, VoxelShape shape2, IBooleanFunction op)
    {
        return VoxelShapes.join(shape1, shape2, op);
    }

    public static ActionResultType openMenu(SupportedMods mod, World level, BlockPos pos, PlayerEntity player, Class<? extends TileEntity> tileEntityClass)
    {
        if(!level.isClientSide && mod.isLoaded())
        {
            TileEntity tileentity = level.getBlockEntity(pos);

            if(tileentity != null && tileentity.getClass().equals(tileEntityClass))
            {
                NetworkHooks.openGui((ServerPlayerEntity) player, (MultiScreenRecipeCreatorTile) tileentity, pos);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.CONSUME;
    }
}
