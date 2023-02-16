package fr.eno.craftcreator.screen.widgets.buttons;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IconButton extends Button
{
    private ItemStack icon;

    public IconButton(int x, int y, ItemStack icon)
    {
        this(x, y, icon, b -> {});
        this.icon = icon;
    }

    public IconButton(int x, int y, ItemStack icon, OnPress onPress)
    {
        super(x, y, 20, 20, new TextComponent(""), onPress);
        this.icon = icon;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.enableBlend();
        ScreenUtils.renderSizedButton(matrixStack, x, y, width, height, active, ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height));
        matrixStack.pushPose();
        float scale = 1F;
        matrixStack.scale(scale, scale, scale);
        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(this.icon, (int) ((x + width / 2 - 8) /scale), (int) ((y + height / 2 - 8)/scale));
        matrixStack.popPose();
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY)
    {
        if(isMouseOver(mouseX, mouseY) && visible)
            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, Collections.singletonList(icon.getHoverName()), mouseX, mouseY);
    }

    public static List<IconButton> getButtons(List<Item> items)
    {
        List<IconButton> buttons = new ArrayList<>();
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
