package fr.eno.craftcreator.container;

import fr.eno.craftcreator.container.slot.LockedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.tileentity.FurnaceRecipeCreatorTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.items.SlotItemHandler;

public class FurnaceRecipeCreatorContainer extends CommonContainer
{
	public FurnaceRecipeCreatorContainer(int containerId, Inventory inventory, FriendlyByteBuf packet)
	{
		super(InitContainers.FURNACE_RECIPE_CREATOR.get(), containerId);
		FurnaceRecipeCreatorTile tile = (FurnaceRecipeCreatorTile) inventory.player.level.getBlockEntity(packet.readBlockPos());
		int index = 0;

		this.addSlot(new SimpleSlotItemHandler(tile, index++, 56, 17));
		this.addSlot(new SimpleSlotItemHandler(tile, index++, 116, 35));
		
		ItemStack fuel = new ItemStack(Items.COAL_BLOCK);
		fuel.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 10);
		this.addSlot(new LockedSlot(tile, index, 56, 53, fuel));
		
		this.bindPlayerInventory(inventory);
	}
}