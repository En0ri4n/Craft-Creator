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
import fr.eno.craftcreator.recipes.utils.DatapackHelper;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;

import java.util.*;

// TODO: improve code
public abstract class ModRecipeSerializer
{
    private static final List<Character> TO_ESCAPE = Arrays.asList('$', '(', ')', '{', '}', '[', ']', '.');
    protected static final Gson GSON = new GsonBuilder().setLenient().create();

    protected final SupportedMods mod;
    protected SerializerType currentSerializeType;
    
    protected ModRecipeSerializer(SupportedMods mod)
    {
        this.mod = mod;
    }

    public void removeRecipe(KubeJSModifiedRecipe kubeJSModifiedRecipe, SerializerType serializerType)
    {
        if(serializerType == SerializerType.KUBE_JS)
        {
            String serializedRecipe = "event.remove(" + GSON.toJson(kubeJSModifiedRecipe.toJson()) + ")";
    
            if(!KubeJSHelper.isModifiedRecipePresent(mod, kubeJSModifiedRecipe))
                KubeJSHelper.addModifiedRecipe(mod, serializedRecipe);
        }
    }

    public void addModifiedRecipe(KubeJSModifiedRecipe kubeJSModifiedRecipe)
    {
        String serializedRecipe = String.format("event.%s(", kubeJSModifiedRecipe.getType().getDescriptor()) + GSON.toJson(kubeJSModifiedRecipe.toJson()) + ")";

        if(!KubeJSHelper.isModifiedRecipePresent(mod, kubeJSModifiedRecipe))
            KubeJSHelper.addModifiedRecipe(this.mod, serializedRecipe);
    }

    public static void removeModifiedRecipe(SupportedMods mod, KubeJSModifiedRecipe recipe)
    {
        KubeJSHelper.removeModifiedRecipe(mod, recipe);
    }

    /**
     * Add a recipe with the specified serializer type (Minecraft Datapack or KubeJS)<br>
     * Send a feedback to the player with the result of the operation
     *
     * @param recipeJson the json of the recipe
     * @param recipeType the type of the recipe
     * @param result the result of the recipe (the id most of the time)
     */
    protected void addRecipeTo(JsonObject recipeJson, IRecipeType<?> recipeType, ResourceLocation result)
    {
        Feedback feedback;
        
        switch(currentSerializeType)
        {
            case KUBE_JS:
                    feedback = KubeJSHelper.addRecipeToFile(this.mod, recipeType, "event.custom(" + GSON.toJson(recipeJson) + ")");
                break;
            default:
            case MINECRAFT_DATAPACK:
                feedback = DatapackHelper.serializeRecipe(recipeType, result, recipeJson);
                break;
        }
        
        sendFeedback(feedback, result.getPath(), CommonUtils.getRecipeTypeName(recipeType).getPath());
    }

    /**
     * Remove a custom added recipe from the specified serializer type (Minecraft Datapack or KubeJS)<br>
     * Send a feedback only in logs
     *
     * @param mod the mod of the recipe to remove
     * @param recipe the recipe to remove
     * @param serializerType the serializer type of the recipe to remove (Minecraft Datapack or KubeJS)
     */
    public <C extends IInventory> void removeAddedRecipeFrom(SupportedMods mod, IRecipe<C> recipe, SerializerType serializerType)
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

    /**
     * Send a feedback to the player with the specified arguments
     *
     * @param feedback the feedback to send
     * @param args the arguments of the feedback
     */
    private void sendFeedback(Feedback feedback, Object... args)
    {
        IFormattableTextComponent message = feedback.args(args);
        ClientUtils.sendClientPlayerMessage(message);
    }

