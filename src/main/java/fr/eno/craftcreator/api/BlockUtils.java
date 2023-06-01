package fr.eno.craftcreator.api;


import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class BlockUtils
{
    /**
     * Create a VoxelShape from a box (from 0 to 16) (Only for compatibilty)
     *
     * @return the VoxelShape with the given size
     * @see net.minecraft.world.phys.shapes.Shapes#box(double, double, double, double, double, double)
     */
    public static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return Shapes.box(x1 / 16.0D, y1 / 16.0D, z1 / 16.0D, x2 / 16.0D, y2 / 16.0D, z2 / 16.0D);
    }

    /**
     * Create a VoxelShape from a box (from 0 to 1) (Only for compatibilty)
     *
     * @return the VoxelShape with the given size
     * @see net.minecraft.world.phys.shapes.Shapes#box(double, double, double, double, double, double)
     */
    public static VoxelShape create(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return Shapes.box(x1, y1, z1, x2, y2, z2);
    }

    /**
     * Join two VoxelShape with the given IBooleanFunction (Only for compatibilty)
     *
     * @return the joined VoxelShape
     * @see Shapes#join(VoxelShape, VoxelShape, net.minecraft.world.phys.shapes.BooleanOp)
     */
    public static VoxelShape join(VoxelShape shape1, VoxelShape shape2, BooleanOp op)
    {
        return Shapes.join(shape1, shape2, op);
    }

    /**
     * Open the GUI if the mod is loaded and the tile entity is the same as the given one (Only for compatibilty)
     *
     * @param mod             the mod to check
     * @param tileEntityClass the class of the tile entity
     * @return the result of the action
     */
    public static InteractionResult openMenu(SupportedMods mod, Level level, BlockPos pos, Player player, Class<? extends BlockEntity> tileEntityClass)
    {
        if(!level.isClientSide && mod.isLoaded())
        {
            BlockEntity tileentity = level.getBlockEntity(pos);

            if(CommonUtils.isBlockEntity(tileentity, tileEntityClass))
            {
                NetworkHooks.openGui((ServerPlayer) player, (MultiScreenRecipeCreatorTile) tileentity, pos);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.CONSUME;
    }
}
