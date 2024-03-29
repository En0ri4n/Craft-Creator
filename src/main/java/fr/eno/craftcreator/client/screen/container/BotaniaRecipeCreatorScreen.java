package fr.eno.craftcreator.client.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.base.ModRecipeCreators;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.client.screen.widgets.NumberDataFieldWidget;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.client.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import vazkii.botania.common.block.ModSubtiles;

import java.util.ArrayList;
import java.util.List;

import static fr.eno.craftcreator.base.ModRecipeCreators.*;

public class BotaniaRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<BotaniaRecipeCreatorContainer>
{
    private static final int MANA_FIELD = 0;

    public BotaniaRecipeCreatorScreen(BotaniaRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
    }

    @Override
    protected void initFields()
    {
        addNumberField(leftPos + imageWidth - 44, topPos + imageHeight / 2 - 16, 35, 100, 1);
    }

    @Override
    protected void initWidgets()
    {
    }

    @Override
    protected void retrieveExtraData()
    {
    }

    @Override
    protected RecipeInfos getExtraRecipeInfos(RecipeInfos recipeInfos)
    {
        for(NumberDataFieldWidget textField : dataFields)
        {
            if(textField.visible)
            {
                if(textField.getValue().isEmpty()) textField.setNumberValue(100, false);

                if(this.getCurrentRecipe().equals(ModRecipeCreators.PURE_DAISY))
                {
                    recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.TIME, textField.getIntValue(), false));
                    continue;
                }

                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.MANA, textField.getIntValue(), false));
            }
        }

        return recipeInfos;
    }

    @Override
    protected void updateGui()
    {
        int dataFieldY = topPos + imageHeight / 2 - 15;

        RecipeCreator currentRecipe = getCurrentRecipe();
        
        if(currentRecipe.is(MANA_INFUSION))
        {
            showDataField(0);
            setDataFieldPos(MANA_FIELD, leftPos + imageWidth - 44, dataFieldY);
            setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
        }
        else if(currentRecipe.is(ELVEN_TRADE, PURE_DAISY))
        {
            showDataField(0);
            setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2 - 35 / 2, dataFieldY);
            setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
        }
        else if(currentRecipe.is(BREWERY, TERRA_PLATE))
        {
            showDataField(0);
            setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2, dataFieldY);
            setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 5, this.topPos + 31);
        }
        else if(currentRecipe.is(PETAL_APOTHECARY))
        {
            setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 1, this.topPos + 31);
        }
        else if(currentRecipe.is(RUNIC_ALTAR))
        {
            showDataField(0);
            setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2, dataFieldY);
            setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 2, this.topPos + 31);
        }
    }

    @Override
    public void renderGui(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RecipeCreator currentRecipe = getCurrentRecipe();
        if(currentRecipe.equals(MANA_INFUSION) || currentRecipe.equals(ELVEN_TRADE) || currentRecipe.equals(RUNIC_ALTAR) || currentRecipe.equals(TERRA_PLATE) || currentRecipe.equals(BREWERY))
        {
            renderDataFieldAndTitle(MANA_FIELD, References.getTranslate("screen.botania_recipe_creator.field.mana"), matrixStack, mouseX, mouseY, partialTicks);
        }
        else if(currentRecipe.equals(PURE_DAISY))
        {
            int i = 0;
            for(int x = 0; x < 3; x++)
                for(int y = 0; y < 3; y++)
                {
                    if(i == 4)
                    {
                        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModSubtiles.pureDaisy), this.leftPos + 8 + 18 * x, this.topPos + 18 + y * 18);
                        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModSubtiles.pureDaisy), this.leftPos + 116 + 18 * x, this.topPos + 18 + y * 18);
                    }
                    else if(i != 5)
                    {
                        Slot inputSlot = PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, this.getMenu().slots).get(0);
                        Slot outputSlot = PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, this.getMenu().slots).get(1);
                        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(inputSlot.hasItem() ? inputSlot.getItem() : ItemStack.EMPTY, this.leftPos + 8 + 18 * x, this.topPos + 18 + y * 18);
                        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(outputSlot.hasItem() ? outputSlot.getItem() : ItemStack.EMPTY, this.leftPos + 116 + 18 * x, this.topPos + 18 + y * 18);
                    }

                    i++;
                }

            renderDataFieldAndTitle(MANA_FIELD, References.getTranslate("screen.botania_recipe_creator.field.time"), matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected List<PositionnedSlot> getTaggableSlots()
    {
        return SlotHelper.BOTANIA_SLOTS_INPUT;
    }

    @Override
    protected List<PositionnedSlot> getNbtTaggableSlots()
    {
        return new ArrayList<>();
    }
}