package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.recipes.utils.ListEntriesHelper;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.screen.utils.ListScreen;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class ModSelectionScreen extends ListScreen
{
    public ModSelectionScreen()
    {
        super(new StringTextComponent(""));
    }

    @Override
    protected void init()
    {
        this.clearLists();

        this.addList(new SimpleListWidget(minecraft, 10, 10, this.width / 4, this.height - 20, 20, 14, 5, References.getTranslate("screen.mod_selection.list.mod"), null));
        this.getList(0).setEntries(ListEntriesHelper.getStringEntryList(SupportedMods.values()));

        this.addList(new SimpleListWidget(minecraft, this.width / 4 + 20, this.height / 2, this.width - (this.width / 4 + 20) - 10, this.height / 2 - 10, 20, 14, 5, References.getTranslate("screen.mod_selection.list.recipe_type"), null));

        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_manager.button.remove_recipe"), this.width - 123 - 10, this.height - 23, 120, 20, button -> ClientUtils.openScreen(new RemoveRecipeManagerScreen(this))));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);

        matrixStack.push();
        float scale = 3F;
        matrixStack.scale(scale, scale, scale);
        Screen.drawCenteredString(matrixStack, minecraft.fontRenderer, References.getTranslate("screen.mod_selection.infos.modId"), Math.floorDiv((this.width / 4 + 10) + (width - (this.width / 4 + 10)) / 2, (int) scale), (int) (10 / scale), 0xFFFFFF);
        matrixStack.pop();
        if(this.getList(0).getSelected() != null)
        {
            matrixStack.push();
            scale = scale / 2F;
            matrixStack.scale(scale, scale, scale);
            Screen.drawCenteredString(matrixStack, minecraft.fontRenderer, References.getTranslate("screen.mod_selection.infos.recipeType"), (int) (((this.width / 4 + 10) + (width - (this.width / 4 + 10)) / 2) / scale), (int) ((this.height / 2D - font.FONT_HEIGHT * scale - 3) / scale), 0xFFFFFF);
            matrixStack.pop();
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        boolean bool = super.mouseClicked(mouseX, mouseY, button);

        if(this.getList(0).getSelected() != null)
            this.getList(1).setEntries(ListEntriesHelper.getRecipeTypes(this.getModId()));

        if(this.doubleClickCounter > 0 && this.getList(1).getSelected() != null)
            ClientUtils.openScreen(new RecipeManagerScreen(this.getModId(), this.getRecipeType()));

        return bool;
    }

    private String getModId()
    {
        return ((SimpleListWidget.StringEntry) this.getList(0).getSelected()).getResource();
    }

    private ResourceLocation getRecipeType()
    {
        return ResourceLocation.tryCreate(((SimpleListWidget.StringEntry) this.getList(1).getSelected()).getResource());
    }
}
