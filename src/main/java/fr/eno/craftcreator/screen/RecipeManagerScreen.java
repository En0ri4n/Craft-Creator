package fr.eno.craftcreator.screen;


import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.recipes.utils.DatapackHelper;
import fr.eno.craftcreator.recipes.utils.ListEntriesHelper;
import fr.eno.craftcreator.screen.widgets.DropdownListWidget;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.screen.widgets.SimpleTextFieldWidget;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleButton;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;
import java.util.Collections;

public class RecipeManagerScreen extends ListScreen
{
    private String modId;
    private ResourceLocation recipeType;

    private DropdownListWidget<DropdownListWidget.StringEntry> modDropdown;
    private DropdownListWidget<DropdownListWidget.StringEntry> recipeTypeDropdown;
    
    private SimpleTextFieldWidget searchField;

    public RecipeManagerScreen()
    {
        super(new TextComponent(""));
        this.modId = DropdownListWidget.Entries.getModIds().get(0).getValue();
        this.recipeType = ClientUtils.parse(DropdownListWidget.Entries.getRecipeTypes(modId).get(0).getValue());
    }

    @Override
    protected void init()
    {
        this.clearLists();

        int bottomHeight = 20;

        addRenderableWidget(modDropdown = new DropdownListWidget<>(width / 2 - 200, 0, 200, 20, 20, DropdownListWidget.Entries.getModIds(), (entry) ->
        {
            this.modId = entry.getValue();
            this.recipeTypeDropdown.setEntries(DropdownListWidget.Entries.getRecipeTypes(entry.getValue()));
            this.recipeType = ClientUtils.parse(this.recipeTypeDropdown.getEntries().get(0).getValue());
            this.searchField.setValue("");
            updateLists(true);
        }));

        addRenderableWidget(recipeTypeDropdown = new DropdownListWidget<>(width / 2, 0, 200, 20, 20, DropdownListWidget.Entries.getRecipeTypes(this.modId), (entry) ->
        {
            this.recipeType = ClientUtils.parse(entry.getValue());
            updateLists(true);
        }));


        this.addList(new SimpleListWidget(10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.recipes"), (entry) ->
        {
            Recipe<?> recipeToRemove = ((SimpleListWidget.RecipeEntry) entry).getRecipe();
            ModRecipeCreatorDispatcher.getSeralizer(this.modId).removeRecipe(new KubeJSModifiedRecipe(KubeJSModifiedRecipe.KubeJSModifiedRecipeType.REMOVED, Collections.singletonMap(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID, recipeToRemove.getId().toString())), ModRecipeSerializer.SerializerType.KUBE_JS);
            updateLists(false);
        }, SupportedMods.isKubeJSLoaded()));
        
        this.addRenderableWidget(searchField = new SimpleTextFieldWidget(new TextComponent(""), ClientUtils.getFontRenderer(), 10, height - 30, this.width / 3 - 35, 20, textField ->
        {
            if(!textField.getValue().isEmpty())
            {
                this.setEntries(0, ListEntriesHelper.getFilteredRecipes(this.recipeType, textField.getValue()), true);
            }
            else
                updateLists(false);
        }));

        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.recipe_manager.button.clear_search"), 10 + this.width / 3 - 35 + 2, height - 30, 20, 20, button ->
        {
            updateLists(true);
            this.searchField.setValue("");
        }));

        this.addList(new SimpleListWidget(this.width / 3 + 10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.added_recipes"), (entry) ->
        {
            SimpleListWidget.RecipeEntry recipeEntry = (SimpleListWidget.RecipeEntry) entry;

            if(!SupportedMods.isKubeJSLoaded())
            {
                DatapackHelper.deleteRecipe(recipeEntry.getRecipe());
            }
            else
            {
                ModRecipeCreatorDispatcher.getSeralizer(this.modId).removeAddedRecipeFrom(getCurrentMod(), recipeEntry.getRecipe(), ModRecipeSerializer.SerializerType.KUBE_JS);
                updateLists(false);
            }
        }, true));

        this.addList(new SimpleListWidget(this.width / 3 * 2 + 10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.modified_recipes"), (entry) ->
        {
            ModRecipeSerializer.removeModifiedRecipe(getCurrentMod(), ((SimpleListWidget.ModifiedRecipeEntry) entry).getRecipe());
            updateLists(false);
        }, SupportedMods.isKubeJSLoaded()));

        updateLists(true);
        this.getLists().forEach(slw -> slw.setCanHaveSelected(true));

        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.recipe_manager.button.back"), this.width / 2 - 40, this.height - bottomHeight - 7, 80, 20, button -> ClientUtils.openScreen(null)));
        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.recipe_manager.button.remove_recipe"), this.width - 130, this.height - bottomHeight - 7, 120, 20, button -> ClientUtils.openScreen(new RemoveRecipeManagerScreen())));
    }

    private void updateLists(boolean resetScroll)
    {
        this.setEntries(0, ListEntriesHelper.getRecipes(this.recipeType), resetScroll);
        this.setEntries(1, ListEntriesHelper.getAddedRecipesEntryList(getCurrentMod(), this.recipeType), resetScroll);
        this.setEntries(2, ListEntriesHelper.getModifiedRecipesEntryList(getCurrentMod()), resetScroll);
    }

    private SupportedMods getCurrentMod()
    {
        return SupportedMods.getMod(this.modId);
    }

    private RecipeType<?> getCurrentRecipeType()
    {
        return CommonUtils.getRecipeTypeByName(this.recipeType);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        this.searchField.render(matrixStack, mouseX, mouseY, partialTicks);
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