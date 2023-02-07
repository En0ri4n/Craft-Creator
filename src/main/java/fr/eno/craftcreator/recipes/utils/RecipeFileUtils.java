package fr.eno.craftcreator.recipes.utils;

import com.google.gson.*;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.kubejs.KubeJSManager;
import fr.eno.craftcreator.recipes.serializers.ModRecipeSerializer;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.Utils;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// TODO: re arrange methods (in new classes) and sort them
public class RecipeFileUtils
{
    private static final Pattern RECIPE_PATTERN = Pattern.compile("\n.*event\\.custom\\(([^)]*)\\)");
    private static final Pattern MODIFIED_RECIPE_PATTERN = Pattern.compile("\n.*event\\.(.*)\\(([^)]*)\\)");

    public final static String ADDED_RECIPES_START = "ADDED-RECIPES-START";
    public final static String MODIFIED_RECIPE_START = "REMOVED-RECIPES-START";

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public static File getRecipeFile(SupportedMods mod)
    {
        File recipeFile = new File(KubeJSManager.getInstance().getKubeJSRecipesFolder(), mod.getModId() + ".js");

        if(!recipeFile.exists() || recipeFile.length() <= 0L)
        {
            try
            {
                recipeFile.createNewFile();

                try(InputStream inputStream = Utils.notNull(RecipeFileUtils.class.getResourceAsStream("/kubejs_base.js")))
                {
                    String fileContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    writeTo(recipeFile, String.format(fileContent, MODIFIED_RECIPE_START, ADDED_RECIPES_START));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return recipeFile;
    }

    public static <C extends IInventory, T extends IRecipe<C>> List<T> getAddedRecipesFor(SupportedMods mod, IRecipeType<T> recipeType)
    {
        List<T> recipes = new ArrayList<>();

        File recipeFile = RecipeFileUtils.getRecipeFile(mod);
        String recipeFileContent = readFile(recipeFile);

        checkTypeGroup(recipeFileContent, recipeType);

        Matcher customRecipeMatcher = RECIPE_PATTERN.matcher(recipeFileContent);

        while(customRecipeMatcher.find())
        {
            String recipeJson = customRecipeMatcher.group(1);
            JsonObject jsonObject = GSON.fromJson(new StringReader(recipeJson), JsonObject.class);
            IRecipeSerializer<T> currentSerializer;

            ResourceLocation recipeTypeLocation = ClientUtils.parse(jsonObject.get("type").getAsString());

            if(recipeType == IRecipeType.CRAFTING && recipeTypeLocation.toString().contains(CommonUtils.getRecipeTypeName(recipeType).toString()))
                currentSerializer = recipeTypeLocation.toString().contains("shaped") ? (IRecipeSerializer<T>) IRecipeSerializer.SHAPED_RECIPE : (IRecipeSerializer<T>) IRecipeSerializer.SHAPELESS_RECIPE;
            else currentSerializer = getSerializer(recipeTypeLocation);

            if(recipeTypeLocation.toString().contains(CommonUtils.getRecipeTypeName(recipeType).toString())) addRecipeTo(mod, recipes, jsonObject, Utils.notNull(currentSerializer));
        }

        return recipes;
    }

    @SuppressWarnings("unchecked")
    private static <C extends IInventory, T extends IRecipe<C>> IRecipeSerializer<T> getSerializer(ResourceLocation resourceLocation)
    {
        return (IRecipeSerializer<T>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(resourceLocation);
    }

    @SuppressWarnings("unchecked")
    private static <C extends IInventory, T extends IRecipe<C>> IRecipeType<T> getType(IRecipeType<?> recipeType)
    {
        return (IRecipeType<T>) recipeType;
    }

    private static <T extends IRecipe<?>> void addRecipeTo(SupportedMods mod, List<T> recipes, JsonObject jsonObject, IRecipeSerializer<T> recipeSerializer)
    {
        try
        {
            T tempRecipe = recipeSerializer.fromJson(new ResourceLocation(mod.getModId(), "recipe"), jsonObject);
            T recipe = recipeSerializer.fromJson(new ResourceLocation(mod.getModId(), ModRecipeCreatorDispatcher.getOutput(tempRecipe).getIngredientsWithCount().stream().findAny().orElse(CraftIngredients.CraftIngredient.EMPTY).getId().getPath()), jsonObject);
            recipes.add(recipe);
        }
        catch(JsonSyntaxException | ResourceLocationException e)
        {
            e.printStackTrace();
        }
    }

    public static <C extends IInventory, T extends IRecipe<C>> void removeAddedRecipe(SupportedMods mod, T addedRecipe)
    {
        IRecipeType<T> recipeType = getType(addedRecipe.getType());

        File recipeFile = RecipeFileUtils.getRecipeFile(mod);
        String recipeFileContent = readFile(recipeFile);

        checkTypeGroup(recipeFileContent, recipeType);

        Matcher recipeMatcher = RECIPE_PATTERN.matcher(recipeFileContent);

        while(recipeMatcher.find())
        {
            JsonObject jsonObject = GSON.fromJson(recipeMatcher.group(1), JsonObject.class);
            IRecipeSerializer<T> currentSerializer = getSerializer(ClientUtils.parse(jsonObject.get("type").getAsString()));

            T tempRecipe = currentSerializer.fromJson(new ResourceLocation(mod.getModId(), "recipe"), jsonObject);
            T recipe = currentSerializer.fromJson(new ResourceLocation(mod.getModId(), ModRecipeCreatorDispatcher.getOutput(tempRecipe).getIngredientsWithCount().stream().findAny().orElse(CraftIngredients.CraftIngredient.EMPTY).getId().getPath()), jsonObject);

            if(!CommonUtils.equals(recipe.getSerializer(), addedRecipe.getSerializer())) continue;

            PacketBuffer existingRecipeBuffer = new PacketBuffer(Unpooled.buffer());
            PacketBuffer jsonRecipeBuffer = new PacketBuffer(Unpooled.buffer());
            currentSerializer.toNetwork(existingRecipeBuffer, addedRecipe);
            currentSerializer.toNetwork(jsonRecipeBuffer, recipe);

            if(existingRecipeBuffer.compareTo(jsonRecipeBuffer) == 0)
            {
                recipeFileContent = recipeFileContent.replace(recipeMatcher.group(), "");
                break;
            }
        }

        writeTo(recipeFile, recipeFileContent);
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

    public static void writeTo(File file, String content)
    {
        try
        {
            Files.write(file.toPath(), Collections.singletonList(content), StandardCharsets.UTF_8);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static List<ModifiedRecipe> getModifiedRecipes(SupportedMods mod)
    {
        List<ModifiedRecipe> modifiedRecipes = new ArrayList<>();

        File recipeFile = RecipeFileUtils.getRecipeFile(mod);
        String fileContent = readFile(recipeFile);

        Matcher modifiedRecipeMatcher = MODIFIED_RECIPE_PATTERN.matcher(fileContent);

        while(modifiedRecipeMatcher.find())
        {
            if(!ModifiedRecipe.ModifiedRecipeType.isModifiedRecipeType(modifiedRecipeMatcher.group(1))) continue;

            ModifiedRecipe modifiedRecipe = getModifiedRecipe(ModifiedRecipe.ModifiedRecipeType.byDescriptor(modifiedRecipeMatcher.group(1)), modifiedRecipeMatcher.group(2));

            modifiedRecipes.add(modifiedRecipe);
        }

        return modifiedRecipes;
    }

    public static void addModifiedRecipe(SupportedMods mod, String serializedModifiedRecipe)
    {
        File recipeFile = RecipeFileUtils.getRecipeFile(mod);
        String fileContent = readFile(recipeFile);

        fileContent = check(fileContent, MODIFIED_RECIPE_START, "[{]", "\n\t// " + MODIFIED_RECIPE_START);

        Pattern modifiedRecipePattern = Pattern.compile(MODIFIED_RECIPE_START + "()");
        Matcher modifiedRecipeMatcher = modifiedRecipePattern.matcher(fileContent);

        if(!modifiedRecipeMatcher.find()) return;

        fileContent = modifiedRecipeMatcher.replaceFirst(serializedModifiedRecipe);

        writeTo(recipeFile, fileContent);
    }

    public static Pattern getRecipeTypePattern(IRecipeType<?> recipeType)
    {
        return Pattern.compile(getStartRecipeTypeGroup(recipeType) + "()");
    }

    public static void addRecipeToFile(SupportedMods mod, IRecipeType<?> recipeType, String serializedRecipe)
    {
        File recipeFile = RecipeFileUtils.getRecipeFile(mod);
        String fileContent = readFile(recipeFile);

        fileContent = checkTypeGroup(fileContent, recipeType);

        Pattern recipeTypePattern = getRecipeTypePattern(recipeType);
        Matcher recipeTypeMatcher = recipeTypePattern.matcher(fileContent);

        if(!recipeTypeMatcher.find()) return;

        fileContent = recipeTypeMatcher.replaceFirst(recipeTypeMatcher.group() + "\n\t" + serializedRecipe);

        writeTo(recipeFile, fileContent);
    }

    private static String check(String contentToCheck, String toSearch, String toSearchIfNotFound, String toAppendIfNotFound)
    {
        Pattern pattern = Pattern.compile(toSearch + "()");
        Matcher matcher = pattern.matcher(contentToCheck);

        if(!matcher.find())
        {
            Pattern notFoundPattern = Pattern.compile(toSearchIfNotFound + "()");
            Matcher notFoundMatcher = notFoundPattern.matcher(contentToCheck);

            if(notFoundMatcher.find())
            {
                contentToCheck = notFoundMatcher.replaceFirst(notFoundMatcher.group() + toAppendIfNotFound);
                return check(contentToCheck, toSearch, toSearchIfNotFound, toAppendIfNotFound);
            }
        }

        return contentToCheck;
    }

    private static String checkTypeGroup(String recipeFileContent, IRecipeType<?> recipeType)
    {
        recipeFileContent = check(recipeFileContent, ADDED_RECIPES_START, "[{]", "\n\n\t// " + ADDED_RECIPES_START);
        recipeFileContent = check(recipeFileContent, getStartRecipeTypeGroup(recipeType), ADDED_RECIPES_START, "\n\t// " + getStartRecipeTypeGroup(recipeType));
        return recipeFileContent;
    }

    private static String getStartRecipeTypeGroup(IRecipeType<?> type)
    {
        return CommonUtils.getRecipeTypeName(type).getPath() + "-recipes";
    }

    public static void setRecipeType(JsonObject obj, IRecipeType<?> type)
    {
        obj.addProperty("type", CommonUtils.getRecipeTypeName(type).toString());
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

    private static ModifiedRecipe getModifiedRecipe(ModifiedRecipe.ModifiedRecipeType modifiedRecipeType, String line)
    {
        ModifiedRecipe modifiedRecipe = new ModifiedRecipe(modifiedRecipeType);
        JsonObject json = GSON.fromJson(line, JsonElement.class).getAsJsonObject();

        for(ModRecipeSerializer.RecipeDescriptors descriptor : ModRecipeSerializer.RecipeDescriptors.values())
            if(json.has(descriptor.getTag())) modifiedRecipe.setDescriptor(descriptor, json.get(descriptor.getTag()).getAsString());

        return modifiedRecipe;
    }

    public static void removeModifiedRecipe(SupportedMods mod, ModifiedRecipe recipe)
    {
        File recipeFile = RecipeFileUtils.getRecipeFile(mod);
        String fileContent = readFile(recipeFile);

        Matcher modifiedRecipeMatcher = MODIFIED_RECIPE_PATTERN.matcher(fileContent);

        while(modifiedRecipeMatcher.find())
        {
            if(!ModifiedRecipe.ModifiedRecipeType.isModifiedRecipeType(modifiedRecipeMatcher.group(1))) continue;

            ModifiedRecipe modifiedRecipe = getModifiedRecipe(recipe.getType(), modifiedRecipeMatcher.group(2));

            if(modifiedRecipe.getRecipeMap().equals(recipe.getRecipeMap()))
            {
                fileContent = fileContent.replace(modifiedRecipeMatcher.group(), "");
                break;
            }
        }

        writeTo(recipeFile, Collections.singletonList(fileContent));
    }

    public static boolean isModifiedRecipePresent(SupportedMods mod, ModifiedRecipe recipe)
    {
        File recipeFile = RecipeFileUtils.getRecipeFile(mod);
        String fileContent = readFile(recipeFile);

        Matcher modifiedRecipeMatcher = MODIFIED_RECIPE_PATTERN.matcher(fileContent);

        while(modifiedRecipeMatcher.find())
        {
            if(!ModifiedRecipe.ModifiedRecipeType.isModifiedRecipeType(modifiedRecipeMatcher.group(1))) continue;

            ModifiedRecipe modifiedRecipe = getModifiedRecipe(recipe.getType(), modifiedRecipeMatcher.group(2));

            if(modifiedRecipe.getRecipeMap().equals(recipe.getRecipeMap())) return true;
        }

        return false;
    }
}
