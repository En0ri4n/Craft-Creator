package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ThermalRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    private boolean isEnergyMod;

    public ThermalRecipeCreatorTile()
    {
        super(SupportedMods.THERMAL, InitTileEntities.THERMAL_RECIPE_CREATOR.get(), SlotHelper.THERMAL_SLOTS_SIZE);
        isEnergyMod = false;
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);

        if(dataName.equals("is_energy_mod"))
            isEnergyMod = (boolean) data;
    }

    @Override
    public Object getData(String dataName)
    {
        if(dataName.equals("is_energy_mod"))
            return isEnergyMod;

        return super.getData(dataName);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound)
    {
        super.load(state, compound);
        this.isEnergyMod = compound.getBoolean("isEnergyMod");
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundTag)
    {
        compoundTag.putBoolean("isEnergyMod", isEnergyMod);
        return super.save(compoundTag);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent(References.getTranslate("tile.thermal_recipe_creator.name").getString());
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory inventory, PlayerEntity player)
    {
        return new ThermalRecipeCreatorContainer(containerId, inventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getBlockPos()));
    }
}
