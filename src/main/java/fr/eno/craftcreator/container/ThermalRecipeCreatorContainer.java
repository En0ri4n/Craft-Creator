package fr.eno.craftcreator.container;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.container.slot.DefinedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.network.PacketBuffer;

public class ThermalRecipeCreatorContainer extends CommonContainer
{
    public ThermalRecipeCreatorContainer(int containerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.THERMAL_RECIPE_CREATOR.get(), containerId, inventory, packet);

        for(int i = 0; i < SlotHelper.THERMAL_SLOTS_SIZE; i++)
        {
            int x = SlotHelper.THERMAL_SLOTS.get(i).getxPos();
            int y = SlotHelper.THERMAL_SLOTS.get(i).getyPos();

            if(PositionnedSlot.isValidSlot(SlotHelper.PRESS_SLOTS_INPUT, 1, i))
            {
                this.addSlot(new DefinedSlot(tile, i, x, y, is -> is.getItem().getTags().contains(ClientUtils.parse("thermal:crafting/dies"))));
                continue;
            }

            if(PositionnedSlot.contains(SlotHelper.TREE_EXTRACTOR_SLOTS, i))
            {
                if(PositionnedSlot.contains(SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT, i))
                {
                    this.addSlot(new DefinedSlot(tile, i, x, y, stack -> stack.getItem() instanceof BucketItem));
                    continue;
                }
                this.addSlot(new DefinedSlot(tile, i, x, y, is -> Block.byItem(is.getItem()) != Blocks.AIR));
                continue;
            }

            this.addSlot(new SimpleSlotItemHandler(tile, i, x, y));
        }

        this.bindPlayerInventory(inventory, 60, 90);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.THERMAL;
    }
    
    @Override
    public int getContainerSize()
    {
        return SlotHelper.THERMAL_SLOTS_SIZE;
    }
}
