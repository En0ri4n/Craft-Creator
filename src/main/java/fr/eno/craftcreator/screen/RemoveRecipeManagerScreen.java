package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class RemoveRecipeManagerScreen extends ChildrenScreen
{
    private static final ResourceLocation GUI_TEXTURE = References.getLoc("textures/gui/container/gui_background.png");
    private SimpleCheckBox hasInputItemBox;
    private SimpleCheckBox hasOutputItemBox;
    private SimpleCheckBox hasModBox;
    private SimpleCheckBox hasTypeBox;
    private SimpleCheckBox hasIdBox;

    public RemoveRecipeManagerScreen(Screen parentIn)
    {
        super(References.getTranslate("screen.remove_manager.title"), parentIn);
    }

    @Override
    protected void init()
    {
        int i = 0;
        this.addButton(hasInputItemBox = new SimpleCheckBox(10, i++ * 20, 10, 10, new StringTextComponent("Number : " + i), false));
        this.addButton(hasOutputItemBox = new SimpleCheckBox(10, i++ * 20, 15, 15, new StringTextComponent("Number : " + i), false));
        this.addButton(hasModBox = new SimpleCheckBox(10, i++ * 20, 15, 15, new StringTextComponent("Number : " + i), false));
        this.addButton(hasTypeBox = new SimpleCheckBox(10, i++ * 20, 15, 15, new StringTextComponent("Number : " + i), false));
        this.addButton(hasIdBox = new SimpleCheckBox(10, i++ * 20, 15, 15, new StringTextComponent("Number : " + i), false));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(matrixStack);
        minecraft.getTextureManager().bindTexture(GUI_TEXTURE);

        Screen.blit(matrixStack, 10, 10, this.width - 20, this.height - 20, 0, 0, 256, 256, 256, 256);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
