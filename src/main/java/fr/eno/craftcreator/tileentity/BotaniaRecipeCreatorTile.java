package fr.eno.craftcreator.tileentity;


import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class BotaniaRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    public BotaniaRecipeCreatorTile(BlockPos pos, BlockState state)
    {
        super(SupportedMods.BOTANIA, InitTileEntities.BOTANIA_RECIPE_CREATOR.get(), SlotHelper.BOTANIA_SLOTS_SIZE, pos, state);
    }

    @Nonnull
    @Override
    public Component getDisplayName()
    {
        return new TextComponent(References.getTranslate("tile.botania_recipe_creator.name").getString());
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer)
    {
        return new BotaniaRecipeCreatorContainer(pContainerId, pInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(getBlockPos()));
    }
}
