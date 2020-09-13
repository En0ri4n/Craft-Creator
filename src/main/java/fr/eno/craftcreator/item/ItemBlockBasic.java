package fr.eno.craftcreator.item;

import fr.eno.craftcreator.CraftCreator;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ItemBlockBasic extends BlockItem
{
	public ItemBlockBasic(Block blockIn)
	{
		super(blockIn, new Item.Properties().maxStackSize(64).group(CraftCreator.CRAFT_CREATOR_TAB));
	}
}