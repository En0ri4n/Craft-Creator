package fr.eno.craftcreator.tileentity.base;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class InventoryDataContainerTileEntity extends InventoryContainerTileEntity
{
    public InventoryDataContainerTileEntity(BlockEntityType<?> pType, int inventorySize, BlockPos pos, BlockState state)
    {
        super(pType, inventorySize, pos, state);
    }

    public abstract void setData(String dataName, Object data);

    public abstract Object getData(String dataName);
}
