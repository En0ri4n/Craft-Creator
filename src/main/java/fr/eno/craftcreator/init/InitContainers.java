package fr.eno.craftcreator.init;


import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitContainers
{
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MOD_ID);

	public static final RegistryObject<MenuType<RecipeModifierContainer>> RECIPE_MODIFIER = register("recipe_modifier", RecipeModifierContainer::new);

	public static final RegistryObject<MenuType<MinecraftRecipeCreatorContainer>> MINECRAFT_RECIPE_CREATOR = register("minecraft_recipe_creator", MinecraftRecipeCreatorContainer::new);
	public static final RegistryObject<MenuType<BotaniaRecipeCreatorContainer>> BOTANIA_RECIPE_CREATOR = register("botania_recipe_creator", BotaniaRecipeCreatorContainer::new);
    public static final RegistryObject<MenuType<ThermalRecipeCreatorContainer>> THERMAL_RECIPE_CREATOR = register("thermal_recipe_creator", ThermalRecipeCreatorContainer::new);
	public static final RegistryObject<MenuType<CreateRecipeCreatorContainer>> CREATE_RECIPE_CREATOR = register("create_recipe_creator", CreateRecipeCreatorContainer::new);

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String registryName, IContainerFactory<T> container)
	{
		return CONTAINERS.register(registryName, () -> IForgeMenuType.create(container));
	}
}
