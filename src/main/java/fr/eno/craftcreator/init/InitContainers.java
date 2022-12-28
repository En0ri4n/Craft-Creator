package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitContainers
{
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MOD_ID);

	public static final RegistryObject<MenuType<CraftingTableRecipeCreatorContainer>> CRAFTING_TABLE_RECIPE_CREATOR = CONTAINERS.register("crafting_table_recipe_creator", () -> IForgeMenuType.create(CraftingTableRecipeCreatorContainer::new));
	public static final RegistryObject<MenuType<FurnaceRecipeCreatorContainer>> FURNACE_RECIPE_CREATOR = CONTAINERS.register("furnace_recipe_creator", () -> IForgeMenuType.create(FurnaceRecipeCreatorContainer::new));
	public static final RegistryObject<MenuType<StoneCutterRecipeCreatorContainer>> STONE_CUTTER_RECIPE_CREATOR = CONTAINERS.register("stone_cutter_recipe_creator", () -> IForgeMenuType.create(StoneCutterRecipeCreatorContainer::new));
	public static final RegistryObject<MenuType<SmithingTableRecipeCreatorContainer>> SMITHING_TABLE_RECIPE_CREATOR = CONTAINERS.register("smithing_table_recipe_creator", () -> IForgeMenuType.create(SmithingTableRecipeCreatorContainer::new));

	public static final RegistryObject<MenuType<BotaniaRecipeCreatorContainer>> BOTANIA_RECIPE_CREATOR;
    public static final RegistryObject<MenuType<ThermalRecipeCreatorContainer>> THERMAL_RECIPE_CREATOR;

    static
	{
		BOTANIA_RECIPE_CREATOR = SupportedMods.BOTANIA.isLoaded() ? CONTAINERS.register("botania_recipe_creator", () -> IForgeMenuType.create(BotaniaRecipeCreatorContainer::new)) : null;
		THERMAL_RECIPE_CREATOR = SupportedMods.THERMAL.isLoaded() ? CONTAINERS.register("thermal_recipe_creator", () -> IForgeMenuType.create(ThermalRecipeCreatorContainer::new)) : null;
	}
}