    /**
     * Get the JsonArray of the given multi inputs<br>
     * Same as {@link #getInputArray(RecipeEntry.Input)} but for multi inputs
     *
     * @param ingredients the multi inputs
     * @return the JsonArray of the given inputs
     * @see #singletonItemJsonObject(RecipeEntry)
     */
    protected JsonArray getInputArray(RecipeEntry.MultiInput ingredients)
    {
        JsonArray array = new JsonArray();
        ingredients.getInputs().forEach(recipeInput -> array.add(singletonItemJsonObject(recipeInput)));
        return array;
    }

    /**
     * Get the JsonArray of the given input
     *
     * @param ingredient the input
     * @return the JsonArray of the given input
     * @see #singletonItemJsonObject(RecipeEntry)
     */
    protected JsonArray getInputArray(RecipeEntry.Input ingredient)
    {
        JsonArray array = new JsonArray();
        array.add(singletonItemJsonObject(ingredient));
        return array;
    }

    /**
     * Add nbt to the given json object
     *
     * @param result the result
     * @param nbt the nbt to add
     */
    // I don't know if nbt are supported by other mods, so it's here if needed
    protected void addNbtToResult(JsonObject result, String nbt)
    {
        result.addProperty("type", "forge:nbt");
        result.addProperty("nbt", nbt.replace("\"", "\""));
    }

    /**
     * Get the JsonObject of the given output
     *
     * @param result the output
     * @return the JsonObject of the given output
     */
    protected JsonObject getResult(RecipeEntry.Output result)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("item", result.registryName().toString());
        if(result.count() > 1)
            map.put("count", result.count());

