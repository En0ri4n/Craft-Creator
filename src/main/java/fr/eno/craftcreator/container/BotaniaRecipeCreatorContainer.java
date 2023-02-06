package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.DefinedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import vazkii.botania.api.brew.IBrewItem;

public class BotaniaRecipeCreatorContainer extends CommonContainer
{
    public BotaniaRecipeCreatorContainer(int containerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.BOTANIA_RECIPE_CREATOR.get(), containerId, inventory, packet);

        for(int i = 0; i < SlotHelper.BOTANIA_SLOTS_SIZE; i++)
        {
            if(PositionnedSlot.contains(SlotHelper.PURE_DAISY_SLOTS_INPUT, i) || PositionnedSlot.isValidSlot(SlotHelper.MANA_INFUSION_SLOTS_INPUT, 1, i))
            {
                this.addSlot(new DefinedSlot(tile, i, SlotHelper.BOTANIA_SLOTS.get(i).getxPos(), SlotHelper.BOTANIA_SLOTS.get(i).getyPos(), is -> Block.byItem(is.getItem()) != Blocks.AIR));
                continue;
            }
            else if(PositionnedSlot.contains(SlotHelper.BREWERY_SLOTS_OUTPUT, i))
            {
                this.addSlot(new DefinedSlot(tile, i, SlotHelper.BOTANIA_SLOTS.get(i).getxPos(), SlotHelper.BOTANIA_SLOTS.get(i).getyPos(), is -> is.getItem() instanceof IBrewItem));
                continue;
            }

            this.addSlot(new SimpleSlotItemHandler(tile, i, SlotHelper.BOTANIA_SLOTS.get(i).getxPos(), SlotHelper.BOTANIA_SLOTS.get(i).getyPos()));
        }

        this.bindPlayerInventory(inventory);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.BOTANIA;
    }
}
