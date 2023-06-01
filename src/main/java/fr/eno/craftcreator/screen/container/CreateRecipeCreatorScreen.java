package fr.eno.craftcreator.screen.container;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.screen.widgets.RecipeEntryWidget;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<CreateRecipeCreatorContainer>
{
    private RecipeEntryWidget inputWidget;
    private BlockPos tilePos;

    public CreateRecipeCreatorScreen(CreateRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
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

    }

    @Override
    protected void initWidgets()
    {
        int gapX = 10;
        int gapY = 22;
        int widgetHeight = 110;

        inputWidget = new RecipeEntryWidget(getCurrentRecipe(), tilePos, PositionnedSlot.getSlotsFor(SlotHelper.CUTTING_SLOTS_INPUT, getMenu().getContainerSlots()).get(0), leftPos + gapX, topPos + gapY, imageWidth / 2 - 2 * gapX, widgetHeight);
    }

    @Override
    protected void retrieveExtraData()
    {

    }

    @Override
    protected RecipeInfos getExtraRecipeInfos(RecipeInfos recipeInfos)
    {
        return recipeInfos;
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);

        if(dataName.startsWith("inputs"))
        {
            PairValues<String, List<JsonObject>> inputs = (PairValues<String, List<JsonObject>>) data;
            inputWidget.setEntries(inputs.getSecondValue());
        }
    }

    @Override
    protected void updateGui()
    {
        this.inputWidget.refresh(getCurrentRecipe());
        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 21, this.topPos + this.imageHeight / 2 + 8);
    }

    @Override
    protected void renderGui(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(inputWidget != null)
            inputWidget.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int pMouseX, int pMouseY)
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
    public void tick()
    {
        super.tick();

        if(inputWidget != null)
            inputWidget.tick();
    }

    @Override
    protected void renderTooltip(MatrixStack poseStack, int mouseX, int mouseY)
    {
        if(!inputWidget.isFocused())
            super.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(!inputWidget.isFocused()) super.mouseClicked(mouseX, mouseY, button);
        inputWidget.mouseClicked(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY)
    {
        super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        inputWidget.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        return true;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta)
    {
        super.mouseScrolled(pMouseX, pMouseY, pDelta);
        inputWidget.mouseScrolled(pMouseX, pMouseY, pDelta);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if(!inputWidget.isFocused()) return super.keyPressed(keyCode, scanCode, modifiers);
        inputWidget.keyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if(!inputWidget.isFocused()) return super.charTyped(codePoint, modifiers);
        inputWidget.charTyped(codePoint, modifiers);
        return true;
    }
}
