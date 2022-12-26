package fr.eno.craftcreator.kubejs.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import fr.eno.craftcreator.kubejs.KubeJSManager;
import fr.eno.craftcreator.kubejs.jsserializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.PairValue;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.common.crafting.StateIngredientTag;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unchecked", "unused"})
public class RecipeFileUtils
{
    private static final Pattern RECIPE_PATTERN = Pattern.compile("(?<=\\()(.*?)(?=\\))");

    public final static String ADD_RECIPE_START = "ADDED-RECIPES-START";
    public final static String MODIFIED_RECIPE_START = "REMOVED-RECIPES-START";

    private static final List<String> MODIFIED_START_PATTERNS = Arrays.asList("event.remove", "event.replaceInput", "event.replaceOutput");

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public static File getRecipeFile(String modId)
    {
        File recipe = new File(KubeJSManager.getInstance().getKubeJSRecipesFolder(), modId + ".js");

        if(!recipe.exists() || recipe.length() <= 0L)
        {
            try
            {
                recipe.createNewFile();

                List<String> lines = Arrays.asList("// AUTO-GENERATED by Craft Creator",
                        "onEvent('recipes', event => {",
                        "",
                        "\t// " + MODIFIED_RECIPE_START,
                        "",
                        "\t// " + ADD_RECIPE_START,
                        "",
                        "})");

                Files.write(recipe.toPath(), lines, StandardCharsets.UTF_8);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return recipe;
    }

    /*
     * This method is used to get the recipe file of a mod
     * It will create the file if it doesn't exist
     * It will also add the start of the file if it doesn't exist
     */
    public static <T extends Recipe<?>> List<T> getAddedRecipesFor(String modId, RecipeType<T> recipeType)
    {
        List<T> recipes = new ArrayList<>();

        RecipeSerializer<T> serializer = (RecipeSerializer<T>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(getName(recipeType));

        try
        {
            File recipeFile = RecipeFileUtils.getRecipeFile(modId); // Get the recipe file

            checkTypeGroup(recipeFile, recipeType);

            boolean isCraftingTableCraft = getName(recipeType).toString().contains("minecraft:crafting");

            List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);

            for(String line : lines)
                if((line.contains(getName(recipeType).toString()) || isCraftingTableCraft) && line.contains("event.custom"))
                {
                    Matcher JSON_MATCHER = RECIPE_PATTERN.matcher(line);

                    if(JSON_MATCHER.find())
                    {
                        if(isCraftingTableCraft)
                        {
                            JsonObject jsonObject = GSON.fromJson(new StringReader(JSON_MATCHER.group()), JsonObject.class);
                            RecipeSerializer<T> craftingTableSerializer = (RecipeSerializer<T>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(new ResourceLocation(jsonObject.get("type").getAsString()));
                            addRecipesTo(modId, recipes, jsonObject, craftingTableSerializer);
                            continue;
                        }

                        JsonObject jsonObject = GSON.fromJson(new StringReader(JSON_MATCHER.group()), JsonObject.class);
                        addRecipesTo(modId, recipes, jsonObject, serializer);
                    }
                }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return recipes;
    }

    /*
     * This method is used to get the modified recipes of a mod
     */
    private static <T extends Recipe<?>> void addRecipesTo(String modId, List<T> recipes, JsonObject jsonObject, RecipeSerializer<T> craftingTableSerializer)
    {
        assert craftingTableSerializer != null;
        T tempRecipe = craftingTableSerializer.fromJson(new ResourceLocation(modId, "recipe"), jsonObject);
        T recipe = craftingTableSerializer.fromJson(new ResourceLocation(modId, Objects.requireNonNull(ModDispatcher.getOutput(tempRecipe)).values().stream().findAny().orElse(null).getPath()), jsonObject);
        recipes.add(recipe);
    }

    public static <C extends Container, T extends Recipe<C>> void removeAddedRecipe(T addedRecipe, String modId)
    {
        RecipeType<T> recipeType = (RecipeType<T>) addedRecipe.getType();
        RecipeSerializer<T> serializer = (RecipeSerializer<T>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(getName(recipeType));

        try
        {
            File recipeFile = RecipeFileUtils.getRecipeFile(modId);

            checkTypeGroup(recipeFile, recipeType);
            boolean isCraftingTableCraft = getName(recipeType).toString().contains("minecraft:crafting");

            List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);

            int index = 0;
            for(String line : lines)
            {
                if(line.contains(getName(recipeType).toString()) && line.contains("event.custom"))
                {
                    Matcher JSON_MATCHER = RECIPE_PATTERN.matcher(line);

                    if(JSON_MATCHER.find())
                    {
                        JsonObject jsonObject = GSON.fromJson(new StringReader(JSON_MATCHER.group()), JsonObject.class);
                        T tempRecipe;
                        T recipe;
                        if(isCraftingTableCraft)
                        {
                            RecipeSerializer<T> craftingTableSerializer = (RecipeSerializer<T>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(new ResourceLocation(jsonObject.get("type").getAsString()));
                            if(compareAndRemoveIfEquals(addedRecipe, modId, craftingTableSerializer, recipeFile, lines, index, jsonObject))
                                return;

                            index++;
                            continue;
                        }

                        if(compareAndRemoveIfEquals(addedRecipe, modId, serializer, recipeFile, lines, index, jsonObject))
                            return;
                    }
                }
                index++;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static <C extends Container, T extends Recipe<C>> boolean compareAndRemoveIfEquals(T addedRecipe, String modId, RecipeSerializer<T> serializer, File recipeFile, List<String> lines, int index, JsonObject jsonObject) throws IOException
    {
        T tempRecipe;
        T recipe;
        tempRecipe = serializer.fromJson(new ResourceLocation(modId, "recipe"), jsonObject);
        recipe = serializer.fromJson(new ResourceLocation(modId, Objects.requireNonNull(ModDispatcher.getOutput(tempRecipe)).values().stream().findAny().orElse(null).getPath()), jsonObject);

        FriendlyByteBuf existingRecipeBuffer = new FriendlyByteBuf(Unpooled.buffer());
        FriendlyByteBuf jsonRecipeBuffer = new FriendlyByteBuf(Unpooled.buffer());
        serializer.toNetwork(existingRecipeBuffer, addedRecipe);
        serializer.toNetwork(jsonRecipeBuffer, recipe);

        if(existingRecipeBuffer.compareTo(jsonRecipeBuffer) == 0)
        {
            lines.remove(index);
            Files.write(recipeFile.toPath(), lines, StandardCharsets.UTF_8);
            return true;
        }
        return false;
    }

    public static <T extends Recipe<?>> List<ModifiedRecipe> getModifiedRecipesFor()
    {
        List<ModifiedRecipe> modifiedRecipes = new ArrayList<>();

        for(SupportedMods mod : SupportedMods.values())
        {
            try
            {
                File recipeFile = RecipeFileUtils.getRecipeFile(mod.getModId());
                List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);

                for(String line : lines)
                {
                    Optional<String> descriptor = MODIFIED_START_PATTERNS.stream().filter(line::contains).findAny();

                    if(descriptor.isPresent())
                    {
                        Matcher JSON_MATCHER = RECIPE_PATTERN.matcher(line);

                        if(JSON_MATCHER.find())
                        {
                            ModifiedRecipeType modifiedRecipeType = ModifiedRecipeType.byDescriptor(descriptor.get());
                            ModifiedRecipe modifiedRecipe = new ModifiedRecipe(modifiedRecipeType);
                            getModifiedRecipe(modifiedRecipe, JSON_MATCHER.group());
                            modifiedRecipes.add(modifiedRecipe);
                        }
                    }
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return modifiedRecipes;
    }

    public static void insertAndWriteLines(String modId, RecipeType<?> recipeType, String line)
    {
        try
        {
            File recipeFile = RecipeFileUtils.getRecipeFile(modId);

            checkTypeGroup(recipeFile, recipeType);

            List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);

            if(lines.stream().anyMatch(lineFromLines -> lineFromLines.contains(line))) return;

            for(int i = 0; i < lines.size(); i++)
                if(lines.get(i).contains(getStartRecipeTypeGroup(recipeType)))
                {
                    if(lines.get(i).contains(line)) break;

                    lines.add(i + 1, "\t" + line);
                    break;
                }
            Files.write(recipeFile.toPath(), lines, StandardCharsets.UTF_8);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void insertAndWriteLinesToRemoveRecipe(String modId, String line)
    {
        try
        {
            File recipeFile = RecipeFileUtils.getRecipeFile(modId);

            List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);
            if(lines.contains(line)) return;

            for(int i = 0; i < lines.size(); i++)
                if(lines.get(i).contains(MODIFIED_RECIPE_START))
                {
                    if(lines.get(i).contains(line)) break;

                    lines.add(i + 1, "\t" + line);
                    break;
                }
            Files.write(recipeFile.toPath(), lines, StandardCharsets.UTF_8);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void checkTypeGroup(File recipeFile, RecipeType<?> recipeType)
    {
        try
        {
            List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);

            boolean hasGroup = false;

            for(String line : lines)
                if(line.contains(getStartRecipeTypeGroup(recipeType)))
                {
                    hasGroup = true;
                    break;
                }

            if(!hasGroup)
            {
                for(int i = 0; i < lines.size(); i++)
                    if(lines.get(i).contains(ADD_RECIPE_START))
                    {
                        lines.add(i + 1, "\t// " + getStartRecipeTypeGroup(recipeType));
                        Files.write(recipeFile.toPath(), lines, StandardCharsets.UTF_8);
                        break;
                    }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String getStartRecipeTypeGroup(RecipeType<?> type)
    {
        return getName(type).getPath() + "-recipes";
    }

    public static void setRecipeType(JsonObject obj, RecipeType<?> type)
    {
        obj.addProperty("type", getName(type).toString());
    }

    public static ResourceLocation getName(RecipeType<?> recipeType)
    {
        return Registry.RECIPE_TYPE.getKey(recipeType);
    }

    public static <T extends Recipe<C>, C extends Container> RecipeType<T> byName(ResourceLocation location)
    {
        return (RecipeType<T>) Registry.RECIPE_TYPE.getOptional(location).orElse(null);
    }

    public static List<JsonObject> getJsonRecipes(RecipeType<?> recipeType, String modId)
    {
        List<JsonObject> jsonRecipes = new ArrayList<>();

        try
        {
            File recipeFile = RecipeFileUtils.getRecipeFile(modId);

            checkTypeGroup(recipeFile, recipeType);

            List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);

            for(String line : lines)
                if(line.contains(getName(recipeType).toString()) && line.contains("event.custom"))
                {
                    Matcher JSON_MATCHER = RECIPE_PATTERN.matcher(line);

                    if(JSON_MATCHER.find())
                    {
                        JsonObject jsonObject = GSON.fromJson(new StringReader(JSON_MATCHER.group()), JsonObject.class);
                        jsonRecipes.add(jsonObject);
                    }
                }
        }
        catch(IOException ignored)
        {
        }

        return jsonRecipes;
    }

    private static void getModifiedRecipe(ModifiedRecipe modifiedRecipe, String line)
    {
        JsonObject json = GSON.fromJson(line, JsonElement.class).getAsJsonObject();

        for(ModRecipesJSSerializer.RecipeDescriptors descriptor : ModRecipesJSSerializer.RecipeDescriptors.values())
        {
            if(json.has(descriptor.getTag()))
            {
                modifiedRecipe.setDescriptor(descriptor, json.get(descriptor.getTag()).getAsString());
            }
        }
    }

    @Nullable
    public static PairValue<String, Integer> getParam(Recipe<?> recipe)
    {
        return ModDispatcher.getParameters(recipe);
    }

    public static Multimap<String, ResourceLocation> getInput(Recipe<?> recipe)
    {
        Multimap<String, ResourceLocation> locations = ArrayListMultimap.create();

        if(recipe instanceof IPureDaisyRecipe recipePureDaisy)
        {
            if(recipePureDaisy.getInput() instanceof StateIngredientTag)
                locations.put("Tag", ((StateIngredientTag) recipePureDaisy.getInput()).getTagId());
            else locations.put("Block", recipePureDaisy.getInput().pick(new Random()).getBlock().getRegistryName());
            return locations;
        }

        locations.putAll(getIngredients(recipe.getIngredients()));

        if(locations.isEmpty()) locations.put("Item", recipe.getResultItem().getItem().getRegistryName());

        return locations;
    }

    private static Multimap<String, ResourceLocation> getIngredients(NonNullList<Ingredient> ingredients)
    {
        Multimap<String, ResourceLocation> locations = ArrayListMultimap.create();

        ingredients.forEach(ingredient ->
        {
            JsonElement json = ingredient.toJson();
            if(json instanceof JsonArray) json.getAsJsonArray().forEach(je -> getAndSave(je, locations));
            else getAndSave(json, locations);
        });

        return locations;
    }

    private static void getAndSave(JsonElement element, Multimap<String, ResourceLocation> map)
    {
        if(element.getAsJsonObject().has("item"))
            map.put("Item", ResourceLocation.tryParse(element.getAsJsonObject().get("item").getAsString()));
        else map.put("Tag", ResourceLocation.tryParse(element.getAsJsonObject().get("tag").getAsString()));
    }

    public static void removeModifiedRecipe(ModifiedRecipe recipe)
    {
        for(SupportedMods mod : SupportedMods.values())
        {
            try
            {
                File recipeFile = RecipeFileUtils.getRecipeFile(mod.getModId());
                List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);

                int index = 0;
                for(String line : lines)
                {
                    String descriptor = recipe.getType().getDescriptor();
                    if(line.contains(descriptor))
                    {
                        Matcher JSON_MATCHER = RECIPE_PATTERN.matcher(line);

                        if(JSON_MATCHER.find())
                        {
                            ModifiedRecipeType modifiedRecipeType = recipe.getType();
                            ModifiedRecipe modifiedRecipe = new ModifiedRecipe(modifiedRecipeType);
                            getModifiedRecipe(modifiedRecipe, JSON_MATCHER.group());

                            if(modifiedRecipe.getRecipeMap().equals(recipe.getRecipeMap()))
                            {
                                lines.remove(index);
                                Files.write(recipeFile.toPath(), lines, StandardCharsets.UTF_8);
                                return;
                            }
                        }
                    }
                    index++;
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static boolean isModifiedRecipePresent(ModifiedRecipe recipe)
    {
        for(SupportedMods mod : SupportedMods.values())
        {
            try
            {
                File recipeFile = RecipeFileUtils.getRecipeFile(mod.getModId());
                List<String> lines = Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8);

                int index = 0;
                for(String line : lines)
                {
                    String descriptor = recipe.getType().getDescriptor();

                    if(!line.contains(descriptor)) continue;

                    Matcher JSON_MATCHER = RECIPE_PATTERN.matcher(line);

                    if(JSON_MATCHER.find())
                    {
                        ModifiedRecipeType modifiedRecipeType = recipe.getType();
                        ModifiedRecipe modifiedRecipe = new ModifiedRecipe(modifiedRecipeType);
                        getModifiedRecipe(modifiedRecipe, JSON_MATCHER.group());

                        if(modifiedRecipe.getRecipeMap().equals(recipe.getRecipeMap()))
                        {
                            return true;
                        }
                    }
                    index++;
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    public enum ModifiedRecipeType
    {
        REMOVED("event.remove", new TextComponent("Removed").withStyle(ChatFormatting.RED)),
        REPLACED_INPUT("event.replaceInput", new TextComponent("Input Replaced").withStyle(ChatFormatting.GOLD)),
        REPLACED_OUTPUT("event.replaceOutput", new TextComponent("Output Replaced").withStyle(ChatFormatting.YELLOW));

        private final String descriptor;
        private final MutableComponent title;

        ModifiedRecipeType(String descriptor, MutableComponent title)
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

        public MutableComponent getTitle()
        {
            return title;
        }
    }
}
