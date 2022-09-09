package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
