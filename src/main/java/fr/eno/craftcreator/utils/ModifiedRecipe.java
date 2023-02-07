package fr.eno.craftcreator.utils;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.recipes.serializers.ModRecipeSerializer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public class ModifiedRecipe
{
    private final ModifiedRecipeType type;
    private final Map<ModRecipeSerializer.RecipeDescriptors, String> recipeDescriptors;

    public ModifiedRecipe(ModifiedRecipeType type)
    {
        this.type = type;
        this.recipeDescriptors = new HashMap<>();
    }

    public ModifiedRecipe(ModifiedRecipeType type, Map<ModRecipeSerializer.RecipeDescriptors, String> recipeDescriptors)
    {
        this.type = type;
        this.recipeDescriptors = recipeDescriptors;
    }

    public String getInputItem()
    {
        return recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM);
    }

    public String getOutputItem()
    {
        return recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM);
    }

    public String getModId()
    {
        return recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.MOD_ID);
    }

    public String getRecipeType()
    {
        return recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE);
    }

    public String getRecipeId()
    {
        return recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID);
    }

    public <C extends IInventory> IRecipe<C> getRecipeIfExists()
    {
        return null;
    }

    public void setDescriptor(ModRecipeSerializer.RecipeDescriptors descriptor, String value)
    {
        recipeDescriptors.put(descriptor, value);
    }

    public Map<ModRecipeSerializer.RecipeDescriptors, String> getRecipeMap()
    {
        return this.recipeDescriptors;
    }

    public ModifiedRecipeType getType()
    {
        return type;
    }

    public JsonObject toJson()
    {
        JsonObject json = new JsonObject();
        json.addProperty("type", type.name());
        for(Map.Entry<ModRecipeSerializer.RecipeDescriptors, String> entry : recipeDescriptors.entrySet())
        {
            json.addProperty(entry.getKey().getTag(), entry.getValue());
        }

        return json;
    }


    public enum ModifiedRecipeType
    {
        REMOVED("remove", new StringTextComponent("Removed").withStyle(TextFormatting.RED)),
        REPLACED_INPUT("replaceInput", new StringTextComponent("Input Replaced").withStyle(TextFormatting.GOLD)),
        REPLACED_OUTPUT("replaceOutput", new StringTextComponent("Output Replaced").withStyle(TextFormatting.YELLOW));

        private final String descriptor;
        private final IFormattableTextComponent title;

        ModifiedRecipeType(String descriptor, IFormattableTextComponent title)
        {
            this.descriptor = descriptor;
            this.title = title;
        }

        public String getDescriptor()
        {
            return descriptor;
        }

        public static ModifiedRecipeType byDescriptor(String descriptor)
        {
            for(ModifiedRecipeType type : ModifiedRecipeType.values())
            {
                if(type.getDescriptor().equals(descriptor)) return type;
            }

            return null;
        }

        public static boolean isModifiedRecipeType(String descriptor)
        {
            return byDescriptor(descriptor) != null;
        }

        public IFormattableTextComponent getTitle()
        {
            return title;
        }
    }
}
