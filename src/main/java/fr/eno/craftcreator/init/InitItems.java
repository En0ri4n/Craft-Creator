package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.item.ItemBlockBasic;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
		BOTANIA_RECIPE_CREATOR = SupportedMods.BOTANIA.isLoaded() ? ITEMS.register("botania_recipe_creator", () -> new ItemBlockBasic(InitBlocks.BOTANIA_RECIPE_CREATOR.get())) : null;
	}
}