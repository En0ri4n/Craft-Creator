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
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.utils.Callable;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.Utils;
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

@SuppressWarnings({"unused", "deprecation"})
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
    private boolean canDisplayTooltips;

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
        this.canDisplayTooltips = true;
    }

    public void setCanDisplayTooltips(boolean bool)
    {
        this.canDisplayTooltips = bool;
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
            Screen.drawCenteredString(pPoseStack, minecraft.font, this.title, this.x0 + this.width / 2, this.y0 - this.titleBoxHeight / 2 - minecraft.font.lineHeight / 2, 0xFFFFFF);
            //this.renderBackground(pPoseStack);
        }

        this.hoveredEntry = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;
        this.isListHovered = ClientUtils.isMouseHover(x0, y0, pMouseX, pMouseY, width, height - itemHeight);
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        // Render background
        Entry hovered = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;
        ClientUtils.bindTexture(BACKGROUND_TILE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int alpha = 100;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.vertex(this.x0, this.y1, 0.0D).uv((float) this.x0 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.vertex(this.x1, this.y1, 0.0D).uv((float) this.x1 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.vertex(this.x1, this.y0, 0.0D).uv((float) this.x1 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.vertex(this.x0, this.y0, 0.0D).uv((float) this.x0 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        tesselator.end();

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
            bufferbuilder.vertex(scrollbarPosition, this.y1, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(j, this.y1, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(j, this.y0, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, this.y0, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, (i2 + l1), 0.0D).uv(0.0F, 1.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(j, (i2 + l1), 0.0D).uv(1.0F, 1.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(j, i2, 0.0D).uv(1.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, i2, 0.0D).uv(0.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, (i2 + l1 - 1), 0.0D).uv(0.0F, 1.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((j - 1), (i2 + l1 - 1), 0.0D).uv(1.0F, 1.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((j - 1), i2, 0.0D).uv(1.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, i2, 0.0D).uv(0.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            tesselator.end();
        }

        this.renderDecorations(pPoseStack, pMouseX, pMouseY);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    protected void renderList(@Nonnull MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks)
    {
        int itemCount = this.getItemCount();
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

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
                    bufferbuilder.vertex(l1, (i1 + j1 + 2), 0.0D).endVertex();
                    bufferbuilder.vertex(i2, (i1 + j1 + 2), 0.0D).endVertex();
                    bufferbuilder.vertex(i2, (i1 - 2), 0.0D).endVertex();
                    bufferbuilder.vertex(l1, (i1 - 2), 0.0D).endVertex();
                    tesselator.end();
                    RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                    bufferbuilder.vertex((l1 + 1), (i1 + j1 + 1), 0.0D).endVertex();
                    bufferbuilder.vertex((i2 - 1), (i1 + j1 + 1), 0.0D).endVertex();
                    bufferbuilder.vertex((i2 - 1), (i1 - 1), 0.0D).endVertex();
                    bufferbuilder.vertex((l1 + 1), (i1 - 1), 0.0D).endVertex();
                    tesselator.end();
                    RenderSystem.enableTexture();
                }

                int rowLeft = this.getRowLeft();
                entry.render(matrixStack, itemIndex, rowTop, rowLeft, rowWidth, j1, mouseX, mouseY, Objects.equals(this.hoveredEntry, entry) && canDisplayTooltips, partialTicks);
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

    public void setEntries(List<? extends Entry> entries)
    {
        this.clearEntries();
        entries.forEach(this::addEntry);
        // this.setScrollAmount(0D);
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

        if(entry != null && isListHovered && canDisplayTooltips)
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

            Screen.drawString(matrixStack, ClientUtils.getFont(), stringToDisplay, hasItemDisplay ? leftPos + 16 + 5 : leftPos + width / 2 - Utils.width(stringToDisplay) / 2, (topPos + height / 2 - ClientUtils.getFont().lineHeight / 2), color);
        }

        protected String getString(int width, String displayStr)
        {
            int stringWidth = Utils.width(displayStr);

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

            matrixStack.pushPose();
            float scale = 1F;
            int yPos = height / 2 - 16 / 2;
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(item, (int) ((left + 1) / scale), (int) ((top + yPos) / scale));
            matrixStack.popPose();
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

            tooltips.add(new StringTextComponent(this.recipe.getId().toString()).withStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE));
            tooltips.add(new StringTextComponent(""));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.input"));
            addToTooltip(input);

            tooltips.add(new StringTextComponent("").withStyle(TextFormatting.DARK_AQUA));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.output"));
            addToTooltip(output);

            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
        }

        private void addToTooltip(CraftIngredients input)
        {
            for(CraftIngredients.CraftIngredient craftIngredient : input.getIngredientsWithCount())
            {
                IFormattableTextComponent ingredientTooltipLine = new StringTextComponent(craftIngredient.getDescription());
                TextFormatting baseColor = TextFormatting.AQUA;
                ingredientTooltipLine.withStyle(baseColor);

                IFormattableTextComponent separator = new StringTextComponent(" : ");
                separator.withStyle(TextFormatting.WHITE);
                ingredientTooltipLine.append(separator);

                IFormattableTextComponent ingredientValue = new StringTextComponent("");

                if(craftIngredient instanceof CraftIngredients.BlockIngredient)
                {
                    CraftIngredients.BlockIngredient blockIngredient = (CraftIngredients.BlockIngredient) craftIngredient;
                    ingredientValue.append(new StringTextComponent(blockIngredient.getId().toString()));
                    ingredientValue.withStyle(TextFormatting.DARK_AQUA);
                }
                else if(craftIngredient instanceof CraftIngredients.ItemLuckIngredient)
                {
                    CraftIngredients.ItemLuckIngredient itemLuckIngredient = (CraftIngredients.ItemLuckIngredient) craftIngredient;

                    ingredientValue.append(new StringTextComponent(itemLuckIngredient.getId().toString())).withStyle(TextFormatting.DARK_AQUA);

                    IFormattableTextComponent countComponent = new StringTextComponent(" (x").append(String.valueOf(itemLuckIngredient.getCount())).append(")").withStyle(TextFormatting.GRAY);
                    ingredientValue.append(countComponent);

                    if(itemLuckIngredient.getLuck() != 1D && itemLuckIngredient.getLuck() > 0D)
                    {
                        IFormattableTextComponent luckComponent = new StringTextComponent(" ").append(String.valueOf(itemLuckIngredient.getLuck() * 100)).append("%").withStyle(TextFormatting.DARK_GRAY);
                        ingredientValue.append(luckComponent);
                    }
                }
                else if(craftIngredient instanceof CraftIngredients.MultiItemIngredient)
                {
                    CraftIngredients.MultiItemIngredient multiItemIngredient = (CraftIngredients.MultiItemIngredient) craftIngredient;

                    IFormattableTextComponent countComponent = new StringTextComponent(" (x").append(String.valueOf(multiItemIngredient.getCount())).append(")").withStyle(TextFormatting.GRAY);
                    ingredientTooltipLine.append(countComponent);
                    tooltips.add(ingredientTooltipLine);

                    multiItemIngredient.getIds().forEach((resourceLocation, isTag) ->
                    {
                        IFormattableTextComponent itemEntryComponent = new StringTextComponent("    ").append(new StringTextComponent("- ").withStyle(TextFormatting.WHITE));
                        itemEntryComponent.append(new StringTextComponent(isTag ? "Tag" : "Item").withStyle(TextFormatting.YELLOW));
                        itemEntryComponent.append(new StringTextComponent(" : ").withStyle(TextFormatting.WHITE));
                        itemEntryComponent.append(new StringTextComponent(resourceLocation.toString()).withStyle(TextFormatting.RED));
                        tooltips.add(itemEntryComponent);
                    });
                    continue;
                }
                else if(craftIngredient instanceof CraftIngredients.TagIngredient)
                {
                    CraftIngredients.TagIngredient tagIngredient = (CraftIngredients.TagIngredient) craftIngredient;
                    ingredientValue.append(new StringTextComponent(tagIngredient.getId().toString())).withStyle(TextFormatting.DARK_AQUA);

                    IFormattableTextComponent countComponent = new StringTextComponent(" (x").append(String.valueOf(tagIngredient.getCount())).append(")").withStyle(TextFormatting.GRAY);
                    ingredientValue.append(countComponent);
                }
                else if(craftIngredient instanceof CraftIngredients.ItemIngredient)
                {
                    CraftIngredients.ItemIngredient itemIngredient = (CraftIngredients.ItemIngredient) craftIngredient;
                    ingredientValue.append(new StringTextComponent(itemIngredient.getId().toString())).withStyle(TextFormatting.DARK_AQUA);

                    IFormattableTextComponent countComponent = new StringTextComponent(" (x").append(String.valueOf(itemIngredient.getCount())).append(")").withStyle(TextFormatting.GRAY);
                    ingredientValue.append(countComponent);
                }
                else if(craftIngredient instanceof CraftIngredients.FluidIngredient)
                {
                    CraftIngredients.FluidIngredient fluidIngredient = (CraftIngredients.FluidIngredient) craftIngredient;

                    ingredientValue.append(new StringTextComponent(fluidIngredient.getId().toString()));
                    ingredientValue.withStyle(TextFormatting.BLUE);

                    IFormattableTextComponent component = new StringTextComponent(" (");
                    component.append(String.valueOf(fluidIngredient.getAmount()));
                    component.append(new StringTextComponent("mb)"));
                    component.withStyle(TextFormatting.GRAY);
                    ingredientValue.append(component);
                }
                else if(craftIngredient instanceof CraftIngredients.DataIngredient)
                {
                    CraftIngredients.DataIngredient dataIngredient = (CraftIngredients.DataIngredient) craftIngredient;
                    ingredientValue.append(new StringTextComponent("" + (dataIngredient.isDouble() ? dataIngredient.getData().doubleValue() : dataIngredient.getData().intValue())).withStyle(TextFormatting.LIGHT_PURPLE));
                    ingredientValue.append(new StringTextComponent(" ").append(dataIngredient.getUnit().getDisplayUnit()).withStyle(TextFormatting.DARK_GRAY));
                }
                else if(craftIngredient instanceof CraftIngredients.NBTIngredient)
                {
                    CraftIngredients.NBTIngredient nbtIngredient = (CraftIngredients.NBTIngredient) craftIngredient;

                    tooltips.add(ingredientTooltipLine);

                    getNbtComponent(tooltips, gson.fromJson(nbtIngredient.getNbt().getAsString(), JsonObject.class), 1);
                    continue;
                }

                ingredientTooltipLine.append(ingredientValue);

                tooltips.add(ingredientTooltipLine);
            }
        }

        public void getNbtComponent(List<ITextComponent> parent, JsonObject compoundTag, int step)
        {
            for(String nbtKey : compoundTag.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()))
            {
                IFormattableTextComponent itemEntryKey = new StringTextComponent(String.join("", Collections.nCopies(step, "    "))).append(new StringTextComponent(nbtKey).withStyle(TextFormatting.RED)).append(new StringTextComponent(" : ").withStyle(TextFormatting.WHITE));

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
                                    value.append(matcher.group());
                                else
                                    value.append(je.getAsJsonPrimitive().getAsString());

                                itemEntryKey.append(value.withStyle(TextFormatting.GOLD));
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
                        value.append(matcher.group());
                    else
                        value.append(compoundTag.get(nbtKey).getAsJsonPrimitive().getAsString());

                    itemEntryKey.append(value.withStyle(TextFormatting.GOLD));
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

            if(recipe.getRecipeId() != null)
            {
                ResourceLocation recipeId = ResourceLocation.tryParse(recipe.getRecipeId());
                IRecipe<?> recipe = ClientUtils.getClientLevel().getRecipeManager().getRecipes().stream().filter(r -> r.getId().equals(recipeId)).findFirst().orElse(null);

                if(recipe != null)
                {
                    item = ModRecipeCreatorDispatcher.getOutput(recipe).getIcon().getItem();
                }
            }

            int yPos = height / 2 - 16 / 2;
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(item), left + yPos, top + yPos);

            RenderSystem.color4f(1F, 1.0F, 1.0F, 1F);
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(Items.BARRIER), left + yPos, top + yPos);
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
            tooltips.add(new StringTextComponent(recipeDescriptors.values().stream().findFirst().orElse(References.getLoc("empty").toString())).withStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE));
            tooltips.add(new StringTextComponent(""));
            recipeDescriptors.forEach((tag, value) -> tooltips.add(new StringTextComponent(tag.toString()).withStyle(TextFormatting.DARK_PURPLE).append(new StringTextComponent(" : ")).append(new StringTextComponent(value).withStyle(TextFormatting.DARK_AQUA))));

            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
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
            if(!tooltips.isEmpty() && ClientUtils.getCurrentScreen() != null)
            {
                tooltips.clear();
                ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
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
            matrixStack.pushPose();
            float scale = 1.1F;
            matrixStack.scale(scale, scale, scale);
            Screen.drawCenteredString(matrixStack, ClientUtils.getFont(), displayStr, (int) ((left + width / 2) / scale), (int) ((top + height / 2 - (ClientUtils.getFont().lineHeight * scale) / 2) / scale), color);
            matrixStack.popPose();

            Item item = ForgeRegistries.ITEMS.getValue(getResourceLocation());

            int yPos = height / 2 - 16 / 2;
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(item == null ? Items.BARRIER : item), left + yPos, top + yPos);
        }

        @Override
        public String toString()
        {
            return this.resourceLocation.toString();
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            if(!tooltips.isEmpty() && ClientUtils.getCurrentScreen() != null)
            {
                tooltips.clear();
                ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
            }
        }
    }
}
