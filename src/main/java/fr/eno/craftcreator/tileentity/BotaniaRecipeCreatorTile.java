package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.*;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.utils.*;
import io.netty.buffer.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.util.text.*;

import javax.annotation.*;

public class BotaniaRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
    private int screenIndex;

    public BotaniaRecipeCreatorTile()
    {
        super(InitTileEntities.BOTANIA_RECIPE_CREATOR.get(), SlotHelper.BOTANIA_SLOTS_SIZE);
        this.screenIndex = 0;
    }

    public void setScreenIndex(int index)
    {
        this.screenIndex = index;
    }

    public int getScreenIndex()
    {
        return this.screenIndex;
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound)
    {
        compound.putInt("ScreenIndex", this.screenIndex);
        return super.write(compound);
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound)
    {
        this.screenIndex = compound.getInt("ScreenIndex");
        super.read(state, compound);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName()
    {
        return References.getTranslate("tile.botania_recipe_creator.name");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player)
    {
        return new BotaniaRecipeCreatorContainer(windowId, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(this.getPos()));
    }
}
