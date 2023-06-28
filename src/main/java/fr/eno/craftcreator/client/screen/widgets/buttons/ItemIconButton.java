package fr.eno.craftcreator.client.screen.widgets.buttons;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;

public class ItemIconButton extends Button
{
    private ItemStack icon;

    private IFormattableTextComponent tooltip;

    public ItemIconButton(int x, int y, ItemStack icon)
    {
        this(x, y, icon, b -> {});
        this.icon = icon;
    }

    public ItemIconButton(int x, int y, ItemStack icon, IPressable onPress)
    {
        super(x, y, 20, 20, new StringTextComponent(""), onPress);
        this.tooltip = icon.getHoverName().copy();
        this.icon = icon;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.enableBlend();
        ScreenUtils.renderSizedButton(matrixStack, x, y, width, height, active, ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height));
        RenderSystem.pushMatrix();
        float scale = 0.85F;
        RenderSystem.scalef(scale, scale, scale);
        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(this.icon, (int) ((x + width / 2 - 6) / scale), (int) ((y + height / 2 - 6)/scale));
        RenderSystem.popMatrix();
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        if(isMouseOver(mouseX, mouseY) && visible)
        {
            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, Collections.singletonList(tooltip), mouseX, mouseY);
        }
    }

    public void setTooltip(ResourceLocation recipeTypeLocation)
    {
        this.tooltip = this.icon.getHoverName().copy()
            .append(new StringTextComponent(" (" + recipeTypeLocation.getPath() + ")")
                    .withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
    }

    public static List<ItemIconButton> getButtons(List<RecipeCreator> recipeCreators)
    {
        List<ItemIconButton> buttons = Lists.newArrayList();
        for(RecipeCreator recipeCreator : recipeCreators)
        {
            ItemIconButton button = new ItemIconButton(0, 0, new ItemStack(recipeCreator.getRecipeIcon()));
            button.setTooltip(recipeCreator.getRecipeTypeLocation());
            buttons.add(button);
        }

        return buttons;
    }

    public Item getItem()
    {
        return this.icon.getItem();
    }

    public void setItem(Item icon)
    {
        this.icon = icon.getDefaultInstance();
    }
}
