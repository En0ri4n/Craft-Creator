package fr.eno.craftcreator.recipes.kubejs;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.base.ModifiedRecipe;
import fr.eno.craftcreator.utils.FormattableString;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

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

    public <C extends Container> Recipe<C> getRecipeIfExists()
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
    
    public MutableComponent getDisplayTitle()
    {
        if(recipeDescriptors.containsKey(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID))
            return new TextComponent(recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID));
        else if(recipeDescriptors.containsKey(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM))
            return new TextComponent(recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM));
        else if(recipeDescriptors.containsKey(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM))
            return new TextComponent(recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM));
        else if(recipeDescriptors.containsKey(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE))
            return new TextComponent(recipeDescriptors.get(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE));
        else return new TextComponent(recipeDescriptors.getOrDefault(ModRecipeSerializer.RecipeDescriptors.MOD_ID, "Unknown"));
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
    public CompoundTag serialize()
    {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putString("type", type.getDescriptor());
        for(Map.Entry<ModRecipeSerializer.RecipeDescriptors, String> entry : recipeDescriptors.entrySet())
        {
            compoundNBT.putString(entry.getKey().getTag(), entry.getValue());
        }
        return compoundNBT;
    }

    public static KubeJSModifiedRecipe deserialize(CompoundTag compound)
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
        REMOVED("remove", new TextComponent("Removed").withStyle(ChatFormatting.RED)),
        REPLACED_INPUT("replaceInput", new TextComponent("Input Replaced").withStyle(ChatFormatting.GOLD)),
        REPLACED_OUTPUT("replaceOutput", new TextComponent("Output Replaced").withStyle(ChatFormatting.YELLOW));

        public static final String CUSTOM = "custom";
        private final String descriptor;
        private final MutableComponent title;

        KubeJSModifiedRecipeType(String descriptor, MutableComponent title)
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

        public MutableComponent getTitle()
        {
            return title;
        }
    }
}
