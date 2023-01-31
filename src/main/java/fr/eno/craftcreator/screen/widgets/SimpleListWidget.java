package fr.eno.craftcreator.screen.widgets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.recipes.serializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.utils.Callable;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class SimpleListWidget extends AbstractList<SimpleListWidget.Entry>
{
    private static final ResourceLocation BACKGROUND_TILE = References.getLoc("textures/gui/background_tile.png");
    private final ITextComponent title;
    private final int titleBoxHeight;
    private final int scrollBarWidth;
    private boolean canHaveSelected;
    private final boolean hasTitleBox;
    private Callable<Entry> onSelected;
    private boolean isVisible;
    private Callable<Entry> onDelete;
    private Entry hoveredEntry;
    private boolean isListHovered;

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
    public void render(@Nonnull MatrixStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        if(!this.isVisible) return;

        if(hasTitleBox)
        {
            Screen.fill(pPoseStack, this.x0, this.y0 - titleBoxHeight, this.x0 + this.width, this.y0, 0xf2c3a942);
            Screen.drawCenteredString(pPoseStack, minecraft.fontRenderer, this.title, this.x0 + this.width / 2, this.y0 - this.titleBoxHeight / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
            //this.renderBackground(pPoseStack);
        }

        this.hoveredEntry = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;
        this.isListHovered = ClientUtils.isMouseHover(x0, y0, pMouseX, pMouseY, width, height - itemHeight);
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuffer();

        // Render background
        Entry hovered = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;
        ClientUtils.bindTexture(BACKGROUND_TILE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int alpha = 100;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(this.x0, this.y1, 0.0D).tex((float) this.x0 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.pos(this.x1, this.y1, 0.0D).tex((float) this.x1 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.pos(this.x1, this.y0, 0.0D).tex((float) this.x1 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.pos(this.x0, this.y0, 0.0D).tex((float) this.x0 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        tesselator.draw();

        int rowLeft = this.getRowLeft();
        int k = this.y0 + 4 - (int) this.getScrollAmount();

        this.renderList(pPoseStack, rowLeft, k, pMouseX, pMouseY, pPartialTick);

        int scrollbarPosition = this.getScrollbarPosition();
        int j = scrollbarPosition + 6;
        int maxScroll = this.getMaxScroll();
        if(maxScroll > 0)
        {
            RenderSystem.disableTexture();
            int l1 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
            l1 = MathHelper.clamp(l1, 32, this.y1 - this.y0 - 8);
            int i2 = (int) this.getScrollAmount() * (this.y1 - this.y0 - l1) / maxScroll + this.y0;
            if(i2 < this.y0)
            {
                i2 = this.y0;
            }

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(scrollbarPosition, this.y1, 0.0D).tex(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(j, this.y1, 0.0D).tex(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(j, this.y0, 0.0D).tex(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(scrollbarPosition, this.y0, 0.0D).tex(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(scrollbarPosition, (i2 + l1), 0.0D).tex(0.0F, 1.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.pos(j, (i2 + l1), 0.0D).tex(1.0F, 1.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.pos(j, i2, 0.0D).tex(1.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.pos(scrollbarPosition, i2, 0.0D).tex(0.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.pos(scrollbarPosition, (i2 + l1 - 1), 0.0D).tex(0.0F, 1.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.pos((j - 1), (i2 + l1 - 1), 0.0D).tex(1.0F, 1.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.pos((j - 1), i2, 0.0D).tex(1.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.pos(scrollbarPosition, i2, 0.0D).tex(0.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            tesselator.draw();
        }

        this.renderDecorations(pPoseStack, pMouseX, pMouseY);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    protected void renderList(@Nonnull MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks)
    {
        int itemCount = this.getItemCount();
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuffer();

        for(int itemIndex = 0; itemIndex < itemCount; ++itemIndex)
        {
            int rowTop = this.getRowTop(itemIndex);
            int rowBottom = this.getRowTop(itemIndex) + this.itemHeight;
            if(rowBottom >= this.y0 + this.itemHeight && rowTop <= this.y1 - this.itemHeight)
            {
                int i1 = y + itemIndex * this.itemHeight + this.headerHeight;
                int j1 = this.itemHeight - 4;
                Entry entry = this.getEntry(itemIndex);
                int rowWidth = this.getRowWidth();
                if(canHaveSelected && this.isSelectedItem(itemIndex))
                {
                    int l1 = this.x0 + this.width / 2 - rowWidth / 2;
                    int i2 = this.x0 + this.width / 2 + rowWidth / 2;
                    RenderSystem.disableTexture();
                    float f = this.isFocused() ? 1.0F : 0.5F;
                    RenderSystem.color4f(f, f, f, 1.0F);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                    bufferbuilder.pos(l1, (i1 + j1 + 2), 0.0D).endVertex();
                    bufferbuilder.pos(i2, (i1 + j1 + 2), 0.0D).endVertex();
                    bufferbuilder.pos(i2, (i1 - 2), 0.0D).endVertex();
                    bufferbuilder.pos(l1, (i1 - 2), 0.0D).endVertex();
                    tesselator.draw();
                    RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                    bufferbuilder.pos((l1 + 1), (i1 + j1 + 1), 0.0D).endVertex();
                    bufferbuilder.pos((i2 - 1), (i1 + j1 + 1), 0.0D).endVertex();
                    bufferbuilder.pos((i2 - 1), (i1 - 1), 0.0D).endVertex();
                    bufferbuilder.pos((l1 + 1), (i1 - 1), 0.0D).endVertex();
                    tesselator.draw();
                    RenderSystem.enableTexture();
                }

                int rowLeft = this.getRowLeft();
                entry.render(matrixStack, itemIndex, rowTop, rowLeft, rowWidth, j1, mouseX, mouseY, Objects.equals(this.hoveredEntry, entry), partialTicks);
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

        if(this.isListHovered)
        {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        return true;
    }

    @Override
    public boolean mouseScrolled(double p_93416_, double p_93417_, double p_93418_)
    {
        if(this.isListHovered)
            return super.mouseScrolled(p_93416_, p_93417_, p_93418_);

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(!this.isVisible) return false;

        this.updateScrollingState(mouseX, mouseY, button);

        if(canHaveSelected)
        {
            if(hoveredEntry != null)
            {
                this.setSelected(hoveredEntry);
            }
        }

        return true;
    }

    public void setEntries(List<Entry> entries)
    {
        this.clearEntries();
        entries.forEach(this::addEntry);
        this.setScrollAmount(0D);
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

        if(entry != null && isListHovered)
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

    public abstract static class Entry extends AbstractList.AbstractListEntry<SimpleListWidget.Entry>
    {
        protected Minecraft minecraft = Minecraft.getInstance();
        protected final List<ITextComponent> tooltips;

        protected Entry()
        {
            this.tooltips = new ArrayList<>();
        }

        public abstract String toString();

        public abstract void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY);

        protected void displayTruncatedString(MatrixStack matrixStack, String stringToDisplay, int leftPos, int topPos, int width, int height, boolean hasItemDisplay, boolean isMouseOver)
        {
            if(stringToDisplay.contains("/"))
                stringToDisplay = stringToDisplay.substring(stringToDisplay.indexOf('/') + 1);

            stringToDisplay = getString(width, stringToDisplay);

            int color = isMouseOver ? 0xF1f115 : 0xFFFFFF;

            Screen.drawString(matrixStack, minecraft.fontRenderer, stringToDisplay, hasItemDisplay ? leftPos + 16 + 5 : leftPos + width / 2 - minecraft.fontRenderer.getStringWidth(stringToDisplay) / 2, (topPos + height / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2), color);
        }

        protected String getString(int width, String displayStr)
        {
            int stringWidth = minecraft.fontRenderer.getStringWidth(displayStr);

            if(stringWidth > width - (16 + 5))
            {
                int letters = displayStr.toCharArray().length;
                int letterWidth = stringWidth / letters;
                int def_width = width - (16 + 5);
                int width_much = stringWidth - def_width;
                int lettersToRemove = width_much / letterWidth;
                displayStr = displayStr.substring(0, displayStr.length() - lettersToRemove - 3) + "...";
            }
            return displayStr;
        }
    }

    public static class RecipeEntry extends Entry
    {
        private final Gson gson = new GsonBuilder().setLenient().create();
        private final Pattern numberPattern = Pattern.compile("[0-9]+");

        private final IRecipe<?> recipe;

        public RecipeEntry(IRecipe<?> recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void render(@Nonnull MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            String displayStr = recipe.getId().toString().substring(recipe.getId().toString().indexOf(':') + 1);

            displayTruncatedString(matrixStack, displayStr, left, top, width, height, true, isMouseOver);

            ItemStack item = ModRecipeCreatorDispatcher.getOutput(recipe).getIcon();

            matrixStack.push();
            float scale = 1F;
            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(item, (int) ((left + 1) / scale), (int) ((top + yPos) / scale));
            matrixStack.pop();
        }

        @Override
        public String toString()
        {
            return recipe.getId().toString();
        }

        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            tooltips.clear();
            CraftIngredients input = ModRecipeCreatorDispatcher.getInputs(recipe);
            CraftIngredients output = ModRecipeCreatorDispatcher.getOutput(recipe);

            tooltips.add(new StringTextComponent(this.recipe.getId().toString()).mergeStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE));
            tooltips.add(new StringTextComponent(""));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.input"));
            addToTooltip(input);

            tooltips.add(new StringTextComponent("").mergeStyle(TextFormatting.DARK_AQUA));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.output"));
            addToTooltip(output);

            assert minecraft.currentScreen != null;
            minecraft.currentScreen.func_243308_b(matrixStack, tooltips, mouseX, mouseY);
        }

        private void addToTooltip(CraftIngredients input)
        {
            for(CraftIngredients.CraftIngredient craftIngredient : input.getIngredientsWithCount())
            {
                IFormattableTextComponent ingredientTooltipLine = new StringTextComponent(craftIngredient.getDescription());
                TextFormatting baseColor = TextFormatting.AQUA;
                ingredientTooltipLine.mergeStyle(baseColor);

                IFormattableTextComponent separator = new StringTextComponent(" : ");
                separator.mergeStyle(TextFormatting.WHITE);
                ingredientTooltipLine.appendSibling(separator);

                IFormattableTextComponent ingredientValue = new StringTextComponent("");

                if(craftIngredient instanceof CraftIngredients.BlockIngredient)
                {
                    CraftIngredients.BlockIngredient blockIngredient = (CraftIngredients.BlockIngredient) craftIngredient;
                    ingredientValue.appendSibling(new StringTextComponent(blockIngredient.getId().toString()));
                    ingredientValue.mergeStyle(TextFormatting.DARK_AQUA);
                }
                else if(craftIngredient instanceof CraftIngredients.ItemLuckIngredient)
                {
                    CraftIngredients.ItemLuckIngredient itemLuckIngredient = (CraftIngredients.ItemLuckIngredient) craftIngredient;

                    ingredientValue.appendSibling(new StringTextComponent(itemLuckIngredient.getId().toString())).mergeStyle(TextFormatting.DARK_AQUA);

                    IFormattableTextComponent countComponent = new StringTextComponent(" (x").appendString(String.valueOf(itemLuckIngredient.getCount())).appendString(")").mergeStyle(TextFormatting.GRAY);
                    ingredientValue.appendSibling(countComponent);

                    if(itemLuckIngredient.getLuck() != 1D && itemLuckIngredient.getLuck() > 0D)
                    {
                        IFormattableTextComponent luckComponent = new StringTextComponent(" ").appendString(String.valueOf(itemLuckIngredient.getLuck() * 100)).appendString("%").mergeStyle(TextFormatting.DARK_GRAY);
                        ingredientValue.appendSibling(luckComponent);
                    }
                }
                else if(craftIngredient instanceof CraftIngredients.MultiItemIngredient)
                {
                    CraftIngredients.MultiItemIngredient multiItemIngredient = (CraftIngredients.MultiItemIngredient) craftIngredient;

                    IFormattableTextComponent countComponent = new StringTextComponent(" (x").appendString(String.valueOf(multiItemIngredient.getCount())).appendString(")").mergeStyle(TextFormatting.GRAY);
                    ingredientTooltipLine.appendSibling(countComponent);
                    tooltips.add(ingredientTooltipLine);

                    multiItemIngredient.getIds().forEach((resourceLocation, isTag) ->
                    {
                        IFormattableTextComponent itemEntryComponent = new StringTextComponent("    ").appendSibling(new StringTextComponent("- ").mergeStyle(TextFormatting.WHITE));
                        itemEntryComponent.appendSibling(new StringTextComponent(isTag ? "Tag" : "Item").mergeStyle(TextFormatting.YELLOW));
                        itemEntryComponent.appendSibling(new StringTextComponent(" : ").mergeStyle(TextFormatting.WHITE));
                        itemEntryComponent.appendSibling(new StringTextComponent(resourceLocation.toString()).mergeStyle(TextFormatting.RED));
                        tooltips.add(itemEntryComponent);
                    });
                    continue;
                }
                else if(craftIngredient instanceof CraftIngredients.TagIngredient)
                {
                    CraftIngredients.TagIngredient tagIngredient = (CraftIngredients.TagIngredient) craftIngredient;
                    ingredientValue.appendSibling(new StringTextComponent(tagIngredient.getId().toString())).mergeStyle(TextFormatting.DARK_AQUA);

                    IFormattableTextComponent countComponent = new StringTextComponent(" (x").appendString(String.valueOf(tagIngredient.getCount())).appendString(")").mergeStyle(TextFormatting.GRAY);
                    ingredientValue.appendSibling(countComponent);
                }
                else if(craftIngredient instanceof CraftIngredients.ItemIngredient)
                {
                    CraftIngredients.ItemIngredient itemIngredient = (CraftIngredients.ItemIngredient) craftIngredient;
                    ingredientValue.appendSibling(new StringTextComponent(itemIngredient.getId().toString())).mergeStyle(TextFormatting.DARK_AQUA);

                    IFormattableTextComponent countComponent = new StringTextComponent(" (x").appendString(String.valueOf(itemIngredient.getCount())).appendString(")").mergeStyle(TextFormatting.GRAY);
                    ingredientValue.appendSibling(countComponent);
                }
                else if(craftIngredient instanceof CraftIngredients.FluidIngredient)
                {
                    CraftIngredients.FluidIngredient fluidIngredient = (CraftIngredients.FluidIngredient) craftIngredient;

                    ingredientValue.appendSibling(new StringTextComponent(fluidIngredient.getId().toString()));
                    ingredientValue.mergeStyle(TextFormatting.BLUE);

                    IFormattableTextComponent component = new StringTextComponent(" (");
                    component.appendString(String.valueOf(fluidIngredient.getAmount()));
                    component.appendSibling(new StringTextComponent("mb)"));
                    component.mergeStyle(TextFormatting.GRAY);
                    ingredientValue.appendSibling(component);
                }
                else if(craftIngredient instanceof CraftIngredients.DataIngredient)
                {
                    CraftIngredients.DataIngredient dataIngredient = (CraftIngredients.DataIngredient) craftIngredient;
                    ingredientValue.appendSibling(new StringTextComponent("" + (dataIngredient.isDouble() ? dataIngredient.getData().doubleValue() : dataIngredient.getData().intValue())).mergeStyle(TextFormatting.LIGHT_PURPLE));
                    ingredientValue.appendSibling(new StringTextComponent(" ").appendString(dataIngredient.getUnit().getDisplayUnit()).mergeStyle(TextFormatting.DARK_GRAY));
                }
                else if(craftIngredient instanceof CraftIngredients.NBTIngredient)
                {
                    CraftIngredients.NBTIngredient nbtIngredient = (CraftIngredients.NBTIngredient) craftIngredient;

                    tooltips.add(ingredientTooltipLine);

                    getNbtComponent(tooltips, gson.fromJson(nbtIngredient.getNbt().getString(), JsonObject.class), 1);
                    continue;
                }

                ingredientTooltipLine.appendSibling(ingredientValue);

                tooltips.add(ingredientTooltipLine);
            }
        }

        public void getNbtComponent(List<ITextComponent> parent, JsonObject compoundTag, int step)
        {
            for(String nbtKey : compoundTag.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()))
            {
                IFormattableTextComponent itemEntryKey = new StringTextComponent(String.join("", Collections.nCopies(step, "    "))).appendSibling(new StringTextComponent(nbtKey).mergeStyle(TextFormatting.RED)).appendSibling(new StringTextComponent(" : ").mergeStyle(TextFormatting.WHITE));

                parent.add(itemEntryKey);

                if(compoundTag.get(nbtKey).isJsonObject())
                {
                    getNbtComponent(parent, compoundTag.get(nbtKey).getAsJsonObject(), step + 1);
                }
                else if(compoundTag.get(nbtKey).isJsonArray())
                {
                    compoundTag.get(nbtKey).getAsJsonArray().forEach(je ->
                    {
                        if(je.isJsonObject()) getNbtComponent(parent, je.getAsJsonObject(), step + 1);
                        else if(je.isJsonPrimitive())
                        {
                            if(je.getAsJsonPrimitive().isString())
                            {
                                String str = je.getAsJsonPrimitive().getAsString().replace("\"", "");
                                Matcher matcher = numberPattern.matcher(str);
                                StringTextComponent value = new StringTextComponent("");

                                if(matcher.find())
                                    value.appendString(matcher.group());
                                else
                                    value.appendString(je.getAsJsonPrimitive().getAsString());

                                itemEntryKey.appendSibling(value.mergeStyle(TextFormatting.GOLD));
                            }
                        }
                    });
                }
                else if(compoundTag.get(nbtKey).isJsonPrimitive())
                {
                    String str = compoundTag.get(nbtKey).getAsJsonPrimitive().getAsString().replace("\"", "");
                    Matcher matcher = numberPattern.matcher(str);
                    StringTextComponent value = new StringTextComponent("");

                    if(matcher.find())
                        value.appendString(matcher.group());
                    else
                        value.appendString(compoundTag.get(nbtKey).getAsJsonPrimitive().getAsString());

                    itemEntryKey.appendSibling(value.mergeStyle(TextFormatting.GOLD));
                }
            }
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
            String name = recipe.getRecipeMap().values().stream().findFirst().orElse(References.getLoc("empty").toString());
            String displayStr = name.substring(name.indexOf(':') + 1);

            displayTruncatedString(matrixStack, displayStr, left, top, width, height, true, isMouseOver);

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
                }
                catch(Exception ignored)
                {
                }
            }

            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(item), left + yPos, top + yPos);
        }

        @Override
        public String toString()
        {
            return null;
        }

        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            tooltips.clear();
            Map<ModRecipesJSSerializer.RecipeDescriptors, String> recipeDescriptors = recipe.getRecipeMap();
            tooltips.add(new StringTextComponent(recipeDescriptors.values().stream().findFirst().orElse(References.getLoc("empty").toString())).mergeStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE));
            tooltips.add(new StringTextComponent(""));
            recipeDescriptors.forEach((tag, value) -> tooltips.add(new StringTextComponent(tag.toString()).mergeStyle(TextFormatting.DARK_PURPLE).appendSibling(new StringTextComponent(" : ")).appendSibling(new StringTextComponent(value).mergeStyle(TextFormatting.DARK_AQUA))));

            ClientUtils.getCurrentScreen().func_243308_b(matrixStack, tooltips, mouseX, mouseY);
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
            displayTruncatedString(matrixStack, resource, left, top, width, height, false, isMouseOver);
        }

        @Override
        public String toString()
        {
            return this.resource;
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            if(!tooltips.isEmpty() && minecraft.currentScreen != null)
            {
                tooltips.clear();
                minecraft.currentScreen.func_243308_b(matrixStack, tooltips, mouseX, mouseY);
            }
        }

        public StringEntry setTooltips(List<ITextComponent> tooltips)
        {
            this.tooltips = tooltips;
            return this;
        }
    }

    public static class ResourceLocationEntry extends Entry
    {
        private final ResourceLocation resourceLocation;

        public ResourceLocationEntry(ResourceLocation resourceLocation)
        {
            this.resourceLocation = resourceLocation;
        }

        public ResourceLocation getResourceLocation()
        {
            return resourceLocation;
        }

        @Override
        public void render(@Nonnull MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            //Screen.fill(matrixStack, left, top, left + width, top + height, 0xFFFFFF);

            String displayStr = getString(width, resourceLocation.toString());

            int color = isMouseOver ? 0xF1f115 : 0xFFFFFF;
            matrixStack.push();
            float scale = 1.1F;
            matrixStack.scale(scale, scale, scale);
            Screen.drawCenteredString(matrixStack, minecraft.fontRenderer, displayStr, (int) ((left + width / 2) / scale), (int) ((top + height / 2 - (minecraft.fontRenderer.FONT_HEIGHT * scale) / 2) / scale), color);
            matrixStack.pop();

            Item item = ForgeRegistries.ITEMS.getValue(getResourceLocation());

            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(item == null ? Items.BARRIER : item), left + yPos, top + yPos);
        }

        @Override
        public String toString()
        {
            return this.resourceLocation.toString();
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            if(!tooltips.isEmpty() && minecraft.currentScreen != null)
            {
                tooltips.clear();
                minecraft.currentScreen.func_243308_b(matrixStack, tooltips, mouseX, mouseY);
            }
        }
    }
}
