package fr.eno.craftcreator.container;


import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MinecraftRecipeCreatorContainer extends CommonContainer
{
    public MinecraftRecipeCreatorContainer(int containerId, Inventory inventory, FriendlyByteBuf packet)
    {
        super(InitContainers.MINECRAFT_RECIPE_CREATOR.get(), containerId, inventory, packet);

        for(int i = 0; i < SlotHelper.MINECRAFT_SLOTS_SIZE; ++i)
        {
            int x = SlotHelper.MINECRAFT_SLOTS.get(i).getxPos();
            int y = SlotHelper.MINECRAFT_SLOTS.get(i).getyPos();

            this.addSlot(new SimpleSlotItemHandler(this.tile, i, x, y));
        }

        this.bindInventory(inventory);

        activeSlots(true);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.MINECRAFT;
    }
    
    @Override
    public int getContainerSize()
    {
        return SlotHelper.MINECRAFT_SLOTS_SIZE;
    }
}