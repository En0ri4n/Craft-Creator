package fr.eno.craftcreator.client.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import fr.eno.craftcreator.client.screen.container.base.FlatMultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.client.screen.widgets.buttons.EnumButton;
import fr.eno.craftcreator.client.screen.widgets.buttons.pressable.NullPressable;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.recipes.utils.EntryType;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;

import static fr.eno.craftcreator.base.ModRecipeCreators.*;

public class CreateRecipeCreatorScreen extends FlatMultiScreenModRecipeCreatorScreen<CreateRecipeCreatorContainer>
{
    private EnumButton<HeatCondition> heatButton;

    public CreateRecipeCreatorScreen(CreateRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void initFields()
    {
        addNumberField(0, 0, 90, 100, 1);
    }

    @Override
    protected void initWidgets()
    {
        super.initWidgets();

        addButton(heatButton = new EnumButton<>(Arrays.asList(HeatCondition.values()), leftPos + 10, topPos + imageHeight / 2 + 10, 75, 20, 0x80800000, NullPressable.get()));
    }

    @Override
    protected void retrieveExtraData() {}

    @Override
    protected RecipeInfos getExtraRecipeInfos(RecipeInfos recipeInfos)
    {
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("processing_time", getDataField(0).getIntValue(), false));
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("heat_requirement", heatButton.getSelected().ordinal(), false));
        return recipeInfos;
    }

    @Override
    protected void updateGui()
    {
        super.updateGui();

        heatButton.visible = false;

        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 21, this.topPos + this.imageHeight / 2 + 8);

        if(getCurrentRecipe().is(CRUSHING, CUTTING))
        {
            showDataField(0);
            setDataFieldValue(100, false, 0);
            setDataFieldPos(0, leftPos + 10, topPos + imageHeight / 2 + 20);

            inputWidget.setHasCount(false);
            inputWidget.setHasChance(false);
        }
        else if(getCurrentRecipe().is(MIXING))
        {
            inputWidget.setHasCount(false);
            inputWidget.setHasFluidAmount(true);
            inputWidget.setHasChance(false);

            outputWidget.setHasChance(false);
            outputWidget.setHasCount(true);

            heatButton.visible = true;
        }
        else if(getCurrentRecipe().is(MILLING))
        {
            showDataField(0);
            setDataFieldValue(100, false, 0);
            setDataFieldPos(0, leftPos + 10, topPos + imageHeight / 2 + 20);

            inputWidget.setHasCount(false);
            inputWidget.setHasChance(false);
            inputWidget.setAllowedTypes(EntryType.ITEM, EntryType.TAG);

            outputWidget.setHasCount(true);
            outputWidget.setHasChance(true);
            outputWidget.setAllowedTypes(EntryType.ITEM);
        }
        else if(getCurrentRecipe().is(COMPACTING, FILLING))
        {
            inputWidget.setHasCount(false);
            inputWidget.setHasFluidAmount(true);
            inputWidget.setHasChance(false);
            inputWidget.setAllowedTypes(EntryType.ITEM, EntryType.TAG, EntryType.FLUID);

            outputWidget.setHasCount(false);
            outputWidget.setHasChance(false);
            outputWidget.setAllowedTypes(EntryType.ITEM);
        }
        else if(getCurrentRecipe().is(PRESSING))
        {
            inputWidget.setHasCount(false);
            inputWidget.setHasChance(false);
            inputWidget.setAllowedTypes(EntryType.ITEM, EntryType.TAG);

            outputWidget.setHasCount(false);
            outputWidget.setHasChance(false);
            outputWidget.setAllowedTypes(EntryType.ITEM);
        }
        else if(getCurrentRecipe().is(SPLASHING))
        {
            inputWidget.setHasCount(false);
            inputWidget.setHasChance(false);
            inputWidget.setAllowedTypes(EntryType.ITEM, EntryType.TAG);

            outputWidget.setHasCount(true);
            outputWidget.setHasChance(true);
            outputWidget.setAllowedTypes(EntryType.ITEM);
        }
        else if(getCurrentRecipe().is(EMPTYING))
        {
            inputWidget.setHasCount(false);
            inputWidget.setHasChance(false);
            inputWidget.setAllowedTypes(EntryType.ITEM, EntryType.TAG);

            outputWidget.setHasCount(false);
            outputWidget.setHasFluidAmount(true);
            outputWidget.setHasChance(false);
            outputWidget.setAllowedTypes(EntryType.ITEM, EntryType.FLUID);
        }
    }

    @Override
    protected void renderGui(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(getCurrentRecipe().is(CRUSHING, CUTTING, MILLING))
        {
            renderDataFieldTitle(0, new StringTextComponent("Processing Time :"), matrixStack);
        }

        super.renderGui(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
