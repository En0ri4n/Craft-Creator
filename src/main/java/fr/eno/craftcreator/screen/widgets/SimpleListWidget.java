package fr.eno.craftcreator.screen.widgets;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.jsserializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.kubejs.utils.ModDispatcher;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.utils.Callable;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings({"deprecation"})
public class SimpleListWidget extends ObjectSelectionList<SimpleListWidget.Entry>
{
    private static final ResourceLocation BACKGROUND_TILE = References.getLoc("textures/gui/background_tile.png");
    private final Component title;
    private final int titleBoxHeight;
    private final int scrollBarWidth;
    private boolean canHaveSelected;
    private final boolean hasTitleBox;
    private Callable<Entry> onSelected;
    private boolean isVisible;
    private Callable<Entry> onDelete;
    private Entry hoveredEntry;

    public SimpleListWidget(Minecraft mcIn, int leftIn, int topIn, int widthIn, int heightIn, int slotHeightIn, int titleBoxHeight, int scrollBarWidth, Component titleIn, @Nullable Callable<Entry> onDelete)
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
        this.title = new TextComponent("");
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
    public void render(@Nonnull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        if(!this.isVisible) return;

        if(hasTitleBox)
        {
            Screen.fill(pPoseStack, this.x0, this.y0 - titleBoxHeight, this.x0 + this.width, this.y0, 0xf2c3a942);
            Screen.drawCenteredString(pPoseStack, minecraft.font, this.title, this.x0 + this.width / 2, this.y0 - this.titleBoxHeight / 2 - minecraft.font.lineHeight / 2, 0xFFFFFF);
            //this.renderBackground(pPoseStack);
        }

        this.hoveredEntry = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        // Render background
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        Entry hovered = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;
        if(true)
        { // Background
            RenderSystem.setShaderTexture(0, BACKGROUND_TILE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int alpha = 100;
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(this.x0, this.y1, 0.0D).uv((float) this.x0 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
            bufferbuilder.vertex(this.x1, this.y1, 0.0D).uv((float) this.x1 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
            bufferbuilder.vertex(this.x1, this.y0, 0.0D).uv((float) this.x1 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
            bufferbuilder.vertex(this.x0, this.y0, 0.0D).uv((float) this.x0 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, alpha).endVertex();
            tesselator.end();
        }

        int rowLeft = this.getRowLeft();
        int k = this.y0 + 4 - (int) this.getScrollAmount();

        this.renderList(pPoseStack, rowLeft, k, pMouseX, pMouseY, pPartialTick);

        if(false)
        { // Bottom and top
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(519);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(this.x0, this.y0, -100.0D).uv(0.0F, (float) this.y0 / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((this.x0 + this.width), this.y0, -100.0D).uv((float) this.width / 32.0F, (float) this.y0 / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((this.x0 + this.width), 0.0D, -100.0D).uv((float) this.width / 32.0F, 0.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex(this.x0, 0.0D, -100.0D).uv(0.0F, 0.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex(this.x0, this.height, -100.0D).uv(0.0F, (float) this.height / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((this.x0 + this.width), this.height, -100.0D).uv((float) this.width / 32.0F, (float) this.height / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((this.x0 + this.width), this.y1, -100.0D).uv((float) this.width / 32.0F, (float) this.y1 / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex(this.x0, this.y1, -100.0D).uv(0.0F, (float) this.y1 / 32.0F).color(64, 64, 64, 255).endVertex();
            tesselator.end();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.vertex(this.x0, (this.y0 + 4), 0.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.vertex(this.x1, (this.y0 + 4), 0.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.vertex(this.x1, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(this.x0, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(this.x0, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(this.x1, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(this.x1, (this.y1 - 4), 0.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.vertex(this.x0, (this.y1 - 4), 0.0D).color(0, 0, 0, 0).endVertex();
            tesselator.end();
        }

        int scrollbarPosition = this.getScrollbarPosition();
        int j = scrollbarPosition + 6;
        int maxScroll = this.getMaxScroll();
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
            bufferbuilder.vertex(scrollbarPosition, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(j, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(j, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, (i2 + l1), 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(j, (i2 + l1), 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(j, i2, 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, i2, 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, (i2 + l1 - 1), 0.0D).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((j - 1), (i2 + l1 - 1), 0.0D).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((j - 1), i2, 0.0D).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex(scrollbarPosition, i2, 0.0D).color(192, 192, 192, 255).endVertex();
            tesselator.end();
        }

        this.renderDecorations(pPoseStack, pMouseX, pMouseY);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @SuppressWarnings("deprecation")
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
                    RenderSystem.setShaderColor(f, f, f, 1.0F);
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
                    bufferbuilder.vertex(l1, (i1 + j1 + 2), 0.0D).endVertex();
                    bufferbuilder.vertex(i2, (i1 + j1 + 2), 0.0D).endVertex();
                    bufferbuilder.vertex(i2, (i1 - 2), 0.0D).endVertex();
                    bufferbuilder.vertex(l1, (i1 - 2), 0.0D).endVertex();
                    tesselator.end();
                    RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
                    bufferbuilder.vertex((l1 + 1), (i1 + j1 + 1), 0.0D).endVertex();
                    bufferbuilder.vertex((i2 - 1), (i1 + j1 + 1), 0.0D).endVertex();
                    bufferbuilder.vertex((i2 - 1), (i1 - 1), 0.0D).endVertex();
                    bufferbuilder.vertex((l1 + 1), (i1 - 1), 0.0D).endVertex();
                    tesselator.end();
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
        return super.keyPressed(keyCode, scanCode, modifiers);
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

    public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
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

    public static abstract class Entry extends ObjectSelectionList.Entry<SimpleListWidget.Entry>
    {
        protected Minecraft minecraft = Minecraft.getInstance();
        protected final List<Component> tooltips;

        protected Entry()
        {
            this.tooltips = new ArrayList<>();
        }

        public abstract String toString();

        public abstract void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY);
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
            if(displayStr.contains("/")) displayStr = displayStr.substring(displayStr.indexOf('/') + 1);

            displayStr = StringEntry.getString(width, displayStr, minecraft.font.width(displayStr));

            int color = isMouseOver ? 0xF1f115 : 0xFFFFFF;

            Screen.drawString(matrixStack, minecraft.font, displayStr, left + 16 + 5, (top + height / 2 - minecraft.font.lineHeight / 2), color);

            ItemStack item = ModDispatcher.getOneOutput(recipe);

            matrixStack.pushPose();
            float scale = 1F;
            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderAndDecorateItem(item, (int) ((left + 1) / scale), (int) ((top + yPos) / scale));
            matrixStack.popPose();
        }

        @Override
        public String toString()
        {
            return recipe.getId().toString();
        }

        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
        {
            tooltips.clear();
            Multimap<String, ResourceLocation> input = RecipeFileUtils.getInput(recipe);
            Map<String, ResourceLocation> output = ModDispatcher.getOutput(recipe);

            tooltips.add(new TextComponent(this.recipe.getId().toString()).withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
            tooltips.add(new TextComponent(""));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.input"));
            input.forEach((typeName, loc) -> tooltips.add(new TextComponent(typeName).withStyle(typeName.equalsIgnoreCase("item") ? ChatFormatting.AQUA : typeName.equalsIgnoreCase("block") ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_PURPLE).append(new TextComponent(" : ")).append(new TextComponent(loc.toString()).withStyle(ChatFormatting.DARK_AQUA))));

            tooltips.add(new TextComponent(""));
            tooltips.add(References.getTranslate("screen.widget.simple_list.tooltip.output"));
            output.forEach((typeName, loc) -> tooltips.add(new TextComponent(typeName).withStyle(typeName.equalsIgnoreCase("item") ? ChatFormatting.AQUA : typeName.equalsIgnoreCase("block") ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_PURPLE).append(new TextComponent(" : ")).append(new TextComponent(loc.toString()).withStyle(ChatFormatting.DARK_AQUA))));

            PairValue<String, Integer> param = RecipeFileUtils.getParam(recipe);

            if(param != null)
            {
                tooltips.add(new TextComponent(""));
                tooltips.add(new TextComponent(param.getFirstValue()).withStyle(ChatFormatting.DARK_GRAY).append(new TextComponent(" : ")).append(new TextComponent(String.valueOf(param.getSecondValue())).withStyle(ChatFormatting.GRAY)));
            }

            minecraft.screen.renderTooltip(matrixStack, tooltips, Optional.empty(), mouseX, mouseY);
        }

        public Recipe<?> getRecipe()
        {
            return recipe;
        }

        @Override
        public @NotNull Component getNarration()
        {
            return new TextComponent(this.recipe.getId().toString());
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
        public void render(@Nonnull PoseStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            String name = recipe.getRecipeMap().values().stream().findFirst().get();
            String displayStr = name.substring(name.indexOf(':') + 1);
            if(displayStr.contains("/")) displayStr = displayStr.substring(displayStr.indexOf('/') + 1);

            displayStr = StringEntry.getString(width, displayStr, minecraft.font.width(displayStr));
            int color = isMouseOver ? 0xF1f115 : 0xFFFFFF;

            Screen.drawString(matrixStack, minecraft.font, displayStr, left + 16 + 5, (top + height / 2 - minecraft.font.lineHeight / 2), color);

            Item item = Items.BARRIER;

            if(recipe.getOutputItem() != null)
            {
                try
                {
                    item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(recipe.getOutputItem()));
                }
                catch(Exception ignored)
                {
                }
            }
            else if(recipe.getInputItem() != null)
            {
                try
                {
                    item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(recipe.getInputItem()));
                }
                catch(Exception ignored)
                {
                }
            }

            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(item), left + yPos, top + yPos);
        }

        @Override
        public String toString()
        {
            return null;
        }

        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
        {
            tooltips.clear();
            Map<ModRecipesJSSerializer.RecipeDescriptors, String> recipeDescriptors = recipe.getRecipeMap();

            tooltips.add(new TextComponent(recipeDescriptors.values().stream().findFirst().get()).withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
            tooltips.add(new TextComponent(""));
            recipeDescriptors.forEach((tag, value) -> tooltips.add(new TextComponent(tag.toString()).withStyle(ChatFormatting.DARK_PURPLE).append(new TextComponent(" : ")).append(new TextComponent(value).withStyle(ChatFormatting.DARK_AQUA))));

            this.minecraft.screen.renderTooltip(matrixStack, tooltips, Optional.empty(), mouseX, mouseY);
        }

        public ModifiedRecipe getRecipe()
        {
            return recipe;
        }

        @Override
        public Component getNarration()
        {
            return new TextComponent(this.recipe.getRecipeMap().values().stream().findFirst().get());
        }
    }

    public static class StringEntry extends Entry
    {
        private List<Component> tooltips;
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
        public void render(@Nonnull PoseStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            //Screen.fill(matrixStack, left, top, left + width, top + height, 0xFFFFFF);

            String displayStr = resource;
            displayStr = getString(width, displayStr, minecraft.font.width(displayStr));

            int color = isMouseOver ? 0xF1f115 : 0xFFFFFF;
            matrixStack.pushPose();
            float scale = 1F;
            matrixStack.scale(scale, scale, scale);
            Screen.drawCenteredString(matrixStack, minecraft.font, displayStr, (int) ((left + width / 2) / scale), (int) ((top + height / 2 - (minecraft.font.lineHeight * scale) / 2) / scale) + 2, color);
            matrixStack.popPose();
        }

        private static String getString(int width, String displayStr, int stringWidth)
        {
            if(stringWidth > width - (16 + 5))
            {
                int letters = displayStr.toCharArray().length;
                int string_width = stringWidth;
                int letter_width = string_width / letters;
                int def_width = width - (16 + 5);
                int width_much = string_width - def_width;
                int letters_to_remove = width_much / letter_width;
                displayStr = displayStr.substring(0, displayStr.length() - letters_to_remove - 3) + "...";
            }
            return displayStr;
        }

        @Override
        public String toString()
        {
            return this.resource;
        }

        @Override
        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
        {
            if(!tooltips.isEmpty() && minecraft.screen != null)
            {
                tooltips.clear();
                minecraft.screen.renderTooltip(matrixStack, tooltips, Optional.empty(), mouseX, mouseY);
            }
        }

        public StringEntry setTooltips(List<Component> tooltips)
        {
            this.tooltips = tooltips;
            return this;
        }

        @Override
        public Component getNarration()
        {
            return new TextComponent(this.resource);
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
        public void render(@Nonnull PoseStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            //Screen.fill(matrixStack, left, top, left + width, top + height, 0xFFFFFF);

            String displayStr = resourceLocation.toString();
            //int k = 16 - 32;
            if(minecraft.font.width(displayStr) > width - (16 + 5))
            {
                int letters = displayStr.toCharArray().length;
                int string_width = minecraft.font.width(displayStr);
                int letter_width = string_width / letters;
                int def_width = width - (16 + 5);
                int width_much = string_width - def_width;
                int letters_to_remove = width_much / letter_width;
                displayStr = displayStr.substring(0, displayStr.length() - letters_to_remove - 3) + "...";
            }

            int color = isMouseOver ? 0xF1f115 : 0xFFFFFF;
            matrixStack.pushPose();
            float scale = 1.1F;
            matrixStack.scale(scale, scale, scale);
            Screen.drawCenteredString(matrixStack, minecraft.font, displayStr, (int) ((left + width / 2) / scale), (int) ((top + height / 2 - (minecraft.font.lineHeight * scale) / 2) / scale), color);
            matrixStack.popPose();

            Item item = ForgeRegistries.ITEMS.getValue(getResourceLocation());

            int yPos = height / 2 - 16 / 2;
            minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(item == null ? Items.BARRIER : item), left + yPos, top + yPos);
        }

        @Override
        public String toString()
        {
            return this.resourceLocation.toString();
        }

        @Override
        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
        {
            if(!tooltips.isEmpty() && minecraft.screen != null)
            {
                tooltips.clear();
                minecraft.screen.renderTooltip(matrixStack, tooltips, Optional.empty(), mouseX, mouseY);
            }
        }

        @Override
        public Component getNarration()
        {
            return new TextComponent(this.resourceLocation.toString());
        }
    }
}
