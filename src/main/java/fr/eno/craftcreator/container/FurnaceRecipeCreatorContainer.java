package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.LockedSlot;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.FurnaceRecipeCreatorTile;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;

public class FurnaceRecipeCreatorContainer extends CommonContainer
{
	public FurnaceRecipeCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
	{
		super(InitContainers.FURNACE_RECIPE_CREATOR.get(), windowId, 10);
		FurnaceRecipeCreatorTile tile = (FurnaceRecipeCreatorTile) playerInventory.player.world.getTileEntity(packet.readBlockPos());
		int index = 0;

		this.addSlot(new Slot(tile, index++, 56, 17));
		this.addSlot(new Slot(tile, index++, 116, 35));
		
		ItemStack fuel = new ItemStack(Items.COAL_BLOCK);
		fuel.addEnchantment(Enchantments.PROTECTION, 10);
		this.addSlot(new LockedSlot(tile, index++, 56, 53, fuel));
		
		this.bindPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return true;
	}
}