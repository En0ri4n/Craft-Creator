package fr.eno.craftcreator.item;

import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.base.SupportedMods;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class ItemBlockBasic extends BlockItem
{
	public ItemBlockBasic(SupportedMods mod, Block blockIn)
	{
		super(blockIn, getProperties(mod));
	}

	private static Item.Properties getProperties(SupportedMods mod)
	{
		Item.Properties properties = new Item.Properties();
		properties.stacksTo(64);
		properties.rarity(Rarity.EPIC);
		if(mod.isLoaded())
			properties.tab(CraftCreator.CRAFT_CREATOR_TAB);

		return properties;
	}
}