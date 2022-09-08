package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DropDownListWidget extends ExtendedList<DropDownListWidget.Entry>
{
    private final int titleBoxHeight;
    private final int scrollBarWidth;
    private boolean canHaveSelected;
    private boolean isWidgetSelected;
    private boolean isActive;

    public DropDownListWidget(Minecraft mcIn, int leftIn, int topIn, int widthIn, int heightIn, int slotHeightIn, int titleBoxHeight, int scrollBarWidth)
    {
        super(mcIn, widthIn - scrollBarWidth, heightIn - titleBoxHeight, 0, 0, slotHeightIn);
        this.x0 = leftIn;
        this.x1 = leftIn + widthIn - scrollBarWidth;
        this.y0 = topIn + titleBoxHeight;
        this.y1 = topIn + heightIn - titleBoxHeight;
        this.width = widthIn - scrollBarWidth;
        this.height = heightIn - titleBoxHeight;
        this.titleBoxHeight = titleBoxHeight;
        this.scrollBarWidth = scrollBarWidth;
        this.canHaveSelected = true;
        this.isActive = true;
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
        if(isActive())
        {
            if(this.getSelected() == null) this.setSelected(this.getEntry(0));
            Screen.fill(matrixStack, this.x0, this.y0 - titleBoxHeight, this.x0 + this.width, this.y0, new Color(108, 22, 255, 150).getRGB());
            Screen.drawString(matrixStack, minecraft.fontRenderer, ((StringEntry) this.getSelected()).getResource(), this.x0 + 2, this.y0 - this.titleBoxHeight / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2, Color.WHITE.getRGB());
            //this.renderBackground(matrixStack);

            if(this.isWidgetSelected)
            {
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
        }
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
        if(isActive())
        {
            if(this.isWidgetSelected)
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
            }

            if(!isWidgetSelected)
                this.isWidgetSelected = GuiUtils.isMouseHover(this.x0, this.y0 - titleBoxHeight, (int) mouseX, (int) mouseY, this.width, this.titleBoxHeight);
            else
                this.isWidgetSelected = GuiUtils.isMouseHover(this.x0, this.y0 + titleBoxHeight, (int) mouseX, (int) mouseY, this.width + scrollBarWidth, this.height);
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

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    public static abstract class Entry extends ExtendedList.AbstractListEntry<DropDownListWidget.Entry>
    {
        protected Minecraft minecraft = Minecraft.getInstance();

        public abstract void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY);
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
            if(minecraft.fontRenderer.getStringWidth(displayStr) > width - (16 + 5))
            {
                int letters = displayStr.toCharArray().length;
                int string_width = minecraft.fontRenderer.getStringWidth(displayStr);
                int letter_width = string_width / letters;
                int def_width = width - (16 + 5);
                int width_much = string_width - def_width;
                int letters_to_remove = width_much / letter_width;
                displayStr = displayStr.substring(0, displayStr.length() - letters_to_remove - 3) + "...";
            }

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
