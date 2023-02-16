package fr.eno.craftcreator.tileentity;


import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class CreateRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    public CreateRecipeCreatorTile(BlockPos pos, BlockState state)
    {
        super(SupportedMods.CREATE, InitTileEntities.CREATE_RECIPE_CREATOR.get(), SlotHelper.CREATE_SLOTS_SIZE, pos, state);
    }

    @Override
    public Component getDisplayName()
    {
        return References.getTranslate("container.create_recipe_creator.title");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
    {
        return new CreateRecipeCreatorContainer(containerId, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(getBlockPos()));
    }
}
