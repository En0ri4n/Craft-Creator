package fr.eno.craftcreator.tileentity.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class InventoryDataContainerTileEntity extends InventoryContainerTileEntity
{
    public InventoryDataContainerTileEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, int inventorySize)
    {
        super(pType, pWorldPosition, pBlockState, inventorySize);
    }

    public abstract void setData(String dataName, Object data);

    public abstract Object getData(String dataName);
}
