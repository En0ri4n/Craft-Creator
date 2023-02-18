package fr.eno.craftcreator.screen.container;

import cofh.thermal.core.init.TCoreRecipeTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleCheckBox;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ThermalRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<ThermalRecipeCreatorContainer>
{
    private static final int ENERGY_FIELD = 0,
            EXPERIENCE_FIELD = 1,
            RESIN_FIELD = 2,
            CHANCES_FIELD = 2,
            FLUID_FIELD_0 = 6,
            FLUID_FIELD_1 = 4,
            FLUID_FIELD_2 = 5,
            WATER_MOD_FIELD = 6;

    private SimpleCheckBox isEnergyModCheckBox;

    public ThermalRecipeCreatorScreen(ThermalRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
        this.guiTextureSize = 384;
        this.imageWidth = 296;
        this.imageHeight = 256;
    }

    @Override
    protected void initFields()
    {
        addNumberField(leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, -1, 7);
    }

    @Override
    protected void initWidgets()
    {
        addRenderableWidget(isEnergyModCheckBox = new SimpleCheckBox(leftPos + 6, topPos + imageHeight / 2 + 26, 10, 10, new TextComponent(""), false, b ->
        {
            setDataFieldValue(b.selected() ? 1D : 100, b.selected(), ENERGY_FIELD);
            if(b.selected())
                setDataFieldTooltip(ENERGY_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.mod_energy.tooltip", getDefaultEnergy(getCurrentRecipe().getRecipeType())));
            else
                setDataFieldTooltip(ENERGY_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.energy.tooltip"));
        }));
    }

    @Override
    protected void retrieveExtraData()
    {
        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("is_energy_mod", getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN));
    }

    @Override
    public int getArrowXPos(boolean right)
    {
        return right ? super.getArrowXPos(true) - 60 : super.getArrowXPos(false) + 60;
    }

    @Override
    protected RecipeInfos getExtraRecipeInfos(RecipeInfos recipeInfos)
    {
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.ENERGY, isEnergyModCheckBox.selected() ? getDataField(ENERGY_FIELD).getDoubleValue() : getDataField(ENERGY_FIELD).getIntValue()));
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean(RecipeInfos.Parameters.ENERGY_MOD, isEnergyModCheckBox.selected()));
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.EXPERIENCE, getDataField(EXPERIENCE_FIELD).getDoubleValue()));

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR:
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.RESIN_AMOUNT, getDataField(RESIN_FIELD).getIntValue()));
            case PULVERIZER:
            case SAWMILL:
            case SMELTER:
                addChancesParameters(recipeInfos);
                break;
            case INSOLATOR:
                    recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.WATER_MOD, getDataField(WATER_MOD_FIELD).getDoubleValue()));
                    addChancesParameters(recipeInfos);
                break;
            case CENTRIFUGE:
            case CHILLER:
            case BOTTLER:
            case CRUCIBLE:
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.FLUID_AMOUNT_0, getDataField(FLUID_FIELD_0).getIntValue()));
                break;
            case REFINERY:
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.FLUID_AMOUNT_0, getDataField(FLUID_FIELD_0).getIntValue()));
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.FLUID_AMOUNT_1, getDataField(FLUID_FIELD_1).getIntValue()));
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.CHANCE, getDataField(CHANCES_FIELD).getDoubleValue()));
                break;
            case PYROLYZER:
                addChancesParameters(recipeInfos);
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.FLUID_AMOUNT_0, getDataField(FLUID_FIELD_0).getIntValue()));
                break;
        }

        return recipeInfos;
    }

    private void addChancesParameters(RecipeInfos recipeInfos)
    {
        for(int i = 0; i < 4; i++)
            recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("chance_" + i, getDataField(CHANCES_FIELD + i).getDoubleValue()));
    }

    @Override
    protected void updateGui()
    {
        showDataField(ENERGY_FIELD, EXPERIENCE_FIELD);
        setDataField(EXPERIENCE_FIELD, this.leftPos + this.imageWidth - 73 - 8, this.topPos + this.imageHeight / 2 + 23, 73, 0.1D, true);

        setDataField(ENERGY_FIELD, this.leftPos + 20, this.topPos + this.imageHeight / 2 + 23, 70, isEnergyModCheckBox.selected() ? 1D : 100, isEnergyModCheckBox.selected());
        if(isEnergyModCheckBox.selected())
            setDataFieldTooltip(ENERGY_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.mod_energy.tooltip", getDefaultEnergy(getCurrentRecipe().getRecipeType())));
        else
            setDataFieldTooltip(ENERGY_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.energy.tooltip"));

        setDataFieldTooltip(EXPERIENCE_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.experience.tooltip"));

        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - this.executeButton.getWidth() / 2, this.topPos + this.imageHeight / 2 - this.executeButton.getHeight() / 2 + 22);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR:
                showDataField(RESIN_FIELD);
                setDataField(RESIN_FIELD, leftPos + imageWidth / 4 * 3 - 12, topPos + imageHeight / 3 - 13, 55, 25, false);
                setDataFieldTooltip(RESIN_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.resin.tooltip"));
                break;
            case INSOLATOR:
                showDataField(WATER_MOD_FIELD);
                setDataField(WATER_MOD_FIELD, this.leftPos + 48, this.topPos + this.imageHeight / 3 - 10, 45, 1D, true);
                setDataFieldTooltip(WATER_MOD_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.mod_water.tooltip"));
                setupChancesFields();
                break;
            case SAWMILL:
            case PULVERIZER:
            case SMELTER:
                setupChancesFields();
                break;
            case CENTRIFUGE:
                showDataField(FLUID_FIELD_0);
                setDataField(FLUID_FIELD_0, leftPos + imageWidth / 4 * 3, topPos + imageHeight / 3 - 23, 55, 100, false);
                setDataFieldTooltip(FLUID_FIELD_0, References.getTranslate("screen.thermal_recipe_creator.field.fluid.tooltip"));
                break;
            case BOTTLER:
            case CHILLER:
                showDataField(FLUID_FIELD_0);
                setDataField(FLUID_FIELD_0, leftPos + 8, topPos + imageHeight / 3 - 9, 55, 100, false);
                setDataFieldTooltip(FLUID_FIELD_0, References.getTranslate("screen.thermal_recipe_creator.field.fluid.tooltip"));
                break;
            case CRUCIBLE:
                showDataField(FLUID_FIELD_0);
                setDataField(FLUID_FIELD_0, leftPos + imageWidth / 4 * 3 - 12, topPos + imageHeight / 3 - 13, 55, 25, false);
                setDataFieldTooltip(FLUID_FIELD_0, References.getTranslate("screen.thermal_recipe_creator.field.fluid.tooltip"));
                break;
            case REFINERY:
                showDataField(FLUID_FIELD_0, FLUID_FIELD_1, FLUID_FIELD_2, CHANCES_FIELD);
                setDataField(FLUID_FIELD_0, leftPos + imageWidth / 4 - 31, topPos + imageHeight / 3 - 9, 55, 100, false);
                setDataField(CHANCES_FIELD, leftPos + imageWidth / 4 * 3 - 12, topPos + 33, 40, 1D, true);
                setDataField(FLUID_FIELD_1, leftPos + imageWidth / 4 * 3 - 12, topPos + imageHeight / 3 - 13, 55, 100, false);
                setDataField(FLUID_FIELD_2, leftPos + imageWidth / 4 * 3 - 12, topPos + imageHeight / 2 - 17, 55, 100, false);
                setDataFieldTooltip(FLUID_FIELD_0, References.getTranslate("screen.thermal_recipe_creator.field.fluid.tooltip"));
                setDataFieldTooltip(FLUID_FIELD_1, References.getTranslate("screen.thermal_recipe_creator.field.fluid.tooltip"));
                setDataFieldTooltip(FLUID_FIELD_2, References.getTranslate("screen.thermal_recipe_creator.field.fluid.tooltip"));
                setDataFieldTooltip(CHANCES_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.chances.tooltip"));
                break;
            case PYROLYZER:
                setupChancesFields();
                showDataField(FLUID_FIELD_0);
                setDataField(FLUID_FIELD_0, leftPos + imageWidth - 49, topPos + imageHeight / 3 - 20, 40, 25, false);
                setDataFieldTooltip(FLUID_FIELD_0, References.getTranslate("screen.thermal_recipe_creator.field.fluid.tooltip"));
                break;

        }
    }

    private void setupChancesFields()
    {
        for(int i = 0; i < 4; i++)
        {
            showDataField(CHANCES_FIELD + i);
            setDataField(CHANCES_FIELD + i, leftPos + imageWidth / 4 * 3 - 12, topPos + 33 + i * 26, 30, 1D, true);
            setDataFieldTooltip(CHANCES_FIELD + i, References.getTranslate("screen.thermal_recipe_creator.field.chances.tooltip"));
        }
    }

    @Override
    public void renderGui(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        // General fields of thermal : energy, experience and mod energy, may be not used at all by recipes
        renderDataFieldTitle(ENERGY_FIELD, References.getTranslate(isEnergyModCheckBox.selected() ? "screen.thermal_recipe_creator.field.mod_energy" : "screen.thermal_recipe_creator.field.energy"), matrixStack);
        renderDataFieldTitle(EXPERIENCE_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.experience"), matrixStack);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR:
                renderSlotTitle(0, References.getTranslate("screen.thermal_recipe_creator.slot.trunk"), matrixStack);
                renderSlotTitle(1, References.getTranslate("screen.thermal_recipe_creator.slot.leaves"), matrixStack);
                renderDataFieldTitle(RESIN_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.resin_amount"), matrixStack);
                break;
            case INSOLATOR:
                renderDataFieldTitle(WATER_MOD_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.mod_water"), matrixStack);
            case PULVERIZER:
            case SAWMILL:
            case SMELTER:
                renderDataFieldTitle(CHANCES_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.chances"), matrixStack);
                break;
            case PRESS:
                renderSlotTitle(1, References.getTranslate("screen.thermal_recipe_creator.slot.die"), matrixStack);
                break;
            case CHILLER:
                renderSlotTitle(1, References.getTranslate("screen.thermal_recipe_creator.slot.cast"), matrixStack);
            case BOTTLER:
            case CENTRIFUGE:
            case CRUCIBLE:
                renderDataFieldTitle(FLUID_FIELD_0, References.getTranslate("screen.thermal_recipe_creator.field.fluid"), matrixStack);
                break;
            case REFINERY:
                renderDataFieldTitle(FLUID_FIELD_0, References.getTranslate("screen.thermal_recipe_creator.field.fluid"), matrixStack);
                renderDataFieldTitle(FLUID_FIELD_1, References.getTranslate("screen.thermal_recipe_creator.field.fluid"), matrixStack);
                renderDataFieldTitle(FLUID_FIELD_2, References.getTranslate("screen.thermal_recipe_creator.field.fluid"), matrixStack);
                renderDataFieldTitle(CHANCES_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.chances"), matrixStack);
                break;
            case PYROLYZER:
                renderDataFieldTitle(CHANCES_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.chances"), matrixStack);
                renderDataFieldTitle(FLUID_FIELD_0, References.getTranslate("screen.thermal_recipe_creator.field.fluid"), matrixStack);
                break;
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int pMouseX, int pMouseY)
    {
        super.renderLabels(matrixStack, pMouseX, pMouseY);

        renderSideTitles(matrixStack);
    }

    @Override
    protected List<PositionnedSlot> getTaggableSlots()
    {
        return SlotHelper.THERMAL_SLOTS_INPUT;
    }

    @Override
    protected List<PositionnedSlot> getNbtTaggableSlots()
    {
        return new ArrayList<>();
    }

    private int getDefaultEnergy(RecipeType<?> thermalRecipeType)
    {
        // Default energy values for thermal recipes (in RF)
        // Can be improved
        if(thermalRecipeType == TCoreRecipeTypes.RECIPE_SAWMILL) return 2000;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_FURNACE) return 2000;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_PRESS) return 2400;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_CHILLER) return 4000;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_PULVERIZER) return 4000;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_SMELTER) return 3200;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_CENTRIFUGE) return 4000;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_CRUCIBLE) return 40000;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_REFINERY) return 8000;
        else if(thermalRecipeType == TCoreRecipeTypes.RECIPE_BOTTLER) return 400;

        return -1;
    }

    @Override
    protected void updateExtraServerData()
    {
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("is_energy_mod", getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN, isEnergyModCheckBox.selected()));
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);

        if(dataName.equals("is_energy_mod"))
        {
            isEnergyModCheckBox.setSelected((boolean) data);
            setDataFieldValue(isEnergyModCheckBox.selected() ? getDataField(ENERGY_FIELD).getDoubleValue() : getDataField(ENERGY_FIELD).getIntValue(), isEnergyModCheckBox.selected(), ENERGY_FIELD);
            setDataFieldTooltip(ENERGY_FIELD, isEnergyModCheckBox.selected() ? References.getTranslate("screen.thermal_recipe_creator.field.mod_energy.tooltip") : References.getTranslate("screen.thermal_recipe_creator.field.energy.tooltip"));
        }
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
            case CRUCIBLE:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_crucible"));
            case REFINERY:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_refinery"));
            case BOTTLER:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_bottler"));
            case PYROLYZER:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("thermal:machine_pyrolyzer"));
            default:
                return Items.COMMAND_BLOCK;
        }
    }
}
