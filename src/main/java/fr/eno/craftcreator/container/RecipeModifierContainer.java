package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.utils.VanillaCommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.ClientUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("unused")
public class RecipeModifierContainer extends VanillaCommonContainer
{
    public RecipeModifierContainer(int pContainerId, Inventory inventory, FriendlyByteBuf packet)
    {
        super(InitContainers.RECIPE_MODIFIER.get(), pContainerId);

        this.bindPlayerInventory(inventory, 61, 36);
    }

    public static RecipeModifierContainer create()
    {
        return new RecipeModifierContainer(111, ClientUtils.getClientPlayer().getInventory(), null);
    }
}
