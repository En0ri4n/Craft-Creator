package fr.eno.craftcreator.client.screen.container.base;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

public abstract class ModRecipeCreatorDataScreen<T extends Container> extends ContainerScreen<T>
{
    public ModRecipeCreatorDataScreen(T p_97741_, PlayerInventory p_97742_, ITextComponent p_97743_)
    {
        super(p_97741_, p_97742_, p_97743_);
    }

    public abstract void setData(String dataName, Object data);
}
