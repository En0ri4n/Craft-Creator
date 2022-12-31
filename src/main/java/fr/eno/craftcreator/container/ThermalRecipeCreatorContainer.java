package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.DefinedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ThermalRecipeCreatorContainer extends CommonContainer
{
    public ThermalRecipeCreatorContainer(int containerId, Inventory inventory, FriendlyByteBuf packet)
    {
        super(InitContainers.THERMAL_RECIPE_CREATOR.get(), containerId, inventory, packet);

        for(int i = 0; i < SlotHelper.THERMAL_SLOTS_SIZE; i++)
        {
            if(PositionnedSlot.contains(SlotHelper.TREE_EXTRACTOR_SLOTS, i))
            {
                if(PositionnedSlot.isSlotOutput(SlotHelper.TREE_EXTRACTOR_SLOTS, i))
                {
                    this.addSlot(new DefinedSlot(tile, i, SlotHelper.THERMAL_SLOTS.get(i).getxPos(), SlotHelper.THERMAL_SLOTS.get(i).getyPos(), stack -> stack.getItem() instanceof BucketItem));
                    continue;
                }
                this.addSlot(new DefinedSlot(tile, i, 100, 50, is -> Block.byItem(is.getItem()) != Blocks.AIR));
                continue;
            }

            this.addSlot(new SimpleSlotItemHandler(tile, i, 40, 40));
        }

        this.bindPlayerInventory(inventory);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.THERMAL;
    }
}
