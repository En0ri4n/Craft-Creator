package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.DefinedSlot;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.botania.api.brew.IBrewItem;

import javax.annotation.Nonnull;

public class BotaniaRecipeCreatorContainer extends CommonContainer
{
    public BotaniaRecipeCreatorTile tile;

    public BotaniaRecipeCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
    {
        super(InitContainers.BOTANIA_RECIPE_CREATOR.get(), windowId, 10);
        tile = (BotaniaRecipeCreatorTile) playerInventory.player.world.getTileEntity(packet.readBlockPos());

        for(int i = 0; i < SlotHelper.BOTANIA_SLOTS_SIZE; i++)
        {
            if(PositionnedSlot.contains(SlotHelper.PURE_DAISY_SLOTS, i) || PositionnedSlot.isValidSlot(SlotHelper.MANA_INFUSION_SLOTS, 1, i))
            {
                this.addSlot(new DefinedSlot(tile, i, -1000, -1000, is -> Block.getBlockFromItem(is.getItem()) != Blocks.AIR));
                continue;
            }
            else if(PositionnedSlot.isSlotOutput(SlotHelper.BREWERY_SLOTS, i))
            {
                this.addSlot(new DefinedSlot(tile, i, -1000, -1000, is -> is.getItem() instanceof IBrewItem));
                continue;
            }

            this.addSlot(new SlotItemHandler(tile, i, -1000, -1000));
        }

        this.bindPlayerInventory(playerInventory);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn)
    {
        return true;
    }
}
