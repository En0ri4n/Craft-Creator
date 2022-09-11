package fr.eno.craftcreator.kubejs.jsserializers;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class ModRecipesJSSerializer
{
    protected String modId;

    public ModRecipesJSSerializer(String modId)
    {
        this.modId = modId;
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
            RecipeFileUtils.insertAndWriteLinesToRemoveRecipe(this.modId, finishedLine);
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
        RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().world).getRecipeManager();
        return manager.getRecipesForType(type).stream().filter(irecipe -> irecipe.getRecipeOutput().getItem() == result.asItem()).findFirst().orElse(null);
    }
    protected void addRecipeToFile(String recipeJson, IRecipeType<?> recipeType)
    {
        RecipeFileUtils.insertAndWriteLines(this.modId, recipeType, "event.custom(" + recipeJson + ")");
    }

    public abstract PairValue<String, Integer> getParam(IRecipe<?> recipe);

    protected void sendSuccessMessage(IRecipeType<?> type, @Nullable ResourceLocation result)
    {
        TextComponent baseComponent = new StringTextComponent("Recipe ");
        TextComponent recipeNameComp = new StringTextComponent(Objects.requireNonNull(result).getPath() + "_from_" + RecipeFileUtils.getName(type).getPath());
        recipeNameComp.modifyStyle(style ->
        {
            style.applyFormatting(TextFormatting.GREEN);
            style.setUnderlined(true);
            return style;
        });
        TextComponent endComp = new StringTextComponent(" Successfully generated !");
        Objects.requireNonNull(Minecraft.getInstance().player).sendMessage(baseComponent.appendSibling(recipeNameComp).appendSibling(endComp), Minecraft.getInstance().player.getUniqueID());
    }

    JsonArray getArray(Multimap<ResourceLocation, Boolean> ingredients)
    {
        JsonArray array = new JsonArray();
        ingredients.forEach((loc, isTag) -> array.add(RecipeFileUtils.singletonJsonObject(isTag ? "tag" : "item", loc.toString())));
        return array;
    }

    JsonObject getResult(ItemStack result)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("item", Objects.requireNonNull(result.getItem().getRegistryName()).toString());
        if(result.getCount() > 1)
            map.put("count", result.getCount());

        return RecipeFileUtils.mapToJsonObject(map);
    }

    boolean isRecipeExists(IRecipeType<?> recipeType, ResourceLocation resultOutput)
    {
        return recipeType != null && resultOutput != null;
    }

    protected void sendFailMessage()
    {
        Objects.requireNonNull(Minecraft.getInstance().player).sendMessage(References.getTranslate("js_serializer.fail.recipe_exists"), Minecraft.getInstance().player.getUniqueID());
    }

    public abstract Map<String, ResourceLocation> getOutput(IRecipe<?> recipe);

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