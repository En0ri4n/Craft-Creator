package fr.eno.craftcreator.screen.widgets;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import net.minecraft.client.gui.screen.Screen;
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
        int yTextureOffset = ExecuteButton.isMouseHover(x, y, mouseX, mouseY, width, height) ? 20 : 0;
        ClientUtils.bindTexture(References.getLoc("textures/gui/buttons/item_button.png"));
        RenderSystem.enableBlend();
        Screen.blit(matrixStack, x, y, width, height, 0, yTextureOffset, width, height, 20, 40);
        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(this.icon, x + 2, y + 2);
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
