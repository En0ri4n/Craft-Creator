package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.screen.widgets.NumberDataFieldWidget;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("unused")
public class BotaniaRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<BotaniaRecipeCreatorContainer>
{
    private static final int MANA_FIELD = 0;

    public BotaniaRecipeCreatorScreen(BotaniaRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getPos());
    }

    @Override
    protected void init()
    {
        super.init();

        addNumberField(leftPos + imageWidth - 44, topPos + imageHeight / 2 - 16, 35, 100, 1);

        updateScreen();
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        for(NumberDataFieldWidget textField : dataFields)
        {
            if(textField.visible)
            {
                if(textField.getText().isEmpty()) textField.setNumberValue(100);

                if(this.getCurrentRecipe().equals(ModRecipeCreator.PURE_DAISY))
                {
                    this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.TIME, textField.getIntValue()));
                    continue;
                }

                this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.MANA, textField.getIntValue()));
            }
        }

        return this.recipeInfos;
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION:
                showDataField(0);
                setDataFieldPos(MANA_FIELD, leftPos + imageWidth - 44, dataFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
                break;
            case ELVEN_TRADE:
            case PURE_DAISY:
                showDataField(0);
                setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2 - 35 / 2, dataFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
                break;
            case BREWERY:
            case TERRA_PLATE:
                showDataField(0);
                setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2, dataFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 5, this.topPos + 31);
                break;
            case PETAL_APOTHECARY:
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 1, this.topPos + 31);
                break;
            case RUNIC_ALTAR:
                showDataField(0);
                setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2, dataFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 2, this.topPos + 31);
                break;
        }
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION:
            case ELVEN_TRADE:
            case RUNIC_ALTAR:
            case TERRA_PLATE:
            case BREWERY:
            case PETAL_APOTHECARY:
                renderDataFieldAndTitle(MANA_FIELD, References.getTranslate("screen.botania_recipe_creator.field.mana"), matrixStack, mouseX, mouseY, partialTicks);
                break;
            case PURE_DAISY:
                int i = 0;
                for(int x = 0; x < 3; x++)
                    for(int y = 0; y < 3; y++)
                    {
                        if(i == 4)
                        {
                            ClientUtils.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModSubtiles.pureDaisy), this.leftPos + 8 + 18 * x, this.topPos + 18 + y * 18);
                            ClientUtils.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModSubtiles.pureDaisy), this.leftPos + 116 + 18 * x, this.topPos + 18 + y * 18);
                        }
                        else if(i != 5)
                        {
                            Slot inputSlot = PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, this.getContainer().inventorySlots).get(0);
                            Slot outputSlot = PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, this.getContainer().inventorySlots).get(1);
                            ClientUtils.getItemRenderer().renderItemAndEffectIntoGUI(inputSlot.getHasStack() ? inputSlot.getStack() : ItemStack.EMPTY, this.leftPos + 8 + 18 * x, this.topPos + 18 + y * 18);
                            ClientUtils.getItemRenderer().renderItemAndEffectIntoGUI(outputSlot.getHasStack() ? outputSlot.getStack() : ItemStack.EMPTY, this.leftPos + 116 + 18 * x, this.topPos + 18 + y * 18);
                        }

                        i++;
                    }

                renderDataFieldAndTitle(MANA_FIELD, References.getTranslate("screen.botania_recipe_creator.field.time"), matrixStack, mouseX, mouseY, partialTicks);
                break;
        }

        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected List<PositionnedSlot> getTaggeableSlots()
    {
        return SlotHelper.BOTANIA_SLOTS;
    }

    @Override
    protected Item getRecipeIcon()
    {
        switch(getCurrentRecipe())
        {
            case MANA_INFUSION:
                return ModBlocks.creativePool.asItem();
            case ELVEN_TRADE:
                return ModBlocks.livingwoodGlimmering.asItem();
            case BREWERY:
                return ModBlocks.brewery.asItem();
            case PETAL_APOTHECARY:
                return ModBlocks.defaultAltar.asItem();
            case RUNIC_ALTAR:
                return ModBlocks.runeAltar.asItem();
            case TERRA_PLATE:
                return ModItems.spark.asItem();
            default:
                return Items.AIR;
        }
    }

    @Override
    protected PairValues<Integer, Integer> getIconPos()
    {
        switch(getCurrentRecipe())
        {
            case MANA_INFUSION:
            case ELVEN_TRADE:
                return PairValues.create(this.leftPos + imageWidth / 2 - 8, this.topPos + 14);
            case BREWERY:
                return PairValues.create(this.leftPos + 34, this.topPos + 33);
            case PETAL_APOTHECARY:
                return PairValues.create(this.leftPos + Math.floorDiv(imageWidth, 2) + 21, this.topPos + 4);
            case RUNIC_ALTAR:
                return PairValues.create(this.leftPos + Math.floorDiv(imageWidth, 2) + 16, this.topPos + 4);
            case TERRA_PLATE:
                return PairValues.create(this.leftPos + 34, this.topPos + 34);
            default:
                return super.getIconPos();
        }
    }
}