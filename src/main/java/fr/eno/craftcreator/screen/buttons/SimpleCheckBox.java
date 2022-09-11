package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class SimpleCheckBox extends CheckboxButton
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");

    public SimpleCheckBox(int x, int y, int width, int height, ITextComponent title, boolean checked)
    {
        super(x, y, width, height, title, checked);
    }

    public SimpleCheckBox(int x, int y, int width, int height, ITextComponent title, boolean checked, boolean drawTitle)
    {
        super(x, y, width, height, title, checked, drawTitle);
    }

    @Override
    public void renderWidget(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(visible)
        {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getTextureManager().bindTexture(TEXTURE);
            RenderSystem.enableDepthTest();
            FontRenderer fontrenderer = minecraft.fontRenderer;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Screen.blit(matrixStack, this.x, this.y, this.width, this.height, !isFocused() ? 0 : 20, !isChecked() ? 0 : 20, 20, 20, 64, 64);
            this.renderBg(matrixStack, minecraft, mouseX, mouseY);
            if(true)
            {
                drawString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width + 4, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
            }
        }
    }
}
