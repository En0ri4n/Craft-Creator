package fr.eno.craftcreator.container;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.container.utils.VanillaCommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

@SuppressWarnings("unused")
public class RecipeModifierContainer extends VanillaCommonContainer
{
    public RecipeModifierContainer(int pContainerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.RECIPE_MODIFIER.get(), pContainerId);

        this.bindPlayerInventory(inventory, 61, 36);
    }

    public static RecipeModifierContainer create()
    {
        return new RecipeModifierContainer(111, ClientUtils.getClientPlayer().inventory, null);
    }
}
