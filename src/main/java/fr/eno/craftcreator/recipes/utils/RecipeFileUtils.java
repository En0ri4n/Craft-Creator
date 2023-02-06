package fr.eno.craftcreator.recipes.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.kubejs.KubeJSManager;
import fr.eno.craftcreator.recipes.serializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.Utils;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// TODO: re arrange methods (in new classes) and sort them
@SuppressWarnings({"unchecked", "unused", "ConstantConditions"})
public class RecipeFileUtils
{
    private static final Pattern RECIPE_PATTERN = Pattern.compile("\n.*event\\.custom\\(([^)]*)\\)");//Pattern.compile("(?<=\\()(.*?)(?=\\)$)");

    public final static String ADD_RECIPE_START = "ADDED-RECIPES-START";
    public final static String MODIFIED_RECIPE_START = "REMOVED-RECIPES-START";

    private static final List<String> MODIFIED_START_PATTERNS = Arrays.asList("event.remove", "event.replaceInput", "event.replaceOutput");

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public static File getRecipeFile(String modId)
    {
        File recipeFile = new File(KubeJSManager.getInstance().getKubeJSRecipesFolder(), modId + ".js");

        if(!recipeFile.exists() || recipeFile.length() <= 0L)
        {
            try
            {
                recipeFile.createNewFile();

                List<String> lines = Arrays.asList("// AUTO-GENERATED by Craft Creator",
                        "onEvent('recipes', event => {",
                        "",
                        "\t// " + MODIFIED_RECIPE_START,
                        "",
                        "\t// " + ADD_RECIPE_START,
                        "",
                        "})");

                writeTo(recipeFile, lines);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return recipeFile;
    }

    public static <C extends IInventory, T extends IRecipe<C>> List<T> getAddedRecipesFor(String modId, IRecipeType<T> recipeType)
    {
        List<T> recipes = new ArrayList<>();

        File recipeFile = RecipeFileUtils.getRecipeFile(modId); // Get the recipe file

        checkTypeGroup(recipeFile, recipeType);

        String recipeFileContent = readFile(recipeFile);

        Matcher customRecipeMatcher = RECIPE_PATTERN.matcher(recipeFileContent);

        while(customRecipeMatcher.find())
        {
            String recipeJson = customRecipeMatcher.group(1);
            JsonObject jsonObject = GSON.fromJson(new StringReader(recipeJson), JsonObject.class);
            IRecipeSerializer<T> currentSerializer;

            if(recipeType == IRecipeType.CRAFTING && jsonObject.get("type").getAsString().contains(getName(recipeType).toString()))
                currentSerializer = jsonObject.get("type").getAsString().contains("shaped") ? (IRecipeSerializer<T>) IRecipeSerializer.SHAPED_RECIPE : (IRecipeSerializer<T>) IRecipeSerializer.SHAPELESS_RECIPE;
            else
                currentSerializer = (IRecipeSerializer<T>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(ResourceLocation.tryParse(jsonObject.get("type").getAsString()));

            if(jsonObject.get("type").getAsString().contains(getName(recipeType).toString()))
                addRecipeTo(modId, recipes, jsonObject, Utils.notNull(currentSerializer));
        }

        return recipes;
    }

    private static <C extends IInventory, T extends IRecipe<C>> IRecipeSerializer<T>  getSerializer(IRecipeType<T> recipeType)
    {
        return (IRecipeSerializer<T>) ClientUtils.getClientLevel().getRecipeManager().getAllRecipesFor(recipeType).get(0).getSerializer();
    }

    private static <T extends IRecipe<?>> void addRecipeTo(String modId, List<T> recipes, JsonObject jsonObject, IRecipeSerializer<T> recipeSerializer)
    {
        try
        {
            T tempRecipe = recipeSerializer.fromJson(new ResourceLocation(modId, "recipe"), jsonObject);
            T recipe = recipeSerializer.fromJson(new ResourceLocation(modId, ModRecipeCreatorDispatcher.getOutput(tempRecipe).getIngredientsWithCount().stream().findAny().orElse(CraftIngredients.CraftIngredient.EMPTY).getId().getPath()), jsonObject);
            recipes.add(recipe);
        }
        catch(JsonSyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public static <C extends IInventory, T extends IRecipe<C>> void removeAddedRecipe(T addedRecipe, String modId)
    {
        IRecipeType<T> recipeType = (IRecipeType<T>) addedRecipe.getType();

        File recipeFile = RecipeFileUtils.getRecipeFile(modId);

        checkTypeGroup(recipeFile, recipeType);

        String currentFileContent = readFile(recipeFile);
        Matcher jsonMatcher = RECIPE_PATTERN.matcher(currentFileContent);

        while(jsonMatcher.find())
        {
            JsonObject jsonObject = GSON.fromJson(new StringReader(jsonMatcher.group(1)), JsonObject.class);
            IRecipeSerializer<T> currentSerializer = (IRecipeSerializer<T>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(ResourceLocation.tryParse(jsonObject.get("type").getAsString()));

            T tempRecipe = currentSerializer.fromJson(new ResourceLocation(modId, "recipe"), jsonObject);
            T recipe = currentSerializer.fromJson(new ResourceLocation(modId, ModRecipeCreatorDispatcher.getOutput(tempRecipe).getIngredientsWithCount().stream().findAny().orElse(CraftIngredients.CraftIngredient.EMPTY).getId().getPath()), jsonObject);

            if(!recipe.getSerializer().getRegistryName().equals(addedRecipe.getSerializer().getRegistryName()))
                continue;

            PacketBuffer existingRecipeBuffer = new PacketBuffer(Unpooled.buffer());
            PacketBuffer jsonRecipeBuffer = new PacketBuffer(Unpooled.buffer());
            currentSerializer.toNetwork(existingRecipeBuffer, addedRecipe);
            currentSerializer.toNetwork(jsonRecipeBuffer, recipe);

            if(existingRecipeBuffer.compareTo(jsonRecipeBuffer) == 0)
            {
                currentFileContent = currentFileContent.replace(jsonMatcher.group(0), "");
                break;
            }
        }

        writeTo(recipeFile, Collections.singletonList(currentFileContent));
    }

    private static void writeTo(File recipeFile, List<String> content)
    {
        try
        {
            Files.write(recipeFile.toPath(), content, StandardCharsets.UTF_8);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static <T extends IRecipe<?>> List<ModifiedRecipe> getModifiedRecipesFor()
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

    public static void insertAndWriteLines(String modId, IRecipeType<?> recipeType, String line)
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

            writeTo(recipeFile, lines);
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

            writeTo(recipeFile, lines);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void checkTypeGroup(File recipeFile, IRecipeType<?> recipeType)
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
                        writeTo(recipeFile, lines);
                        break;
                    }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String getStartRecipeTypeGroup(IRecipeType<?> type)
    {
        return getName(type).getPath() + "-recipes";
    }

    public static void setRecipeType(JsonObject obj, IRecipeType<?> type)
    {
        obj.addProperty("type", getName(type).toString());
    }

    public static ResourceLocation getName(IRecipeType<?> recipeType)
    {
        return Registry.RECIPE_TYPE.getKey(recipeType);
    }

    public static <T extends IRecipe<C>, C extends IInventory> IRecipeType<T> byName(ResourceLocation location)
    {
        return (IRecipeType<T>) Registry.RECIPE_TYPE.getOptional(location).orElse(null);
    }

    public static List<JsonObject> getJsonRecipes(IRecipeType<?> recipeType, String modId)
    {
        List<JsonObject> jsonRecipes = new ArrayList<>();

        File recipeFile = RecipeFileUtils.getRecipeFile(modId);

        checkTypeGroup(recipeFile, recipeType);

        String recipeFileContent = readFile(recipeFile);
        Matcher jsonMatcher = RECIPE_PATTERN.matcher(recipeFileContent);

        while(jsonMatcher.find())
        {
            JsonObject jsonObject = GSON.fromJson(new StringReader(jsonMatcher.group(1)), JsonObject.class);
            if(jsonObject.get("type").getAsString().equals(getName(recipeType).toString()))
                jsonRecipes.add(jsonObject);
        }

        return jsonRecipes;
    }

    private static String readFile(File recipeFile)
    {
        try
        {
            return Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8).stream().map(String::toString).collect(Collectors.joining("\n"));
        }
        catch(IOException e)
        {
            return "";
        }
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
                                writeTo(recipeFile, lines);
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
        REMOVED("event.remove", new StringTextComponent("Removed").withStyle(TextFormatting.RED)),
        REPLACED_INPUT("event.replaceInput", new StringTextComponent("Input Replaced").withStyle(TextFormatting.GOLD)),
        REPLACED_OUTPUT("event.replaceOutput", new StringTextComponent("Output Replaced").withStyle(TextFormatting.YELLOW));

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

        public IFormattableTextComponent getTitle()
        {
            return title;
        }
    }
}
