package fr.eno.craftcreator.screen.container;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.base.ModRecipeCreators;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.screen.widgets.RecipeEntryWidget;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<CreateRecipeCreatorContainer>
{
    private RecipeEntryWidget inputWidget;
    private RecipeEntryWidget outputWidget;
    private final BlockPos tilePos;

    public CreateRecipeCreatorScreen(CreateRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
        this.tilePos = screenContainer.getTile().getBlockPos();
        this.guiTextureSize = 256;
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void initFields()
    {
        addNumberField(0, 0, 90, 100, 1);
    }

    @Override
    protected void initWidgets()
    {
        int gapX = 10;
        int gapY = 22;
        int widgetHeight = 110;
        int widgetWidth = imageWidth / 2 - 2 * gapX;

        inputWidget = new RecipeEntryWidget(getCurrentRecipe(), tilePos, PositionnedSlot.getSlotsFor(getCurrentRecipe().getSlots(), getMenu().getContainerSlots()).get(0), leftPos + gapX, topPos + gapY, widgetWidth, widgetHeight, false, 1);
        outputWidget = new RecipeEntryWidget(getCurrentRecipe(), tilePos, PositionnedSlot.getSlotsFor(getCurrentRecipe().getSlots(), getMenu().getContainerSlots()).get(0), leftPos + guiTextureSize - gapX - widgetWidth, topPos + gapY, widgetWidth, widgetHeight, true, 100);
    }

    @Override
    protected void retrieveExtraData()
    {

    }

    @Override
    protected RecipeInfos getExtraRecipeInfos(RecipeInfos recipeInfos)
    {
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("processing_time", getDataField(0).getIntValue(), false));
        return recipeInfos;
    }

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
        this.inputWidget.refresh(getCurrentRecipe());
        this.outputWidget.refresh(getCurrentRecipe());
        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 21, this.topPos + this.imageHeight / 2 + 8);

        if(getCurrentRecipe().is(ModRecipeCreators.CRUSHING))
        {
            showDataField(0);
            setDataFieldValue(100, false, 0);
            setDataFieldPos(0, leftPos + 10, topPos + imageHeight / 2 + 20);

            inputWidget.setHasCount(false);
            inputWidget.setHasChance(false);
            outputWidget.setHasTag(false);
        }
    }

    @Override
    protected void renderGui(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(getCurrentRecipe().is(ModRecipeCreators.CRUSHING))
        {
            renderDataFieldTitle(0, new TextComponent("Processing Time :"), matrixStack);
        }

        if(inputWidget != null)
            inputWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        if(outputWidget != null)
            outputWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        if(inputWidget != null)
            inputWidget.renderDropdown(matrixStack, mouseX, mouseY, partialTicks);
        if(outputWidget != null)
            outputWidget.renderDropdown(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int pMouseX, int pMouseY)
    {
        super.renderLabels(matrixStack, pMouseX, pMouseY);
        renderSideTitles(matrixStack);
    }

    @Override
    public int getArrowXPos(boolean right)
    {
        return right ? super.getArrowXPos(true) - 40 : super.getArrowXPos(false) + 40;
    }

    @Override
    protected List<PositionnedSlot> getTaggableSlots()
    {
        return SlotHelper.CREATE_SLOTS_INPUT;
    }

    @Override
    protected List<PositionnedSlot> getNbtTaggableSlots()
    {
        return new ArrayList<>();
    }

    @Override
    public void containerTick()
    {
        super.tick();

        if(inputWidget != null)
        {
            outputWidget.setCanUseWidget(!inputWidget.isFocused());
            inputWidget.tick();
        }

        if(outputWidget != null)
        {
            inputWidget.setCanUseWidget(!outputWidget.isFocused());
            outputWidget.tick();
        }
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY)
    {
        if(!inputWidget.isFocused() && !outputWidget.isFocused())
            super.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(!inputWidget.isFocused() && !outputWidget.isFocused()) super.mouseClicked(mouseX, mouseY, button);
        inputWidget.mouseClicked(mouseX, mouseY, button);
        outputWidget.mouseClicked(mouseX, mouseY, button);
        return true;
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
