package fr.eno.craftcreator.screen.widgets.buttons;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SimpleCheckBox extends Checkbox
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");
    @Nullable
    private Consumer<SimpleCheckBox> onPress;
    private Component tooltip;
    private boolean selected;

    public SimpleCheckBox(int x, int y, int width, int height, Component title, boolean checked)
    {
        this(x, y, width, height, title, checked, true);
    }

    public SimpleCheckBox(int x, int y, int width, int height, Component title, boolean checked, boolean drawTitle)
    {
        this(x, y, width, height, title, new TextComponent(""), checked, drawTitle, null);
    }

    public SimpleCheckBox(int x, int y, int width, int height, Component title, Component tooltip, boolean checked, boolean drawTitle, @Nullable Consumer<SimpleCheckBox> onPress)
    {
        super(x, y, width, height, title, checked, drawTitle);
        this.selected = checked;
    }

    public SimpleCheckBox(int x, int y, int width, int height, Component tooltip, boolean checked, Consumer<SimpleCheckBox> onPress)
    {
        this(x, y, width, height, new TextComponent(""), checked, false);
        this.tooltip = tooltip;
        this.onPress = onPress;
    }

    @Override
    public void renderButton(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
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

            drawString(matrixStack, ClientUtils.getFontRenderer(), this.getMessage(), this.x + this.width + 4, this.y + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }

    @Override
    public void onPress()
    {
        this.setSelected(!this.selected);
    }

    @Override
    public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY)
    {
        if(this.isHovered && this.active && this.tooltip != null)
        {
            ClientUtils.getCurrentScreen().renderTooltip(poseStack, this.tooltip, mouseX, mouseY);
        }
    }

    public void setTooltip(Component tooltip)
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
        if(onPress != null)
        {
            onPress.accept(this);
            ClientUtils.playSound(SoundEvents.UI_BUTTON_CLICK, 1F, 0.25F, SoundSource.MASTER, false);
        }
    }
}
