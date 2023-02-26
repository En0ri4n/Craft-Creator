package fr.eno.craftcreator.screen.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SimpleCheckBox extends CheckboxButton
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");
    @Nullable
    private Consumer<SimpleCheckBox> onPress;
    private ITextComponent tooltip;
    private boolean selected;

    public SimpleCheckBox(int x, int y, int width, int height, ITextComponent title, boolean checked)
    {
        this(x, y, width, height, title, checked, true);
    }

    public SimpleCheckBox(int x, int y, int width, int height, ITextComponent title, boolean checked, boolean drawTitle)
    {
        this(x, y, width, height, title, new StringTextComponent(""), checked, drawTitle, null);
    }

    public SimpleCheckBox(int x, int y, int width, int height, ITextComponent tooltip, boolean checked, Consumer<SimpleCheckBox> onPress)
    {
        this(x, y, width, height, new StringTextComponent(""), tooltip, checked, false, onPress);
    }

    public SimpleCheckBox(int x, int y, int width, int height, ITextComponent title, ITextComponent tooltip, boolean checked, boolean drawTitle, @Nullable Consumer<SimpleCheckBox> onPress)
    {
        super(x, y, width, height, title, checked, drawTitle);
        this.selected = checked;
        this.tooltip = tooltip;
        this.onPress = onPress;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(visible)
        {
            this.isHovered = ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height);
            ClientUtils.bindTexture(TEXTURE);
            RenderSystem.enableDepthTest();
            ClientUtils.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Screen.blit(matrixStack, this.x, this.y, this.width, this.height, !isFocused() ? 0 : 20, !selected() ? 0 : 20, 20, 20, 64, 64);
            this.renderBg(matrixStack, ClientUtils.getMinecraft(), mouseX, mouseY);

            drawString(matrixStack, ClientUtils.getFontRenderer(), this.getMessage(), this.x + this.width + 4, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
        }
    }

    @Override
    public void onPress()
    {
        if(visible && active)
            this.setSelected(!this.selected);

        if(onPress != null)
        {
            onPress.accept(this);
            ClientUtils.playSound(SoundEvents.UI_BUTTON_CLICK, 1F, 0.25F, SoundCategory.MASTER, false);
        }
    }

    @Override
    public void renderToolTip(MatrixStack poseStack, int mouseX, int mouseY)
    {
        if(this.isHovered && this.active && this.tooltip != null)
        {
            ClientUtils.getCurrentScreen().renderTooltip(poseStack, this.tooltip, mouseX, mouseY);
        }
    }

    public void setTooltip(ITextComponent tooltip)
    {
        this.tooltip = tooltip;
    }

    @Override
    public boolean selected()
    {
        return this.selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
