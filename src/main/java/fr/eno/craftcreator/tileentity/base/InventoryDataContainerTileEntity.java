package fr.eno.craftcreator.tileentity.base;

import net.minecraft.tileentity.TileEntityType;

public abstract class InventoryDataContainerTileEntity extends InventoryContainerTileEntity
{
    public InventoryDataContainerTileEntity(TileEntityType<?> pType, int inventorySize)
    {
        super(pType, inventorySize);
    }

    public abstract void setData(String dataName, Object data);

    public abstract Object getData(String dataName);
}
