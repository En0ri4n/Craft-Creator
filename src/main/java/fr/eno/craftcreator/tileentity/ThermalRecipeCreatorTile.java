package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThermalRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    public ThermalRecipeCreatorTile(BlockPos pos, BlockState state)
    {
        super(InitTileEntities.THERMAL_RECIPE_CREATOR.get(), pos, state, SlotHelper.THERMAL_SLOTS_SIZE);
    }

    @Override
    public Component getDisplayName()
    {
        return new TextComponent(References.getTranslate("tile.thermal_recipe_creator.name").getString());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player)
    {
        return new ThermalRecipeCreatorContainer(containerId, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.getBlockPos()));
    }
}
