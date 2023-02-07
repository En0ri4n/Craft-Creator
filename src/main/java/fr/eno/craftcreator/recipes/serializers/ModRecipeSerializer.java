package fr.eno.craftcreator.recipes.serializers;

import com.google.gson.*;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeFileUtils;
import fr.eno.craftcreator.serializer.DatapackHelper;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: improve code
public abstract class ModRecipeSerializer
{
    protected static final Gson gson = new GsonBuilder().create();
    protected final SupportedMods mod;

    public ModRecipeSerializer(SupportedMods mod)
    {
        this.mod = mod;
    }

    public void removeRecipe(IRecipeType<?> recipeType, ModifiedRecipe modifiedRecipe)
    {
        String serializedRecipe = "event.remove(" + gson.toJson(modifiedRecipe.toJson()) + ")";

        if(!RecipeFileUtils.isModifiedRecipePresent(mod, modifiedRecipe))
            RecipeFileUtils.addRecipeToFile(this.mod, recipeType, serializedRecipe);
    }

    public void addModifiedRecipe(ModifiedRecipe modifiedRecipe)
    {
        String serializedRecipe = String.format("event.%s(", modifiedRecipe.getType().getDescriptor()) + gson.toJson(modifiedRecipe.toJson()) + ")";

        if(!RecipeFileUtils.isModifiedRecipePresent(mod, modifiedRecipe))
            RecipeFileUtils.addModifiedRecipe(this.mod, serializedRecipe);
    }

    public static <C extends IInventory> void removeAddedRecipe(IRecipe<C> recipe)
    {
        RecipeFileUtils.removeAddedRecipe(SupportedMods.getMod(recipe.getId().getNamespace()), recipe);
    }

    public static void removeModifiedRecipe(SupportedMods mod, ModifiedRecipe recipe)
    {
        RecipeFileUtils.removeModifiedRecipe(mod, recipe);
    }

    private <C extends IInventory, T extends IRecipe<C>> IRecipe<C> getRecipe(IRecipeType<T> type, IItemProvider result)
    {
        RecipeManager manager = ClientUtils.getClientLevel().getRecipeManager();
        return manager.getAllRecipesFor(type).stream().filter(recipe -> recipe.getResultItem().getItem() == result.asItem()).findFirst().orElse(null);
    }
    protected void addRecipeToKubeJS(String recipeJson, IRecipeType<?> recipeType, ResourceLocation result)
    {
        RecipeFileUtils.addRecipeToFile(this.mod, recipeType, "event.custom(" + recipeJson + ")");
        sendSuccessMessage(recipeType, result);
    }

    private void sendSuccessMessage(IRecipeType<?> type, ResourceLocation result)
    {
        IFormattableTextComponent message = References.getTranslate("message.recipe.added", result.getPath(), CommonUtils.getRecipeTypeName(type).getPath());
        ClientUtils.sendClientPlayerMessage(message);
    }

    protected JsonArray getInputArray(RecipeEntry.MultiInput ingredients)
    {
        JsonArray array = new JsonArray();
        ingredients.getInputs().forEach(recipeInput -> array.add(singletonItemJsonObject(recipeInput)));
        return array;
    }

    protected void addNbtToResult(JsonObject result, String nbt)
    {
        result.addProperty("type", "forge:nbt");
        result.addProperty("nbt", nbt.replace("\"", "\\\""));
    }

    protected JsonObject getResult(RecipeEntry.Output result)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("item", result.registryName().toString());
        if(result.count() > 1)
            map.put("count", result.count());

        return mapToJsonObject(map);
    }

    protected <T extends RecipeEntry> JsonArray listWithSingletonItems(RecipeEntry.MultiEntry<T> multiEntry, String key)
    {
        JsonArray array = new JsonArray();
        multiEntry.getEntries().forEach(recipeEntry -> array.add(singletonItemJsonObject(key, recipeEntry.registryName().toString())));
        return array;
    }

    protected JsonObject singletonItemJsonObject(RecipeEntry recipeEntry)
    {
        return singletonItemJsonObject(recipeEntry.isTag() ? "tag" : "item", recipeEntry.registryName().toString());
    }

    protected JsonObject singletonItemJsonObject(String key, String value)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(key, value);
        return obj;
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
            else obj.addProperty(s, o.toString());
        });
        return obj;
    }

    protected JsonObject createBaseJson(IRecipeType<?> recipeType)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, recipeType);
        return obj;
    }

    protected void sendFailMessage()
    {
        ClientUtils.sendClientPlayerMessage(References.getTranslate("message.recipe_failed"));
    }

    protected void send(JsonObject recipeJson, IRecipeType<?> recipeType, ResourceLocation output, SerializerType serializerType)
    {
        if(serializerType == SerializerType.KUBE_JS) addRecipeToKubeJS(gson.toJson(recipeJson), recipeType, output);
        else DatapackHelper.serializeRecipe(recipeType, output, recipeJson);
    }

    protected void putIfNotEmpty(CraftIngredients inputIngredients, List<Ingredient> ingredients)
    {
        for(Ingredient ingredient : ingredients)
        {
            int count = 1;

            if(ingredient.getItems().length > 0)
                count = ingredient.getItems()[0].getCount();

            if(ingredient.toJson().isJsonObject())
            {
                if(ingredient.toJson().getAsJsonObject().has("tag"))
                    inputIngredients.addIngredient(new CraftIngredients.TagIngredient(ClientUtils.parse(ingredient.toJson().getAsJsonObject().get("tag").getAsString()), count));
                else if(ingredient.toJson().getAsJsonObject().has("item"))
                    inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(ClientUtils.parse(ingredient.toJson().getAsJsonObject().get("item").getAsString()), count));
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
                                multiItemIngredient.add(ClientUtils.parse(value.getAsJsonObject().get("tag").getAsString()), true);
                            else if(value.getAsJsonObject().has("item"))
                                multiItemIngredient.add(ClientUtils.parse(value.getAsJsonObject().get("item").getAsString()), false);
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

    public enum SerializerType
    {
        MINECRAFT_DATAPACK,
        KUBE_JS
        // CRAFT_TWEAKER ( soon ;) )
    }
}