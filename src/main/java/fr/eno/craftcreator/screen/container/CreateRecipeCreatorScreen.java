package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.screen.widgets.RecipeEntryWidget;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<CreateRecipeCreatorContainer>
{
    private RecipeEntryWidget inputWidget;

    public CreateRecipeCreatorScreen(CreateRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
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
        int gapY = 30;
        int widgetHeight = 110;

        inputWidget = new RecipeEntryWidget(leftPos + gapX, topPos + gapY, imageWidth / 2 - 2 * gapX, widgetHeight);
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
    protected void updateGui()
    {
        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 21, this.topPos + this.imageHeight / 2 + 8);
    }

    @Override
    protected void renderGui(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        inputWidget.render(matrixStack, mouseX, mouseY, partialTicks);
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
