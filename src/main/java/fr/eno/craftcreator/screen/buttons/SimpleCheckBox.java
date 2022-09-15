package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class SimpleCheckBox extends Checkbox
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");

    public SimpleCheckBox(int x, int y, int width, int height, Component title, boolean checked)
    {
        super(x, y, width, height, title, checked);
    }

    public SimpleCheckBox(int x, int y, int width, int height, Component title, boolean checked, boolean drawTitle)
    {
        super(x, y, width, height, title, checked, drawTitle);
    }

    @Override
    public void renderButton(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(visible)
        {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getTextureManager().bindForSetup(TEXTURE);
            RenderSystem.enableDepthTest();
            Font fontrenderer = minecraft.font;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Screen.blit(matrixStack, this.x, this.y, this.width, this.height, !isFocused() ? 0 : 20, !selected() ? 0 : 20, 20, 20, 64, 64);
            this.renderBg(matrixStack, minecraft, mouseX, mouseY);
            if(true)
            {
                drawString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width + 4, this.y + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
            }
        }
    }
}
