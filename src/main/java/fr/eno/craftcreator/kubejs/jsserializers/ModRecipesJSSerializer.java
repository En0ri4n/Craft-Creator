package fr.eno.craftcreator.kubejs.jsserializers;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class ModRecipesJSSerializer
{
    protected SupportedMods mod;

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
    protected void addRecipeToFile(String recipeJson, RecipeType<?> recipeType)
    {
        RecipeFileUtils.insertAndWriteLines(this.mod.getModId(), recipeType, "event.custom(" + recipeJson + ")");
    }

    public abstract PairValue<String, Integer> getParam(Recipe<?> recipe);

    protected void sendSuccessMessage(RecipeType<?> type, @Nullable ResourceLocation result)
    {
        TextComponent baseComponent = new TextComponent("Recipe ");
        TextComponent recipeNameComp = new TextComponent(Objects.requireNonNull(result).getPath() + "_from_" + RecipeFileUtils.getName(type).getPath());
        recipeNameComp.withStyle(style ->
        {
            style.applyFormat(ChatFormatting.GREEN);
            style.withUnderlined(true);
            return style;
        });
        TextComponent endComp = new TextComponent(" Successfully generated !");
        Objects.requireNonNull(Minecraft.getInstance().player).sendMessage(baseComponent.append(recipeNameComp).append(endComp), Minecraft.getInstance().player.getUUID());
    }

    protected JsonArray getArray(Multimap<ResourceLocation, Boolean> ingredients)
    {
        JsonArray array = new JsonArray();
        ingredients.forEach((loc, isTag) -> array.add(singletonJsonObject(isTag ? "tag" : "item", loc.toString())));
        return array;
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
        items.forEach(item -> array.add(singletonJsonObject(key, Objects.requireNonNull(item.getRegistryName()).toString())));
        return array;
    }

    protected JsonObject singletonJsonObject(String key, String value)
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
            if(o instanceof Number number) obj.addProperty(s, number);
            else if(o instanceof String string) obj.addProperty(s, string);
            else if(o instanceof Boolean bool) obj.addProperty(s, bool);
            else if(o instanceof Character character) obj.addProperty(s, character);
            else if(o instanceof JsonArray array) obj.add(s, array);
            else if(o instanceof JsonObject object) obj.add(s, object);
        });
        return obj;
    }

    boolean isRecipeExists(RecipeType<?> recipeType, ResourceLocation resultOutput)
    {
        return recipeType != null && resultOutput != null;
    }

    protected void sendFailMessage()
    {
        Objects.requireNonNull(Minecraft.getInstance().player).sendMessage(References.getTranslate("js_serializer.fail.recipe_exists"), Minecraft.getInstance().player.getUUID());
    }

    public abstract Map<String, ResourceLocation> getOutput(Recipe<?> recipe);

    public abstract ItemStack getOneOutput(Map.Entry<String, ResourceLocation> entry);

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