package fr.eno.craftcreator.recipes.serializers;

import com.google.common.collect.Multimap;
import com.google.gson.*;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.RecipeFileUtils;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
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

    public static <C extends Container, T extends Recipe<C>> void removeAddedRecipe(Recipe<C> recipe)
    {
        RecipeFileUtils.removeAddedRecipe(recipe, recipe.getId().getNamespace());
    }

    public static void removeModifiedRecipe(ModifiedRecipe recipe)
    {
        RecipeFileUtils.removeModifiedRecipe(recipe);
    }

    private <C extends Container, T extends Recipe<C>> Recipe<C> getRecipe(RecipeType<T> type, ItemLike result)
    {
        RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        return manager.getAllRecipesFor(type).stream().filter(Recipe -> Recipe.getResultItem().getItem() == result.asItem()).findFirst().orElse(null);
    }
    protected void addRecipeToKubeJS(String recipeJson, RecipeType<?> recipeType, ResourceLocation result)
    {
        RecipeFileUtils.insertAndWriteLines(this.mod.getModId(), recipeType, "event.custom(" + recipeJson + ")");
        sendSuccessMessage(recipeType, result);
    }

    private void sendSuccessMessage(RecipeType<?> type, ResourceLocation result)
    {
        MutableComponent message = References.getTranslate("message.recipe.added", result.getPath(), RecipeFileUtils.getName(type).getPath());
        Objects.requireNonNull(Minecraft.getInstance().player).sendMessage(message, Minecraft.getInstance().player.getUUID());
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
            if(o instanceof Number number) obj.addProperty(s, number);
            else if(o instanceof String string) obj.addProperty(s, string);
            else if(o instanceof Boolean bool) obj.addProperty(s, bool);
            else if(o instanceof Character character) obj.addProperty(s, character);
            else if(o instanceof JsonArray array) obj.add(s, array);
            else if(o instanceof JsonObject object) obj.add(s, object);
        });
        return obj;
    }

    protected JsonObject createBaseJson(RecipeType<?> recipeType)
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

    boolean isRecipeExists(RecipeType<?> recipeType, ResourceLocation resultOutput)
    {
        return recipeType != null && resultOutput != null;
    }

    protected void sendFailMessage()
    {
        Objects.requireNonNull(Minecraft.getInstance().player).sendMessage(References.getTranslate("message.recipe_failed"), Minecraft.getInstance().player.getUUID());
    }

    protected void putIfNotEmpty(CraftIngredients inputIngredients, List<Ingredient> ingredients)
    {
        for(Ingredient ingredient : ingredients)
        {
            int count = 1;

            if(ingredient.getItems().length > 0)
                ingredient.getItems()[0].getCount();

            if(ingredient.toJson().isJsonObject())
            {
                if(ingredient.toJson().getAsJsonObject().has("tag"))
                    inputIngredients.addIngredient(new CraftIngredients.TagIngredient(ResourceLocation.tryParse(ingredient.toJson().getAsJsonObject().get("tag").getAsString()), count));
                else if(ingredient.toJson().getAsJsonObject().has("item"))
                    inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(ResourceLocation.tryParse(ingredient.toJson().getAsJsonObject().get("item").getAsString()), count));
            }
            else
            {
                if(ingredient.toJson().isJsonArray())
                {
                    CraftIngredients.MultiItemIngredient multiItemIngredient = new CraftIngredients.MultiItemIngredient(Utils.generateString(16), count);

                    JsonArray ingredientArray = ingredient.toJson().getAsJsonArray();

                    for(JsonElement value : ingredientArray)
                    {
                        if(value.isJsonObject())
                        {
                            if(value.getAsJsonObject().has("tag"))
                                multiItemIngredient.add(ResourceLocation.tryParse(value.getAsJsonObject().get("tag").getAsString()), true);
                            else if(value.getAsJsonObject().has("item"))
                                multiItemIngredient.add(ResourceLocation.tryParse(value.getAsJsonObject().get("item").getAsString()), false);
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

    public abstract CraftIngredients getOutput(Recipe<?> recipe);

    public abstract CraftIngredients getInput(Recipe<?> recipe);

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