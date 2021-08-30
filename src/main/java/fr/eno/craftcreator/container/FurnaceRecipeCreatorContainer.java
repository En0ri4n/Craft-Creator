package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.*;
import fr.eno.craftcreator.container.utils.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.tileentity.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraftforge.items.*;

public class FurnaceRecipeCreatorContainer extends CommonContainer
{
	public FurnaceRecipeCreatorContainer(int windowId, PlayerInventory playerInventory, PacketBuffer packet)
	{
		super(InitContainers.FURNACE_RECIPE_CREATOR.get(), windowId, 10);
		FurnaceRecipeCreatorTile tile = (FurnaceRecipeCreatorTile) playerInventory.player.world.getTileEntity(packet.readBlockPos());
		int index = 0;

		this.addSlot(new SlotItemHandler(tile, index++, 56, 17));
		this.addSlot(new SlotItemHandler(tile, index++, 116, 35));
		
		ItemStack fuel = new ItemStack(Items.COAL_BLOCK);
		fuel.addEnchantment(Enchantments.PROTECTION, 10);
		this.addSlot(new LockedSlot(tile, index, 56, 53, fuel));
		
		this.bindPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return true;
	}
}