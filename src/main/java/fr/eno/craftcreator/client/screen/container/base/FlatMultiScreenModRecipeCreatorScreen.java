package fr.eno.craftcreator.client.screen.container.base;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.screen.widgets.RecipeEntryWidget;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.PairValues;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public abstract class FlatMultiScreenModRecipeCreatorScreen<T extends CommonContainer> extends MultiScreenModRecipeCreatorScreen<T>
{
    protected RecipeEntryWidget inputWidget;
    protected RecipeEntryWidget outputWidget;
    private final BlockPos tilePos;

    public FlatMultiScreenModRecipeCreatorScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
        this.tilePos = screenContainer.getTile().getBlockPos();
        this.guiTextureSize = 256;
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void initWidgets()
    {
        int gapX = 10;
        int gapY = 22;
        int widgetHeight = 111;
        int widgetWidth = imageWidth / 2 - 2 * gapX;

        inputWidget = new RecipeEntryWidget(getCurrentRecipe(), tilePos,  leftPos + gapX, topPos + gapY, widgetWidth, widgetHeight, false, -1);
        outputWidget = new RecipeEntryWidget(getCurrentRecipe(), tilePos, leftPos + guiTextureSize - gapX - widgetWidth, topPos + gapY, widgetWidth, widgetHeight, true, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);

        if(dataName.startsWith("inputs"))
        {
            PairValues<String, List<JsonObject>> inputs = (PairValues<String, List<JsonObject>>) data;
            if(inputs.getFirstValue().equals(getCurrentRecipe().getRecipeTypeLocation().getPath()))
                inputWidget.setEntries(inputs.getSecondValue());
        }
        else if(dataName.startsWith("outputs"))
        {
            PairValues<String, List<JsonObject>> outputs = (PairValues<String, List<JsonObject>>) data;
            if(outputs.getFirstValue().equals(getCurrentRecipe().getRecipeTypeLocation().getPath()))
                outputWidget.setEntries(outputs.getSecondValue());
        }
    }

    @Override
    protected void updateGui()
    {
        inputWidget.refresh(getCurrentRecipe());
        outputWidget.refresh(getCurrentRecipe());
        inputWidget.setMaxSize(getCurrentRecipe().getMaxInputSize());
        outputWidget.setMaxSize(getCurrentRecipe().getMaxOutputSize());
    }

    @Override
    protected void renderGui(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(inputWidget != null)
            inputWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        if(outputWidget != null)
            outputWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        if(outputWidget != null)
            outputWidget.renderDropdown(matrixStack, mouseX, mouseY, partialTicks);
        if(inputWidget != null)
            inputWidget.renderDropdown(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int pMouseX, int pMouseY)
    {
        super.renderLabels(matrixStack, pMouseX, pMouseY);
        renderSideTitles(matrixStack);
    }

    @Override
    protected void renderSideTitles(MatrixStack matrixStack)
    {
        // Render Labels
        IFormattableTextComponent inputLabel = References.getTranslate("screen.recipe_creator.label.input_counter", TextFormatting.GRAY + String.valueOf(getCurrentRecipe().getMaxInputSize() == -1 ? "∞" : getCurrentRecipe().getMaxInputSize()));
        ITextComponent ouputLabel = References.getTranslate("screen.recipe_creator.label.output_counter", TextFormatting.GRAY + String.valueOf(getCurrentRecipe().getMaxOutputSize() == -1 ? "∞" : getCurrentRecipe().getMaxOutputSize()));
        Screen.drawString(matrixStack, this.font, inputLabel, this.imageWidth / 4 - font.width(inputLabel.getString()) / 2, 8, 0xFFFFFFFF);
        Screen.drawString(matrixStack, this.font, ouputLabel, this.imageWidth / 4 * 3 - font.width(ouputLabel.getString()) / 2, 8, 0xFFFFFFFF);
    }

    @Override
    protected List<PositionnedSlot> getTaggableSlots()
    {
        return new ArrayList<>();
    }

    @Override
    protected List<PositionnedSlot> getNbtTaggableSlots()
    {
        return new ArrayList<>();
    }

    @Override
    public int getArrowXPos(boolean right)
    {
        return right ? super.getArrowXPos(true) - 40 : super.getArrowXPos(false) + 40;
    }

    @Override
    public void tick()
    {
        super.tick();

        if(outputWidget != null && inputWidget != null)
        {
            outputWidget.setCanUseWidget(!inputWidget.isFocused());
            inputWidget.setCanUseWidget(!outputWidget.isFocused());
            inputWidget.tick();
            outputWidget.tick();
        }
    }

    @Override
    protected void renderTooltip(MatrixStack poseStack, int mouseX, int mouseY)
    {
        if(inputWidget != null)
            inputWidget.renderTooltip(poseStack, mouseX, mouseY, width, height);
        if(outputWidget != null)
            outputWidget.renderTooltip(poseStack, mouseX, mouseY, width, height);

        if(inputWidget != null && outputWidget != null)
            if(!inputWidget.isFocused() && !outputWidget.isFocused())
                super.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        inputWidget.mouseClicked(mouseX, mouseY, button);
        outputWidget.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY)
    {
        super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        inputWidget.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        outputWidget.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        return true;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta)
    {
        super.mouseScrolled(pMouseX, pMouseY, pDelta);
        inputWidget.mouseScrolled(pMouseX, pMouseY, pDelta);
        outputWidget.mouseScrolled(pMouseX, pMouseY, pDelta);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if(!inputWidget.isFocused() && !outputWidget.isFocused()) return super.keyPressed(keyCode, scanCode, modifiers);
        inputWidget.keyPressed(keyCode, scanCode, modifiers);
        outputWidget.keyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if(!inputWidget.isFocused() && !outputWidget.isFocused()) return super.charTyped(codePoint, modifiers);
        inputWidget.charTyped(codePoint, modifiers);
        outputWidget.charTyped(codePoint, modifiers);
        return true;
    }
}
