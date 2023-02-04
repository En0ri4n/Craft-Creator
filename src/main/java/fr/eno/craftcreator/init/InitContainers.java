package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.container.MinecraftRecipeCreatorContainer;
import fr.eno.craftcreator.container.RecipeModifierContainer;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitContainers
{
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MOD_ID);

	public static final RegistryObject<ContainerType<RecipeModifierContainer>> RECIPE_MODIFIER = register("recipe_modifier", RecipeModifierContainer::new);

	public static final RegistryObject<ContainerType<MinecraftRecipeCreatorContainer>> MINECRAFT_RECIPE_CREATOR = register("minecraft_recipe_creator", MinecraftRecipeCreatorContainer::new);
	public static final RegistryObject<ContainerType<BotaniaRecipeCreatorContainer>> BOTANIA_RECIPE_CREATOR = register("botania_recipe_creator", BotaniaRecipeCreatorContainer::new);
    public static final RegistryObject<ContainerType<ThermalRecipeCreatorContainer>> THERMAL_RECIPE_CREATOR = register("thermal_recipe_creator", ThermalRecipeCreatorContainer::new);

	private static <T extends Container> RegistryObject<ContainerType<T>> register(String registryName, IContainerFactory<T> container)
	{
		return CONTAINERS.register(registryName, () -> IForgeContainerType.create(container));
	}
}
