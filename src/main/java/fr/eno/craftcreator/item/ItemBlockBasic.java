package fr.eno.craftcreator.item;

import fr.eno.craftcreator.CraftCreator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockBasic extends BlockItem
{
	public ItemBlockBasic(Block blockIn)
	{
		super(blockIn, new Item.Properties().stacksTo(64).tab(CraftCreator.CRAFT_CREATOR_TAB));
	}
}