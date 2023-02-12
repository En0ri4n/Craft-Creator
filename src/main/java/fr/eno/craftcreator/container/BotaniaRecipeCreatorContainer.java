package fr.eno.craftcreator.container;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.container.slot.DefinedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.slot.utils.DefinedPositionnedSlot;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class BotaniaRecipeCreatorContainer extends CommonContainer
{
    public BotaniaRecipeCreatorContainer(int containerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.BOTANIA_RECIPE_CREATOR.get(), containerId, inventory, packet);

        for(int i = 0; i < SlotHelper.BOTANIA_SLOTS_SIZE; i++)
        {
            PositionnedSlot positionnedSlot = SlotHelper.BOTANIA_SLOTS.get(i);

            int x = SlotHelper.BOTANIA_SLOTS.get(i).getxPos();
            int y = SlotHelper.BOTANIA_SLOTS.get(i).getyPos();

            if(positionnedSlot instanceof DefinedPositionnedSlot)
                this.addSlot(new DefinedSlot(tile, i, x, y, ((DefinedPositionnedSlot) positionnedSlot)::isItemValid));
            else
                this.addSlot(new SimpleSlotItemHandler(tile, i, x, y));
        }

        this.bindPlayerInventory(inventory);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.BOTANIA;
    }
    
    @Override
    public int getContainerSize()
    {
        return SlotHelper.BOTANIA_SLOTS_SIZE;
    }
}
