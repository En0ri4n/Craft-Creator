package fr.eno.craftcreator.container;


import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.container.base.VanillaCommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class RecipeModifierContainer extends VanillaCommonContainer
{
    public RecipeModifierContainer(int pContainerId, Inventory inventory, FriendlyByteBuf packet)
    {
        super(InitContainers.RECIPE_MODIFIER.get(), pContainerId);

        this.bindInventory(inventory, 61, 36);
    }

    public static RecipeModifierContainer create()
    {
        return new RecipeModifierContainer(111, ClientUtils.getClientPlayer().getInventory(), null);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return true;
    }
}
