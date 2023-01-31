package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.recipes.serializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.recipes.utils.ListEntriesHelper;
import fr.eno.craftcreator.recipes.utils.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.recipes.utils.RecipeFileUtils;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.screen.utils.ListScreen;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;

public class RecipeManagerScreen extends ListScreen
{
    private final String modId;
    private final ResourceLocation recipeType;

    public RecipeManagerScreen(String modId, ResourceLocation recipeType)
    {
        super(new StringTextComponent(""));
        this.modId = modId;
        this.recipeType = recipeType;
    }

    @Override
    protected void init()
    {
        this.clearLists();

        int bottomHeight = 20;

        this.addList(new SimpleListWidget(minecraft, 10, 10, this.width / 3 - 15, this.height - 10 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.recipes"), (entry) ->
        {
            IRecipe<?> recipeToRemove = ((SimpleListWidget.RecipeEntry) entry).getRecipe();
            ModRecipeCreatorDispatcher.getSeralizer(recipeToRemove.getId().getPath()).removeRecipe(new ModifiedRecipe(RecipeFileUtils.ModifiedRecipeType.REMOVED, Collections.singletonMap(ModRecipesJSSerializer.RecipeDescriptors.RECIPE_ID, recipeToRemove.getId().toString())));
            updateLists();
        }));
        this.addList(new SimpleListWidget(minecraft, this.width / 3 + 10, 10, this.width / 3 - 15, this.height - 10 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.added_recipes"), (entry) ->
        {
            ModRecipesJSSerializer.removeAddedRecipe(((SimpleListWidget.RecipeEntry) entry).getRecipe());
            updateLists();
        }));
        this.addList(new SimpleListWidget(minecraft, this.width / 3 * 2 + 10, 10, this.width / 3 - 15, this.height - 10 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.modified_recipes"), (entry) ->
        {
            ModRecipesJSSerializer.removeModifiedRecipe(((SimpleListWidget.ModifiedRecipeEntry) entry).getRecipe());
            updateLists();
        }));

        updateLists();
        this.getLists().forEach(slw -> slw.setCanHaveSelected(true));

        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.back"), this.width / 2 - 40, this.height - bottomHeight - 7, 80, 20, button -> ClientUtils.openScreen(new ModSelectionScreen())));
        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.modifier_manager"), this.width - 130, this.height - bottomHeight - 7, 120, 20, button -> ClientUtils.openScreen(new RecipeModifierManagerScreen(this))));
    }

    private void updateLists()
    {
        this.setEntries(0, ListEntriesHelper.getRecipes(this.recipeType));
        this.setEntries(1, ListEntriesHelper.getAddedRecipesEntryList(this.modId, this.recipeType));
        this.setEntries(2, ListEntriesHelper.getModifiedRecipesEntryList());
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}