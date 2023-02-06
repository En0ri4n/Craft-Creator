package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class ThermalRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<ThermalRecipeCreatorContainer>
{
    private static final int ENERGY_FIELD = 0, EXPERIENCE_FIELD = 1, CHANCES_FIELD = 2, RESIN_FIELD = 2, SPEED_FIELD = 6;

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

        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.ENERGY_MOD, getNumberField(SPEED_FIELD).getDoubleValue()));
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
                    this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.WATER_MOD, getNumberField(7).getDoubleValue()));
                break;
        }

        return this.recipeInfos;
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        showDataField(ENERGY_FIELD, EXPERIENCE_FIELD, SPEED_FIELD);
        setDataField(ENERGY_FIELD, this.leftPos + 8, this.topPos + this.imageHeight / 2 + 23, 45, 100);
        setDataField(EXPERIENCE_FIELD, this.leftPos + this.imageWidth - 73 - 8, this.topPos + this.imageHeight / 2 + 23, 73, 0.1D);
        setDataField(SPEED_FIELD, this.leftPos + 65, this.topPos + this.imageHeight / 2 + 23, 45, 1D);

        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - this.executeButton.getWidth() / 2, this.topPos + this.imageHeight / 2 - this.executeButton.getHeight() / 2 + 22);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR:
                showDataField(RESIN_FIELD);
                setDataField(RESIN_FIELD, leftPos + imageWidth / 4 * 3 - 12, topPos + imageHeight / 3 - 13, 55, 25);
                break;
            case SAWMILL:
            case PULVERIZER:
            case SMELTER:
            case INSOLATOR:
                for(int i = 0; i < 4; i++)
                {
                    showDataField(CHANCES_FIELD + i);
                    setDataField(CHANCES_FIELD + i, leftPos + imageWidth / 4 * 3 - 12, topPos + 33 + i * 26, 40, 1D);
                }

                if(getCurrentRecipe() == ModRecipeCreator.INSOLATOR)
                {
                    showDataField(7);
                    setDataField(7, this.leftPos + 48, this.topPos + this.imageHeight / 3 - 15, 45, 1D);
                }
                break;
        }
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR:
                this.renderSlotTitle(0, References.getTranslate("screen.thermal_recipe_creator.slot.trunk"), matrixStack);
                this.renderSlotTitle(1, References.getTranslate("screen.thermal_recipe_creator.slot.leaves"), matrixStack);
                renderDataFieldTitle(RESIN_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.resin_amount"), matrixStack);
                break;
            case PULVERIZER:
            case SAWMILL:
            case SMELTER:
            case INSOLATOR:
                renderDataFieldTitle(CHANCES_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.chances"), matrixStack);
                if(getCurrentRecipe() == ModRecipeCreator.INSOLATOR)
                    renderDataFieldTitle(7, References.getTranslate("screen.thermal_recipe_creator.field.mod_water"), matrixStack);
                break;
            case PRESS:
                this.renderSlotTitle(1, References.getTranslate("screen.thermal_recipe_creator.slot.die"), matrixStack);
                break;
        }

        renderDataFieldTitle(ENERGY_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.energy"), matrixStack);
        renderDataFieldTitle(EXPERIENCE_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.experience"), matrixStack);
        renderDataFieldTitle(SPEED_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.mod_energy"), matrixStack);

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
    protected List<PositionnedSlot> getTaggeableSlots()
    {
        return SlotHelper.THERMAL_SLOTS;
    }

    @Override
    protected Item getRecipeIcon(ModRecipeCreator modRecipeCreator)
    {
        switch(modRecipeCreator)
        {
            case TREE_EXTRACTOR:
                return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:device_tree_extractor"));
            case PULVERIZER:
                return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_pulverizer"));
            case SAWMILL:
                return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_sawmill"));
            case SMELTER:
                return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_smelter"));
            case INSOLATOR:
                return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_insolator"));
            case PRESS:
                return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_press"));
            case FURNACE_THERMAL:
                return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_furnace"));
            default:
                return Items.COMMAND_BLOCK;
        }
    }
}
