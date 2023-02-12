package fr.eno.craftcreator.recipes.base;

import com.google.gson.*;
import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.kubejs.KubeJSHelper;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.serializer.DatapackHelper;
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
    private SerializerType serializerType;
    
    public ModRecipeSerializer(SupportedMods mod)
    {
        this.mod = mod;
    }

    public void removeRecipe(IRecipeType<?> recipeType, KubeJSModifiedRecipe kubeJSModifiedRecipe, SerializerType serializerType)
    {
        if(serializerType == SerializerType.KUBE_JS)
        {
            String serializedRecipe = "event.remove(" + gson.toJson(kubeJSModifiedRecipe.toJson()) + ")";
    
            if(!KubeJSHelper.isModifiedRecipePresent(mod, kubeJSModifiedRecipe))
                KubeJSHelper.addModifiedRecipe(mod, serializedRecipe);
        }
    }

    public void addModifiedRecipe(KubeJSModifiedRecipe kubeJSModifiedRecipe)
    {
        String serializedRecipe = String.format("event.%s(", kubeJSModifiedRecipe.getType().getDescriptor()) + gson.toJson(kubeJSModifiedRecipe.toJson()) + ")";

        if(!KubeJSHelper.isModifiedRecipePresent(mod, kubeJSModifiedRecipe))
            KubeJSHelper.addModifiedRecipe(this.mod, serializedRecipe);
    }

    public static void removeModifiedRecipe(SupportedMods mod, KubeJSModifiedRecipe recipe)
    {
        KubeJSHelper.removeModifiedRecipe(mod, recipe);
    }

    private <C extends IInventory, T extends IRecipe<C>> IRecipe<C> getRecipe(IRecipeType<T> type, IItemProvider result)
    {
        RecipeManager manager = ClientUtils.getClientLevel().getRecipeManager();
        return manager.getAllRecipesFor(type).stream().filter(recipe -> recipe.getResultItem().getItem() == result.asItem()).findFirst().orElse(null);
    }
    
    protected void addRecipeTo(JsonObject recipeJson, IRecipeType<?> recipeType, ResourceLocation result)
    {
        Feedback feedback;
        
        switch(serializerType)
        {
            case KUBE_JS:
                    feedback = KubeJSHelper.addRecipeToFile(this.mod, recipeType, "event.custom(" + gson.toJson(recipeJson) + ")");
                break;
            default:
            case MINECRAFT_DATAPACK:
                feedback = DatapackHelper.serializeRecipe(recipeType, result, recipeJson);
                break;
        }
        
        sendFeedback(feedback, result.getPath(), CommonUtils.getRecipeTypeName(recipeType).getPath());
    }
    
    public <C extends IInventory> void removeAddedRecipeFrom(SupportedMods mod, IRecipe<?> recipe, SerializerType serializerType)
    {
        Feedback feedback;
        
        switch(serializerType)
        {
            case KUBE_JS:
                feedback = KubeJSHelper.removeAddedRecipe(mod, recipe);
                break;
            default:
            case MINECRAFT_DATAPACK:
                feedback = Feedback.FILE_ERROR;
                break;
        }

        CraftCreator.LOGGER.debug(feedback.args(recipe.getId().toString()).getString());
    }

    private void sendFeedback(Feedback feedback, Object... args)
    {
        IFormattableTextComponent message = feedback.args(args);
        ClientUtils.sendClientPlayerMessage(message);
    }

    protected JsonArray getInputArray(RecipeEntry.MultiInput ingredients)
    {
        JsonArray array = new JsonArray();
        ingredients.getInputs().forEach(recipeInput -> array.add(singletonItemJsonObject(recipeInput)));
        return array;
    }

    protected JsonArray getInputArray(RecipeEntry.Input ingredient)
    {
        JsonArray array = new JsonArray();
        array.add(singletonItemJsonObject(ingredient));
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

    protected JsonArray getResultArray(RecipeEntry.MultiOutput results)
    {
        JsonArray array = new JsonArray();
        results.getOutputs().forEach(recipeOutput -> array.add(getResult(recipeOutput)));
        return array;
    }

    protected JsonArray getResultArray(RecipeEntry.Output result)
    {
        JsonArray array = new JsonArray();
        array.add(getResult(result));
        return array;
    }

    protected JsonObject getInput(RecipeEntry.Input input)
    {
        Map<String, Object> map = new HashMap<>();

        if(input.isTag())
            map.put("tag", input.registryName().toString());
        else
            map.put("item", input.registryName().toString());

        if(input.count() > 1)
            map.put("count", input.count());

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
        setRecipeType(obj, recipeType);
        return obj;
    }

    protected void putIfNotEmpty(CraftIngredients inputIngredients, List<Ingredient> ingredients)
    {
        for(Ingredient ingredient : ingredients)
        {
            int count = 1;

            if(ingredient.getItems().length > 0)
                count = ingredient.getItems()[0].getCount();
            
            JsonElement ingredientJson = ingredient.toJson();
            
            if(ingredientJson.isJsonObject())
            {
                if(ingredientJson.getAsJsonObject().has("tag"))
                    inputIngredients.addIngredient(new CraftIngredients.TagIngredient(ClientUtils.parse(ingredientJson.getAsJsonObject().get("tag").getAsString()), count));
                else if(ingredientJson.getAsJsonObject().has("item"))
                    inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(ClientUtils.parse(ingredientJson.getAsJsonObject().get("item").getAsString()), count));
            }
            else
            {
                if(ingredientJson.isJsonArray())
                {
                    CraftIngredients.MultiItemIngredient multiItemIngredient = new CraftIngredients.MultiItemIngredient(Utils.generateString(16), count);

                    JsonArray ingredientArray = ingredientJson.getAsJsonArray();

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
    
    protected void setRecipeType(JsonObject obj, IRecipeType<?> type)
    {
        obj.addProperty("type", CommonUtils.getRecipeTypeName(type).toString());
    }

    public abstract CraftIngredients getOutput(IRecipe<?> recipe);

    public abstract CraftIngredients getInput(IRecipe<?> recipe);
    
    public void setSerializerType(SerializerType serializerType)
    {
        this.serializerType = serializerType;
    }
    
    public enum Feedback
    {
        ADDED("serializer.message.recipe.added"),
        REMOVED("serializer.message.recipe.removed"),
        EXISTS("serializer.message.recipe.exists"),
        DONT_EXISTS("serializer.message.recipe.dont_exists"),
        FILE_ERROR("serializer.message.recipe.error");
    
        private final String message;
    
        Feedback(String message)
        {
            this.message = message;
        }
    
        public String getMessageKey()
        {
            return message;
        }
        
        public IFormattableTextComponent args(Object... args)
        {
            return References.getTranslate(getMessageKey(), args);
        }
    }

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
        
        public static RecipeDescriptors byTag(String tag)
        {
            for(RecipeDescriptors descriptor : values())
                if(descriptor.getTag().equals(tag))
                    return descriptor;
            
            return null;
        }
    }

    public enum SerializerType
    {
        MINECRAFT_DATAPACK,
        KUBE_JS
        // CRAFT_TWEAKER ( soon ;) )
    }
    
    public enum SerializerFunction
    {
        ADD,
        REMOVE,
        REMOVE_ADDED,
        MODIFY
    }
}