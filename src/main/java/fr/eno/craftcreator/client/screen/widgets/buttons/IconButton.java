package fr.eno.craftcreator.client.screen.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class IconButton extends Button
{
    private static final ResourceLocation BUTTON = References.getLoc("textures/gui/buttons/gui_button.png");

    private final ResourceLocation icon;
    private final int widthCut;
    private final int heightCut;
    private final int textureWidth;
    private final int textureHeight;

    public IconButton(int x, int y, ResourceLocation icon, int widthCut, int heightCut, int textureWidth, int textureHeight, IPressable onPress)
    {
        super(x, y, 20, 20, new StringTextComponent(""), onPress);
        this.icon = icon;
        this.widthCut = widthCut;
        this.heightCut = heightCut;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.enableBlend();
        ClientUtils.bindTexture(BUTTON);
        ScreenUtils.renderSizedTexture(matrixStack, 3, x, y, width, height, 0, !active ? 0 : !isMouseOver(mouseX, mouseY) ? 20 : 40, 20, 60, 20);
        ClientUtils.bindTexture(icon);
        int offset = 2;

        float color = 0.5F;
        if(isMouseOver(mouseX, mouseY))
            color = 1F;
        ClientUtils.color4f(color, color, color, 1.0F);
        Screen.blit(matrixStack, x + offset, y + offset, width - offset * 2, height - offset * 2, 0, !active ? 0 : !isMouseOver(mouseX, mouseY) ? 16 : 32, widthCut, heightCut, textureWidth, textureHeight);
        ClientUtils.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
