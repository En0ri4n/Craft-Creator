package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.base.VanillaCommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class RecipeModifierContainer extends VanillaCommonContainer
{
    public RecipeModifierContainer(int pContainerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.RECIPE_MODIFIER.get(), pContainerId);

        this.bindPlayerInventory(inventory, 61, 36);
    }

    public static RecipeModifierContainer create(PlayerEntity player)
    {
        return new RecipeModifierContainer(111, player.inventory, null);
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return true;
    }
}
