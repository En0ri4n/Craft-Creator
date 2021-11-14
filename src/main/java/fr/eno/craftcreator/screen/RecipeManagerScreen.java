package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.kubejs.utils.*;
import fr.eno.craftcreator.screen.buttons.*;
import fr.eno.craftcreator.screen.widgets.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;

import javax.annotation.*;

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

        this.addList(new SimpleListWidget(minecraft, 10, 10, this.width / 3 - 15, this.height - 10 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.recipes")));
        this.addList(new SimpleListWidget(minecraft, this.width / 3 + 10, 10, this.width / 3 - 15, this.height - 10 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.added_recipes")));
        this.addList(new SimpleListWidget(minecraft, this.width / 3 * 2 + 10, 10, this.width / 3 - 15, this.height - 10 - bottomHeight, 20, 14, 5, References.getTranslate("screen.recipe_manager.list.modified_recipes")));
        this.setEntries(0, DeserializerHelper.getRecipes(this.recipeType));
        this.setEntries(1, DeserializerHelper.getAddedRecipesEntryList(this.modId, this.recipeType));
        this.setEntries(2, DeserializerHelper.getModifiedRecipesEntryList(this.modId, this.recipeType));
        this.getLists().forEach(slw -> slw.setCanHaveSelected(true));

        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.back"), this.width / 2 - 40, this.height - bottomHeight - 7, 80, 20, button -> minecraft.displayGuiScreen(new ModSelectionScreen())));

        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.modifier_manager"), this.width - 130, this.height - bottomHeight - 7, 120, 20, button -> minecraft.displayGuiScreen(new RecipeModifierManagerScreen(this, this.modId, this.recipeType))));
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