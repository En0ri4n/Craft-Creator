package fr.eno.craftcreator.kubejs.jsserializers;

import fr.eno.craftcreator.*;
import fr.eno.craftcreator.kubejs.utils.*;
import net.minecraft.client.*;
import net.minecraft.inventory.*;
import net.minecraft.item.crafting.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;

public class ModRecipesJSSerializer
{
    protected String modId;

    public ModRecipesJSSerializer(String modId)
    {
        this.modId = modId;
    }

    public <C extends IInventory, T extends IRecipe<C>> void removeRecipe(IRecipeType<T> type, IItemProvider result)
    {
        String line = "event.remove({ id: '" + getRecipe(type, result).getId() + "' })";
        RecipeFileUtils.insertAndWriteLines(this.modId, type, line);
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
}