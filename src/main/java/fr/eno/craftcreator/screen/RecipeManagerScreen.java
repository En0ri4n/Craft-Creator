package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RemoveAddedRecipePacket;
import fr.eno.craftcreator.packets.RemoveModifiedRecipe;
import fr.eno.craftcreator.packets.RemoveRecipePacket;
import fr.eno.craftcreator.packets.RetrieveServerRecipesPacket;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.recipes.utils.ListEntriesHelper;
import fr.eno.craftcreator.screen.widgets.DropdownListWidget;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.screen.widgets.SimpleTextFieldWidget;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleButton;
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
    
    private SimpleTextFieldWidget searchField;

    public RecipeManagerScreen()
    {
        super(new StringTextComponent(""));
        this.modId = DropdownListWidget.Entries.getModIds().get(0).getValue();
        this.recipeType = CommonUtils.parse(DropdownListWidget.Entries.getRecipeTypes(modId).get(0).getValue());
    }

    @Override
    protected void init()
    {
        this.clearLists();

        int bottomHeight = 20;

        addList(modDropdown = new DropdownListWidget<>(width / 2 - 200, 0, 200, 20, 20, DropdownListWidget.Entries.getModIds(), (entry) ->
        {
            this.modId = entry.getValue();
            this.recipeTypeDropdown.setEntries(DropdownListWidget.Entries.getRecipeTypes(this.modId));
            this.recipeType = CommonUtils.parse(this.recipeTypeDropdown.getDropdownEntries().get(0).getValue());
            this.searchField.setValue("");
        }));

        addList(recipeTypeDropdown = new DropdownListWidget<>(width / 2, 0, 200, 20, 20, DropdownListWidget.Entries.getRecipeTypes(getCurrentMod().getModId()), (entry) ->
        {
            this.recipeType = CommonUtils.parse(entry.getValue());
            updateLists(true);
        }));


        addList(new SimpleListWidget(10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.recipes"), (entry) ->
        {
            IRecipe<?> recipeToRemove = ((SimpleListWidget.RecipeEntry) entry).getRecipe();
            InitPackets.NetworkHelper.sendToServer(new RemoveRecipePacket(getCurrentMod(), new KubeJSModifiedRecipe(KubeJSModifiedRecipe.KubeJSModifiedRecipeType.REMOVED, Collections.singletonMap(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID, recipeToRemove.getId().toString())), ModRecipeSerializer.SerializerType.KUBE_JS));
            updateLists(false);
        }, SupportedMods.isKubeJSLoaded()));
        
        addWidget(searchField = new SimpleTextFieldWidget(10, height - 27, this.width / 3 - 35, 20, textField ->
        {
            if(!textField.getValue().isEmpty())
            {
                this.setEntries(2, ListEntriesHelper.getFilteredRecipes(this.recipeType, textField.getValue()), true);
            }
            else
                updateLists(false);
        }));

        addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.clear_search"), 10 + this.width / 3 - 35 + 2, height - 27, 20, 20, button ->
        {
            updateLists(true);
            this.searchField.setValue("");
        }));

        addList(new SimpleListWidget(this.width / 3 + 10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.added_recipes"), (entry) ->
        {
            SimpleListWidget.RecipeEntry recipeEntry = (SimpleListWidget.RecipeEntry) entry;
            ModRecipeSerializer.SerializerType serializerType = recipeEntry.getRecipe().getId().getNamespace().equals(References.MOD_ID) ? ModRecipeSerializer.SerializerType.MINECRAFT_DATAPACK : ModRecipeSerializer.SerializerType.KUBE_JS;
            InitPackets.NetworkHelper.sendToServer(new RemoveAddedRecipePacket(getCurrentMod(), recipeEntry.getRecipe(), serializerType));
            updateLists(false);

        }, true));

        addList(new SimpleListWidget(this.width / 3 * 2 + 10, 30, this.width / 3 - 15, this.height - 30 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.modified_recipes"), (entry) ->
        {
            InitPackets.NetworkHelper.sendToServer(new RemoveModifiedRecipe(getCurrentMod(), ((SimpleListWidget.ModifiedRecipeEntry) entry).getRecipe()));
            updateLists(false);
        }, SupportedMods.isKubeJSLoaded()));

        updateLists(true);
        getLists().forEach(slw -> slw.setCanHaveSelected(true));

        addButton(new SimpleButton(References.getTranslate("screen.button.back"), this.width / 2 - 40, this.height - bottomHeight - 7, 80, 20, button -> ClientUtils.openScreen(null)));
        addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.remove_recipe"), this.width - 130, this.height - bottomHeight - 7, 120, 20, button -> ClientUtils.openScreen(new RemoveRecipeManagerScreen())));
    }

    public <T extends SimpleListWidget.Entry> void addToList(InitPackets.RecipeList list, T entry)
    {
        switch(list)
        {
            case ADDED_RECIPES:
                getList(3).getEntries().add(entry);
                break;
            case MODIFIED_RECIPES:
                getList(4).getEntries().add(entry);
                break;
        }
    }

    private void updateLists(boolean resetScroll)
    {
        clearListsContents(resetScroll, 2, 3, 4);
        // 0 = Mod Id Dropdown
        // 1 = Recipe Type Dropdown
        // 2 = Recipes List
        // 3 = Added Recipes List
        // 4 = Modified Recipes List
        this.setEntries(2, ListEntriesHelper.getRecipes(this.recipeType), resetScroll);
        InitPackets.NetworkHelper.sendToServer(new RetrieveServerRecipesPacket(getCurrentMod(), InitPackets.RecipeList.ADDED_RECIPES, this.recipeType, SupportedMods.isKubeJSLoaded() ? ModRecipeSerializer.SerializerType.KUBE_JS : ModRecipeSerializer.SerializerType.MINECRAFT_DATAPACK));
        if(SupportedMods.isKubeJSLoaded())
            InitPackets.NetworkHelper.sendToServer(new RetrieveServerRecipesPacket(getCurrentMod(), InitPackets.RecipeList.MODIFIED_RECIPES, this.recipeType, ModRecipeSerializer.SerializerType.KUBE_JS));
    }

    private SupportedMods getCurrentMod()
    {
        return SupportedMods.getMod(this.modId);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
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
        modDropdown.mouseClicked(mouseX, mouseY, button);
        recipeTypeDropdown.mouseClicked(mouseX, mouseY, button);
        this.getLists().forEach(list -> list.setCanDisplayTooltips(!modDropdown.isFocused() && !recipeTypeDropdown.isFocused()));

        if(!modDropdown.isFocused() && !recipeTypeDropdown.isFocused())
            return super.mouseClicked(mouseX, mouseY, button);

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        modDropdown.mouseScrolled(mouseX, mouseY, delta);
        recipeTypeDropdown.mouseScrolled(mouseX, mouseY, delta);

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        modDropdown.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        recipeTypeDropdown.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}