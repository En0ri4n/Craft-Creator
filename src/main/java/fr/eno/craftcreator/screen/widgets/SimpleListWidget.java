package fr.eno.craftcreator.screen.widgets;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.jsserializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.utils.Callable;
import fr.eno.craftcreator.utils.GuiUtils;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"unchecked", "deprecation"})
public class SimpleListWidget extends ExtendedList<SimpleListWidget.Entry>
{
    private final ITextComponent title;
    private final int titleBoxHeight;
    private final int scrollBarWidth;
    private boolean canHaveSelected;
    private final boolean hasTitleBox;
    private Callable<Entry> onSelected;
    private boolean isVisible;
    private Callable<Entry> onDelete;

    public SimpleListWidget(Minecraft mcIn, int leftIn, int topIn, int widthIn, int heightIn, int slotHeightIn, int titleBoxHeight, int scrollBarWidth, ITextComponent titleIn, @Nullable Callable<Entry> onDelete)
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
        this.hasTitleBox = true;
        this.isVisible = true;
        this.onDelete = onDelete;
    }

    public SimpleListWidget(Minecraft mcIn, int x, int y, int widthIn, int heightIn, int slotHeightIn, int scrollBarWidth, @Nullable Callable<Entry> onDelete)
    {
        super(mcIn, widthIn - scrollBarWidth, heightIn, 0, 0, slotHeightIn);
        this.x0 = x;
        this.x1 = x + widthIn - scrollBarWidth;
        this.y0 = y;
        this.y1 = y + heightIn;
        this.title = new StringTextComponent("");
        this.titleBoxHeight = 0;
        this.scrollBarWidth = scrollBarWidth;
        this.canHaveSelected = true;
        this.hasTitleBox = false;
        this.isVisible = true;
        this.onDelete = onDelete;
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
        return this.x1;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(!this.isVisible) return;

        if(hasTitleBox)
        {
            Screen.fill(matrixStack, this.x0, this.y0 - titleBoxHeight, this.x0 + this.width, this.y0, new Color(108, 22, 255, 150).getRGB());
            Screen.drawCenteredString(matrixStack, minecraft.fontRenderer, this.title, this.x0 + this.width / 2, this.y0 - this.titleBoxHeight / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2, Color.WHITE.getRGB());
            //this.renderBackground(matrixStack);
        }

        int scrollBarPosX = this.getScrollbarPosition();
        int scrollBarPosXWidth = scrollBarPosX + this.scrollBarWidth;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        // Background
        this.minecraft.getTextureManager().bindTexture(AbstractGui.BACKGROUND_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.5F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(this.x0, this.y1, 0.0D).tex((float) this.x0 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 100).endVertex();
        bufferbuilder.pos(this.x1, this.y1, 0.0D).tex((float) this.x1 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 100).endVertex();
        bufferbuilder.pos(this.x1, this.y0, 0.0D).tex((float) this.x1 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 100).endVertex();
        bufferbuilder.pos(this.x0, this.y0, 0.0D).tex((float) this.x0 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 100).endVertex();
        tessellator.draw();

        int j1 = this.getRowLeft();
        int k = this.y0 + 4 - (int) this.getScrollAmount();

        this.renderList(matrixStack, j1, k, mouseX, mouseY, partialTicks);

        // Scrollbar
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

    @SuppressWarnings("deprecation")
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
                e.render(matrixStack, index, rowTop, j2, rowWidth, j1, mouseX, mouseY, GuiUtils.isMouseHover(x0, y0, mouseX, mouseY, width, height) && Objects.equals(this.getEntryAtPosition(mouseX, mouseY), e), partialTicks);
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if(keyCode == GLFW.GLFW_KEY_DELETE)
        {
            if(this.getSelected() != null && this.onDelete != null)
            {
                this.onDelete.run(this.getSelected());
                this.setSelected(null);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(!this.isVisible) return false;

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

    @Override
    public void setSelected(@Nullable Entry entry)
    {
        if(this.onSelected != null)
        {
            this.onSelected.run(entry);
            this.onSelected = null;
        }
        super.setSelected(entry);
    }

    public void setOnSelectedChange(Callable<Entry> onSelected)
    {
        this.onSelected = onSelected;
    }

    public void setCoordinates(int x, int y)
    {
        this.x0 = x;
        this.y0 = y;
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.x1 = this.x0 + width - scrollBarWidth;
        this.height = height;
        this.y1 = this.y0 + height;
    }

    public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        Entry entry = getEntryAtPosition(mouseX, mouseY);

        if(entry != null)
        {
            entry.renderTooltip(matrixStack, mouseX, mouseY);
        }
    }

    public void setOnDelete(Callable<Entry> onDelete)
    {
        this.onDelete = onDelete;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
    }

    public static abstract class Entry extends ExtendedList.AbstractListEntry<SimpleListWidget.Entry>
    {
        protected Minecraft minecraft = Minecraft.getInstance();

        public abstract String toString();

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

            if(isMouseOver) color = Color.yellow;

            Screen.drawString(matrixStack, minecraft.fontRenderer, displayStr, left + 16 + 5, (top + height / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2), color.getRGB());

            ItemStack item = RecipeFileUtils.getOneOutput(recipe);

            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(item, left + yPos, top + yPos);
        }

        @Override
        public String toString()
        {
            return null;
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

    public static class ModifiedRecipeEntry extends Entry
    {
        private final ModifiedRecipe recipe;

        public ModifiedRecipeEntry(ModifiedRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void render(@Nonnull MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            String name = recipe.getRecipeMap().values().stream().findFirst().get();
            String displayStr = name.substring(name.indexOf(':') + 1);
            if(displayStr.contains("/")) displayStr = displayStr.substring(displayStr.indexOf('/') + 1);

            Color color = Color.WHITE;
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

            if(isMouseOver) color = Color.yellow;

            Screen.drawString(matrixStack, minecraft.fontRenderer, displayStr, left + 16 + 5, (top + height / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2), color.getRGB());

            Item item = Items.BARRIER;

            if(recipe.getOutputItem() != null)
            {
                try
                {
                    item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(recipe.getOutputItem()));
                }
                catch(Exception ignored)
                {
                }
            }
            else if(recipe.getInputItem() != null)
            {
                try
                {
                    item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(recipe.getInputItem()));
                } catch(Exception ignored) {}
            }

            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(new ItemStack(item), left + yPos, top + yPos);
        }

        @Override
        public String toString()
        {
            return null;
        }

        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            List<ITextComponent> tooltips = new ArrayList<>();
            Map<ModRecipesJSSerializer.RecipeDescriptors, String> recipeDescriptors = recipe.getRecipeMap();

            tooltips.add(new StringTextComponent(recipeDescriptors.values().stream().findFirst().get()).mergeStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE));
            tooltips.add(new StringTextComponent(""));
            recipeDescriptors.forEach((tag, value) -> tooltips.add(new StringTextComponent(tag.toString()).mergeStyle(TextFormatting.DARK_PURPLE).appendSibling(new StringTextComponent(" : ")).appendSibling(new StringTextComponent(value).mergeStyle(TextFormatting.DARK_AQUA))));

            net.minecraftforge.fml.client.gui.GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight(), -1, minecraft.fontRenderer);
        }

        public ModifiedRecipe getRecipe()
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
            double scale = 1D;
            RenderSystem.scaled(scale, scale, scale);
            Screen.drawCenteredString(matrixStack, minecraft.fontRenderer, displayStr, (int) ((left + width / 2) / scale), (int) ((top + height / 2 - ((double) minecraft.fontRenderer.FONT_HEIGHT * scale) / 2) / scale) + 2, strColor.getRGB());
            RenderSystem.popMatrix();
        }

        @Override
        public String toString()
        {
            return this.resource;
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

    public static class ResourceLocationEntry extends Entry
    {
        private List<ITextComponent> tooltips;
        private final ResourceLocation resourceLocation;

        public ResourceLocationEntry(ResourceLocation resourceLocation)
        {
            this.resourceLocation = resourceLocation;
            this.tooltips = new ArrayList<>();
        }

        public ResourceLocation getResourceLocation()
        {
            return resourceLocation;
        }

        @Override
        public void render(@Nonnull MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            //Screen.fill(matrixStack, left, top, left + width, top + height, 0xFFFFFF);

            String displayStr = resourceLocation.toString();
            //int k = 16 - 32;
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

            Item item = ForgeRegistries.ITEMS.getValue(getResourceLocation());

            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(new ItemStack(item == null ? Items.BARRIER : item), left + yPos, top + yPos);
        }

        @Override
        public String toString()
        {
            return this.resourceLocation.toString();
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            if(!tooltips.isEmpty())
                net.minecraftforge.fml.client.gui.GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight(), -1, minecraft.fontRenderer);
        }
    }
}
