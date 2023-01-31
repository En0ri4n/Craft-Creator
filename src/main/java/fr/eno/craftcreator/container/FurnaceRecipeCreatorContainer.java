package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.LockedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;

public class FurnaceRecipeCreatorContainer extends CommonContainer
{
	public FurnaceRecipeCreatorContainer(int containerId, PlayerInventory inventory, PacketBuffer byteBuf)
	{
		super(InitContainers.FURNACE_RECIPE_CREATOR.get(), containerId, inventory, byteBuf);

		for(int i = 0; i < SlotHelper.FURNACE_SLOTS_SIZE; i++)
		{
			if(i == SlotHelper.FURNACE_SLOTS_SIZE - 1)
			{
				ItemStack fuel = new ItemStack(Items.COAL_BLOCK, 1);
				fuel.addEnchantment(Enchantments.PROTECTION, 10);
				this.addSlot(new LockedSlot(tile, i, 56, 53, fuel));
			}
			this.addSlot(new SimpleSlotItemHandler(tile, i, SlotHelper.FURNACE_SLOTS.get(i).getxPos(), SlotHelper.FURNACE_SLOTS.get(i).getyPos()));
		}

		this.bindPlayerInventory(inventory);

		activeSlots(true);
	}

	@Override
	public SupportedMods getMod()
	{
		return SupportedMods.MINECRAFT;
	}
}