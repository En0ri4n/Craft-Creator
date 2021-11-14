package fr.eno.craftcreator.screen.widgets;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimpleListWidget extends ExtendedList<SimpleListWidget.Entry>
{
    private final ITextComponent title;
    private final int titleBoxHeight;
    private final int scrollBarWidth;
    private boolean canHaveSelected;

    public SimpleListWidget(Minecraft mcIn, int leftIn, int topIn, int widthIn, int heightIn, int slotHeightIn, int titleBoxHeight, int scrollBarWidth, ITextComponent titleIn)
    {
        super(mcIn, widthIn - scrollBarWidth, heightIn - titleBoxHeight, 0, 0, slotHeightIn);
        this.x0 = leftIn;
        this.x1 = leftIn + widthIn - scrollBarWidth;
        this.y0 = topIn + titleBoxHeight;
        this.y1 = topIn + heightIn - titleBoxHeight;
        this.title = titleIn;
        this.titleBoxHeight = titleBoxHeight;
        this.scrollBarWidth = scrollBarWidth;
        this.canHaveSelected = true;
    }

    public void setCanHaveSelected(boolean bool)
    {
        this.canHaveSelected = bool;
    }

    @Override
    public int getRowWidth()
    {
        return this.width - 4;
    }

    @Override
    protected int getScrollbarPosition()
    {
        return this.x0 + this.width;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        Screen.fill(matrixStack, this.x0, this.y0 - titleBoxHeight, this.x0 + this.width, this.y0, new Color(108, 22, 255, 150).getRGB());
        Screen.drawCenteredString(matrixStack, minecraft.fontRenderer, this.title, this.x0 + this.width / 2, this.y0 - this.titleBoxHeight / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2, Color.WHITE.getRGB());
        //this.renderBackground(matrixStack);
        int scrollBarPosX = this.getScrollbarPosition();
        int scrollBarPosXWidth = scrollBarPosX + this.scrollBarWidth;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if(true)
        {
            this.minecraft.getTextureManager().bindTexture(AbstractGui.BACKGROUND_LOCATION);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.5F);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(this.x0, this.y1, 0.0D).tex((float) this.x0 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 100).endVertex();
            bufferbuilder.pos(this.x1, this.y1, 0.0D).tex((float) this.x1 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 100).endVertex();
            bufferbuilder.pos(this.x1, this.y0, 0.0D).tex((float) this.x1 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 100).endVertex();
            bufferbuilder.pos(this.x0, this.y0, 0.0D).tex((float) this.x0 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 100).endVertex();
            tessellator.draw();
        }

        int j1 = this.getRowLeft();
        int k = this.y0 + 4 - (int) this.getScrollAmount();

        this.renderList(matrixStack, j1, k, mouseX, mouseY, partialTicks);

        int k1 = this.getMaxScroll();
        if(k1 > 0)
        {
            RenderSystem.disableTexture();
            int l1 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
            l1 = MathHelper.clamp(l1, 32, this.y1 - this.y0 - 8);
            int i2 = (int) this.getScrollAmount() * (this.y1 - this.y0 - l1) / k1 + this.y0;
            if(i2 < this.y0)
            {
                i2 = this.y0;
            }

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(scrollBarPosX, this.y1, 0.0D).tex(0.0F, 1.0F).color(0, 0, 0, 100).endVertex();
            bufferbuilder.pos(scrollBarPosXWidth, this.y1, 0.0D).tex(1.0F, 1.0F).color(0, 0, 0, 100).endVertex();
            bufferbuilder.pos(scrollBarPosXWidth, this.y0, 0.0D).tex(1.0F, 0.0F).color(0, 0, 0, 100).endVertex();
            bufferbuilder.pos(scrollBarPosX, this.y0, 0.0D).tex(0.0F, 0.0F).color(0, 0, 0, 100).endVertex();
            bufferbuilder.pos(scrollBarPosX, i2 + l1, 0.0D).tex(0.0F, 1.0F).color(128, 128, 128, 100).endVertex();
            bufferbuilder.pos(scrollBarPosXWidth, i2 + l1, 0.0D).tex(1.0F, 1.0F).color(128, 128, 128, 100).endVertex();
            bufferbuilder.pos(scrollBarPosXWidth, i2, 0.0D).tex(1.0F, 0.0F).color(128, 128, 128, 100).endVertex();
            bufferbuilder.pos(scrollBarPosX, i2, 0.0D).tex(0.0F, 0.0F).color(128, 128, 128, 100).endVertex();
            bufferbuilder.pos(scrollBarPosX, i2 + l1 - 1, 0.0D).tex(0.0F, 1.0F).color(192, 192, 192, 100).endVertex();
            bufferbuilder.pos(scrollBarPosXWidth - 1, i2 + l1 - 1, 0.0D).tex(1.0F, 1.0F).color(192, 192, 192, 100).endVertex();
            bufferbuilder.pos(scrollBarPosXWidth - 1, i2, 0.0D).tex(1.0F, 0.0F).color(192, 192, 192, 100).endVertex();
            bufferbuilder.pos(scrollBarPosX, i2, 0.0D).tex(0.0F, 0.0F).color(192, 192, 192, 100).endVertex();
            tessellator.draw();
        }

        this.renderDecorations(matrixStack, mouseX, mouseY);
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
    }

    protected void renderList(@Nonnull MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks)
    {
        int itemCount = this.getItemCount();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for(int index = 0; index < itemCount; index++)
        {
            int rowTop = this.getRowTop(index);
            int rowBottom = this.getRowTop(index) + this.itemHeight;
            if(rowBottom <= this.y1 && rowTop >= this.y0)
            {
                int i1 = y + index * this.itemHeight + this.headerHeight;
                int j1 = this.itemHeight - 4;
                Entry e = this.getEntry(index);
                int rowWidth = this.getRowWidth();
                boolean isSelected = this.isSelectedItem(index);
                if(isSelected && canHaveSelected)
                {
                    int l1 = this.x0 + this.width / 2 - rowWidth / 2;
                    int i2 = this.x0 + this.width / 2 + rowWidth / 2;
                    RenderSystem.disableTexture();
                    float f = this.isFocused() ? 1.0F : 0.5F;
                    RenderSystem.color4f(f, f, f, 1.0F);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                    bufferbuilder.pos(l1, i1 + j1 + 2, 0.0D).endVertex();
                    bufferbuilder.pos(i2, i1 + j1 + 2, 0.0D).endVertex();
                    bufferbuilder.pos(i2, i1 - 2, 0.0D).endVertex();
                    bufferbuilder.pos(l1, i1 - 2, 0.0D).endVertex();
                    tessellator.draw();
                    RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                    bufferbuilder.pos(l1 + 1, i1 + j1 + 1, 0.0D).endVertex();
                    bufferbuilder.pos(i2 - 1, i1 + j1 + 1, 0.0D).endVertex();
                    bufferbuilder.pos(i2 - 1, i1 - 1, 0.0D).endVertex();
                    bufferbuilder.pos(l1 + 1, i1 - 1, 0.0D).endVertex();
                    tessellator.draw();
                    RenderSystem.enableTexture();
                }

                int j2 = this.getRowLeft();
                e.render(matrixStack, index, rowTop, j2, rowWidth, j1, mouseX, mouseY, this.isMouseOver(mouseX, mouseY) && Objects.equals(this.getEntryAtPosition(mouseX, mouseY), e), partialTicks);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.updateScrollingState(mouseX, mouseY, button);

        if(canHaveSelected)
        {
            Entry entry = getEntryAtPosition(mouseX, mouseY);

            if(entry != null)
            {
                this.setSelected(entry);
            }
        }

        return true;
    }

    public void setEntries(List<Entry> entries)
    {
        this.clearEntries();
        entries.forEach(this::addEntry);
    }

    public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        Entry entry = getEntryAtPosition(mouseX, mouseY);

        if(entry != null)
        {
            entry.renderTooltip(matrixStack, mouseX, mouseY);
        }
    }

    public static abstract class Entry extends ExtendedList.AbstractListEntry<SimpleListWidget.Entry>
    {
        protected Minecraft minecraft = Minecraft.getInstance();

        public abstract void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY);
    }

    public static class RecipeEntry extends Entry
    {
        private final IRecipe<?> recipe;

        public RecipeEntry(IRecipe<?> recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void render(@Nonnull MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            String displayStr = recipe.getId().toString().substring(recipe.getId().toString().indexOf(':') + 1);
            if(displayStr.contains("/")) displayStr = displayStr.substring(displayStr.indexOf('/') + 1);

            Color color = Color.WHITE;
            if(minecraft.fontRenderer.getStringWidth(displayStr) > width - 16)
                displayStr = displayStr.substring(0, 15) + "...";

            if(isMouseOver)
                color = Color.yellow;

            Screen.drawCenteredString(matrixStack, minecraft.fontRenderer, displayStr, left + width / 2, (top + height / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2), color.getRGB());

            ItemStack item = RecipeFileUtils.getOneOutput(recipe);

            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(item, left + yPos, top + yPos);
        }

        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            List<ITextComponent> tooltips = new ArrayList<>();
            Multimap<String, ResourceLocation> input = RecipeFileUtils.getInput(recipe);
            Map<String, ResourceLocation> output = RecipeFileUtils.getOutput(recipe);

            tooltips.add(new StringTextComponent(this.recipe.getId().toString()).mergeStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE));
            tooltips.add(new StringTextComponent(""));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.input"));
            input.forEach((typeName, loc) -> tooltips.add(new StringTextComponent(typeName).mergeStyle(typeName.equalsIgnoreCase("item") ? TextFormatting.AQUA : typeName.equalsIgnoreCase("block") ? TextFormatting.LIGHT_PURPLE : TextFormatting.DARK_PURPLE).appendSibling(new StringTextComponent(" : ")).appendSibling(new StringTextComponent(loc.toString()).mergeStyle(TextFormatting.DARK_AQUA))));

            tooltips.add(new StringTextComponent(""));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.output"));
            output.forEach((typeName, loc) -> tooltips.add(new StringTextComponent(typeName).mergeStyle(typeName.equalsIgnoreCase("item") ? TextFormatting.AQUA : typeName.equalsIgnoreCase("block") ? TextFormatting.LIGHT_PURPLE : TextFormatting.DARK_PURPLE).appendSibling(new StringTextComponent(" : ")).appendSibling(new StringTextComponent(loc.toString()).mergeStyle(TextFormatting.DARK_AQUA))));

            PairValue<String, Integer> param = RecipeFileUtils.getParam(recipe);

            if(param != null)
            {
                tooltips.add(new StringTextComponent(""));
                tooltips.add(new StringTextComponent(param.getFirstValue()).mergeStyle(TextFormatting.DARK_GRAY).appendSibling(new StringTextComponent(" : ")).appendSibling(new StringTextComponent(String.valueOf(param.getSecondValue())).mergeStyle(TextFormatting.GRAY)));
            }

            net.minecraftforge.fml.client.gui.GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight(), -1, minecraft.fontRenderer);
        }

        public IRecipe<?> getRecipe()
        {
            return recipe;
        }
    }

    public static class StringEntry extends Entry
    {
        private List<ITextComponent> tooltips;
        private final String resource;

        public StringEntry(String resource)
        {
            this.resource = resource;
            this.tooltips = new ArrayList<>();
        }

        public String getResource()
        {
            return resource;
        }

        @Override
        public void render(@Nonnull MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            //Screen.fill(matrixStack, left, top, left + width, top + height, 0xFFFFFF);

            String displayStr = resource;
            if(minecraft.fontRenderer.getStringWidth(displayStr) > width)
                displayStr = displayStr.substring(0, 15) + "...";

            Color strColor = isMouseOver ? Color.YELLOW : Color.WHITE;
            RenderSystem.pushMatrix();
            double scale = 1.1D;
            RenderSystem.scaled(scale, scale, scale);
            Screen.drawCenteredString(matrixStack, minecraft.fontRenderer, displayStr, (int) ((left + width / 2) / scale), (int) ((top + height / 2 - ((double) minecraft.fontRenderer.FONT_HEIGHT * scale) / 2) / scale), strColor.getRGB());
            RenderSystem.popMatrix();
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            if(!tooltips.isEmpty())
                net.minecraftforge.fml.client.gui.GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight(), -1, minecraft.fontRenderer);
        }

        public StringEntry setTooltips(List<ITextComponent> tooltips)
        {
            this.tooltips = tooltips;
            return this;
        }
    }
}
