package fr.eno.craftcreator.recipes.kubejs;

import com.google.gson.*;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.utils.Utils;
import io.netty.buffer.Unpooled;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
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
public class KubeJSHelper
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
                
                try(InputStream inputStream = Utils.notNull(KubeJSHelper.class.getResourceAsStream("/kubejs_base.js")))
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
    
    public static <C extends Container, T extends Recipe<C>> List<T> getAddedRecipesFor(SupportedMods mod, RecipeType<T> recipeType)
    {
        List<T> recipes = new ArrayList<>();
        
        String recipeFileContent = checkTypeGroup(readFile(getRecipeFile(mod)), recipeType);
        
        Matcher customRecipeMatcher = RECIPE_PATTERN.matcher(recipeFileContent);
        
        while(customRecipeMatcher.find())
        {
            String recipeJson = customRecipeMatcher.group(1);
            JsonObject jsonObject = GSON.fromJson(new StringReader(recipeJson), JsonObject.class);
            RecipeSerializer<T> currentSerializer;
            
            ResourceLocation recipeTypeLocation = CommonUtils.parse(jsonObject.get("type").getAsString());
            
            currentSerializer = getSerializer(recipeTypeLocation);
            
            if(recipeTypeLocation.toString().contains(CommonUtils.getRecipeTypeName(recipeType).toString()))
                addRecipeTo(mod, recipes, jsonObject, Utils.notNull(currentSerializer));
        }
        
        return recipes;
    }

    public static <C extends Container, T extends Recipe<C>> List<JsonObject> getSerializedAddedRecipesFor(SupportedMods mod, RecipeType<T> recipeType)
    {
        List<JsonObject> recipes = new ArrayList<>();

        String recipeFileContent = checkTypeGroup(readFile(getRecipeFile(mod)), recipeType);

        Matcher customRecipeMatcher = RECIPE_PATTERN.matcher(recipeFileContent);

        while(customRecipeMatcher.find())
        {
            String recipeJson = customRecipeMatcher.group(1);
            JsonObject jsonObject = GSON.fromJson(new StringReader(recipeJson), JsonObject.class);

            ResourceLocation recipeTypeLocation = CommonUtils.parse(jsonObject.get("type").getAsString());

            if(recipeTypeLocation.toString().contains(CommonUtils.getRecipeTypeName(recipeType).toString()))
                recipes.add(jsonObject);
        }

        return recipes;
    }
    
    @SuppressWarnings("unchecked")
    public static <C extends Container, T extends Recipe<C>> RecipeSerializer<T> getSerializer(ResourceLocation resourceLocation)
    {
        if(resourceLocation.toString().equals("minecraft:crafting_shaped"))
            return (RecipeSerializer<T>) RecipeSerializer.SHAPED_RECIPE;
        else if(resourceLocation.toString().equals("minecraft:crafting_shapeless"))
            return (RecipeSerializer<T>) RecipeSerializer.SHAPELESS_RECIPE;

        return (RecipeSerializer<T>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(resourceLocation);
    }
    
    @SuppressWarnings("unchecked")
    private static <C extends Container, T extends Recipe<C>> T getRecipe(JsonObject recipeJson)
    {
        return (T) ForgeRegistries.RECIPE_SERIALIZERS.getValue(CommonUtils.parse(recipeJson.get("type").getAsString())).fromJson(References.getLoc("temp"), recipeJson);
    }
    
    @SuppressWarnings("unchecked")
    private static <C extends Container, T extends Recipe<C>> RecipeType<T> getType(RecipeType<?> recipeType)
    {
        return (RecipeType<T>) recipeType;
    }
    
    private static <T extends Recipe<?>> void addRecipeTo(SupportedMods mod, List<T> recipes, JsonObject jsonObject, RecipeSerializer<T> recipeSerializer)
    {
        try
        {
            T recipe = recipeSerializer.fromJson(getRecipeId(mod, jsonObject), jsonObject);
            recipes.add(recipe);
        }
        catch(JsonSyntaxException | ResourceLocationException e)
        {
            e.printStackTrace();
        }
    }

    public static <E extends Container, T extends Recipe<E>> ResourceLocation getRecipeId(SupportedMods mod, JsonObject recipeJson)
    {
        try
        {
            RecipeSerializer<T> recipeSerializer = getSerializer(CommonUtils.parse(recipeJson.get("type").getAsString()));
            T tempRecipe = recipeSerializer.fromJson(new ResourceLocation(mod.getModId(), "recipe"), recipeJson);
            ResourceLocation rl = new ResourceLocation(mod.getModId(), ModRecipeCreatorDispatcher.getOutput(tempRecipe).getIcon().getItem().getRegistryName().getPath());
            return rl;
        }
        catch(JsonSyntaxException | ResourceLocationException e)
        {
            e.printStackTrace();
        }

        return new ResourceLocation(mod.getModId(), "recipe");
    }

    public static <C extends Container, T extends Recipe<C>> ModRecipeSerializer.Feedback removeAddedRecipe(SupportedMods mod, T addedRecipe)
    {
        String recipeFileContent = checkTypeGroup(readFile(getRecipeFile(mod)), addedRecipe.getType());
        
        Matcher recipeMatcher = RECIPE_PATTERN.matcher(recipeFileContent);
        
        while(recipeMatcher.find())
        {
            JsonObject jsonObject = GSON.fromJson(recipeMatcher.group(1), JsonObject.class);
            RecipeSerializer<T> currentSerializer = getSerializer(CommonUtils.parse(jsonObject.get("type").getAsString()));

            T recipe = currentSerializer.fromJson(getRecipeId(mod, jsonObject), jsonObject);
            
            if(!CommonUtils.equals(recipe.getSerializer(), addedRecipe.getSerializer())) continue;
            
            FriendlyByteBuf existingRecipeBuffer = new FriendlyByteBuf(Unpooled.buffer());
            FriendlyByteBuf jsonRecipeBuffer = new FriendlyByteBuf(Unpooled.buffer());
            currentSerializer.toNetwork(existingRecipeBuffer, addedRecipe);
            currentSerializer.toNetwork(jsonRecipeBuffer, recipe);
            
            if(existingRecipeBuffer.compareTo(jsonRecipeBuffer) == 0)
            {
                recipeFileContent = recipeFileContent.replace(recipeMatcher.group(), "");
                writeTo(getRecipeFile(mod), recipeFileContent);
                return ModRecipeSerializer.Feedback.REMOVED;
            }
        }
        
        return ModRecipeSerializer.Feedback.DONT_EXISTS;
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
        writeTo(file, Collections.singletonList(content));
    }
    
    public static List<KubeJSModifiedRecipe> getModifiedRecipes(SupportedMods mod)
    {
        List<KubeJSModifiedRecipe> kubeJSModifiedRecipes = new ArrayList<>();
        
        File recipeFile = getRecipeFile(mod);
        String fileContent = readFile(recipeFile);
        
        Matcher modifiedRecipeMatcher = MODIFIED_RECIPE_PATTERN.matcher(fileContent);
        
        while(modifiedRecipeMatcher.find())
        {
            if(!KubeJSModifiedRecipe.KubeJSModifiedRecipeType.isModifiedRecipeType(modifiedRecipeMatcher.group(1))) continue;
            
            KubeJSModifiedRecipe kubeJSModifiedRecipe = getModifiedRecipe(KubeJSModifiedRecipe.KubeJSModifiedRecipeType.byDescriptor(modifiedRecipeMatcher.group(1)), modifiedRecipeMatcher.group(2));
            
            kubeJSModifiedRecipes.add(kubeJSModifiedRecipe);
        }
        
        return kubeJSModifiedRecipes;
    }
    
    public static ModRecipeSerializer.Feedback addModifiedRecipe(SupportedMods mod, String serializedModifiedRecipe)
    {
        File recipeFile = getRecipeFile(mod);
        String fileContent = readFile(recipeFile);
        
        fileContent = check(fileContent, MODIFIED_RECIPE_START, "[{]", "\n\t// " + MODIFIED_RECIPE_START);
        
        Matcher modifiedRecipeMatcher = getMatcher(Pattern.compile(MODIFIED_RECIPE_START + "()"), fileContent);
        if(!modifiedRecipeMatcher.find()) return ModRecipeSerializer.Feedback.FILE_ERROR;
    
        Matcher existingRecipeMatcher = getMatcher(Pattern.compile(ModRecipeSerializer.escape(serializedModifiedRecipe, true)), fileContent);
        if(existingRecipeMatcher.find()) return ModRecipeSerializer.Feedback.EXISTS;
        
        writeTo(recipeFile, getReplacedContent(modifiedRecipeMatcher, "\n\t" + serializedModifiedRecipe));
        return ModRecipeSerializer.Feedback.ADDED;
    }
    
    public static Pattern getRecipeTypePattern(RecipeType<?> recipeType)
    {
        return Pattern.compile(getStartRecipeTypeGroup(recipeType) + "()");
    }
    
    public static ModRecipeSerializer.Feedback addRecipeToFile(SupportedMods mod, RecipeType<?> recipeType, String serializedRecipe)
    {
        String fileContent = checkTypeGroup(readFile(getRecipeFile(mod)), recipeType);
        
        Matcher recipeTypeMatcher = getMatcher(getRecipeTypePattern(recipeType), fileContent);
        if(!recipeTypeMatcher.find()) return ModRecipeSerializer.Feedback.FILE_ERROR;
        
        Matcher existingRecipeMatcher = getMatcher(Pattern.compile(ModRecipeSerializer.escape(serializedRecipe, true)), fileContent);
        if(existingRecipeMatcher.find()) return ModRecipeSerializer.Feedback.EXISTS;
        
        writeTo(getRecipeFile(mod), getReplacedContent(recipeTypeMatcher, "\n\t" + serializedRecipe));
        return ModRecipeSerializer.Feedback.ADDED;
    }
    
    private static String check(String contentToCheck, String toSearch, String toSearchIfNotFound, String toAppendIfNotFound)
    {
        Matcher matcher = getMatcher(Pattern.compile(toSearch + "()"), contentToCheck);
        
        if(!matcher.find())
        {
            Matcher notFoundMatcher = getMatcher(Pattern.compile(toSearchIfNotFound + "()"), contentToCheck);
            
            if(notFoundMatcher.find())
            {
                return check(getReplacedContent(notFoundMatcher, toAppendIfNotFound), toSearch, toSearchIfNotFound, toAppendIfNotFound);
            }
        }
        
        return contentToCheck;
    }
    
    private static String checkTypeGroup(String recipeFileContent, RecipeType<?> recipeType)
    {
        recipeFileContent = check(recipeFileContent, ADDED_RECIPES_START, "[{]", "\n\n\t// " + ADDED_RECIPES_START);
        recipeFileContent = check(recipeFileContent, getStartRecipeTypeGroup(recipeType), ADDED_RECIPES_START, "\n\t// " + getStartRecipeTypeGroup(recipeType));
        return recipeFileContent;
    }
    
    private static String getReplacedContent(Matcher matcher, String toAppend)
    {
        return matcher.replaceFirst(matcher.group() + toAppend);
    }
    
    private static Matcher getMatcher(Pattern pattern, String content)
    {
        return pattern.matcher(content);
    }
    
    private static String getStartRecipeTypeGroup(RecipeType<?> type)
    {
        return CommonUtils.getRecipeTypeName(type).getPath() + "-recipes";
    }
    
    private static String readFile(File recipeFile)
    {
        try
        {
            return Files.readAllLines(recipeFile.toPath(), StandardCharsets.UTF_8).stream().map(String::toString).collect(Collectors.joining("\n"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return "";
        }
    }
    
    private static KubeJSModifiedRecipe getModifiedRecipe(KubeJSModifiedRecipe.KubeJSModifiedRecipeType kubeJSModifiedRecipeType, String modifiedRecipeJson)
    {
        KubeJSModifiedRecipe kubeJSModifiedRecipe = new KubeJSModifiedRecipe(kubeJSModifiedRecipeType);
        JsonObject recipeJson = GSON.fromJson(modifiedRecipeJson, JsonElement.class).getAsJsonObject();
        
        recipeJson.entrySet().forEach((entrySet) -> kubeJSModifiedRecipe.setDescriptor(ModRecipeSerializer.RecipeDescriptors.byTag(entrySet.getKey()), entrySet.getValue().getAsString()));
        
        return kubeJSModifiedRecipe;
    }
    
    public static void removeModifiedRecipe(SupportedMods mod, KubeJSModifiedRecipe recipe)
    {
        File recipeFile = getRecipeFile(mod);
        String fileContent = readFile(recipeFile);
        
        Matcher modifiedRecipeMatcher = MODIFIED_RECIPE_PATTERN.matcher(fileContent);
        
        while(modifiedRecipeMatcher.find())
        {
            if(!KubeJSModifiedRecipe.KubeJSModifiedRecipeType.isModifiedRecipeType(modifiedRecipeMatcher.group(1))) continue;
            
            KubeJSModifiedRecipe kubeJSModifiedRecipe = getModifiedRecipe(recipe.getType(), modifiedRecipeMatcher.group(2));
            
            if(kubeJSModifiedRecipe.getRecipeMap().equals(recipe.getRecipeMap()))
            {
                fileContent = fileContent.replace(modifiedRecipeMatcher.group(), "");
                break;
            }
        }
        
        writeTo(recipeFile, Collections.singletonList(fileContent));
    }
    
    public static boolean isModifiedRecipePresent(SupportedMods mod, KubeJSModifiedRecipe recipe)
    {
        File recipeFile = getRecipeFile(mod);
        String fileContent = readFile(recipeFile);
        
        Matcher modifiedRecipeMatcher = getMatcher(MODIFIED_RECIPE_PATTERN, fileContent);
        
        while(modifiedRecipeMatcher.find())
        {
            if(!KubeJSModifiedRecipe.KubeJSModifiedRecipeType.isModifiedRecipeType(modifiedRecipeMatcher.group(1))) continue;
            
            KubeJSModifiedRecipe kubeJSModifiedRecipe = getModifiedRecipe(recipe.getType(), modifiedRecipeMatcher.group(2));
            
            if(kubeJSModifiedRecipe.getRecipeMap().equals(recipe.getRecipeMap())) return true;
        }
        
        return false;
    }
}
