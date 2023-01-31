package fr.eno.craftcreator.recipes.serializers;

import com.google.common.collect.Multimap;
import com.google.gson.*;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.RecipeFileUtils;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@SuppressWarnings("ALL")
public abstract class ModRecipesJSSerializer
{
    protected static final Gson gson = new GsonBuilder().create();
    protected final SupportedMods mod;

    public ModRecipesJSSerializer(SupportedMods mod)
    {
        this.mod = mod;
    }

    public void removeRecipe(ModifiedRecipe modifiedRecipe)
    {
        StringBuilder line = new StringBuilder("event.remove({");// id: '" + getRecipe(type, result).getId() + "' })";
        for(Map.Entry<RecipeDescriptors, String> entry : modifiedRecipe.getRecipeMap().entrySet())
        {
            line.append("\"").append(entry.getKey().getTag()).append("\": '").append(entry.getValue()).append("',");
        }

        String finishedLine = line.replace(line.length() - 1, line.length(), "})").toString();

        if(!RecipeFileUtils.isModifiedRecipePresent(modifiedRecipe))
            RecipeFileUtils.insertAndWriteLinesToRemoveRecipe(this.mod.getModId(), finishedLine);
    }

    public static <C extends IInventory, T extends IRecipe<C>> void removeAddedRecipe(IRecipe<C> recipe)
    {
        RecipeFileUtils.removeAddedRecipe(recipe, recipe.getId().getNamespace());
    }

    public static void removeModifiedRecipe(ModifiedRecipe recipe)
    {
        RecipeFileUtils.removeModifiedRecipe(recipe);
    }

    private <C extends IInventory, T extends IRecipe<C>> IRecipe<C> getRecipe(IRecipeType<T> type, IItemProvider result)
    {
        RecipeManager manager = ClientUtils.getMinecraft().world.getRecipeManager();
        return manager.getRecipesForType(type).stream().filter(recipe -> recipe.getRecipeOutput().getItem() == result.asItem()).findFirst().orElse(null);
    }
    protected void addRecipeToKubeJS(String recipeJson, IRecipeType<?> recipeType, ResourceLocation result)
    {
        RecipeFileUtils.insertAndWriteLines(this.mod.getModId(), recipeType, "event.custom(" + recipeJson + ")");
        sendSuccessMessage(recipeType, result);
    }

    private void sendSuccessMessage(IRecipeType<?> type, ResourceLocation result)
    {
        IFormattableTextComponent message = References.getTranslate("message.recipe.added", result.getPath(), RecipeFileUtils.getName(type).getPath());
        ClientUtils.sendClientPlayerMessage(message);
    }

    protected JsonArray getArray(Multimap<ResourceLocation, Boolean> ingredients)
    {
        JsonArray array = new JsonArray();
        ingredients.forEach((loc, isTag) -> array.add(singletonItemJsonObject(isTag ? "tag" : "item", loc)));
        return array;
    }

    protected void addNbtToResult(JsonObject result, String nbt)
    {
        result.addProperty("type", "forge:nbt");
        result.addProperty("nbt", nbt.replace("\"", "\\\""));
    }

    protected JsonObject getResult(ItemStack result)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("item", Objects.requireNonNull(result.getItem().getRegistryName()).toString());
        if(result.getCount() > 1)
            map.put("count", result.getCount());

