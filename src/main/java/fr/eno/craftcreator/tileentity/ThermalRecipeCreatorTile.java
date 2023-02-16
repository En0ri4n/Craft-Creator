package fr.eno.craftcreator.tileentity;


import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ThermalRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    private boolean isEnergyMod;

    public ThermalRecipeCreatorTile(BlockPos pos, BlockState state)
    {
        super(SupportedMods.THERMAL, InitTileEntities.THERMAL_RECIPE_CREATOR.get(), SlotHelper.THERMAL_SLOTS_SIZE, pos, state);
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
    public void load(CompoundTag compound)
    {
        super.load(compound);
        this.isEnergyMod = compound.getBoolean("isEnergyMod");
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);
        compoundTag.putBoolean("isEnergyMod", isEnergyMod);
    }

    @Override
    public Component getDisplayName()
    {
        return new TextComponent(References.getTranslate("tile.thermal_recipe_creator.name").getString());
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player)
    {
        return new ThermalRecipeCreatorContainer(containerId, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(getBlockPos()));
    }
}
