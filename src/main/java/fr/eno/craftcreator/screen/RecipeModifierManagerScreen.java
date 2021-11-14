package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.DeserializerHelper;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class RecipeModifierManagerScreen extends ChildrenScreen
{
    private final String modId;
    private final ResourceLocation recipeType;

    public RecipeModifierManagerScreen(Screen parentIn, String modId, ResourceLocation recipeType)
    {
        super(new StringTextComponent(""), parentIn);
        this.modId = modId;
        this.recipeType = recipeType;
    }

    @Override
    protected void init()
    {
        this.clearLists();

        int bottomHeight = 20;

        this.addList(new SimpleListWidget(minecraft, 5, 5, this.width / 4, this.height - bottomHeight, 15, 15, 5, References.getTranslate("screen.recipe_manager.list.recipes")));
        this.addList(new SimpleListWidget(minecraft, this.width - 5 - this.width / 4, 5, this.width / 4, this.height - bottomHeight, 15, 15, 5, References.getTranslate("screen.recipe_manager.list.modified_recipes")));

        this.setEntries(0, DeserializerHelper.getRecipes(this.recipeType));
        this.setEntries(1, DeserializerHelper.getModifiedRecipesEntryList(this.modId, this.recipeType));

        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.back"), this.width / 2 - 40, this.height - bottomHeight - 7, 80, 20, button -> minecraft.displayGuiScreen(new ModSelectionScreen())));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
