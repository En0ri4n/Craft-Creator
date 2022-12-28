package fr.eno.craftcreator.item;

import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.init.InitBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockBasic extends BlockItem
{
	public ItemBlockBasic(Block blockIn)
	{
		super(blockIn, new Item.Properties().stacksTo(64).tab(CraftCreator.CRAFT_CREATOR_TAB));
	}

	@Override
	public Component getHighlightTip(ItemStack item, Component displayName)
	{
		return getBlock() == InitBlocks.THERMAL_RECIPE_CREATOR.get() ? new TextComponent(displayName.getString() + "§4 - In Development... - Use at your own risk !") : displayName;
	}

	@Override
	public Component getDescription()
	{
		return getBlock() == InitBlocks.THERMAL_RECIPE_CREATOR.get() ? new TextComponent("§4In Development...\nUse at your own risk !") : new TextComponent("");
	}
}