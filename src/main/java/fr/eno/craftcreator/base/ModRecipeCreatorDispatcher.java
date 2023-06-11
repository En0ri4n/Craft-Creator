package fr.eno.craftcreator.base;

import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.BotaniaRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.CreateRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.ThermalRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import net.minecraft.item.crafting.IRecipe;

public class ModRecipeCreatorDispatcher
{
    /**
     * Get the serializer for the mod
     *
     * @param modId the mod id
     * @return the serializer for the mod (Minecraft Serializer if the mod is not supported, avoiding null value)
     */
    public static ModRecipeSerializer getSeralizer(String modId)
    {
        switch(SupportedMods.getMod(modId))
        {
            case BOTANIA:
                return BotaniaRecipeSerializer.get();
            case THERMAL:
                return ThermalRecipeSerializer.get();
            case CREATE:
                return CreateRecipeSerializer.get();
            default:
                return MinecraftRecipeSerializer.get();
        }
    }

    /**
     * Get the output of the recipe
     *
     * @param recipe the recipe
     * @return the output of the recipe (empty if the mod is not supported)
     */
    public static CraftIngredients getOutput(IRecipe<?> recipe)
    {
        String modId = CommonUtils.getRecipeTypeName(recipe.getType()).getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getOutput(recipe);
        }

        return CraftIngredients.create();
    }

    /**
     * Get the inputs of the recipe
     *
     * @param recipe the recipe
     * @return the inputs of the recipe (empty if the mod is not supported)
     */
    public static CraftIngredients getInputs(IRecipe<?> recipe)
    {
        String modId = CommonUtils.getRecipeTypeName(recipe.getType()).getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getInput(recipe);
        }

        return CraftIngredients.create();
    }
}
