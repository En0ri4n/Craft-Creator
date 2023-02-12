package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ThermalRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<ThermalRecipeCreatorContainer>
{
    private static final int ENERGY_FIELD = 0, EXPERIENCE_FIELD = 1, ENERGY_MOD_FIELD = 2, RESIN_FIELD = 3, CHANCES_FIELD = 3, FLUID = 3, WATER_MOD = 7;

    public ThermalRecipeCreatorScreen(ThermalRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
        this.guiTextureSize = 384;
        this.imageWidth = 296;
        this.imageHeight = 256;
    }

    @Override
    protected void init()
    {
        super.init();

        addNumberField(leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, -1, 8);

        updateScreen();
    }

    @Override
    public int getArrowXPos(boolean right)
    {
        return right ? super.getArrowXPos(true) - 60 : super.getArrowXPos(false) + 60;
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        super.getRecipeInfos();

        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.ENERGY_MOD, getNumberField(ENERGY_MOD_FIELD).getDoubleValue()));
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.EXPERIENCE, getNumberField(EXPERIENCE_FIELD).getDoubleValue()));
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.ENERGY, getNumberField(ENERGY_FIELD).getIntValue()));

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR:
                this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.RESIN_AMOUNT, getNumberField(RESIN_FIELD).getIntValue()));
            case PULVERIZER:
            case SAWMILL:
            case SMELTER:
            case INSOLATOR:
                for(int i = 0; i < 4; i++)
                    this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("chance_" + i, getNumberField(CHANCES_FIELD + i).getDoubleValue()));
                if(getCurrentRecipe() == ModRecipeCreator.INSOLATOR)
                    this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.WATER_MOD, getNumberField(WATER_MOD).getDoubleValue()));
                break;
            case CENTRIFUGE:
            case CHILLER:
                this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.FLUID_AMOUNT, getNumberField(FLUID).getIntValue()));
                break;
        }

        return this.recipeInfos;
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        showDataField(ENERGY_FIELD, EXPERIENCE_FIELD, ENERGY_MOD_FIELD);
        setDataField(ENERGY_FIELD, this.leftPos + 8, this.topPos + this.imageHeight / 2 + 23, 45, 100);
        setDataField(EXPERIENCE_FIELD, this.leftPos + this.imageWidth - 73 - 8, this.topPos + this.imageHeight / 2 + 23, 73, 0.1D);
        setDataField(ENERGY_MOD_FIELD, this.leftPos + 65, this.topPos + this.imageHeight / 2 + 23, 45, 1D);

        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - this.executeButton.getWidth() / 2, this.topPos + this.imageHeight / 2 - this.executeButton.getHeight() / 2 + 22);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR:
                showDataField(RESIN_FIELD);
                setDataField(RESIN_FIELD, leftPos + imageWidth / 4 * 3 - 12, topPos + imageHeight / 3 - 13, 55, 25);
                break;
            case INSOLATOR:
                showDataField(7);
                setDataField(7, this.leftPos + 48, this.topPos + this.imageHeight / 3 - 15, 45, 1D);
            case SAWMILL:
            case PULVERIZER:
            case SMELTER:
                for(int i = 0; i < 4; i++)
                {
                    showDataField(CHANCES_FIELD + i);
                    setDataField(CHANCES_FIELD + i, leftPos + imageWidth / 4 * 3 - 12, topPos + 33 + i * 26, 40, 1D);
                }
                break;
            case CENTRIFUGE:
                showDataField(FLUID);
                setDataField(FLUID, leftPos + imageWidth / 4 * 3, topPos + imageHeight / 3 - 23, 55, 100);
                break;
            case CHILLER:
                showDataField(FLUID);
                setDataField(FLUID, leftPos + 8, topPos + imageHeight / 3 - 9, 55, 100);
                break;
        }
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        // General fields of thermal : energy, experience and mod energy, may be not used at all by recipes
        renderDataFieldTitle(ENERGY_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.energy"), matrixStack);
        renderDataFieldTitle(EXPERIENCE_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.experience"), matrixStack);
        renderDataFieldTitle(ENERGY_MOD_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.mod_energy"), matrixStack);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR:
                renderSlotTitle(0, References.getTranslate("screen.thermal_recipe_creator.slot.trunk"), matrixStack);
                renderSlotTitle(1, References.getTranslate("screen.thermal_recipe_creator.slot.leaves"), matrixStack);
                renderDataFieldTitle(RESIN_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.resin_amount"), matrixStack);
                break;
            case INSOLATOR:
                renderDataFieldTitle(7, References.getTranslate("screen.thermal_recipe_creator.field.mod_water"), matrixStack);
            case PULVERIZER:
            case SAWMILL:
            case SMELTER:
                renderDataFieldTitle(CHANCES_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.chances"), matrixStack);
                break;
            case PRESS:
                this.renderSlotTitle(1, References.getTranslate("screen.thermal_recipe_creator.slot.die"), matrixStack);
                break;
            case CHILLER:
                renderSlotTitle(1, References.getTranslate("screen.thermal_recipe_creator.slot.cast"), matrixStack);
            case CENTRIFUGE:
                renderDataFieldTitle(FLUID, References.getTranslate("screen.thermal_recipe_creator.field.fluid"), matrixStack);
                break;
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int pMouseX, int pMouseY)
    {
        super.renderLabels(matrixStack, pMouseX, pMouseY);

        // Render Labels
        IFormattableTextComponent inputLabel = References.getTranslate("screen.recipe_creator.label.input").copy().withStyle(TextFormatting.UNDERLINE);
        ITextComponent ouputLabel = References.getTranslate("screen.recipe_creator.label.output");
        Screen.drawString(matrixStack, this.font, inputLabel, this.imageWidth / 4 - font.width(inputLabel.getString()) / 2, 8, 0xFFFFFFFF);
        Screen.drawString(matrixStack, this.font, ouputLabel, this.imageWidth / 4 * 3 - font.width(ouputLabel.getString()) / 2, 8, 0xFFFFFFFF);
    }

    @Override
    protected List<PositionnedSlot> getTaggableSlots()
    {
        return SlotHelper.THERMAL_SLOTS;
    }

    @Override
    protected List<PositionnedSlot> getNbtTaggableSlots()
    {
        return new ArrayList<>();
    }

    @Override
    protected Item getRecipeIcon(ModRecipeCreator modRecipeCreator)
    {
        switch(modRecipeCreator)
        {
            case TREE_EXTRACTOR:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:device_tree_extractor"));
            case PULVERIZER:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_pulverizer"));
            case SAWMILL:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_sawmill"));
            case SMELTER:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_smelter"));
            case INSOLATOR:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_insolator"));
            case PRESS:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_press"));
            case FURNACE_THERMAL:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_furnace"));
            case CENTRIFUGE:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_centrifuge"));
            case CHILLER:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_chiller"));
            default:
                return Items.COMMAND_BLOCK;
        }
    }
}
