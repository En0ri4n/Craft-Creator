package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.screen.buttons.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.util.text.*;

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