        return mapToJsonObject(map);
    }

    protected JsonArray listWithSingletonItems(List<Item> items, String key)
    {
        JsonArray array = new JsonArray();
        items.forEach(item -> array.add(singletonItemJsonObject(key, Utils.notNull(item.getRegistryName()))));
        return array;
    }

    protected JsonObject singletonItemJsonObject(String key, ResourceLocation value)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(key, value.toString());
        return obj;
    }

    protected JsonObject singletonItemJsonObject(ResourceLocation key)
    {
        return singletonItemJsonObject(isItem(key) ? "item" : "tag", key);
    }

    protected JsonObject mapToJsonObject(Map<String, Object> map)
    {
        JsonObject obj = new JsonObject();
        map.forEach((s, o) ->
        {
            if(o instanceof Number) obj.addProperty(s, (Number) o);
            else if(o instanceof String) obj.addProperty(s, (String) o);
            else if(o instanceof Boolean) obj.addProperty(s, (Boolean) o);
            else if(o instanceof Character) obj.addProperty(s, (Character) o);
            else if(o instanceof JsonArray) obj.add(s, (JsonArray) o);
            else if(o instanceof JsonObject) obj.add(s, (JsonObject) o);
        });
        return obj;
    }

    protected JsonObject createBaseJson(IRecipeType<?> recipeType)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, recipeType);
        return obj;
    }

    protected boolean isItem(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.ITEMS.containsKey(resourceLocation);
    }

    protected <T, V> List<PairValues<T, V>> singletonList(PairValues<T, V> value)
    {
        return Collections.singletonList(value);
    }

    boolean isRecipeExists(IRecipeType<?> recipeType, ResourceLocation resultOutput)
    {
        return recipeType != null && resultOutput != null;
    }

    protected void sendFailMessage()
    {
        ClientUtils.sendClientPlayerMessage(References.getTranslate("message.recipe_failed"));
    }

    protected void putIfNotEmpty(CraftIngredients inputIngredients, List<Ingredient> ingredients)
    {
        for(Ingredient ingredient : ingredients)
        {
            int count = 1;

            if(ingredient.getMatchingStacks().length > 0)
                count = ingredient.getMatchingStacks()[0].getCount();

            if(ingredient.serialize().isJsonObject())
            {
                if(ingredient.serialize().getAsJsonObject().has("tag"))
                    inputIngredients.addIngredient(new CraftIngredients.TagIngredient(ResourceLocation.tryCreate(ingredient.serialize().getAsJsonObject().get("tag").getAsString()), count));
                else if(ingredient.serialize().getAsJsonObject().has("item"))
                    inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(ResourceLocation.tryCreate(ingredient.serialize().getAsJsonObject().get("item").getAsString()), count));
            }
            else
            {
                if(ingredient.serialize().isJsonArray())
                {
                    CraftIngredients.MultiItemIngredient multiItemIngredient = new CraftIngredients.MultiItemIngredient(Utils.generateString(16), count);

                    JsonArray ingredientArray = ingredient.serialize().getAsJsonArray();

                    for(JsonElement value : ingredientArray)
                    {
                        if(value.isJsonObject())
                        {
                            if(value.getAsJsonObject().has("tag"))
                                multiItemIngredient.add(ResourceLocation.tryCreate(value.getAsJsonObject().get("tag").getAsString()), true);
                            else if(value.getAsJsonObject().has("item"))
                                multiItemIngredient.add(ResourceLocation.tryCreate(value.getAsJsonObject().get("item").getAsString()), false);
                        }
                    }

                    inputIngredients.addIngredient(multiItemIngredient);
                }
            }
        }
    }

    protected void putIfNotEmptyLuckedItems(CraftIngredients inputIngredients, List<ItemStack> stacks, List<Float> chances, String description)
    {
        for(int i = 0; i < stacks.size();  i++)
        {
            ItemStack stack = stacks.get(i);
            if(!stack.isEmpty())
                inputIngredients.addIngredient(new CraftIngredients.ItemLuckIngredient(stack.getItem().getRegistryName(), stack.getCount(), chances.get(i), description));
        }
    }

    public abstract CraftIngredients getOutput(IRecipe<?> recipe);

    public abstract CraftIngredients getInput(IRecipe<?> recipe);

    public enum RecipeDescriptors
    {
        INPUT_ITEM("input"),
        OUTPUT_ITEM("output"),
        MOD_ID("mod"),
        RECIPE_TYPE("type"),
        RECIPE_ID("id");

        private final String tag;

        RecipeDescriptors(String tag)
        {
            this.tag = tag;
        }

        public String getTag()
        {
            return tag;
        }
    }
}