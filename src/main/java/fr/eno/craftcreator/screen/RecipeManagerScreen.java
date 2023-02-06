package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.recipes.serializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.recipes.utils.ListEntriesHelper;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.recipes.utils.RecipeFileUtils;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleButton;
import fr.eno.craftcreator.screen.widgets.DropdownListWidget;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;

public class RecipeManagerScreen extends ListScreen
{
    private String modId;
    private ResourceLocation recipeType;

    private DropdownListWidget<DropdownListWidget.StringEntry> modDropdown;
    private DropdownListWidget<DropdownListWidget.StringEntry> recipeTypeDropdown;

    public RecipeManagerScreen()
    {
        super(new StringTextComponent(""));
        this.modId = DropdownListWidget.Entries.getModIds().get(0).getValue();
        this.recipeType = ResourceLocation.tryParse(DropdownListWidget.Entries.getRecipeTypes(modId).get(0).getValue());
    }

    @Override
    protected void init()
    {
        this.clearLists();

        int bottomHeight = 20;

        addWidget(modDropdown = new DropdownListWidget<>(width / 2 - 200, 0, 200, 20, 20, DropdownListWidget.Entries.getModIds(), (entry) ->
        {
            this.modId = entry.getValue();
            this.recipeTypeDropdown.setEntries(DropdownListWidget.Entries.getRecipeTypes(entry.getValue()));
            this.recipeType = ResourceLocation.tryParse(this.recipeTypeDropdown.getEntries().get(0).getValue());
            updateLists();
        }));

        addWidget(recipeTypeDropdown = new DropdownListWidget<>(width / 2, 0, 200, 20, 20, DropdownListWidget.Entries.getRecipeTypes(this.modId), (entry) ->
        {
            this.recipeType = ResourceLocation.tryParse(entry.getValue());
            updateLists();
        }));


        this.addList(new SimpleListWidget(minecraft, 10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.recipes"), (entry) ->
        {
            IRecipe<?> recipeToRemove = ((SimpleListWidget.RecipeEntry) entry).getRecipe();
            ModRecipeCreatorDispatcher.getSeralizer(this.modId).removeRecipe(new ModifiedRecipe(RecipeFileUtils.ModifiedRecipeType.REMOVED, Collections.singletonMap(ModRecipesJSSerializer.RecipeDescriptors.RECIPE_ID, recipeToRemove.getId().toString())));
            updateLists();
        }));

        this.addList(new SimpleListWidget(minecraft, this.width / 3 + 10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.added_recipes"), (entry) ->
        {
            ModRecipesJSSerializer.removeAddedRecipe(((SimpleListWidget.RecipeEntry) entry).getRecipe());
            updateLists();
        }));

        this.addList(new SimpleListWidget(minecraft, this.width / 3 * 2 + 10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.modified_recipes"), (entry) ->
        {
            ModRecipesJSSerializer.removeModifiedRecipe(((SimpleListWidget.ModifiedRecipeEntry) entry).getRecipe());
            updateLists();
        }));

        updateLists();
        this.getLists().forEach(slw -> slw.setCanHaveSelected(true));

        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.back"), this.width / 2 - 40, this.height - bottomHeight - 7, 80, 20, button -> ClientUtils.openScreen(null)));
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
        modDropdown.render(matrixStack, mouseX, mouseY, partialTicks);
        recipeTypeDropdown.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.getLists().forEach(list -> list.setCanDisplayTooltips(!modDropdown.mouseClicked(mouseX, mouseY, button) && !recipeTypeDropdown.mouseClicked(mouseX, mouseY, button)));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        modDropdown.mouseScrolled(mouseX, mouseY, delta);
        recipeTypeDropdown.mouseScrolled(mouseX, mouseY, delta);

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}