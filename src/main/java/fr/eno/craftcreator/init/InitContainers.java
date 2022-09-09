package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitContainers
{
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MOD_ID);

	public static final RegistryObject<ContainerType<CraftingTableRecipeCreatorContainer>> CRAFTING_TABLE_RECIPE_CREATOR = CONTAINERS.register("crafting_table_recipe_creator", () -> IForgeContainerType.create(CraftingTableRecipeCreatorContainer::new));
	public static final RegistryObject<ContainerType<FurnaceRecipeCreatorContainer>> FURNACE_RECIPE_CREATOR = CONTAINERS.register("furnace_recipe_creator", () -> IForgeContainerType.create(FurnaceRecipeCreatorContainer::new));
	public static final RegistryObject<ContainerType<StoneCutterRecipeCreatorContainer>> STONE_CUTTER_RECIPE_CREATOR = CONTAINERS.register("stone_cutter_recipe_creator", () -> IForgeContainerType.create(StoneCutterRecipeCreatorContainer::new));
	public static final RegistryObject<ContainerType<SmithingTableRecipeCreatorContainer>> SMITHING_TABLE_RECIPE_CREATOR = CONTAINERS.register("smithing_table_recipe_creator", () -> IForgeContainerType.create(SmithingTableRecipeCreatorContainer::new));

	public static final RegistryObject<ContainerType<BotaniaRecipeCreatorContainer>> BOTANIA_RECIPE_CREATOR;

	static
	{
		BOTANIA_RECIPE_CREATOR = SupportedMods.isBotaniaLoaded() ? CONTAINERS.register("botania_recipe_creator", () -> IForgeContainerType.create(BotaniaRecipeCreatorContainer::new)) : null;
	}
}
