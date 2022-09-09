package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class RecipeManagerSelectionScreen extends Screen
{
    public RecipeManagerSelectionScreen(ITextComponent titleIn)
    {
        super(titleIn);
    }

    @Override
    protected void init()
    {
        super.init();

        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_manager_selection.add_manager"), 10, 10, 100, 30, button -> minecraft.displayGuiScreen(new ModSelectionScreen())));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
