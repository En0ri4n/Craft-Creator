package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class BotaniaRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    public BotaniaRecipeCreatorTile()
    {
        super(InitTileEntities.BOTANIA_RECIPE_CREATOR.get(), SlotHelper.BOTANIA_SLOTS_SIZE);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent(References.getTranslate("tile.botania_recipe_creator.name").getString());
    }

    @Override
    public Container createMenu(int pContainerId, PlayerInventory pInventory, PlayerEntity pPlayer)
    {
        return new BotaniaRecipeCreatorContainer(pContainerId, pInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getPos()));
    }
}
