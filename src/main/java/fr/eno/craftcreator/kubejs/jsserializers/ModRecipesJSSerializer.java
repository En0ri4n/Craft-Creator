package fr.eno.craftcreator.kubejs.jsserializers;

import fr.eno.craftcreator.*;
import fr.eno.craftcreator.kubejs.utils.*;
import net.minecraft.client.*;
import net.minecraft.inventory.*;
import net.minecraft.item.crafting.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;

import java.util.List;
import java.util.Map;

public class ModRecipesJSSerializer
{
    public static final String REMOVED_RECIPES = "removed_recipes";
    protected String modId;

    public ModRecipesJSSerializer(String modId)
    {
        this.modId = modId;
    }

    public <C extends IInventory, T extends IRecipe<C>> void removeRecipe(Map<RemoveTag, String> removeTags)
    {
        StringBuilder line = new StringBuilder("event.remove({");// id: '" + getRecipe(type, result).getId() + "' })";
        for(Map.Entry<RemoveTag, String> entry : removeTags.entrySet())
        {
            line.append("\"").append(entry.getKey().getTag()).append("\": '").append(entry.getValue()).append("',");
        }

        String finishedLine = line.replace(line.length() - 1, line.length(), "})").toString();
        System.out.println(finishedLine);
        RecipeFileUtils.insertAndWriteLinesToRemoveRecipe(this.modId, finishedLine);
    }

    private <C extends IInventory, T extends IRecipe<C>> IRecipe<C> getRecipe(IRecipeType<T> type, IItemProvider result)
    {
        RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
        return manager.getRecipesForType(type).stream().filter(irecipe -> irecipe.getRecipeOutput().getItem() == result.asItem()).findFirst().orElse(null);
    }

    protected void sendSuccessMessage(IRecipeType<?> type, ResourceLocation result)
    {
        TextComponent baseComponent = new StringTextComponent("Recipe ");
        TextComponent recipeNameComp = new StringTextComponent(result.getPath() + "_from_" + RecipeFileUtils.getName(type).getPath());
        recipeNameComp.modifyStyle(style ->
        {
            style.applyFormatting(TextFormatting.GREEN);
            style.setUnderlined(true);
            return style;
        });
        TextComponent endComp = new StringTextComponent(" Successfully generated !");
        Minecraft.getInstance().player.sendMessage(baseComponent.appendSibling(recipeNameComp).appendSibling(endComp), Minecraft.getInstance().player.getUniqueID());
    }

    protected void sendFailMessage()
    {
        Minecraft.getInstance().player.sendMessage(References.getTranslate("js_serializer.fail.recipe_exists"), Minecraft.getInstance().player.getUniqueID());
    }

    public static ModRecipesJSSerializer getInstance(String modId)
    {
        return new ModRecipesJSSerializer(modId);
    }

    public enum RemoveTag
    {
        INPUT_ITEM("input"),
        OUTPUT_ITEM("output"),
        MOD_ID("mod"),
        RECIPE_TYPE("type"),
        RECIPE_ID("id");

        private final String tag;

        RemoveTag(String tag)
        {
            this.tag = tag;
        }

        public String getTag()
        {
            return tag;
        }
    }
}