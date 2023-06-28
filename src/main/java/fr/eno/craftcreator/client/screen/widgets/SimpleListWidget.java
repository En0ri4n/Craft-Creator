package fr.eno.craftcreator.client.screen.widgets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.EntryType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class SimpleListWidget extends AbstractList<SimpleListWidget.Entry>
{
    private static final ResourceLocation BACKGROUND_TILE = References.getLoc("textures/gui/widgets/background_tile.png");
    protected static final int MAX_ITEMS_DISPLAYED = 10;

    private final ITextComponent title;
    private final int titleBoxHeight;
    private final int scrollBarWidth;
    private final boolean canDelete;
    private boolean canHaveSelected;
    private final boolean hasTitleBox;
    private Consumer<Entry> onSelected;
    private boolean isVisible;
    private final Consumer<Entry> onDelete;
    private Entry hoveredEntry;
    private boolean isListHovered;
    private boolean canDisplayTooltips;

    public SimpleListWidget(int leftIn, int topIn, int widthIn, int heightIn, int slotHeightIn, int titleBoxHeight, int scrollBarWidth, ITextComponent titleIn, @Nullable Consumer<Entry> onDelete, boolean canDelete)
    {
        super(ClientUtils.getMinecraft(), widthIn - scrollBarWidth, heightIn - titleBoxHeight, 0, 0, slotHeightIn);
        this.x0 = leftIn;
        this.x1 = leftIn + widthIn - scrollBarWidth;
        this.y0 = topIn + titleBoxHeight;
        this.y1 = topIn + heightIn - titleBoxHeight;
        this.title = titleIn;
        this.titleBoxHeight = titleBoxHeight;
        this.scrollBarWidth = scrollBarWidth;
        this.canHaveSelected = true;
        this.hasTitleBox = titleBoxHeight > 0;
        this.isVisible = true;
        this.onDelete = onDelete;
        this.canDisplayTooltips = true;
        this.canDelete = canDelete;
    }

    public List<Entry> getEntries()
    {
        return children();
    }
    
    public int getSize()
    {
        return this.getItemCount();
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
        return this.width - this.scrollBarWidth;
    }

    @Override
    protected int getScrollbarPosition()
    {
        return this.x1 - this.scrollBarWidth;
    }

    @Override
    public void render(@Nonnull MatrixStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        if(!this.isVisible) return;

        if(hasTitleBox)
        {
            Screen.fill(pPoseStack, this.x0, this.y0 - titleBoxHeight, this.x0 + this.width, this.y0, 0xf2c3a942);
            Screen.drawCenteredString(pPoseStack, ClientUtils.getFontRenderer(), this.title, this.x0 + this.width / 2, this.y0 - this.titleBoxHeight / 2 - minecraft.font.lineHeight / 2, 0xFFFFFF);
            //this.renderBackground(pPoseStack);
        }

        this.hoveredEntry = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;
        this.isListHovered = ScreenUtils.isMouseHover(x0, y0, pMouseX, pMouseY, x1 - x0, y1 - y0);
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        // Render background
        ClientUtils.bindTexture(getBackgroundTile());
        float dark = 0.2F;
        ClientUtils.color4f(dark, dark, dark, 1.0F);
        Screen.blit(pPoseStack, this.x0, this.y0, 0, 0, x1 - x0, y1 - y0, 16, 16);

        int rowLeft = this.getRowLeft();
        int k = this.y0 + 4 - (int) this.getScrollAmount();

        this.renderList(pPoseStack, rowLeft, k, pMouseX, pMouseY, pPartialTick);

        int scrollbarPosition = this.getScrollbarPosition();
        int scrollBarPosX1 = scrollbarPosition + scrollBarWidth;
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
            ClientUtils.renderQuad(bufferbuilder, scrollbarPosition, this.y0, scrollBarPosX1, this.y1, 0, 0, 0, 255);
            ClientUtils.renderQuad(bufferbuilder, scrollbarPosition, i2, scrollBarPosX1, i2 + l1, 128, 128, 128, 255);
            ClientUtils.renderQuad(bufferbuilder, scrollbarPosition, i2, scrollBarPosX1, i2 + 1, 192, 192, 192, 255);
            tesselator.end();
        }

        this.renderDecorations(pPoseStack, pMouseX, pMouseY);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @Override
    protected int getRowTop(int pIndex)
    {
        return super.getRowTop(pIndex);
    }

    protected void trimWidthToEntries()
    {
        int maxWidth = ClientUtils.getCurrentScreen().width - x0;
        int maxEntryWidth = ClientUtils.getBiggestStringWidth(getEntries().stream().map(Entry::getEntryValue).collect(Collectors.toList()));
        this.width = Math.min(maxWidth, Math.max(width, maxEntryWidth));
        this.x1 = this.x0 + this.width + scrollBarWidth;

        this.y1 = y0 + Math.min(getEntries().size(), MAX_ITEMS_DISPLAYED) * itemHeight + 4;
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

            if(rowTop >= this.y0 && rowBottom <= this.y1)
            {
                int innerHeight = this.itemHeight - 4;
                Entry entry = this.getEntry(itemIndex);
                int rowWidth = this.getRowWidth();

                if(canHaveSelected && this.isSelectedItem(itemIndex))
                {
                    int l1 = this.x0 + this.width / 2 - rowWidth / 2;
                    int i2 = this.x0 + this.width / 2 + rowWidth / 2;
                    RenderSystem.disableTexture();
                    float f = this.isFocused() ? 1.0F : 0.5F;
                    ClientUtils.color4f(f, f, f, 1.0F);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                    bufferbuilder.vertex(l1, (rowTop + innerHeight + 2), 0.0D).endVertex();
                    bufferbuilder.vertex(i2, (rowTop + innerHeight + 2), 0.0D).endVertex();
                    bufferbuilder.vertex(i2, (rowTop - 2), 0.0D).endVertex();
                    bufferbuilder.vertex(l1, (rowTop - 2), 0.0D).endVertex();
                    tesselator.end();
                    ClientUtils.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                    bufferbuilder.vertex((l1 + 1), (rowTop + innerHeight + 1), 0.0D).endVertex();
                    bufferbuilder.vertex((i2 - 1), (rowTop + innerHeight + 1), 0.0D).endVertex();
                    bufferbuilder.vertex((i2 - 1), (rowTop - 1), 0.0D).endVertex();
                    bufferbuilder.vertex((l1 + 1), (rowTop - 1), 0.0D).endVertex();
                    tesselator.end();
                    RenderSystem.enableTexture();
                }

                int rowLeft = this.getRowLeft();
                entry.render(matrixStack, itemIndex, rowTop, rowLeft, rowWidth, innerHeight, mouseX, mouseY, Objects.equals(this.hoveredEntry, entry) && canDisplayTooltips, partialTicks);
            }
        }
    }

    /**
     * Returns true if the mouse is over the list.<br>
     * (Due to the way scrollbar width is not included in the list width, this override check if the mouse is over the scrollbar too)
     */
    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY)
    {
        return pMouseX >= this.x0 && pMouseX <= this.x1 + this.scrollBarWidth && pMouseY >= this.y0 && pMouseY <= this.y1;
    }

    protected ResourceLocation getBackgroundTile()
    {
        return BACKGROUND_TILE;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if(keyCode == GLFW.GLFW_KEY_DELETE)
        {
            if(this.getSelected() != null && this.onDelete != null && canDelete)
            {
                this.onDelete.accept(this.getSelected());
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

    public void setEntries(List<? extends Entry> entries, boolean resetScroll)
    {
        this.clearEntries();
        entries.forEach(this::addEntry);
        if(resetScroll)
            this.setScrollAmount(0D);
        this.height = Math.min(entries.size(), MAX_ITEMS_DISPLAYED) * itemHeight;
    }

    @Override
    public void setSelected(@Nullable Entry entry)
    {
        if(this.onSelected != null && entry != null)
        {
            this.onSelected.accept(entry);
        }

        super.setSelected(entry);
    }

    public void setOnSelectedChange(Consumer<Entry> onSelected)
    {
        this.onSelected = onSelected;
    }

    public void setPos(int x, int y)
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

    public boolean isVisible()
    {
        return isVisible;
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
    }

    public void tick()
    {
        for(Entry entry : getEntries())
        {
            entry.tick();
        }
    }

    public abstract static class Entry extends AbstractList.AbstractListEntry<SimpleListWidget.Entry>
    {
        protected static final Gson gson = new GsonBuilder().setLenient().create();
        private static final Pattern numberPattern = Pattern.compile("[0-9]+");

        protected final List<ITextComponent> tooltips;

        protected Entry()
        {
            this.tooltips = new ArrayList<>();
        }

        public abstract String getEntryValue();

        public abstract void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY);

        protected void tick() {}

        protected void displayTruncatedString(MatrixStack matrixStack, String stringToDisplay, int leftPos, int topPos, int width, int height, boolean hasItemDisplay, boolean isMouseOver)
        {
            stringToDisplay = ScreenUtils.truncateString(width, stringToDisplay);

            int color = isMouseOver ? 0xF1f115 : 0xFFFFFF;

            Screen.drawString(matrixStack, ClientUtils.getFontRenderer(), stringToDisplay, hasItemDisplay ? leftPos + 16 + 5 : leftPos + width / 2 - ClientUtils.width(stringToDisplay) / 2, (topPos + height / 2 - ClientUtils.getFontRenderer().lineHeight / 2), color);
        }

        static void addToTooltip(List<ITextComponent> tooltips, CraftIngredients input)
        {
            for(CraftIngredients.CraftIngredient craftIngredient : input.getIngredientsWithCount())
            {
                IFormattableTextComponent ingredientTooltipLine = new StringTextComponent(craftIngredient.getDescription());
                TextFormatting baseColor = TextFormatting.BLUE;
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
                    ingredientValue.withStyle(TextFormatting.AQUA);

                    IFormattableTextComponent component = new StringTextComponent(" (");
                    component.append(String.valueOf(fluidIngredient.getAmount()));
                    component.append(new StringTextComponent("mb)"));
                    component.withStyle(TextFormatting.GRAY);
                    ingredientValue.append(component);
                }
                else if(craftIngredient instanceof CraftIngredients.DataIngredient)
                {
                    CraftIngredients.DataIngredient dataIngredient = (CraftIngredients.DataIngredient) craftIngredient;
                    ingredientValue.append(new StringTextComponent(String.valueOf(dataIngredient.isDouble() ? dataIngredient.getData().doubleValue() : dataIngredient.getData().intValue())).withStyle(TextFormatting.LIGHT_PURPLE));
                    ingredientValue.append(new StringTextComponent(" ").append(dataIngredient.getUnit().getDisplayUnit()).withStyle(TextFormatting.DARK_GRAY));
                }
                else if(craftIngredient instanceof CraftIngredients.StringDataIngredient)
                {
                    CraftIngredients.StringDataIngredient stringDataIngredient = (CraftIngredients.StringDataIngredient) craftIngredient;
                    ingredientValue.append(new StringTextComponent(String.valueOf(stringDataIngredient.getStringData())).withStyle(TextFormatting.YELLOW));
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

        protected static void getNbtComponent(List<ITextComponent> parent, JsonObject compoundTag, int step)
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

            displayTruncatedString(matrixStack, displayStr, left, top, width, height, true, isMouseOver);

            ItemStack item = ModRecipeCreatorDispatcher.getOutput(recipe).getIcon();

            matrixStack.pushPose();
            float scale = 1F;
            int yPos = height / 2 - 16 / 2;
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(item, (int) ((left + 1) / scale), (int) ((top + yPos) / scale));
            matrixStack.popPose();
        }

        @Override
        public String getEntryValue()
        {
            return recipe.getId().toString();
        }

        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            tooltips.clear();
            CraftIngredients input = ModRecipeCreatorDispatcher.getInputs(recipe);
            CraftIngredients output = ModRecipeCreatorDispatcher.getOutput(recipe);

            tooltips.add(new StringTextComponent(this.recipe.getId().toString()).withStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE)
                    .append(new StringTextComponent("§r§8§o (" + CommonUtils.getRecipeTypeName(recipe.getType()).getPath() + ")")));
            tooltips.add(new StringTextComponent(""));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.input"));
            addToTooltip(tooltips, input);

            tooltips.add(new StringTextComponent("").withStyle(TextFormatting.DARK_AQUA));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.output"));
            addToTooltip(tooltips, output);
            
            GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, ClientUtils.getCurrentScreen().width, ClientUtils.getCurrentScreen().height, -1, ClientUtils.getFontRenderer());
        }

        public IRecipe<?> getRecipe()
        {
            return recipe;
        }
    }

    public static class ModifiedRecipeEntry extends Entry
    {
        private final KubeJSModifiedRecipe recipe;

        public ModifiedRecipeEntry(KubeJSModifiedRecipe recipe)
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
                ResourceLocation recipeId = CommonUtils.parse(recipe.getRecipeId());
                item = ForgeRegistries.ITEMS.containsKey(recipeId) ? ForgeRegistries.ITEMS.getValue(recipeId) : Items.BARRIER;
            }

            int yPos = height / 2 - 16 / 2;
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(item), left + yPos, top + yPos);
    
            RenderSystem.pushMatrix();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.translatef(0F, 0F, 100F);
            ClientUtils.color4f(1F, 1F, 1F, 0.5F);
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(Items.BARRIER), left + yPos, top + yPos);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.popMatrix();
        }

        @Override
        public String getEntryValue()
        {
            return null;
        }

        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {
            tooltips.clear();
            Map<ModRecipeSerializer.RecipeDescriptors, String> recipeDescriptors = recipe.getRecipeMap();
            tooltips.add(recipe.getDisplayTitle().withStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE));
            tooltips.add(new StringTextComponent(""));
            recipeDescriptors.forEach((tag, value) -> tooltips.add(new StringTextComponent(tag.getDisplay().getString()).withStyle(TextFormatting.DARK_PURPLE).append(new StringTextComponent(" : ").withStyle(TextFormatting.WHITE)).append(new StringTextComponent(value).withStyle(TextFormatting.DARK_AQUA))));

            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
        }

        public KubeJSModifiedRecipe getRecipe()
        {
            return recipe;
        }
    }

    public static class StringEntry extends Entry
    {
        private final List<ITextComponent> tooltips;
        private final String resource;

        public StringEntry(String resource)
        {
            this.resource = resource;
            this.tooltips = new ArrayList<>();
        }

        @Override
        public void render(@Nonnull MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            displayTruncatedString(matrixStack, resource, left, top, width, height, false, isMouseOver);
        }

        @Override
        public String getEntryValue()
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
    }

    public static class ResourceLocationEntry extends Entry
    {
        private final ResourceLocation resourceLocation;
        private final EntryType type;
        /** Used to display all items if resource location is a tag */
        private ItemStack displayStack;
        private int displayCounter;

        public ResourceLocationEntry(ResourceLocation resourceLocation, EntryType type)
        {
            this.resourceLocation = resourceLocation;
            this.type = type;
            this.displayCounter = 0;
            this.displayStack = ItemStack.EMPTY;
        }

        public ResourceLocation getResourceLocation()
        {
            return resourceLocation;
        }

        @Override
        public void render(@Nonnull MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            int yPos = height / 2 - 16 / 2;
            RenderSystem.pushMatrix(); // Need to use RenderSystem to get the actual matrixstack BECAUSE MINECRAFT CREATE A NEW MATRIXSTACK EVERY FRAME, WTF ?!?!?!
            RenderSystem.translatef(0F, 0F, 200F); // Fixes z-fighting with inventory items
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(displayStack, left + yPos, top + yPos);
            RenderSystem.popMatrix();

            String displayStr = ScreenUtils.truncateString(width, resourceLocation.toString());
            displayTruncatedString(matrixStack, displayStr, left, top, width, height, true, isMouseOver);
        }

        @Override
        public void tick()
        {
            if(type.isItem())
            {
                displayStack = new ItemStack(CommonUtils.getItem(getResourceLocation()));
            }
            else if(type.isFluid())
            {
                displayStack = new ItemStack(CommonUtils.getFluid(getResourceLocation()).getBucket());
            }
            else if(type.isTag())
            {
                ITag<Item> tag = CommonUtils.getTag(getResourceLocation());

                if(tag.getValues().size() > 0)
                {
                    int displayTime = 20;
                    if(displayCounter / displayTime >= tag.getValues().size())
                        displayCounter = 0;

                    displayStack = new ItemStack(tag.getValues().get(displayCounter / displayTime));

                    if(!Screen.hasShiftDown())
                        displayCounter++;
                }
            }
            else
            {
                displayStack = ItemStack.EMPTY;
            }
        }

        @Override
        public String getEntryValue()
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
