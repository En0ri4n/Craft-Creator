package fr.eno.craftcreator.screen.widgets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class SimpleListWidget extends AbstractSelectionList<SimpleListWidget.Entry>
{
    private static final ResourceLocation BACKGROUND_TILE = References.getLoc("textures/gui/widgets/background_tile.png");
    protected static final int MAX_ITEMS_DISPLAYED = 10;

    private final Component title;
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

    public SimpleListWidget(int leftIn, int topIn, int widthIn, int heightIn, int slotHeightIn, int titleBoxHeight, int scrollBarWidth, Component titleIn, Consumer<Entry> onDelete, boolean canDelete)
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
        return this.x1;
    }

    @Override
    public void render(@Nonnull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        if(!this.isVisible) return;

        if(hasTitleBox)
        {
            Screen.fill(pPoseStack, this.x0, this.y0 - titleBoxHeight, this.x0 + this.width, this.y0, 0xf2c3a942);
            Screen.drawCenteredString(pPoseStack, ClientUtils.getFontRenderer(), this.title, this.x0 + this.width / 2, this.y0 - this.titleBoxHeight / 2 - minecraft.font.lineHeight / 2, 0xFFFFFF);
            //this.renderBackground(pPoseStack);
        }

        this.hoveredEntry = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;
        this.isListHovered = ScreenUtils.isMouseHover(x0, y0, pMouseX, pMouseY, width, height - itemHeight);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        // Render background
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        ClientUtils.bindTexture(BACKGROUND_TILE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int alpha = 100;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(this.x0, this.y1, 0.0D).uv((float) this.x0 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.vertex(this.x1, this.y1, 0.0D).uv((float) this.x1 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.vertex(this.x1, this.y0, 0.0D).uv((float) this.x1 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        bufferbuilder.vertex(this.x0, this.y0, 0.0D).uv((float) this.x0 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
        tesselator.end();

        int rowLeft = this.getRowLeft();
        int k = this.y0 + 4 - (int) this.getScrollAmount();

        this.renderList(pPoseStack, rowLeft, k, pMouseX, pMouseY, pPartialTick);

        int scrollbarPosition = this.getScrollbarPosition();
        int scrollBarPosX1 = scrollbarPosition + scrollBarWidth;
        int maxScroll = this.getMaxScroll();
        int i = getScrollbarPosition();
        if(maxScroll > 0)
        {
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            int l1 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
            l1 = Mth.clamp(l1, 32, this.y1 - this.y0 - 8);
            int i2 = (int) this.getScrollAmount() * (this.y1 - this.y0 - l1) / maxScroll + this.y0;
            if(i2 < this.y0)
            {
                i2 = this.y0;
            }

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            ClientUtils.renderQuad(bufferbuilder, scrollbarPosition, this.y0, scrollBarPosX1, this.y1, 0, 0, 0, 255);
            ClientUtils.renderQuad(bufferbuilder, scrollbarPosition, i2, scrollBarPosX1, i2 + l1, 128, 128, 128, 255);
            ClientUtils.renderQuad(bufferbuilder, scrollbarPosition, i2, scrollBarPosX1, i2 + 1, 192, 192, 192, 255);
            tesselator.end();
        }

        this.renderDecorations(pPoseStack, pMouseX, pMouseY);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    protected void renderList(@Nonnull PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks)
    {
        int itemCount = this.getItemCount();
        Tesselator tesselator = Tesselator.getInstance();
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
                    RenderSystem.setShader(GameRenderer::getPositionShader);
                    float f = this.isFocused() ? 1.0F : 0.5F;
                    ClientUtils.color4f(f, f, f, 1.0F);
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
                    bufferbuilder.vertex(l1, (i1 + j1 + 2), 0.0D).endVertex();
                    bufferbuilder.vertex(i2, (i1 + j1 + 2), 0.0D).endVertex();
                    bufferbuilder.vertex(i2, (i1 - 2), 0.0D).endVertex();
                    bufferbuilder.vertex(l1, (i1 - 2), 0.0D).endVertex();
                    tesselator.end();
                    ClientUtils.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
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
        if(this.isListHovered) return super.mouseScrolled(p_93416_, p_93417_, p_93418_);

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
        if(resetScroll) this.setScrollAmount(0D);
        this.height = Math.min(entries.size(), MAX_ITEMS_DISPLAYED) * itemHeight;
    }

    @Override
    public void setSelected(Entry entry)
    {
        if(this.onSelected != null)
        {
            this.onSelected.accept(entry);
        }

        super.setSelected(entry);
    }

    public void setOnSelectedChange(Consumer<Entry> onSelected)
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

    public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
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
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_)
    {

    }

    public abstract static class Entry extends AbstractSelectionList.Entry<SimpleListWidget.Entry>
    {
        protected static final Gson gson = new GsonBuilder().setLenient().create();
        private static final Pattern numberPattern = Pattern.compile("[0-9]+");

        protected final List<Component> tooltips;

        protected Entry()
        {
            this.tooltips = new ArrayList<>();
        }

        public abstract String getEntryValue();

        public abstract void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY);

        protected void displayTruncatedString(PoseStack matrixStack, String stringToDisplay, int leftPos, int topPos, int width, int height, boolean hasItemDisplay, boolean isMouseOver)
        {
            stringToDisplay = getString(width, stringToDisplay);

            int color = isMouseOver ? 0xF1f115 : 0xFFFFFF;

            Screen.drawString(matrixStack, ClientUtils.getFontRenderer(), stringToDisplay, hasItemDisplay ? leftPos + 16 + 5 : leftPos + width / 2 - ClientUtils.width(stringToDisplay) / 2, (topPos + height / 2 - ClientUtils.getFontRenderer().lineHeight / 2), color);
        }

        protected String getString(int width, String displayStr)
        {
            int stringWidth = ClientUtils.width(displayStr);

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

        static void addToTooltip(List<Component> tooltips, CraftIngredients input)
        {
            for(CraftIngredients.CraftIngredient craftIngredient : input.getIngredientsWithCount())
            {
                MutableComponent ingredientTooltipLine = new TextComponent(craftIngredient.getDescription());
                ChatFormatting baseColor = ChatFormatting.BLUE;
                ingredientTooltipLine.withStyle(baseColor);

                MutableComponent separator = new TextComponent(" : ");
                separator.withStyle(ChatFormatting.WHITE);
                ingredientTooltipLine.append(separator);

                MutableComponent ingredientValue = new TextComponent("");

                if(craftIngredient instanceof CraftIngredients.BlockIngredient)
                {
                    CraftIngredients.BlockIngredient blockIngredient = (CraftIngredients.BlockIngredient) craftIngredient;
                    ingredientValue.append(new TextComponent(blockIngredient.getId().toString()));
                    ingredientValue.withStyle(ChatFormatting.DARK_AQUA);
                }
                else if(craftIngredient instanceof CraftIngredients.ItemLuckIngredient)
                {
                    CraftIngredients.ItemLuckIngredient itemLuckIngredient = (CraftIngredients.ItemLuckIngredient) craftIngredient;

                    ingredientValue.append(new TextComponent(itemLuckIngredient.getId().toString())).withStyle(ChatFormatting.DARK_AQUA);

                    MutableComponent countComponent = new TextComponent(" (x").append(String.valueOf(itemLuckIngredient.getCount())).append(")").withStyle(ChatFormatting.GRAY);
                    ingredientValue.append(countComponent);

                    if(itemLuckIngredient.getLuck() != 1D && itemLuckIngredient.getLuck() > 0D)
                    {
                        MutableComponent luckComponent = new TextComponent(" ").append(String.valueOf(itemLuckIngredient.getLuck() * 100)).append("%").withStyle(ChatFormatting.DARK_GRAY);
                        ingredientValue.append(luckComponent);
                    }
                }
                else if(craftIngredient instanceof CraftIngredients.MultiItemIngredient)
                {
                    CraftIngredients.MultiItemIngredient multiItemIngredient = (CraftIngredients.MultiItemIngredient) craftIngredient;

                    MutableComponent countComponent = new TextComponent(" (x").append(String.valueOf(multiItemIngredient.getCount())).append(")").withStyle(ChatFormatting.GRAY);
                    ingredientTooltipLine.append(countComponent);
                    tooltips.add(ingredientTooltipLine);

                    multiItemIngredient.getIds().forEach((resourceLocation, isTag) ->
                    {
                        MutableComponent itemEntryComponent = new TextComponent("    ").append(new TextComponent("- ").withStyle(ChatFormatting.WHITE));
                        itemEntryComponent.append(new TextComponent(isTag ? "Tag" : "Item").withStyle(ChatFormatting.YELLOW));
                        itemEntryComponent.append(new TextComponent(" : ").withStyle(ChatFormatting.WHITE));
                        itemEntryComponent.append(new TextComponent(resourceLocation.toString()).withStyle(ChatFormatting.RED));
                        tooltips.add(itemEntryComponent);
                    });
                    continue;
                }
                else if(craftIngredient instanceof CraftIngredients.TagIngredient)
                {
                    CraftIngredients.TagIngredient tagIngredient = (CraftIngredients.TagIngredient) craftIngredient;
                    ingredientValue.append(new TextComponent(tagIngredient.getId().toString())).withStyle(ChatFormatting.DARK_AQUA);

                    MutableComponent countComponent = new TextComponent(" (x").append(String.valueOf(tagIngredient.getCount())).append(")").withStyle(ChatFormatting.GRAY);
                    ingredientValue.append(countComponent);
                }
                else if(craftIngredient instanceof CraftIngredients.ItemIngredient)
                {
                    CraftIngredients.ItemIngredient itemIngredient = (CraftIngredients.ItemIngredient) craftIngredient;
                    ingredientValue.append(new TextComponent(itemIngredient.getId().toString())).withStyle(ChatFormatting.DARK_AQUA);

                    MutableComponent countComponent = new TextComponent(" (x").append(String.valueOf(itemIngredient.getCount())).append(")").withStyle(ChatFormatting.GRAY);
                    ingredientValue.append(countComponent);
                }
                else if(craftIngredient instanceof CraftIngredients.FluidIngredient)
                {
                    CraftIngredients.FluidIngredient fluidIngredient = (CraftIngredients.FluidIngredient) craftIngredient;

                    ingredientValue.append(new TextComponent(fluidIngredient.getId().toString()));
                    ingredientValue.withStyle(ChatFormatting.AQUA);

                    MutableComponent component = new TextComponent(" (");
                    component.append(String.valueOf(fluidIngredient.getAmount()));
                    component.append(new TextComponent("mb)"));
                    component.withStyle(ChatFormatting.GRAY);
                    ingredientValue.append(component);
                }
                else if(craftIngredient instanceof CraftIngredients.DataIngredient)
                {
                    CraftIngredients.DataIngredient dataIngredient = (CraftIngredients.DataIngredient) craftIngredient;
                    ingredientValue.append(new TextComponent("" + (dataIngredient.isDouble() ? dataIngredient.getData().doubleValue() : dataIngredient.getData().intValue())).withStyle(ChatFormatting.LIGHT_PURPLE));
                    ingredientValue.append(new TextComponent(" ").append(dataIngredient.getUnit().getDisplayUnit()).withStyle(ChatFormatting.DARK_GRAY));
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

        protected static void getNbtComponent(List<Component> parent, JsonObject compoundTag, int step)
        {
            for(String nbtKey : compoundTag.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()))
            {
                MutableComponent itemEntryKey = new TextComponent(String.join("", Collections.nCopies(step, "    "))).append(new TextComponent(nbtKey).withStyle(ChatFormatting.RED)).append(new TextComponent(" : ").withStyle(ChatFormatting.WHITE));

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
                                TextComponent value = new TextComponent("");

                                if(matcher.find()) value.append(matcher.group());
                                else value.append(je.getAsJsonPrimitive().getAsString());

                                itemEntryKey.append(value.withStyle(ChatFormatting.GOLD));
                            }
                        }
                    });
                }
                else if(compoundTag.get(nbtKey).isJsonPrimitive())
                {
                    String str = compoundTag.get(nbtKey).getAsJsonPrimitive().getAsString().replace("\"", "");
                    Matcher matcher = numberPattern.matcher(str);
                    TextComponent value = new TextComponent("");

                    if(matcher.find()) value.append(matcher.group());
                    else value.append(compoundTag.get(nbtKey).getAsJsonPrimitive().getAsString());

                    itemEntryKey.append(value.withStyle(ChatFormatting.GOLD));
                }
            }
        }
    }

    public static class RecipeEntry extends Entry
    {
        private final Recipe<?> recipe;

        public RecipeEntry(Recipe<?> recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void render(@Nonnull PoseStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            String displayStr = recipe.getId().toString().substring(recipe.getId().toString().indexOf(':') + 1);

            displayTruncatedString(matrixStack, displayStr, left, top, width, height, true, isMouseOver);

            ItemStack item = ModRecipeCreatorDispatcher.getOutput(recipe).getIcon();

            int yPos = height / 2 - 16 / 2;
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(item, left + 1, top + yPos);
        }

        @Override
        public String getEntryValue()
        {
            return recipe.getId().toString();
        }

        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
        {
            tooltips.clear();
            CraftIngredients input = ModRecipeCreatorDispatcher.getInputs(recipe);
            CraftIngredients output = ModRecipeCreatorDispatcher.getOutput(recipe);

            tooltips.add(new TextComponent(this.recipe.getId().toString()).withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
            tooltips.add(new TextComponent(""));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.input"));
            addToTooltip(tooltips, input);

            tooltips.add(new TextComponent("").withStyle(ChatFormatting.DARK_AQUA));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.output"));
            addToTooltip(tooltips, output);

            ClientUtils.getCurrentScreen().renderTooltip(matrixStack, tooltips, Optional.empty(), mouseX, mouseY);
        }

        public Recipe<?> getRecipe()
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
        public void render(@Nonnull PoseStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            String name = recipe.getRecipeMap().values().stream().findFirst().orElse(References.getLoc("empty").toString());
            String displayStr = name.substring(name.indexOf(':') + 1);

            displayTruncatedString(matrixStack, displayStr, left, top, width, height, true, isMouseOver);

            Item item = Items.BARRIER;

            if(recipe.getRecipeId() != null)
            {
                ResourceLocation recipeId = ClientUtils.parse(recipe.getRecipeId());
                item = ForgeRegistries.ITEMS.containsKey(recipeId) ? ForgeRegistries.ITEMS.getValue(recipeId) : Items.BARRIER;
            }

            int yPos = height / 2 - 16 / 2;
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(item), left + yPos, top + yPos);

            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.getModelViewStack().translate(0D, 0D, 100D);
            ClientUtils.color4f(1F, 1F, 1F, 0.5F);
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(Items.BARRIER), left + yPos, top + yPos);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.getModelViewStack().popPose();
        }

        @Override
        public String getEntryValue()
        {
            return null;
        }

        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
        {
            tooltips.clear();
            Map<ModRecipeSerializer.RecipeDescriptors, String> recipeDescriptors = recipe.getRecipeMap();
            tooltips.add(recipe.getDisplayTitle().withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
            tooltips.add(new TextComponent(""));
            recipeDescriptors.forEach((tag, value) -> tooltips.add(new TextComponent(tag.getDisplay().getString()).withStyle(ChatFormatting.DARK_PURPLE).append(new TextComponent(" : ").withStyle(ChatFormatting.WHITE)).append(new TextComponent(value).withStyle(ChatFormatting.DARK_AQUA))));

            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
        }

        public KubeJSModifiedRecipe getRecipe()
        {
            return recipe;
        }
    }

    public static class StringEntry extends Entry
    {
        private final List<Component> tooltips;
        private final String resource;

        public StringEntry(String resource)
        {
            this.resource = resource;
            this.tooltips = new ArrayList<>();
        }

        @Override
        public void render(@Nonnull PoseStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            displayTruncatedString(matrixStack, resource, left, top, width, height, false, isMouseOver);
        }

        @Override
        public String getEntryValue()
        {
            return this.resource;
        }

        @Override
        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
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
        /**
         * Used to display all items if resource location is a tag
         */
        private int counter;

        public ResourceLocationEntry(ResourceLocation resourceLocation)
        {
            this.resourceLocation = resourceLocation;
            this.counter = 0;
        }

        public ResourceLocation getResourceLocation()
        {
            return resourceLocation;
        }

        @Override
        public void render(@Nonnull PoseStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            Item item = ForgeRegistries.ITEMS.getValue(getResourceLocation());
            ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(getResourceLocation()));

            if(item == Items.AIR && tag.size() > 0)
            {
                if(counter / 40 >= tag.size()) counter = 0;

                item = tag.stream().toList().get(counter / 40);
            }

            String displayStr = getString(width, resourceLocation.toString());
            displayTruncatedString(matrixStack, displayStr, left, top, width, height, true, isMouseOver);

            if(item != Items.AIR)
            {
                int yPos = height / 2 - 16 / 2;
                ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(item), left + yPos, top + yPos);
            }

            if(!Screen.hasShiftDown()) counter++;
        }

        @Override
        public String getEntryValue()
        {
            return this.resourceLocation.toString();
        }

        @Override
        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
        {
            if(!tooltips.isEmpty() && ClientUtils.getCurrentScreen() != null)
            {
                tooltips.clear();
                ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
            }
        }
    }
}
