package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.utils.Callable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DropdownListWidget<T extends DropdownListWidget.Entry<?>> extends Widget
{
    private T selected;
    private List<T> entries;

    private final int x0;
    private final int y0;
    private final int x1;
    private final int y1;

    private final int itemHeight;
    private final int maxItems;

    private int scrollAmount;

    private boolean hovered;
    private boolean scrolling;
    private final Callable<T> onSelected;

    public DropdownListWidget(int x, int y, int width, int height, int itemHeight, List<T> entries, Callable<T> onSelected)
    {
        super(x, y, width, height, new StringTextComponent(""));
        this.itemHeight = itemHeight;
        this.maxItems = 10;
        this.entries = entries;
        this.selected = entries.get(0);

        this.x0 = x;
        this.y0 = y;
        this.x1 = x + width;
        this.y1 = y + height + (Math.min(entries.size(), maxItems)) * itemHeight;
        this.onSelected = onSelected;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.hovered = ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height);

        ClientUtils.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        Screen.fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xf2c3a942);
        Screen.fill(matrixStack, this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, Color.DARK_GRAY.getRGB());
        int color = 0xFFFFFFFF;
        drawCenteredString(matrixStack, ClientUtils.getFontRenderer(), this.getMessage().copy().append(" ▼"), this.x + this.width / 2, this.y + height / 2 - ClientUtils.getFontRenderer().lineHeight / 2, color | MathHelper.ceil(this.alpha * 255.0F) << 24);

        if(this.isFocused())
        {
            Screen.fill(matrixStack, x0, y0 + height, x1, y1, 0x88C0C0C0);
            if(getMaxScrollAmount() > 0)
            {
                int scrollBarX0 = x + width - 4;
                int scrollBarX1 = x + width;

                RenderSystem.disableTexture();
                int y0 = this.y0 + height;
                int currentHeight = (int) ((float) ((this.y1 - y0) * (this.y1 - y0)) / (float) this.getMaxPosition());
                currentHeight = MathHelper.clamp(currentHeight, 32, this.y1 - y0 - 8);
                int currentY = this.scrollAmount * (this.y1 - y0 - currentHeight) / getMaxScrollAmount() + y0;
                if(currentY < y0)
                {
                    currentY = y0;
                }

                Screen.fill(matrixStack, scrollBarX0, y0, scrollBarX1, this.y1, 0xFF101010);
                Screen.fill(matrixStack, scrollBarX0, currentY, scrollBarX1, currentY + currentHeight, 0xFFA0A0A0);
                Screen.fill(matrixStack, scrollBarX0, currentY, scrollBarX1 - 1, currentY + currentHeight - 1, 0xFF303030);
            }

            for(int i = 0; i < entries.size(); ++i)
            {
                int rowTop = getRowTop(i);
                int rowBottom = this.getRowBottom(i);
                if(rowTop >= this.y0 + height && rowBottom <= this.y1)
                {
                    T entry = this.entries.get(i);
                    int rowWidth = this.getRowWidth();
                    int rowLeft = this.x0;
                    entry.render(matrixStack, i, rowLeft, rowTop, rowWidth, itemHeight, mouseX, mouseY, ScreenUtils.isMouseHover(rowLeft, rowTop, mouseX, mouseY, rowWidth, itemHeight));
                }
            }
        }
    }

    protected int getRowWidth()
    {
        return getScrollbarPosition();
    }

    protected int getRowTop(int index)
    {
        return this.y0 - this.getScrollAmount() + index * this.itemHeight + this.height;
    }

    private int getRowBottom(int index)
    {
        return this.getRowTop(index) + this.itemHeight;
    }

    private int getMaxScrollAmount()
    {
        return Math.max(0, this.getMaxPosition() - (maxItems * itemHeight - 4));
    }

    private int getScrollAmount()
    {
        return this.scrollAmount;
    }

    protected int getScrollbarPosition()
    {
        return getMaxScrollAmount() > 0 ? width - 4 : width;
    }

    protected int getMaxPosition()
    {
        return this.entries.size() * this.itemHeight + height + itemHeight * 2;
    }

    @Nonnull
    @Override
    public ITextComponent getMessage()
    {
        return this.getSelectedEntry().getDisplayName();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.updateScrollingState(mouseX, mouseY, button);

        if(hovered)
        {
            this.setFocused(true);
            return true;
        }

        if(isFocused())
        {
            if(!ScreenUtils.isMouseHover(x0, y0 + height, (int) mouseX, (int) mouseY, x1 - x0, y1 - y0 - height))
            {
                this.setFocused(false);
                return false;
            }

            T entry = this.getEntryAtPosition(mouseX, mouseY);

            if(entry != null)
            {
                this.setSelected(entry);
                this.setFocused(false);
                return false;
            }
        }

        return false;
    }

    public List<T> getEntries()
    {
        return entries;
    }

    public void setEntries(List<T> entries)
    {
        this.entries = entries;
        this.scrollAmount = 0;
        this.setSelected(entries.get(0));
    }

    public void setSelected(T entry)
    {
        this.selected = entry;
        if(this.onSelected != null) this.onSelected.run(entry);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double p_231045_6_, double p_231045_8_)
    {
        if(super.mouseDragged(mouseX, mouseY, button, p_231045_6_, p_231045_8_))
        {
            return true;
        }
        else if(button == 0 && this.scrolling)
        {
            if(mouseY < (double) this.y0)
            {
                this.setScrollAmount(0.0D);
            }
            else if(mouseY > (double) this.y1)
            {
                this.setScrollAmount(this.getMaxScrollAmount());
            }
            else
            {
                double d0 = Math.max(1, this.getMaxScrollAmount());
                int i = this.y1 - this.y0;
                int j = MathHelper.clamp((int) ((float) (i * i) / (float) this.getMaxPosition()), 32, i - 8);
                double d1 = Math.max(1.0D, d0 / (double) (i - j));
                this.setScrollAmount(this.getScrollAmount() + p_231045_8_ * d1);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void updateScrollingState(double mouseX, double mouseY, int button)
    {
        this.scrolling = button == 0 && mouseX >= (double) this.getScrollbarPosition() && mouseX < (double) (this.getScrollbarPosition() + 6);
    }

    @Override
    public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double scroll)
    {
        this.setScrollAmount(this.scrollAmount - scroll * (double) this.itemHeight / 2.0D);
        return super.mouseScrolled(p_231043_1_, p_231043_3_, scroll);
    }

    private void setScrollAmount(double scrollAmount)
    {
        this.scrollAmount = (int) Math.max(0, Math.min(scrollAmount, this.getMaxScrollAmount()));
    }

    private T getEntryAtPosition(double mouseX, double mouseY)
    {
        for(int i = 0; i < entries.size(); ++i)
        {
            int rowTop = getRowTop(i);
            int rowBottom = this.getRowBottom(i);
            if(rowTop >= this.y0 + height && rowBottom <= this.y1)
            {
                T entry = this.entries.get(i);
                int rowWidth = this.getRowWidth();

                if(ScreenUtils.isMouseHover(this.x0, rowTop, (int) mouseX, (int) mouseY, rowWidth, itemHeight)) return entry;
            }
        }
        return null;
    }

    public T getSelectedEntry()
    {
        return this.selected;
    }

    public static class Entries
    {
        public static List<StringEntry> getRecipeTypes(String modid)
        {
            SupportedMods mod = SupportedMods.getMod(modid);
            return mod.getSupportedRecipeTypes().stream().map(recipeTypeLocation -> new StringEntry(recipeTypeLocation.toString(), new StringTextComponent(recipeTypeLocation.toString()))).collect(Collectors.toList());//Registry.RECIPE_TYPE.stream().filter(type -> CommonUtils.getRecipeTypeName(type).getNamespace().equals(modid)).map(recipeType -> new DropdownListWidget.StringEntry(CommonUtils.getRecipeTypeName(recipeType).toString(), new StringTextComponent(CommonUtils.getRecipeTypeName(recipeType).toString()))).collect(Collectors.toList());
        }

        public static List<StringEntry> getModIds()
        {
            return SupportedMods.getSupportedLoadedMods().stream().map(mod -> new StringEntry(mod.getModId(), new StringTextComponent(mod.getModId()))).collect(Collectors.toList());
        }
    }

    public static abstract class Entry<T>
    {
        private Entry() {}

        public abstract T getValue();

        public abstract IFormattableTextComponent getDisplayName();

        public abstract void render(MatrixStack matrixStack, int index, int x, int y, int width, int itemHeight, int mouseX, int mouseY, boolean hovered);
    }

    public static class StringEntry extends Entry<String>
    {
        private final String value;
        private final IFormattableTextComponent displayName;

        public StringEntry(String value, IFormattableTextComponent displayName)
        {
            this.value = value;
            this.displayName = displayName;
        }

        @Override
        public void render(MatrixStack matrixStack, int index, int x, int y, int width, int itemHeight, int mouseX, int mouseY, boolean hovered)
        {
            int color = 0xFFFFFF;
            if(hovered)
            {
                color = 0xf2c3a942;
            }

            Screen.fill(matrixStack, x, y, x + width, y + itemHeight, 0xFF000000);
            Screen.fill(matrixStack, x + 1, y + 1, x + width - 1, y + itemHeight - 1, 0xFF585858);

            matrixStack.translate(0, 0, 100);
            ClientUtils.getFontRenderer().draw(matrixStack, this.displayName.getString(), x + 2, y + Math.floorDiv(itemHeight - 8, 2), color);
        }

        @Override
        public String getValue()
        {
            return this.value;
        }

        @Override
        public IFormattableTextComponent getDisplayName()
        {
            return this.displayName;
        }
    }
}
