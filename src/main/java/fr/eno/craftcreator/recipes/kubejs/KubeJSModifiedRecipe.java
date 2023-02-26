package fr.eno.craftcreator.recipes.kubejs;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.base.ModifiedRecipe;
import fr.eno.craftcreator.utils.FormattableString;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public class KubeJSModifiedRecipe extends ModifiedRecipe
{
    public static final FormattableString BASE_LINE = FormattableString.of("event.%s(%s)");

    private final KubeJSModifiedRecipeType type;
    private final Map<ModRecipeSerializer.RecipeDescriptors, String> recipeDescriptors;

    public KubeJSModifiedRecipe(KubeJSModifiedRecipeType type)
    {
        this.type = type;
        this.recipeDescriptors = new HashMap<>();
    }

    public KubeJSModifiedRecipe(KubeJSModifiedRecipeType type, Map<ModRecipeSerializer.RecipeDescriptors, String> recipeDescriptors)
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
    
    public IFormattableTextComponent getDisplayTitle()
    {
        if(recipeDescriptors.containsKey(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID))
            return new StringTextComponent(recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID));
        else if(recipeDescriptors.containsKey(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM))
            return new StringTextComponent(recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM));
        else if(recipeDescriptors.containsKey(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM))
            return new StringTextComponent(recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM));
        else if(recipeDescriptors.containsKey(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE))
            return new StringTextComponent(recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE));
        else return new StringTextComponent(recipeDescriptors.getOrDefault(ModRecipeSerializer.RecipeDescriptors.MOD_ID, "Unknown"));
    }

    public KubeJSModifiedRecipeType getType()
    {
        return type;
    }

    @Override
    public FormattableString getBaseLine()
    {
        return BASE_LINE;
    }

    @Override
    public String toJson()
    {
        JsonObject json = new JsonObject();
        for(Map.Entry<ModRecipeSerializer.RecipeDescriptors, String> entry : recipeDescriptors.entrySet())
        {
            json.addProperty(entry.getKey().getTag(), entry.getValue());
        }

        return GSON.toJson(json);
    }

    @Override
    public CompoundNBT serialize()
    {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString("type", type.getDescriptor());
        for(Map.Entry<ModRecipeSerializer.RecipeDescriptors, String> entry : recipeDescriptors.entrySet())
        {
            compoundNBT.putString(entry.getKey().getTag(), entry.getValue());
        }
        return compoundNBT;
    }

    public static KubeJSModifiedRecipe deserialize(CompoundNBT compound)
    {
        KubeJSModifiedRecipeType type = KubeJSModifiedRecipeType.byDescriptor(compound.getString("type"));
        if(type != null)
        {
            KubeJSModifiedRecipe recipe = new KubeJSModifiedRecipe(type);
            for(ModRecipeSerializer.RecipeDescriptors descriptor : ModRecipeSerializer.RecipeDescriptors.values())
            {
                if(compound.contains(descriptor.getTag()))
                    recipe.setDescriptor(descriptor, compound.getString(descriptor.getTag()));
            }
            return recipe;
        }
        return null;
    }


    public enum KubeJSModifiedRecipeType
    {
        REMOVED("remove", new StringTextComponent("Removed").withStyle(TextFormatting.RED)),
        REPLACED_INPUT("replaceInput", new StringTextComponent("Input Replaced").withStyle(TextFormatting.GOLD)),
        REPLACED_OUTPUT("replaceOutput", new StringTextComponent("Output Replaced").withStyle(TextFormatting.YELLOW));

        public static final String CUSTOM = "custom";
        private final String descriptor;
        private final IFormattableTextComponent title;

        KubeJSModifiedRecipeType(String descriptor, IFormattableTextComponent title)
        {
            this.descriptor = descriptor;
            this.title = title;
        }

        public String getDescriptor()
        {
            return descriptor;
        }

        public static KubeJSModifiedRecipeType byDescriptor(String descriptor)
        {
            for(KubeJSModifiedRecipeType type : KubeJSModifiedRecipeType.values())
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
