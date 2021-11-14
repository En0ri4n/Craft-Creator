package fr.eno.craftcreator.init;

import fr.eno.craftcreator.*;
import fr.eno.craftcreator.item.*;
import fr.eno.craftcreator.kubejs.utils.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.registries.*;

public class InitItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MOD_ID);
	
	public static final RegistryObject<Item> CRAFTING_TABLE_RECIPE_CREATOR = ITEMS.register("crafting_table_recipe_creator", () -> new ItemBlockBasic(InitBlocks.CRAFTING_TABLE_RECIPE_CREATOR.get()));
	public static final RegistryObject<Item> FURNACE_RECIPE_CREATOR = ITEMS.register("furnace_recipe_creator", () -> new ItemBlockBasic(InitBlocks.FURNACE_RECIPE_CREATOR.get()));
	public static final RegistryObject<Item> STONE_CUTTER_RECIPE_CREATOR = ITEMS.register("stone_cutter_recipe_creator", () -> new ItemBlockBasic(InitBlocks.STONE_CUTTER_RECIPE_CREATOR.get()));
	public static final RegistryObject<Item> SMITHING_TABLE_RECIPE_CREATOR = ITEMS.register("smithing_table_recipe_creator", () -> new ItemBlockBasic(InitBlocks.SMITHING_TABLE_RECIPE_CREATOR.get()));

	public static final RegistryObject<Item> BOTANIA_RECIPE_CREATOR;

	static
	{
		BOTANIA_RECIPE_CREATOR = SupportedMods.isBotaniaLoaded() ? ITEMS.register("botania_recipe_creator", () -> new ItemBlockBasic(InitBlocks.BOTANIA_RECIPE_CREATOR.get())) : null;
	}
}