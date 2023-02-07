package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.item.ItemBlockBasic;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class InitItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MOD_ID);
	
	public static final RegistryObject<Item> MINECRAFT_RECIPE_CREATOR = register("minecraft_recipe_creator", () -> new ItemBlockBasic(SupportedMods.MINECRAFT, InitBlocks.MINECRAFT_RECIPE_CREATOR.get()));
	public static final RegistryObject<Item> BOTANIA_RECIPE_CREATOR = register("botania_recipe_creator", () -> new ItemBlockBasic(SupportedMods.BOTANIA, InitBlocks.BOTANIA_RECIPE_CREATOR.get()));
	public static final RegistryObject<Item> THERMAL_RECIPE_CREATOR = register("thermal_recipe_creator", () -> new ItemBlockBasic(SupportedMods.THERMAL, InitBlocks.THERMAL_RECIPE_CREATOR.get()));

	private static RegistryObject<Item> register(String registryName, Supplier<Item> item)
	{
		return ITEMS.register(registryName, item);
	}
}