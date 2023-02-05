package fr.eno.craftcreator.screen.widgets;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class IconButton extends Button
{
    private ItemStack icon;

    public IconButton(int x, int y, ItemStack icon)
    {
        this(x, y, icon, b -> {});
        this.icon = icon;
    }

    public IconButton(int x, int y, ItemStack icon, IPressable onPress)
    {
        super(x, y, 20, 20, new StringTextComponent(""), onPress);
        this.icon = icon;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.enableBlend();
        Utils.renderSizedButton(matrixStack, x, y, width, height, active, Utils.isMouseHover(x, y, mouseX, mouseY, width, height));
        RenderSystem.pushMatrix();
        float scale = 0.85F;
        RenderSystem.scalef(scale, scale, scale);
        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(this.icon, (int) ((x + width / 2 - 6) /scale), (int) ((y + height / 2 - 6)/scale));
        RenderSystem.popMatrix();
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        if(isMouseOver(mouseX, mouseY) && visible)
            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, Collections.singletonList(icon.getHoverName()), mouseX, mouseY);
    }

    public static List<IconButton> getButtons(List<Item> items)
    {
        List<IconButton> buttons = Lists.newArrayList();
        for(Item item : items)
        {
            buttons.add(new IconButton(0, 0, new ItemStack(item)));
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