        return mapToJsonObject(map);
    }

    /**
     * Get the JsonObject of the given lucked output
     *
     * @param result the lucked output
     * @return the JsonObject of the given lucked output
     */
    private JsonObject getLuckedResult(RecipeEntry.LuckedOutput result)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("item", result.registryName().toString());
        if(result.count() > 1)
            map.put("count", result.count());
        if(result.getChance() != 1D)
            map.put("chance", result.getChance());

        return mapToJsonObject(map);
    }

    /**
     * Get the JsonArray of the given multi outputs (supports lucked outputs)<br>
     * Same as {@link #getResultArray(RecipeEntry.Output)} but for multi outputs
     *
     * @param results the multi outputs
     * @return the JsonArray of the given outputs
     */
    protected JsonArray getResultArray(RecipeEntry.MultiOutput results)
    {
        JsonArray array = new JsonArray();
        results.getOutputs().forEach(recipeOutput ->
        {
            if(recipeOutput instanceof RecipeEntry.LuckedOutput)
                array.add(getLuckedResult(((RecipeEntry.LuckedOutput) recipeOutput)));
            else
                array.add(getResult(recipeOutput));
        });
        return array;
    }

    /**
     * Get the JsonArray of the given output
     *
     * @param result the output
     * @return the JsonArray of the given output
     */
    protected JsonArray getResultArray(RecipeEntry.Output result)
    {
        JsonArray array = new JsonArray();
        array.add(getResult(result));
        return array;
    }

    /**
     * Get the JsonObject of the given input (with count, not like {@link #singletonItemJsonObject(RecipeEntry)}
     *
     * @param input the input
     * @return the JsonObject of the given input
     */
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

    /**
     * Get the JsonArray of the given multi recipe entries (with no count property)<br>
     *
     * @param multiEntry the multi recipe entries
     * @return the JsonArray of the given inputs or outputs
     * @see #singletonItemJsonObject(RecipeEntry)
     */
    protected <T extends RecipeEntry> JsonArray listWithSingletonItems(RecipeEntry.MultiEntry<T> multiEntry)
    {
        JsonArray array = new JsonArray();
        multiEntry.getEntries().forEach(recipeEntry -> array.add(singletonItemJsonObject(recipeEntry)));
        return array;
    }

    /**
     * Get the JsonObject of the given recipe entry (with no count property)
     *
     * @param recipeEntry the recipe entry
     * @return the JsonObject of the given input or output
     */
    protected JsonObject singletonItemJsonObject(RecipeEntry recipeEntry)
    {
        return singletonItemJsonObject(recipeEntry.isTag() ? "tag" : "item", recipeEntry.registryName().toString());
    }

    /**
     * Get the JsonObject of the given key and value
     *
     * @param key the key
     * @param value the value
     * @return the JsonObject of the given key and value
     */
    protected JsonObject singletonItemJsonObject(String key, String value)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(key, value);
        return obj;
    }

    /**
     * Convert all keys and values of the given map to a properties of the JsonObject (can be {@link JsonElement})
     *
     * @param map the map to convert
     * @return the JsonObject with all properties
     */
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

    /**
     * Create a base JsonObject for the given recipe type<br>
     * (e.g. "type": "minecraft:smelting" for {@link IRecipeType#SMELTING})
     *
     * @param recipeType the recipe type
     * @return the base JsonObject
     */
    protected JsonObject createBaseJson(IRecipeType<?> recipeType)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", CommonUtils.getRecipeTypeName(recipeType).toString());
        return obj;
    }

    /**
     * Escape illegals character from the current string
     *
     * @param toEscape the string to escape
     * @param isPattern if the string is a pattern
     * @return the escaped string
     */
    public static String escape(String toEscape, boolean isPattern)
    {
        List<Character> list = Collections.singletonList('"');

        for(Character c : isPattern ? TO_ESCAPE : list)
            toEscape = toEscape.replace(String.valueOf(c), String.valueOf('\\') + c);

        System.out.println(isPattern + " : " + toEscape);
        return toEscape;
    }

    /**
     * Set the serializer type to use to serialize the recipes
     *
     * @param serializerType the serializer type
     */
    public void setSerializerType(SerializerType serializerType)
    {
        this.currentSerializeType = serializerType;
    }

    /**
     * Get the input of the given recipe
     *
     * @param recipe The recipe
     * @return The input of the recipe
     */
    public abstract CraftIngredients getInput(IRecipe<?> recipe);

    /**
     * Get the output of the given recipe
     *
     * @param recipe The recipe
     * @return The output of the recipe
     */
    public abstract CraftIngredients getOutput(IRecipe<?> recipe);

    /**
     * Put the given ingredients to the given craft ingredients if they are not empty
     *
     * @param craftIngredients The craft ingredients
     * @param ingredients The ingredients
     */
    // TODO: Needs to be improved
    protected void putIfNotEmpty(CraftIngredients craftIngredients, List<Ingredient> ingredients)
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
                    craftIngredients.addIngredient(new CraftIngredients.TagIngredient(ClientUtils.parse(ingredientJson.getAsJsonObject().get("tag").getAsString()), count));
                else if(ingredientJson.getAsJsonObject().has("item"))
                    craftIngredients.addIngredient(new CraftIngredients.ItemIngredient(ClientUtils.parse(ingredientJson.getAsJsonObject().get("item").getAsString()), count));
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

                    craftIngredients.addIngredient(multiItemIngredient);
                }
            }
        }
    }

    /**
     * Put the given items into the given input ingredients if they are not empty, with their given chances (only outputs can be lucked)
     *
     * @param craftIngredients The input ingredients
     * @param stacks The items
     * @param chances The chances
     */
    protected void putIfNotEmptyLuckedItems(CraftIngredients craftIngredients, List<ItemStack> stacks, List<Float> chances)
    {
        for(int i = 0; i < stacks.size();  i++)
        {
            ItemStack stack = stacks.get(i);
            if(!stack.isEmpty())
                craftIngredients.addIngredient(new CraftIngredients.ItemLuckIngredient(stack.getItem().getRegistryName(), stack.getCount(), chances.get(i), "Item"));
        }
    }

    /**
     * Feedbacks for the serializer (e.g. for the chat)
     */
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

        /**
         * Get only the raw message key
         *
         * @return The message key
         */
        public String getMessageKey()
        {
            return message;
        }

        /**
         * Get the translate with the given args
         *
         * @param args The args
         * @return The translate
         */
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