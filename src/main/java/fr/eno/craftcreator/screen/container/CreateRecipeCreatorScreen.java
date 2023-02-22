package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.screen.widgets.RecipeEntryWidget;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<CreateRecipeCreatorContainer>
{
    private RecipeEntryWidget inputWidget;

    public CreateRecipeCreatorScreen(CreateRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
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
        inputWidget = new RecipeEntryWidget(leftPos + 10, topPos + 30, 100, 100);
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
    protected void renderGui(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
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
    protected Item getRecipeIcon(ModRecipeCreator modRecipeCreator)
    {
        switch(modRecipeCreator)
        {
            case CRUSHING:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("create:crushing_wheel"));
            case CUTTING:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("create:cutting"));
            default:
                return Items.COMMAND_BLOCK;
        }
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
        inputWidget.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY)
    {
        inputWidget.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta)
    {
        inputWidget.mouseScrolled(pMouseX, pMouseY, pDelta);
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        inputWidget.keyPressed(keyCode, scanCode, modifiers);
        if(!inputWidget.isFocused())
            return super.keyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        inputWidget.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }
}
