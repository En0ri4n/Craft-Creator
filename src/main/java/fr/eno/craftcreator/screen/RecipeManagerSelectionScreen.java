package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public class RecipeManagerSelectionScreen extends Screen
{
    public RecipeManagerSelectionScreen(Component titleIn)
    {
        super(titleIn);
    }

    @Override
    protected void init()
    {
        super.init();

        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.recipe_manager_selection.add_manager"), 10, 10, 100, 30, button -> minecraft.setScreen(new ModSelectionScreen())));
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
